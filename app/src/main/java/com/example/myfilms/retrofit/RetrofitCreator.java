package com.example.myfilms.retrofit;

import com.example.myfilms.constants.APIConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCreator implements NetworkAPI {
    private static Retrofit retrofit;
    public RetrofitCreator(){
        retrofit = new Retrofit
                .Builder()
                .baseUrl(APIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public SearchFilmes getSearchMovies() {
        return retrofit.create(SearchFilmes.class);
    }
}
