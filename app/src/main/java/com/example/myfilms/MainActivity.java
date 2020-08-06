package com.example.myfilms;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "erro";
    private Button botaoBusca;
    private EditText editTextBusca;
    private RecyclerView recyclerViewBusca;
    private SearchList filme;
    private ImageView imagemView;
    private SQLiteDatabase banco;
    private ArrayList<Search> filmesExistentes = new ArrayList<>(0);
    private Context context;
    private MovieAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        //recuperar ids
        botaoBusca = findViewById(R.id.botaoBuscaId);
        editTextBusca = findViewById(R.id.editTextBuscaID);
        recyclerViewBusca = findViewById(R.id.recyclerViewBuscaID);
        imagemView = findViewById(R.id.imagemID);
        filmesExistentes = new ArrayList<>(0);
        //configurar banco de dados
        configurarBanco();
        recuperarFilmes();
        //configurar recyclerview
        configurarRecycle();
        //verificar permissão de uso internet
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        } else {
            //evento do botao
            botaoBusca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textoDigitado = editTextBusca.getText().toString();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            botaoBusca.getWindowToken(), 0);
                    if (textoDigitado == null)
                        Toast.makeText(getApplicationContext(), "Texto não pode ser vazio", Toast.LENGTH_LONG).show();
                    else {
                        textoDigitado = tratarEspaco(textoDigitado);
                        configurarRetrofit(textoDigitado);
                    }
                }
            });
        }
    }

    //troca os espaços digitado pelo usuario por + para poder usar na url
    private String tratarEspaco(String texto) {
        String retorno = "";
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == ' ') {
                retorno += "+";
            } else {
                retorno += texto.charAt(i);
            }
        }
        return retorno;
    }

    //metodo que configura o Retrofit e adiciona novos itens no recycleView
    private void configurarRetrofit(String textoDigitado) {
        //criar o retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.omdbapi.com/").addConverterFactory(GsonConverterFactory.create()).build();
        //retorna uma classe que implementa a interface SearchFilms por meio do polimorfismo
        final SearchFilmes filmes = retrofit.create(SearchFilmes.class);
        Call<SearchList> filmesBuscado = filmes.getSearch(textoDigitado);
        //executar de forma assincrona pois está dentro da ui thread pois ela n pode esperar a requisição pois uma requisação a internet pode ser lenta e irá travar (sincrona se tivesse dentro de uma classe ou thread)
        //calback classe anonima para ver o retorno de erro ou sucesso e proceder de forma certa
        filmesBuscado.enqueue(new Callback<SearchList>() {
            @Override
            public void onResponse(Call<SearchList> call, Response<SearchList> response) {
                //conector ao servidor porem a requisição não retornou algo desejado
                if (!response.isSuccessful()) {
                    Log.e(TAG, "erro: " + response.code());
                } else {
                    filme = response.body();
                    int cnt = 0;
                    if (filme.filmes != null) {
                        for (Search c : filme.filmes) {
                            if (filmesExistentes.size() == 0 || !filmesExistentes.contains(c)) {
                                //baixarImagem
                                configurarImagem(c);
                                String nome = tratarEspaco(c.getTitle());
                                cnt++;
                            }
                        }
                        if (cnt == 0)
                            Toast.makeText(getApplicationContext(), "Filme já existente", Toast.LENGTH_LONG).show();
                        else if (cnt == 1)
                            Toast.makeText(getApplicationContext(), "Filme cadastrado", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Filmes cadastrados", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Filme não encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<SearchList> call, Throwable t) {
                Log.e(TAG, "erro: " + t.getMessage());
            }
        });
    }

    //configurar recyclerView
    private void configurarRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBusca.setLayoutManager(layoutManager);
        adaptador = new MovieAdapter(filmesExistentes);
        recyclerViewBusca.setAdapter(adaptador);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DescricaoActivity.class);
                intent.putExtra("titulo", filmesExistentes.get(recyclerViewBusca.getChildAdapterPosition(v)).getTitle());
                intent.putExtra("ano", filmesExistentes.get(recyclerViewBusca.getChildAdapterPosition(v)).getYear());
                intent.putExtra("imagem", filmesExistentes.get(recyclerViewBusca.getChildAdapterPosition(v)).getImage());
                intent.putExtra("tipo", filmesExistentes.get(recyclerViewBusca.getChildAdapterPosition(v)).getType());
                startActivity(intent);
            }
        });
        //criar uma linha vertical entre os itens da lista
        recyclerViewBusca.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    //metodos para manipulação do banco de dados
    private void configurarBanco() {
        try {
            banco = this.openOrCreateDatabase("filmes", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS 'filme'('id' INTEGER  PRIMARY KEY AUTOINCREMENT ,'title' VARCHAR, 'year' VARCHAR, 'imdbID' VARCHAR, 'type' VARCHAR, 'poster' VARCHAR )");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void configuraApostrofo(Search c) {
        String a = "";
        char b = '\'';
        for (int i = 0; i < c.getTitle().length(); i++) {
            if (c.getTitle().charAt(i) == b) {
                a += "\'";
            }
            a += c.getTitle().charAt(i);
        }
        c.setTitle(a);
    }

    private void addBanco(Search c) {
        try {
            configuraApostrofo(c);
            String a = Base64.encodeToString(c.getImage(), Base64.DEFAULT);
            banco.execSQL("INSERT INTO filme(title, year, imdbID, type,poster) VALUES('" + c.getTitle() + "','" + c.getYear() + "','" + c.getImdbID() + "','" + c.getType() + "','" + a + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void recuperarFilmes() {
        try {
            Cursor cursor = banco.rawQuery("SELECT * FROM filme", null);
            //recuperar ids das colunas
            int indiceColunaTitle = cursor.getColumnIndex("title");
            int indiceColunaYear = cursor.getColumnIndex("year");
            int indiceColunaImdbID = cursor.getColumnIndex("imdbID");
            int indiceColunaType = cursor.getColumnIndex("type");
            int indiceColunaImagem = cursor.getColumnIndex("poster");
            cursor.moveToFirst();
            while (cursor != null) {
                Search c = new Search();
                c.setTitle(cursor.getString(indiceColunaTitle));
                c.setYear(cursor.getString(indiceColunaYear));
                c.setImdbID(cursor.getString(indiceColunaImdbID));
                c.setType(cursor.getString(indiceColunaType));
                String aux = cursor.getString(indiceColunaImagem);
                byte[] resultado = Base64.decode(aux, Base64.DEFAULT);
                c.setImage(resultado);
                filmesExistentes.add(c);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //configurar imagens
    private void configurarImagem(final Search c) {
        Glide.with(context).asBitmap().load(c.getPoster())
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] imagemArrayByte = stream.toByteArray();
                        c.setImage(imagemArrayByte);
                        adaptador.insertItem(c);
                        addBanco(c);
                    }
                });
    }


}
