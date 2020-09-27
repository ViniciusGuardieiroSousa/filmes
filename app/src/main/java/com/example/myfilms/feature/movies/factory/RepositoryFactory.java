package com.example.myfilms.feature.movies.factory;

import android.content.Context;

import com.example.myfilms.core.repository.Repository;
import com.example.myfilms.feature.movies.data.MovieRepositoryImpl;
import com.example.myfilms.feature.movies.domain.entity.Movie;

public final class RepositoryFactory {

    private static Repository<Movie> movieRepository;

    public static Repository<Movie> getMovieRepository(
            Context context
    ) throws Exception {
        if (movieRepository == null) {
            movieRepository = new MovieRepositoryImpl(
                    DataSourceFactory.getDataSource(context)
            );
        }
        return movieRepository;
    }

}
