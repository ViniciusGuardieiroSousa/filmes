package com.example.myfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DescricaoActivity extends AppCompatActivity {
    private TextView titulo,ano;
    private ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao);

        titulo = findViewById(R.id.tituloID);
        ano = findViewById(R.id.anoID);
        poster = findViewById(R.id.imagemID);


        Bundle a = getIntent().getExtras();
        if(a!=null){
            titulo.setText(a.getString("imagem"));
            ano.setText(a.getString("ano"));
        }
    }
}
