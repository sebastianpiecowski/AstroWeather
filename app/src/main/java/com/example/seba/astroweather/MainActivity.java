package com.example.seba.astroweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration configInfo = getResources().getConfiguration();


        if(configInfo.orientation == Configuration.ORIENTATION_PORTRAIT && !isTablet(getBaseContext())) {
            setContentView(R.layout.activity_portrait);
            ViewPager pager = (ViewPager) findViewById(R.id.container);
            pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
            TabLayout tabLayout = findViewById(R.id.tabs);

            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        }
        else if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE || isTablet(getBaseContext())) {
            setContentView(R.layout.activity_landscape);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
            Fragment sunFragment=SunFragment.newInstance();
            Fragment moonFragment=MoonFragment.newInstance();
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
                //refresh dla danego fragmentu
                Snackbar.make(view, "Update widokow", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //tabLayout.getSelectedTabPosition()
            }
        });
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

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
            Intent modifySettings = new Intent(MainActivity.this, SettingsFragment.class);
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
                case 0: return SunFragment.newInstance();
                case 1: return MoonFragment.newInstance();
                default: return SunFragment.newInstance();
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
}
