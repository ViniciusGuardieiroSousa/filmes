package com.example.myfilms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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



    private AdaptadorImagemTexto adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        //recuperar ids
        botaoBusca = findViewById(R.id.botaoBuscaId);
        editTextBusca = findViewById(R.id.editTextBuscaID);
        recyclerViewBusca = findViewById(R.id.recyclerViewBuscaID);
        imagemView = findViewById(R.id.imagemID);
        //configurar recyclerview
        configurarRecycle();




        final Context context = this;
        //verificar permissão de uso internet
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        } else {
            //evento do botao
            botaoBusca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textoDigitado = editTextBusca.getText().toString();
                    textoDigitado = tratarEspaco(textoDigitado);
                    configurarRetrofit(textoDigitado);
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
        SearchFilmes filmes = retrofit.create(SearchFilmes.class);
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
                    if(filme.filmes!=null){
                        for(Search c:filme.filmes){
                            adaptador.updateList(c);
                            System.out.println(c.getTitle());
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

    private void configurarRecycle(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBusca.setLayoutManager(layoutManager);
        adaptador = new AdaptadorImagemTexto(new ArrayList<Search>(0));
        recyclerViewBusca.setAdapter(adaptador);
        //criar uma linha vertical entre os itens da lista
        recyclerViewBusca.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


}
