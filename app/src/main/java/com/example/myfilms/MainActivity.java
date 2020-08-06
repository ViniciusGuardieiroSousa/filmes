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
    private Button searchButton;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private SearchList searchList;
    private ImageView posterImageView;
    private SQLiteDatabase database;
    private ArrayList<Search> moviesExisting = new ArrayList<>(0);
    private Context context;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        searchButton = findViewById(R.id.botaoBuscaId);
        searchEditText = findViewById(R.id.editTextBuscaID);
        recyclerView = findViewById(R.id.recyclerViewBuscaID);
        posterImageView = findViewById(R.id.imagemID);
        moviesExisting = new ArrayList<>(0);
        configDatabase();
        getMoviesOnDatabase();
        configRecycle();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        } else {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textoDigitado = searchEditText.getText().toString();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            searchButton.getWindowToken(), 0);
                    if (textoDigitado == null)
                        Toast.makeText(getApplicationContext(), "Texto não pode ser vazio", Toast.LENGTH_LONG).show();
                    else {
                        textoDigitado = treatBlankSpaceOnSearchText(textoDigitado);
                        configRetrofit(textoDigitado);
                    }
                }
            });
        }
    }

    private String treatBlankSpaceOnSearchText(String texto) {
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

    private void configRetrofit(String textoDigitado) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.omdbapi.com/").addConverterFactory(GsonConverterFactory.create()).build();
        final SearchFilmes filmes = retrofit.create(SearchFilmes.class);
        Call<SearchList> filmesBuscado = filmes.getSearch(textoDigitado);
        filmesBuscado.enqueue(new Callback<SearchList>() {
            @Override
            public void onResponse(Call<SearchList> call, Response<SearchList> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "erro: " + response.code());
                } else {
                    searchList = response.body();
                    int cnt = 0;
                    if (searchList.filmes != null) {
                        for (Search c : searchList.filmes) {
                            if (moviesExisting.size() == 0 || !moviesExisting.contains(c)) {
                                configImage(c);
                                String nome = treatBlankSpaceOnSearchText(c.getTitle());
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

    private void configRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(moviesExisting);
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DescricaoActivity.class);
                intent.putExtra("titulo", moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getTitle());
                intent.putExtra("ano", moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getYear());
                intent.putExtra("imagem", moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getImage());
                intent.putExtra("tipo", moviesExisting.get(recyclerView.getChildAdapterPosition(v)).getType());
                startActivity(intent);
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void configDatabase() {
        try {
            database = this.openOrCreateDatabase("filmes", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS 'filme'('id' INTEGER  PRIMARY KEY AUTOINCREMENT ,'title' VARCHAR, 'year' VARCHAR, 'imdbID' VARCHAR, 'type' VARCHAR, 'poster' VARCHAR )");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void configApostrophe(Search c) {
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

    private void addMovieOnDatabase(Search c) {
        try {
            configApostrophe(c);
            String a = Base64.encodeToString(c.getImage(), Base64.DEFAULT);
            database.execSQL("INSERT INTO filme(title, year, imdbID, type,poster) VALUES('" + c.getTitle() + "','" + c.getYear() + "','" + c.getImdbID() + "','" + c.getType() + "','" + a + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMoviesOnDatabase() {
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM filme", null);
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
                moviesExisting.add(c);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configImage(final Search c) {
        Glide.with(context).asBitmap().load(c.getPoster())
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] imagemArrayByte = stream.toByteArray();
                        c.setImage(imagemArrayByte);
                        movieAdapter.insertItem(c);
                        addMovieOnDatabase(c);
                    }
                });
    }


}
