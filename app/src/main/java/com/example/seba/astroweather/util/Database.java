package com.example.seba.astroweather.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.seba.astroweather.model.City;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private SQLiteDatabase database;
    private DatabaseUtil dbUtil;
    private String[] allColumns = { "id", "city"};

    public Database(Context context) {
        dbUtil = new DatabaseUtil(context);
    }

    public void open() throws SQLException {
        database = dbUtil.getWritableDatabase();
    }

    public City createCity(String city) {
        ContentValues values = new ContentValues();
        values.put("city", city);
        long id = database.insert("cities", null,
                values);
        Cursor cursor = database.query("cities",
                allColumns, "id" + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        City newCity = cursorToCity(cursor);
        cursor.close();
        return newCity;
    }

    public void deleteCity(String city) {
        database.delete("cities", "city =  ?", new String[] { city });
    }
    public void renameCity(String old, String city){
        deleteCity(old);
        createCity(city);


    }
    public List<String> getAllCities() {
        List<String> cities = new ArrayList<>();

        Cursor cursor = database.query("cities",
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = cursorToCity(cursor);
            cities.add(city.getName());
            cursor.moveToNext();
        }
        cursor.close();
        return cities;
    }

    private City cursorToCity(Cursor cursor) {
        City city = new City();
        city.setId(cursor.getLong(0));
        city.setName(cursor.getString(1));
        return city;
    }
}