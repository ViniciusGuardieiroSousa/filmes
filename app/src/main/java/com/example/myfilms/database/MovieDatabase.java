package com.example.myfilms.database;

import com.example.myfilms.Search;
import com.example.myfilms.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.List;

public interface MovieDatabase {

    public ArrayList<Search> getMovies() throws DatabaseException;
    public void insertMovie(Search movie) throws DatabaseException;
    public void insertMovies(List<Search> movies) throws DatabaseException;

}
