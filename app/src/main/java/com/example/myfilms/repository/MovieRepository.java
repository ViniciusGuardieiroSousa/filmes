package com.example.myfilms.repository;

import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.mapper.DBMovieToMovieMapper;
import com.example.myfilms.mapper.ListMapper;
import com.example.myfilms.mapper.ListMapperImpl;
import com.example.myfilms.mapper.Mapper;
import com.example.myfilms.mapper.MovieToDBMovie;
import com.example.myfilms.repository.database.MovieDatabase;
import com.example.myfilms.repository.dtos.DBMovie;
import com.example.myfilms.ui.domainModel.Movie;

import java.util.List;

public class MovieRepository implements Repository<Movie> {

    private MovieDatabase database;
    private Mapper<Movie, DBMovie> movieMapperToDBMovie;
    private ListMapper<DBMovie, Movie> dbMovieListMapperToMovieList;


    public MovieRepository(MovieDatabase database) {
        this.database = database;
        movieMapperToDBMovie = new MovieToDBMovie();
        Mapper<DBMovie, Movie> dbMovieMapperToMovie = new DBMovieToMovieMapper();
        dbMovieListMapperToMovieList = new ListMapperImpl<>(dbMovieMapperToMovie);
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
}
