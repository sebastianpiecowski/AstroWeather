package com.example.seba.astroweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.seba.astroweather.fragment.ForecastFragment;
import com.example.seba.astroweather.fragment.WeatherFragment;


public class Astro2 extends AppCompatActivity  {
    static SharedPreferences sharedPrefs;
    static TextView longitude;
    static TextView latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration configInfo = getResources().getConfiguration();
        sharedPrefs= getSharedPreferences("preferences", 0);


        if(configInfo.orientation == Configuration.ORIENTATION_PORTRAIT && !isTablet(getBaseContext())) {
            setContentView(R.layout.activity2_portrait);
            ViewPager pager = findViewById(R.id.container);
            pager.setAdapter(new Astro2.SectionsPagerAdapter(getSupportFragmentManager()));
            TabLayout tabLayout = findViewById(R.id.tabs);
            latitude=findViewById(R.id.latitude);
            longitude=findViewById(R.id.longitude);
            latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
            longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        }
        else if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet(getBaseContext())) {
            setContentView(R.layout.activity_landscape);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            Fragment weatherFragment= WeatherFragment.newInstance();
            Fragment forecastFragment = ForecastFragment.newInstance();
            latitude=findViewById(R.id.latitude);
            longitude=findViewById(R.id.longitude);
            latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
            longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
            fragmentTransaction.replace(R.id.sunTest, weatherFragment);
            fragmentTransaction.replace(R.id.moonTest, forecastFragment);
            fragmentTransaction.commit();
        }
        else if(configInfo.orientation == Configuration.ORIENTATION_PORTRAIT && isTablet(getBaseContext())) {
            setContentView(R.layout.activity_tablet_portrait);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            Fragment weatherFragment=WeatherFragment.newInstance();
            Fragment forecastFragment=ForecastFragment.newInstance();
            Toolbar toolbar=findViewById(R.id.toolbar);
            toolbar.setTitle(toolbar.getTitle()+" - tablet v.");
            latitude=findViewById(R.id.latitude);
            longitude=findViewById(R.id.longitude);
            latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
            longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
            fragmentTransaction.replace(R.id.sunTest, weatherFragment);
            fragmentTransaction.replace(R.id.moonTest, forecastFragment);
            fragmentTransaction.commit();
        }
        else if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE && isTablet(getBaseContext())){
            setContentView(R.layout.activity_tablet_landscape);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            Fragment weatherFragment= WeatherFragment.newInstance();
            Fragment forecastFragment= ForecastFragment.newInstance();
            Toolbar toolbar=findViewById(R.id.toolbar);
            toolbar.setTitle(toolbar.getTitle()+" - tablet v.");
            latitude=findViewById(R.id.latitude);
            longitude=findViewById(R.id.longitude);
            latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
            longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
            fragmentTransaction.replace(R.id.sunTest, weatherFragment);
            fragmentTransaction.replace(R.id.moonTest, forecastFragment);
            fragmentTransaction.commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent modifySettings = new Intent(Astro2.this, SettingsFragment.class);
            startActivity(modifySettings);
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return WeatherFragment.newInstance();
                case 1: return ForecastFragment.newInstance();
                default: return WeatherFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    public static boolean isTablet(Context ctx){
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public static void setTopGeographicalLocation(String x, String y) {
        latitude.setText(x);
        longitude.setText(y);
    }
}
