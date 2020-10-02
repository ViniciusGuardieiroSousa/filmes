package com.example.myfilms.feature.movies.data;

import com.example.myfilms.core.mapper.ListMapper;
import com.example.myfilms.core.mapper.ListMapperImpl;
import com.example.myfilms.core.mapper.Mapper;
import com.example.myfilms.core.repository.DataSource;
import com.example.myfilms.core.repository.EnqueueListener;
import com.example.myfilms.feature.movies.data.database.MovieDatabase;
import com.example.myfilms.feature.movies.data.dtos.DBMovie;
import com.example.myfilms.feature.movies.data.dtos.NetworkMovie;
import com.example.myfilms.feature.movies.data.dtos.NetworkResult;
import com.example.myfilms.feature.movies.data.mapper.DBMovieToMovieMapper;
import com.example.myfilms.feature.movies.data.mapper.MovieToDBMovieMapper;
import com.example.myfilms.feature.movies.data.mapper.NetworkMovieToMovieMapper;
import com.example.myfilms.feature.movies.data.retrofit.MoviesAPI;
import com.example.myfilms.feature.movies.domain.entity.Movie;
import com.example.myfilms.feature.movies.exceptions.DatabaseException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSourceImpl implements DataSource<Movie> {

    private static String RESPONSE_NOT_SUCCESSFUL_MESSAGE = "Response not successful";

    private MovieDatabase database;
    private Mapper<Movie, DBMovie> movieMapperToDBMovie;
    private ListMapper<DBMovie, Movie> dbMovieListMapperToMovieList;
    private EnqueueListener<Movie> enqueueListener;
    private MoviesAPI moviesAPI;

    public MovieDataSourceImpl(
            MovieDatabase database,
            MoviesAPI moviesAPI
    ) {
        this.database = database;
        movieMapperToDBMovie = new MovieToDBMovieMapper();
        Mapper<DBMovie, Movie> dbMovieMapperToMovie = new DBMovieToMovieMapper();
        dbMovieListMapperToMovieList = new ListMapperImpl<>(dbMovieMapperToMovie);
        this.moviesAPI = moviesAPI;
    }

    @Override
    public void insertEnqueueListener(EnqueueListener enqueueListener) {
        this.enqueueListener = enqueueListener;
    }

    @Override
    public void insertItem(Movie item) throws DatabaseException {
        DBMovie dbMovie = movieMapperToDBMovie.map(item);
        database.insertMovie(dbMovie);
    }

    @Override
    public List<Movie> getSavedItems() throws DatabaseException {
        List<DBMovie> moviesSaved = database.getMovies();
        return dbMovieListMapperToMovieList.map(moviesSaved);

    }

    @Override
    public void searchItem(String itemName) {
        Call<NetworkResult> moviesSearched = moviesAPI.getSearch(itemName);
        moviesSearched.enqueue(new Callback<NetworkResult>() {
            @Override
            public void onResponse(Call<NetworkResult> call, Response<NetworkResult> response) {
                treatRetrofitResponse(response);
            }

            @Override
            public void onFailure(Call<NetworkResult> call, Throwable t) {
                responseError(t);
            }
        });
    }

    private void treatRetrofitResponse(Response<NetworkResult> response) {
        if (response.isSuccessful()) {
            responseSuccessful(response.body());
        } else {
            responseError(new Throwable(RESPONSE_NOT_SUCCESSFUL_MESSAGE));
        }
    }

    private void responseSuccessful(NetworkResult networkResult) {
        List<Movie> result = null;
        if (networkResultHaveMovies(networkResult)) {
            result = convertNetworkMoviesOnMovies(networkResult.movies);
        }
        enqueueListener.doOnResponse(result);
    }

    private void responseError(Throwable throwable){
        enqueueListener.doOnFailure(throwable);
    }

    private boolean networkResultHaveMovies(NetworkResult networkResult) {
        return networkResult != null && networkResult.movies != null;
    }

    private List<Movie> convertNetworkMoviesOnMovies(List<NetworkMovie> networkMovies) {
        Mapper<NetworkMovie, Movie> networkMovieToMovieMapper = new NetworkMovieToMovieMapper();
        ListMapper<NetworkMovie, Movie> networkMovieListToMovieListMapper =
                new ListMapperImpl<>(networkMovieToMovieMapper);
        return networkMovieListToMovieListMapper.map(networkMovies);
    }
}
