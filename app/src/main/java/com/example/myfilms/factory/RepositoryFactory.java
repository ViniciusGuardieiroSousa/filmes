package com.example.myfilms.factory;

import android.content.Context;

import com.example.myfilms.repository.EnqueueListener;
import com.example.myfilms.repository.MovieRepository;
import com.example.myfilms.ui.domainModel.Movie;

public final class RepositoryFactory {
    private static String DATABASE_NAME_CONSTANTS = "Movies";

    private static MovieRepository movieRepository;

    public static MovieRepository getMovieRepository(
            Context context,
            EnqueueListener<Movie> enqueueListener
    ) throws Exception{
        if(movieRepository==null){
            movieRepository = new MovieRepository(
                    DatabaseFactory.getMovieDataBase(
                            context,
                            DATABASE_NAME_CONSTANTS,
                            Context.MODE_PRIVATE
                    ),
                    APIFactory.getMoviesAPI(),
                    enqueueListener
            );
        }
        return movieRepository;
    }
}
