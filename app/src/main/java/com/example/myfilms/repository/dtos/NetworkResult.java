package com.example.myfilms.repository.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkResult {
    @SerializedName("Search")
    public List<NetworkMovie> movies;
}
