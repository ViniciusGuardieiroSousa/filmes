package com.example.myfilms.feature.movies.data.mapper;

import android.util.Base64;

import com.example.myfilms.core.mapper.Mapper;
import com.example.myfilms.feature.movies.data.dtos.DBMovie;
import com.example.myfilms.feature.movies.domain.entity.Movie;

public class MovieToDBMovieMapper implements Mapper<Movie, DBMovie> {
    @Override
    public DBMovie map(Movie movie) {
        DBMovie movieMapped = new DBMovie();
        String encodeImage = Base64.encodeToString(movie.getImage(), Base64.DEFAULT);
        movieMapped.setImage(encodeImage);
        movieMapped.setImdbID(movie.getImdbID());
        movieMapped.setPoster(movie.getPoster());
        movieMapped.setTitle(movie.getTitle());
        movieMapped.setType(movie.getType());
        movieMapped.setYear(movie.getYear());
        return movieMapped;
    }
}