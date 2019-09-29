package com.example.myfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FIlmesActivity extends AppCompatActivity {
    private TextView textFilmes;
    private Button botaoVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmes);
        //recuperar o que foi passado na Intent
        Bundle recuperar = getIntent().getExtras();
        boolean assistido = false;
        if(recuperar!= null)
            assistido = recuperar.getBoolean("assistido");
        //recuperar ids
        textFilmes = findViewById(R.id.textFilmesID);
        botaoVoltar = findViewById(R.id.botaoVoltarId);

        if(assistido == true){
            filmeAssistido();
        }
        else{
            filmeAssistir();
        }

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FIlmesActivity.this, MainActivity.class));
            }
        });

    }
    private void filmeAssistido(){
        textFilmes.setText("Assistidos");
    }
    private void filmeAssistir(){
        textFilmes.setText("Para Assistir");
    }
}
