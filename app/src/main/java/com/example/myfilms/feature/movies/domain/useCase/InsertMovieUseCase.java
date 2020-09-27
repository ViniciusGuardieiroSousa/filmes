package com.example.myfilms.feature.movies.domain.useCase;

import com.example.myfilms.core.interactors.InputUseCase;
import com.example.myfilms.core.repository.Repository;
import com.example.myfilms.feature.movies.domain.entity.Movie;

public class InsertMovieUseCase extends InputUseCase<Movie> {

    private Repository<Movie> movieRepository;

    public InsertMovieUseCase(Repository<Movie> movieRepository){
        this.movieRepository = movieRepository;
    }

    @Override
    public void invoke(Movie movie) throws Exception {
        movieRepository.insertItem(movie);
    }
}
