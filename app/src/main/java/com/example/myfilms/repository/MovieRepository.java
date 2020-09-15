package com.example.myfilms.repository;

import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.mapper.DBMovieToMovieMapper;
import com.example.myfilms.mapper.ListMapper;
import com.example.myfilms.mapper.ListMapperImpl;
import com.example.myfilms.mapper.Mapper;
import com.example.myfilms.mapper.MovieToDBMovie;
import com.example.myfilms.mapper.NetworkMovieToMovieMapper;
import com.example.myfilms.repository.database.MovieDatabase;
import com.example.myfilms.repository.dtos.DBMovie;
import com.example.myfilms.repository.dtos.NetworkMovie;
import com.example.myfilms.repository.dtos.NetworkResult;
import com.example.myfilms.repository.retrofit.MoviesAPI;
import com.example.myfilms.ui.domainModel.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository implements Repository<Movie> {

    private static String RESPONSE_NOT_SUCCESSFUL_MESSAGE = "Response not successful";

    private MovieDatabase database;
    private Mapper<Movie, DBMovie> movieMapperToDBMovie;
    private ListMapper<DBMovie, Movie> dbMovieListMapperToMovieList;
    private EnqueueListener<Movie> enqueueListener;
    private MoviesAPI moviesAPI;

    public MovieRepository(
            MovieDatabase database,
            MoviesAPI moviesAPI,
            EnqueueListener<Movie> enqueueListener
    ) {
        this.database = database;
        movieMapperToDBMovie = new MovieToDBMovie();
        Mapper<DBMovie, Movie> dbMovieMapperToMovie = new DBMovieToMovieMapper();
        dbMovieListMapperToMovieList = new ListMapperImpl<>(dbMovieMapperToMovie);
        this.moviesAPI = moviesAPI;
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
