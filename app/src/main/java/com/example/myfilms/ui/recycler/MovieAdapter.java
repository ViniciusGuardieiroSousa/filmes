package com.example.myfilms.ui.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfilms.R;
import com.example.myfilms.ui.domainModel.Movie;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> implements View.OnClickListener {
    private final List<Movie> movieList;
    private View.OnClickListener listener;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
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
        holder.bind(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }


    public void insertItem(Movie newMovie) {
        movieList.add(newMovie);
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

