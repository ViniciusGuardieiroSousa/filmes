package com.example.myfilms.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLDatabase {

    protected SQLiteDatabase database;

    protected SQLDatabase(Context context, String sqlName, int tableMode) throws SQLException {
        database = context.openOrCreateDatabase(sqlName, tableMode, null);
    }

    public void createTable(String tableCommand) throws SQLException {
        execCommand(tableCommand);
    }

    private void execCommand(String sqlCommand) throws SQLException {
        database.execSQL(sqlCommand);
    }


}
