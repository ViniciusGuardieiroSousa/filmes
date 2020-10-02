package com.example.myfilms.feature.movies.domain.useCase;

import com.example.myfilms.core.interactors.OutputUseCase;
import com.example.myfilms.core.repository.Repository;
import com.example.myfilms.feature.movies.domain.entity.Movie;

import java.util.List;

public class GetMoviesUseCase extends OutputUseCase<List<Movie>> {

    private Repository<Movie> movieRepository;

    public GetMoviesUseCase(Repository<Movie> movieRepository){
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> invoke() throws Exception{
        return movieRepository.getSavedItems();
    }
}
