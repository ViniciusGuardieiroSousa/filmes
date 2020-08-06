package com.example.myfilms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


class MovieAdapter extends RecyclerView.Adapter<MovieHolder> implements View.OnClickListener {
    private final List<Search> searchList;
    private View.OnClickListener listener;

    public MovieAdapter(ArrayList<Search> searchList) {
        this.searchList = searchList;
    }

    @NonNull
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movies, parent, false);
        v.setOnClickListener(this);
        return new MovieHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bind(searchList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchList != null ? searchList.size() : 0;
    }


    public void insertItem(Search newMovie) {
        searchList.add(newMovie);
        notifyItemInserted(getItemCount());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }
}

