package com.example.myfilms.factory;

import android.content.Context;

import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.repository.database.MovieDatabase;
import com.example.myfilms.repository.database.MovieSQLDatabase;


public final class DatabaseFactory {
    private static MovieSQLDatabase movieSQLDatabase;

    public static MovieDatabase getMovieDataBase(
            Context context,
            String name,
            int TableMode
    ) throws DatabaseException {
        try {
            if (movieSQLDatabase == null)
                movieSQLDatabase = new MovieSQLDatabase(context, name, TableMode);
            return movieSQLDatabase;
        } catch (Exception exception) {
            throw new DatabaseException("Error to get database");
        }
    }
}
