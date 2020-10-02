package com.example.myfilms.feature.movies.presentation.ViewModel;

import android.util.Log;

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

import java.util.ArrayList;
import java.util.List;

import static com.example.myfilms.feature.movies.presentation.constants.LogTagsConstants.DATABASE_TAG_ERROR_CONSTANTS;

public class MainActivityViewModel extends ViewModel {

    private static String ERROR_TO_ADD_MOVIES_ON_DATABASE_MESSAGE_CONSTANTS =
            "Error to add a movie on a database";

    private GetMoviesUseCase getMoviesUseCase;
    private InsertMovieUseCase insertMovieUseCase;
    private SearchMovieUseCase searchMovieUseCase;
    private MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> searchMoviesLiveData = new MutableLiveData<>();
    private MutableLiveData<Exception> exceptionMutableLiveData = new MutableLiveData<>();
    private List<Movie> moviesExisting = new ArrayList<>();

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
        List<Movie> movies = getMoviesUseCase.invoke();
        moviesLiveData.setValue(movies);
        moviesExisting = movies;
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
                if(moviesIsNotNull(listResponse)){
                    ArrayList<Movie> result = null;
                    addDifferentMoviesOnMoviesExisting(listResponse);
                    searchMoviesLiveData.setValue(result);
                }
                searchMoviesLiveData.setValue(new ArrayList<Movie>(0));
            }

            @Override
            public void doOnFailure(Throwable throwable) {
                searchMoviesLiveData.setValue(new ArrayList<Movie>(0));
            }
        };
    }

    private Boolean moviesIsNotNull(List<Movie> movies) {
        return movies!=null;
    }

    private void  addDifferentMoviesOnMoviesExisting(List<Movie> moviesResult) {
        for (Movie movie : moviesResult) {
            if (movieIsNotOnMoviesExistingVariable(movie)) {
                moviesResult.add(movie);
                try{
                    insertMovie(movie);
                }catch (Exception e){
                    Log.e(DATABASE_TAG_ERROR_CONSTANTS,ERROR_TO_ADD_MOVIES_ON_DATABASE_MESSAGE_CONSTANTS);
                }
            }
        }
    }

    private Boolean movieIsNotOnMoviesExistingVariable(Movie movie) {
        return moviesExisting.size() == 0 || !moviesExisting.contains(movie);
    }
}
