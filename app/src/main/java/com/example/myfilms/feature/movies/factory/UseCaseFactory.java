package com.example.myfilms.feature.movies.factory;


import android.content.Context;

import com.example.myfilms.core.repository.EnqueueListener;
import com.example.myfilms.core.repository.Repository;
import com.example.myfilms.feature.movies.domain.entity.Movie;
import com.example.myfilms.feature.movies.domain.useCase.GetMoviesUseCase;
import com.example.myfilms.feature.movies.domain.useCase.InsertMovieUseCase;
import com.example.myfilms.feature.movies.domain.useCase.SearchMovieUseCase;

public class UseCaseFactory {

    private Repository<Movie> movieRepository;
    private GetMoviesUseCase getMoviesUseCase;
    private InsertMovieUseCase insertMovieUseCase;
    private SearchMovieUseCase searchMovieUseCase;

    public UseCaseFactory(Context context) throws Exception {
        this.movieRepository = RepositoryFactory.getMovieRepository(context);
    }

    public GetMoviesUseCase getMoviesUseCase() {
        if (getMoviesUseCase == null) {
            getMoviesUseCase = new GetMoviesUseCase(
                    movieRepository
            );
        }
        return getMoviesUseCase;
    }

    public InsertMovieUseCase getInsertMovieUseCase() {
        if (insertMovieUseCase == null) {
            insertMovieUseCase = new InsertMovieUseCase(
                    movieRepository
            );
        }
        return insertMovieUseCase;
    }

    public SearchMovieUseCase getSearchMovieUseCase(EnqueueListener enqueueListener) {
        if (searchMovieUseCase == null) {
            searchMovieUseCase = new SearchMovieUseCase(
                    movieRepository,
                    enqueueListener
            );
        }
        return searchMovieUseCase;
    }

}
