package com.example.myfilms.mapper;

import android.util.Base64;

import com.example.myfilms.repository.dtos.NetworkMovie;
import com.example.myfilms.ui.domainModel.Movie;

public class NetworkMovieToMovieMapper implements Mapper<NetworkMovie, Movie> {
    @Override
    public Movie map(NetworkMovie networkMovie) {
        Movie movieMapped = new Movie();
        movieMapped.setImage(networkMovie.getImage());
        movieMapped.setImdbID(networkMovie.getImdbID());
        movieMapped.setPoster(networkMovie.getPoster());
        movieMapped.setTitle(networkMovie.getTitle());
        movieMapped.setType(networkMovie.getType());
        movieMapped.setYear(networkMovie.getYear());
        return movieMapped;
    }
}
