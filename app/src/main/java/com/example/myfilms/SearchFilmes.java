package com.example.myfilms;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface SearchFilmes {

    @GET("?apikey=b1005863&type=movie&")
    Call<SearchList>getSearch(@Query("s") String filme);
}
