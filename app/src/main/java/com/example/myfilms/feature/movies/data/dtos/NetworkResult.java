package com.example.myfilms.feature.movies.data.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkResult {
    @SerializedName("Search")
    public List<NetworkMovie> movies;
}
