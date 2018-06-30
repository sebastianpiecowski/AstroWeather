package com.example.seba.astroweather;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.seba.astroweather.util.Database;

import java.util.Iterator;
import java.util.List;

public class SettingsFragment extends AppCompatActivity {

    EditText longitude;
    EditText latitude;
    EditText city;
    Spinner cities;
    Spinner refreshrate;
    Spinner units;
    SharedPreferences prefs;
    static Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        db = new Database(this);
        db.open();

        saveElements();

        initLatitudeLongitude();
        initCities();
        initRefreshRate();
        initUnit();
        configureAdd(R.id.btnAdd);
        configureRemove(R.id.btnRemove);
        configureSave(R.id.btnSave);
    }

    private void saveElements(){
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        city = findViewById(R.id.city);
        cities = findViewById(R.id.spinnerCities);
        refreshrate = findViewById(R.id.spinnerRefreshrate);
        units = findViewById(R.id.spinnerUnit);
        prefs = getSharedPreferences("preferences", 0);
    }
    public static void test(String old, String city){
        db.renameCity(old,city);

    }
    private void initLatitudeLongitude(){

        longitude.setText("1");
        latitude.setText("1");
    }

    private void initCities() {
        List<String> cityArraySpinner = db.getAllCities();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityArraySpinner);
        cities.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(prefs.getString("city", ""));
        cities.setSelection(spinnerPosition);
    }

    private void initRefreshRate() {
        String[] frequencyArraySpinner = new String[] {"1", "5", "10", "15"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencyArraySpinner);
        refreshrate.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(String.valueOf(prefs.getInt("refreshrate", 1)));
        refreshrate.setSelection(spinnerPosition);
    }

    private void initUnit() {
        String[] unitArraySpinner = new String[] {"metric", "imperial"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitArraySpinner);
        units.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(prefs.getString("unit", "metric"));
        units.setSelection(spinnerPosition);
    }

    private void configureAdd(int resource) {
        (findViewById(resource)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = city.getText().toString();
                if (cityName.equals("")) {
                    alertMsg("City must not be empty.");
                    return;
                }

                List<String> cities = db.getAllCities();
                for (Iterator<String> iterator = cities.iterator(); iterator.hasNext(); ) {
                    String city = iterator.next();
                    if (city.equals(cityName)) {
                        alertMsg("City already exists.");
                        return;
                    }
                }

                db.createCity(cityName);
                initCities();

                alertMsg(cityName + " added.");
            }
        });
    }

    private void configureRemove(int resource) {
        (findViewById(resource)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cities.getSelectedItem() == null) {
                    alertMsg("City list empty.");
                    return;
                }
                String cityName = cities.getSelectedItem().toString();
                db.deleteCity(cityName);
                initCities();

                alertMsg(cityName + " removed.");
            }
        });
    }

    private void configureSave(int resource) {
        (findViewById(resource)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (longitude.getText().toString().equals("")) {
                    longitude.setText(prefs.getString("longitude", "1"));
                }
                else {
                    if (new Double(longitude.getText().toString()) > 90) {
                        alertMsg("Wrong longitude");
                        return;
                    }
                    if (new Double(longitude.getText().toString()) < -90) {
                        alertMsg("Wrong longitude");
                        return;
                    }
                }
                if (latitude.getText().toString().equals("")) {
                    latitude.setText(prefs.getString("latitude", "1"));
                }
                else {
                    if (new Double(latitude.getText().toString()) > 90) {
                        alertMsg("Wrong latitude");
                        return;
                    }
                    if (new Double(latitude.getText().toString()) < -90) {
                        alertMsg("Wrong latitude");
                        return;
                    }
                }
                if (cities.getSelectedItem() == null) {
                    alertMsg("Add at least one city.");
                    return;
                }

                SharedPreferences.Editor spEditor = prefs.edit();

                spEditor.putString("longitude", longitude.getText().toString()).apply();
                spEditor.putString("latitude", latitude.getText().toString()).apply();
                spEditor.putString("city", cities.getSelectedItem().toString()).apply();
                spEditor.putInt("refreshrate", Integer.valueOf(refreshrate.getSelectedItem().toString())).apply();
                spEditor.putString("unit", units.getSelectedItem().toString()).apply();

                prefs = getSharedPreferences("preferences", 0);

                alertMsg("Settings saved.");
            }
        });
    }

    private void alertMsg(String Message)
    {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

}
