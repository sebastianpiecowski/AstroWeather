package com.example.seba.astroweather.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtil extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE = "create table cities (id integer primary key autoincrement, city text not null);";
    public DatabaseUtil(Context context) {
        super(context, "cities.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
