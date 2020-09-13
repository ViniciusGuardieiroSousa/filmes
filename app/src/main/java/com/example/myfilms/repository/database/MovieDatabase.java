package com.example.myfilms.repository.database;

import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.repository.dtos.DBMovie;
import com.example.myfilms.ui.domainModel.Movie;

import java.util.ArrayList;
import java.util.List;

public interface MovieDatabase {

    public List<DBMovie> getMovies() throws DatabaseException;
    public void insertMovie(DBMovie movie) throws DatabaseException;
    public void insertMovies(List<DBMovie> movies) throws DatabaseException;

}
