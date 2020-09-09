package com.example.myfilms.ui.domainModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchList {
    @SerializedName("Search")
    public List<Movie> filmes;

    public List<Movie> getFilmes() {
        return filmes;
    }
}
