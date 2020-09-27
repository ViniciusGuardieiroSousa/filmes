package com.example.myfilms.feature.movies.data.retrofit;

import com.example.myfilms.feature.movies.data.dtos.NetworkResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MoviesAPI {

    @GET("?apikey=b1005863&type=movie&")
    Call<NetworkResult> getSearch(@Query("s") String movieName);
}
