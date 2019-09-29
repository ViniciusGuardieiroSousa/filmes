package com.example.myfilms;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchList {
    @SerializedName("Search")
    public List<Search> filmes;
    public int size(){
        return filmes.size();
    }
}
