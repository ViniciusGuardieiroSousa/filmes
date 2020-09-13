package com.example.myfilms.repository.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.repository.dtos.DBMovie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_IMDB_ID;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_POSTER;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_TITLE;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_TYPE;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_YEAR;

public class MovieSQLDatabase extends SQLDatabase implements MovieDatabase {

    MovieSQLDatabase(
            Context context,
            String sqlName,
            int tableMode
    ) throws DatabaseException {
        super(context, sqlName, tableMode);
        try {
            createTable(MovieSQLCommands.CREATE_TABLE_MOVIE_SQL_COMMAND);
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public List<DBMovie> getMovies() throws DatabaseException {
        try {
            Cursor cursor = database.rawQuery(MovieSQLCommands.GET_MOVIES_ON_TABLE, null);
            return getMoviesFromCursor(cursor);
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    private ArrayList<DBMovie> getMoviesFromCursor(Cursor cursor) throws DatabaseException,
            NullPointerException {
        cursor.moveToFirst();
        ArrayList<DBMovie> moviesSaved = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                DBMovie movie = getMovieFromCursor(cursor);
                moviesSaved.add(movie);
                cursor.moveToNext();
            }
        }
        return moviesSaved;
    }

    private DBMovie getMovieFromCursor(
            Cursor cursor
    ) throws DatabaseException, NullPointerException {
        try {
            Map<String, Integer> columnIndexes = getColumnIndexes(cursor);
            DBMovie movie = new DBMovie();
            movie.setTitle(cursor.getString(columnIndexes.get(COLUMN_TITLE)));
            movie.setYear(cursor.getString(columnIndexes.get(COLUMN_YEAR)));
            movie.setImdbID(cursor.getString(columnIndexes.get(COLUMN_IMDB_ID)));
            movie.setType(cursor.getString(columnIndexes.get(COLUMN_TYPE)));
            String poster = cursor.getString(columnIndexes.get(COLUMN_POSTER));
            movie.setImage(poster);
            return movie;
        } catch (Exception exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    private Map<String, Integer> getColumnIndexes(Cursor cursor) {
        HashMap<String, Integer> columnIndexes = new HashMap<>();
        columnIndexes.put(COLUMN_TITLE, cursor.getColumnIndex(COLUMN_TITLE));
        columnIndexes.put(COLUMN_YEAR, cursor.getColumnIndex(COLUMN_YEAR));
        columnIndexes.put(COLUMN_IMDB_ID, cursor.getColumnIndex(COLUMN_IMDB_ID));
        columnIndexes.put(COLUMN_TYPE, cursor.getColumnIndex(COLUMN_TYPE));
        columnIndexes.put(COLUMN_POSTER, cursor.getColumnIndex(COLUMN_POSTER));
        return columnIndexes;
    }

    @Override
    public void insertMovie(DBMovie movie) throws DatabaseException {
        try {
            executeCommand(MovieSQLCommands.getInsertCommand(movie));
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void insertMovies(List<DBMovie> movies) throws DatabaseException {
        for (DBMovie movie : movies) {
            insertMovie(movie);
        }
    }
}
