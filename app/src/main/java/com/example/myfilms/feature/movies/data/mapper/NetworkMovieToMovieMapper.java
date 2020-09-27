package com.example.myfilms.feature.movies.data.mapper;

import com.example.myfilms.core.mapper.Mapper;
import com.example.myfilms.feature.movies.data.dtos.NetworkMovie;
import com.example.myfilms.feature.movies.domain.entity.Movie;

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
