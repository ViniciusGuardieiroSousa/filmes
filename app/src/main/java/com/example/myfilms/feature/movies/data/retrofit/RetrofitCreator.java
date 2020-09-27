package com.example.myfilms.feature.movies.data.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCreator implements NetworkAPI {
    private static Retrofit retrofit;

    public RetrofitCreator() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(APIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public MoviesAPI getMoviesAPI() {
        return retrofit.create(MoviesAPI.class);
    }
}
