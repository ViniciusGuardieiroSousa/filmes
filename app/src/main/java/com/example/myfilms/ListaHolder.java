package com.example.myfilms;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//classe java responsável por associar o layout para acessar os campos
public class ListaHolder extends RecyclerView.ViewHolder {
    public TextView titulo;
    public ImageView poster;

    public ListaHolder(@NonNull View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.textoRVId);
        poster = (ImageView) itemView.findViewById(R.id.imagemRVId);

    }
}
