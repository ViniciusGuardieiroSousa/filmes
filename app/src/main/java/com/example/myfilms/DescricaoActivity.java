package com.example.myfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DescricaoActivity extends AppCompatActivity {
    private TextView titulo,ano,tipo;
    private ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao);

        titulo = findViewById(R.id.tituloID);
        ano = findViewById(R.id.anoID);
        poster = findViewById(R.id.imagemExibirId);
        tipo = findViewById(R.id.tipoID);

        Bundle a = getIntent().getExtras();
        if(a!=null){
            titulo.setText(a.getString("titulo"));
            ano.setText(a.getString("ano"));
            byte[] aux = a.getByteArray("imagem");
            tipo.setText(a.getString("tipo"));
            Bitmap exibir = BitmapFactory.decodeByteArray(aux, 0, aux.length);
            poster.setImageBitmap(exibir);
        }


    }
}
