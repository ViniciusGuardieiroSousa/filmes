package com.example.myfilms.repository.database;

import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.repository.dtos.DBMovie;

import java.util.List;

public interface MovieDatabase {

    List<DBMovie> getMovies() throws DatabaseException;

    void insertMovie(DBMovie movie) throws DatabaseException;
}
