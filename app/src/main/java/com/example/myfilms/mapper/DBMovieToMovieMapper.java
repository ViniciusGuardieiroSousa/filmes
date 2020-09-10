package com.example.myfilms.mapper;

import android.util.Base64;

import com.example.myfilms.repository.dtos.DBMovie;
import com.example.myfilms.ui.domainModel.Movie;

public class DBMovieToMovieMapper implements Mapper <DBMovie, Movie> {
    @Override
    public Movie map(DBMovie dbMovie) {

        Movie movieMapped = new Movie();
        byte[] posterDecoded = Base64.decode(dbMovie.getImage(), Base64.DEFAULT);
        movieMapped.setImage(posterDecoded);
        movieMapped.setImdbID(dbMovie.getImdbID());
        movieMapped.setPoster(dbMovie.getPoster());
        movieMapped.setTitle(dbMovie.getTitle());
        movieMapped.setType(dbMovie.getType());
        movieMapped.setYear(dbMovie.getYear());

        return movieMapped;
    }
}
