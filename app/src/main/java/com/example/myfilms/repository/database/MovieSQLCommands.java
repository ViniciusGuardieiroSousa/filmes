package com.example.myfilms.repository.database;

import com.example.myfilms.repository.dtos.DBMovie;

import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_IMDB_ID;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_POSTER;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_TITLE;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_TYPE;
import static com.example.myfilms.repository.database.MovieTableConstants.COLUMN_YEAR;
import static com.example.myfilms.repository.database.MovieTableConstants.TABLE_ID;
import static com.example.myfilms.repository.database.MovieTableConstants.TABLE_NAME;

public final class MovieSQLCommands {

    public final static String CREATE_TABLE_MOVIE_SQL_COMMAND =
            "CREATE TABLE IF NOT EXISTS '" + TABLE_NAME + "'('"
                    + TABLE_ID + "' INTEGER  PRIMARY KEY AUTOINCREMENT ,'"
                    + COLUMN_TITLE + "' VARCHAR, '"
                    + COLUMN_YEAR + "' VARCHAR, '"
                    + COLUMN_IMDB_ID + "' VARCHAR, '"
                    + COLUMN_TYPE + "' VARCHAR, '"
                    + COLUMN_POSTER + "' VARCHAR" + " )";

    public final static String GET_MOVIES_ON_TABLE = "SELECT * FROM " + TABLE_NAME;

    public static String getInsertCommand(DBMovie movie) {
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

    private static String threatApostrophe(String text) {
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

    private static boolean isApostrophe(char character) {
        return character == '\'';
    }

}
