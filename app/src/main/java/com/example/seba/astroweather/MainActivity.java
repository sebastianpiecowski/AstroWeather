package com.example.seba.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        configureActivity(R.id.astro1Button, Astro1.class);
        configureActivity2(R.id.astro2Button, Astro2.class);
        configureExit(R.id.exitButton);
    }
    private void configureActivity(int resource, final Class activityClass) {
        ((Button) findViewById(resource)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, activityClass));
            }
        });
    }
    private void configureActivity2(int resource, final Class activityClass) {
        ((Button) findViewById(resource)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("preferences", 0);
                if (sp.contains("city")) startActivity(new Intent(MainActivity.this, activityClass));
                else {
                    alert("Add new location before checking weather.");
                    startActivity(new Intent(MainActivity.this, SettingsFragment.class));
                }
            }
        });
    }
    private void configureExit(int resource) {
        ((Button)findViewById(resource)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        finish();
                        System.exit(0);
            }

        });
    }
    private void alert(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }
}