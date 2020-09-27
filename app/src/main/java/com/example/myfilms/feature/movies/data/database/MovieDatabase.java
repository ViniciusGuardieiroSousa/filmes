package com.example.myfilms.feature.movies.data.database;

import com.example.myfilms.feature.movies.exceptions.DatabaseException;
import com.example.myfilms.feature.movies.data.dtos.DBMovie;

import java.util.List;

public interface MovieDatabase {

    List<DBMovie> getMovies() throws DatabaseException;

    void insertMovie(DBMovie movie) throws DatabaseException;
}
