package com.example.myfilms.database;

import com.example.myfilms.Search;

import java.util.List;

public interface Database {

    public List<Search> getMovies();
    public void insertMovie(Search movie);
    public void insertMovies(List<Search> movies);

}
