package com.example.myfilms;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;


class AdaptadorImagemTexto extends RecyclerView.Adapter<ListaHolder>implements View.OnClickListener {
    private final List<Search> mDataset;
    private Context context;
    private View.OnClickListener listener;



    //construtor
    public AdaptadorImagemTexto(ArrayList<Search> mDataset) {

        this.mDataset = mDataset;

    }


    @NonNull
    public ListaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //infla a classe cm o layout lista itens
        //o listaHolder é onde armazena os objetos
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_lista_itens, parent, false);
        v.setOnClickListener(this);
        return new ListaHolder(v);//parent.getContext pega o contexto da activity que chamou
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
        return mDataset != null ? mDataset.size() : 0;
    }

    //metodo usado para adicionar novo elemento fora dessa classe

    public void updateList(Search user) {
        insertItem(user);
    }

    // Método que irá inserir novo elemento em mDataset e informar que há um novo elemento
    private void insertItem(Search novoFilme) {

                mDataset.add(novoFilme);
                notifyItemInserted(getItemCount());

    }

    public  void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }
}

