package com.example.myfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDescriptionActivity extends AppCompatActivity {
    private TextView title, year, type;
    private ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        title = findViewById(R.id.tituloID);
        year = findViewById(R.id.anoID);
        poster = findViewById(R.id.imagemExibirId);
        type = findViewById(R.id.tipoID);
        Bundle a = getIntent().getExtras();
        if(a!=null){
            title.setText(a.getString("titulo"));
            year.setText(a.getString("ano"));
            byte[] aux = a.getByteArray("imagem");
            type.setText(a.getString("tipo"));
            Bitmap exibir = BitmapFactory.decodeByteArray(aux, 0, aux.length);
            poster.setImageBitmap(exibir);
        }
    }
}
