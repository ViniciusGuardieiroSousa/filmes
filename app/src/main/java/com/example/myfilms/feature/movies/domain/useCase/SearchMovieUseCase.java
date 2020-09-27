package com.example.myfilms.feature.movies.domain.useCase;

import com.example.myfilms.core.interactors.InputUseCase;
import com.example.myfilms.core.repository.EnqueueListener;
import com.example.myfilms.core.repository.Repository;
import com.example.myfilms.feature.movies.domain.entity.Movie;

public class SearchMovieUseCase extends InputUseCase<String> {

    private Repository<Movie> movieRepository;

    public SearchMovieUseCase(Repository<Movie> movieRepository, EnqueueListener<Movie> enqueueListener){
        this.movieRepository = movieRepository;
        this.movieRepository.setEnqueueListener(enqueueListener);
    }

    @Override
    public void invoke(String s) throws Exception {
        movieRepository.searchItem(s);
    }
}
