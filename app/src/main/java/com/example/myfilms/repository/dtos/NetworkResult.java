package com.example.myfilms.repository.dtos;

import com.example.myfilms.ui.domainModel.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkResult {
    @SerializedName("Search")
    public List<Movie> filmes;

    public List<Movie> getFilmes() {
        return filmes;
    }
}
