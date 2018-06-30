package com.example.seba.astroweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.seba.astroweather.fragment.MoonFragment;
import com.example.seba.astroweather.fragment.SunFragment;

import static com.example.seba.astroweather.fragment.SunFragment.*;

public class Astro1 extends AppCompatActivity {
    static SharedPreferences sharedPrefs;
    static TextView longitude;
    static TextView latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration configInfo = getResources().getConfiguration();
        sharedPrefs= getSharedPreferences("preferences", 0);


        if(configInfo.orientation == Configuration.ORIENTATION_PORTRAIT && !isTablet(getBaseContext())) {
            setContentView(R.layout.activity_portrait);
            ViewPager pager = findViewById(R.id.container);
            pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
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
            Fragment sunFragment= SunFragment.newInstance();
            Fragment moonFragment= MoonFragment.newInstance();
            latitude=findViewById(R.id.latitude);
            longitude=findViewById(R.id.longitude);
            latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
            longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
            fragmentTransaction.replace(R.id.sunTest, sunFragment);
            fragmentTransaction.replace(R.id.moonTest, moonFragment);
            fragmentTransaction.commit();
        }
        else if(configInfo.orientation == Configuration.ORIENTATION_PORTRAIT && isTablet(getBaseContext())) {
            setContentView(R.layout.activity_tablet_portrait);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            Fragment sunFragment= SunFragment.newInstance();
            Fragment moonFragment=MoonFragment.newInstance();
            Toolbar toolbar=findViewById(R.id.toolbar);
            toolbar.setTitle(toolbar.getTitle()+" - tablet v.");
            latitude=findViewById(R.id.latitude);
            longitude=findViewById(R.id.longitude);
            latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
            longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
            fragmentTransaction.replace(R.id.sunTest, sunFragment);
            fragmentTransaction.replace(R.id.moonTest, moonFragment);
            fragmentTransaction.commit();
        }
        else if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE && isTablet(getBaseContext())){
            setContentView(R.layout.activity_tablet_landscape);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            Fragment sunFragment= newInstance();
            Fragment moonFragment=MoonFragment.newInstance();
            Toolbar toolbar=findViewById(R.id.toolbar);
            toolbar.setTitle(toolbar.getTitle()+" - tablet v.");
            latitude=findViewById(R.id.latitude);
            longitude=findViewById(R.id.longitude);
            latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
            longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
            fragmentTransaction.replace(R.id.sunTest, sunFragment);
            fragmentTransaction.replace(R.id.moonTest, moonFragment);
            fragmentTransaction.commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePrefsAndRefresh();
                MoonFragment.updatePrefsAndRefresh();
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
    public static void updateCoordinates(){
        latitude.setText(" latitude: "+sharedPrefs.getString("latitude", "0"));
        longitude.setText("longitude: "+sharedPrefs.getString("longitude", "0"));
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
            Intent modifySettings = new Intent(Astro1.this, SettingsFragment.class);
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
                case 0: return newInstance();
                case 1: return MoonFragment.newInstance();
                default: return newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    public static void changeLatLong(){

    }
    public static boolean isTablet(Context ctx){
        return (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
