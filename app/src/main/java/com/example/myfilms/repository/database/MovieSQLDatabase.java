package com.example.myfilms.repository.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.example.myfilms.mapper.DBMovieToMovieMapper;
import com.example.myfilms.mapper.ListMapper;
import com.example.myfilms.mapper.ListMapperImpl;
import com.example.myfilms.mapper.Mapper;
import com.example.myfilms.mapper.MovieToDBMovie;
import com.example.myfilms.repository.dtos.DBMovie;
import com.example.myfilms.exceptions.DatabaseException;
import com.example.myfilms.ui.domainModel.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myfilms.constants.MovieTableConstants.COLUMN_IMDB_ID;
import static com.example.myfilms.constants.MovieTableConstants.COLUMN_POSTER;
import static com.example.myfilms.constants.MovieTableConstants.COLUMN_TITLE;
import static com.example.myfilms.constants.MovieTableConstants.COLUMN_TYPE;
import static com.example.myfilms.constants.MovieTableConstants.COLUMN_YEAR;
import static com.example.myfilms.constants.MovieTableConstants.TABLE_NAME;
import static com.example.myfilms.constants.MovieTableConstants.TABLE_ID;

public class MovieSQLDatabase extends SQLDatabase implements MovieDatabase {

    private static String CREATE_TABLE_MOVIE_SQL_COMMAND =
            "CREATE TABLE IF NOT EXISTS '" + TABLE_NAME + "'('"
                    + TABLE_ID + "' INTEGER  PRIMARY KEY AUTOINCREMENT ,'"
                    + COLUMN_TITLE + "' VARCHAR, '"
                    + COLUMN_YEAR + "' VARCHAR, '"
                    + COLUMN_IMDB_ID + "' VARCHAR, '"
                    + COLUMN_TYPE + "' VARCHAR, '"
                    + COLUMN_POSTER + "' VARCHAR" + " )";

    private static String GET_MOVIES_ON_TABLE = "SELECT * FROM " + TABLE_NAME;
    private ListMapper<DBMovie, Movie> dbMovieListMapperToMovieList;
    private Mapper<DBMovie, Movie> dbMovieMapperToMovie = new DBMovieToMovieMapper();
    private Mapper<Movie, DBMovie> movieMapperToDBMovie = new MovieToDBMovie();


    MovieSQLDatabase(
            Context context,
            String sqlName,
            int tableMode
    ) throws DatabaseException {
        super(context, sqlName, tableMode);
        try {
            createTable(CREATE_TABLE_MOVIE_SQL_COMMAND);
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
        dbMovieListMapperToMovieList = new ListMapperImpl<>(dbMovieMapperToMovie);
    }

    @Override
    public List<Movie> getMovies() throws DatabaseException {
        try {
            Cursor cursor = database.rawQuery(GET_MOVIES_ON_TABLE, null);
            List<DBMovie> moviesSaved = getMoviesFromCursor(cursor);
            List<Movie> listMovieMapped = dbMovieListMapperToMovieList.map(moviesSaved);
            return listMovieMapped;
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
    public void insertMovie(Movie movie) throws DatabaseException {
        try {
            DBMovie dbMovie = movieMapperToDBMovie.map(movie);
            executeCommand(getInsertCommand(dbMovie));
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void insertMovies(List<Movie> movies) throws DatabaseException {
        for (Movie movie : movies) {
            insertMovie(movie);
        }
    }

    private String getInsertCommand(DBMovie movie) {
        String titleNormalized = threatApostrophe(movie.getTitle());
        return "INSERT INTO " + TABLE_NAME + "("
                + COLUMN_TITLE + ", "
                + COLUMN_YEAR + ", "
                + COLUMN_IMDB_ID + ", "
                + COLUMN_TYPE + ","
                + COLUMN_POSTER
                + ") VALUES('"
                + titleNormalized + "','"
                + movie.getYear() + "','"
                + movie.getImdbID() + "','"
                + movie.getType() + "','"
                + movie.getImage() + "'" +
                ")";
    }

    private String threatApostrophe(String text) {
        StringBuilder result = new StringBuilder();
        int textSize = text.length();
        for (int i = 0; i < textSize; i++) {
            char character = text.charAt(i);
            if (isApostrophe(character)) {
                result.append("'");
            }
            result.append(character);
        }
        return result.toString();
    }

    private boolean isApostrophe(char character) {
        return character == '\'';
    }
}