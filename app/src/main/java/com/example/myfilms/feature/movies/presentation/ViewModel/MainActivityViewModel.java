package com.example.myfilms.feature.movies.presentation.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myfilms.AppApplication;
import com.example.myfilms.core.repository.EnqueueListener;
import com.example.myfilms.feature.movies.domain.entity.Movie;
import com.example.myfilms.feature.movies.domain.useCase.GetMoviesUseCase;
import com.example.myfilms.feature.movies.domain.useCase.InsertMovieUseCase;
import com.example.myfilms.feature.movies.domain.useCase.SearchMovieUseCase;
import com.example.myfilms.feature.movies.factory.UseCaseFactory;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private GetMoviesUseCase getMoviesUseCase;
    private InsertMovieUseCase insertMovieUseCase;
    private SearchMovieUseCase searchMovieUseCase;
    private MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> searchMoviesLiveData = new MutableLiveData<>();
    private MutableLiveData<Exception> exceptionMutableLiveData = new MutableLiveData<>();

    public MainActivityViewModel() {
        configUseCase();
    }

    public LiveData<List<Movie>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public LiveData<Exception> getExceptionLiveData() {
        return exceptionMutableLiveData;
    }

    public LiveData<List<Movie>> getSearchMoviesLiveData() {
        return searchMoviesLiveData;
    }

    private void configUseCase() {
        try {
            UseCaseFactory useCaseFactory = new UseCaseFactory(AppApplication.context);
            getMoviesUseCase = useCaseFactory.getMoviesUseCase();
            searchMovieUseCase = useCaseFactory.getSearchMovieUseCase(createEnqueueListener());
            insertMovieUseCase = useCaseFactory.getInsertMovieUseCase();

        } catch (Exception exception) {
            exceptionMutableLiveData.setValue(exception);
        }
    }

    public void getMovies() throws Exception {
        moviesLiveData.setValue(getMoviesUseCase.invoke());
    }

    public void insertMovie(Movie movie) throws Exception {
        insertMovieUseCase.invoke(movie);
    }

    public void searchMovies(String text) throws Exception {
        searchMovieUseCase.invoke(text);
    }

    private EnqueueListener createEnqueueListener() {
        return new EnqueueListener<Movie>() {
            @Override
            public void doOnResponse(List<Movie> listResponse) {
                searchMoviesLiveData.setValue(listResponse);

            }

            @Override
            public void doOnFailure(Throwable throwable) {
                searchMoviesLiveData.setValue(null);
            }
        };
    }
}
