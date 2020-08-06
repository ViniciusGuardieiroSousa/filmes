package com.example.myfilms.database;

import android.content.Context;
import android.database.SQLException;

import com.example.myfilms.Search;

import java.util.List;

import static com.example.myfilms.constants.MovieTableConstants.TABLE_MOVIE_SQL_COMMAND;

;

public class MovieSQLDatabase extends SQLDatabase implements Database {

    public MovieSQLDatabase(Context context, String sqlName, int tableMode) throws SQLException {
        super(context, sqlName, tableMode);
        createTable(TABLE_MOVIE_SQL_COMMAND);
    }

    @Override
    public List<Search> getMovies() {
        return null;
    }

    @Override
    public void insertMovie(Search movie) {

    }

    @Override
    public void insertMovies(List<Search> movies) {

    }
}
