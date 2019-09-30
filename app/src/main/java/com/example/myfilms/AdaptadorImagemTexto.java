package com.example.myfilms;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;



class AdaptadorImagemTexto extends RecyclerView.Adapter<ListaHolder> {
    private final List<Search> mDataset;
    private Context context;

    //construtor
    public AdaptadorImagemTexto(ArrayList<Search> mDataset) {
        this.mDataset = mDataset;

    }


    @NonNull
    @Override
    public ListaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListaHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_lista_itens, parent, false));
    }

    //metodo para colcoar a imagem e o texto na tela
    @Override
    public void onBindViewHolder(@NonNull ListaHolder holder, int position) {
        holder.titulo.setText(mDataset.get(position).getTitle());
        //holder.poster.getResources().getDrawable(mDataset.get(position).getImagem());
    }

    //metodo usado para retornar o numero de elementos na lista
    @Override
    public int getItemCount() {
        return mDataset!=null? mDataset.size():0;
    }

    //metodo usado para adicionar novo elemento fora dessa classe

    public void updateList(Search user) {
        insertItem(user);
    }

    // Método que irá inserir novo elemento em mDataset e informar que há um novo elemento
    private void insertItem(Search novoFilme) {
        if(!mDataset.contains(novoFilme)){
            mDataset.add(novoFilme);
            notifyItemInserted(getItemCount());
        }

    }
}

