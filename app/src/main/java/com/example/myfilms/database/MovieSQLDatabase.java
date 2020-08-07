package com.example.myfilms.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Base64;

import com.example.myfilms.Search;
import com.example.myfilms.exceptions.DatabaseException;

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

    public MovieSQLDatabase(
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

    }

    @Override
    public ArrayList<Search> getMovies() throws DatabaseException {
        try {
            Cursor cursor = database.rawQuery(GET_MOVIES_ON_TABLE, null);
            return getMoviesFromCursor(cursor);
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    private ArrayList<Search> getMoviesFromCursor(Cursor cursor) throws DatabaseException {
        Map<String, Integer> columnIndexes = getColumnIndexes(cursor);
        cursor.moveToFirst();
        ArrayList<Search> moviesSaved = new ArrayList<>();
        while (cursor != null) {
            Search movie = getMovieFromCursor(cursor, columnIndexes);
            moviesSaved.add(movie);
            cursor.moveToNext();
        }
        return moviesSaved;
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

    private Search getMovieFromCursor(
            Cursor cursor,
            Map<String, Integer> columnIndexes
    ) throws DatabaseException {
        try{
            Search movie = new Search();
            movie.setTitle(cursor.getString(columnIndexes.get(COLUMN_TITLE)));
            movie.setYear(cursor.getString(columnIndexes.get(COLUMN_YEAR)));
            movie.setImdbID(cursor.getString(columnIndexes.get(COLUMN_IMDB_ID)));
            movie.setType(cursor.getString(columnIndexes.get(COLUMN_TYPE)));
            String poster = cursor.getString(columnIndexes.get(COLUMN_POSTER));
            byte[] posterDecoded = Base64.decode(poster, Base64.DEFAULT);
            movie.setImage(posterDecoded);
            return movie;
        }catch(Exception exception){
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void insertMovie(Search movie) throws DatabaseException {
        try {
            executeCommand(getInsertCommand(movie));
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void insertMovies(List<Search> movies) throws DatabaseException {
        for (Search movie : movies) {
            insertMovie(movie);
        }
    }

    public String getInsertCommand(Search movie) {
        String titleNormalized = threatApostrophe(movie.getTitle());
        String encodeImage = Base64.encodeToString(movie.getImage(), Base64.DEFAULT);
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
                + encodeImage + "'" +
                ")";
    }

    private String threatApostrophe(String text) {
        StringBuilder result = new StringBuilder();
        int textSize = text.length();
        for (int i = 0; i < textSize; i++) {
            char character = text.charAt(i);
            if (isApostrophe(character)) {
                result.append("\'");
            }
            result.append(character);
        }
        return result.toString();
    }

    private boolean isApostrophe(char character) {
        return character == '\'';
    }
}