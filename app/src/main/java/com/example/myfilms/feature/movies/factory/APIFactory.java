package com.example.myfilms.feature.movies.factory;

import com.example.myfilms.feature.movies.data.retrofit.MoviesAPI;
import com.example.myfilms.feature.movies.data.retrofit.RetrofitCreator;

public final class APIFactory {
    private static MoviesAPI networkAPI;

    public static MoviesAPI getMoviesAPI() {
        if (networkAPI == null) {
            RetrofitCreator retrofit = new RetrofitCreator();
            networkAPI = retrofit.getMoviesAPI();
        }
        return networkAPI;
    }

}
