package com.example.myfilms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {
    private Button botaoBusca;
    private EditText editTextBusca;
    private RecyclerView recyclerViewBusca;


    private static final String TAG = "erro";
    private SearchList filme;
    private ImageView imagemView;
    private SQLiteDatabase banco;
    private ArrayList<Search> filmesExistentes = new ArrayList<>(0);
    private Context context ;
    private AdaptadorImagemTexto adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
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
                    if (textoDigitado == null)
                        Toast.makeText(getApplicationContext(), "Texto não pode ser vazio", Toast.LENGTH_LONG);
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
                    int cnt = 100;
                    if (filme.filmes != null) {
                        for (Search c : filme.filmes) {
                            if (filmesExistentes.size() == 0 || !filmesExistentes.contains(c)) {

                                //baixarImagem
                                configurarImagem(c);
                                String nome = tratarEspaco(c.getTitle());
                                addBanco(c);

                            }
                        }
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
        adaptador = new AdaptadorImagemTexto(filmesExistentes);
        recyclerViewBusca.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this,DescricaoActivity.class);
                intent.putExtra("titulo",filmesExistentes.get(recyclerViewBusca.getChildAdapterPosition(v)).getTitle());
                intent.putExtra("ano",filmesExistentes.get(recyclerViewBusca.getChildAdapterPosition(v)).getYear());
                intent.putExtra("imagem",filmesExistentes.get(recyclerViewBusca.getChildAdapterPosition(v)).getPoster());
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

            banco.execSQL("CREATE TABLE IF NOT EXISTS filme(id INTEGER  PRIMARY KEY AUTOINCREMENT ,title VARCHAR, year VARCHAR, imdbID VARCHAR, type VARCHAR)");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void configuraApostrofo(Search c){
        String a = "";
        char b = '\'';
        for(int i = 0; i< c.getTitle().length();i++){
            if(c.getTitle().charAt(i)==b){
                a+="\'";
            }
            a+=c.getTitle().charAt(i);
        }

        c.setTitle(a);
    }
    private void addBanco(Search c) {
        try {
            configuraApostrofo(c);
            banco.execSQL("INSERT INTO filme(title, year, imdbID, type) VALUES('" + c.getTitle() + "','" + c.getYear() + "','" + c.getImdbID() + "','" + c.getType() + "')");
            adaptador.updateList(c);
        }catch (Exception e){
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


            cursor.moveToFirst();
            while (cursor != null) {
                Search c = new Search();
                System.out.println(c.getTitle());
                c.setTitle(cursor.getString(indiceColunaTitle));
                c.setYear(cursor.getString(indiceColunaYear));
                c.setImdbID(cursor.getString(indiceColunaImdbID));
                c.setType(cursor.getString(indiceColunaType));

                filmesExistentes.add(c);

                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //configurar imagens

    private void configurarImagem(final Search c){
        final String nome = tratarEspaco(c.getTitle());
        Glide.with(context).asBitmap().load(c.getPoster())
                .into(new SimpleTarget<Bitmap>(100,100) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String a = saveImage(nome,resource);
                        //System.out.println(a);
                        c.setPoster(a);
                    }
                });
    }
    private String saveImage(String nome,Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + nome + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/assets");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {

            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            Toast.makeText(context, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }

        return savedImagePath;
    }





}
