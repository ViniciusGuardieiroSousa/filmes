package com.example.myfilms.constants;

public final class MovieTableConstants {

    public static String TABLE_NAME = "Movies";
    public static String TABLE_ID = "id";
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_YEAR = "year";
    public static String COLUMN_IMDB_ID = "imdbID";
    public static String COLUMN_TYPE = "type";
    public static String COLUMN_POSTER = "poster";

    public static String TABLE_MOVIE_SQL_COMMAND =
            "CREATE TABLE IF NOT EXISTS '" + TABLE_NAME +
                    "'('" + TABLE_ID + "' INTEGER  PRIMARY KEY AUTOINCREMENT ,'" + COLUMN_TITLE +
                    "' VARCHAR, '" + COLUMN_YEAR + "' VARCHAR, '" + COLUMN_IMDB_ID +
                    "' VARCHAR, '" + COLUMN_TYPE + "' VARCHAR, '" + COLUMN_POSTER + "' VARCHAR )";

}
