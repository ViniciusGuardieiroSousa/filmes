package com.example.myfilms.repository.database;

import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.repository.dtos.DBMovie;
import com.example.myfilms.ui.domainModel.Movie;

import java.util.ArrayList;
import java.util.List;

public interface MovieDatabase {

    public List<Movie> getMovies() throws DatabaseException;
    public void insertMovie(Movie movie) throws DatabaseException;
    public void insertMovies(List<Movie> movies) throws DatabaseException;

}
