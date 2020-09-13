package com.example.myfilms.factory;

import com.example.myfilms.repository.retrofit.MoviesAPI;
import com.example.myfilms.repository.retrofit.RetrofitCreator;

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
