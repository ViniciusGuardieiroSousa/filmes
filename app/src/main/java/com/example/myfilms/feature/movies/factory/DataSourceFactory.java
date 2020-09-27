package com.example.myfilms.feature.movies.factory;

import android.content.Context;

import com.example.myfilms.core.repository.DataSource;
import com.example.myfilms.feature.movies.data.MovieDataSourceImpl;
import com.example.myfilms.feature.movies.domain.entity.Movie;

public final class DataSourceFactory {
    private static String DATABASE_NAME_CONSTANTS = "Movies";

    private static DataSource<Movie> movieDataSourceImpl;

    public static DataSource<Movie> getDataSource(
            Context context
    ) throws Exception{
        if(movieDataSourceImpl ==null){
            movieDataSourceImpl = new MovieDataSourceImpl(
                    DatabaseFactory.getMovieDataBase(
                            context,
                            DATABASE_NAME_CONSTANTS,
                            Context.MODE_PRIVATE
                    ),
                    APIFactory.getMoviesAPI()
            );
        }
        return movieDataSourceImpl;
    }
}
