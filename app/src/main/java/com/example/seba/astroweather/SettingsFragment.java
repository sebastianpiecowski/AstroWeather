package com.example.seba.astroweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.prefs.PreferenceChangeListener;

public class SettingsFragment extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_main);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if ( key.equals("latitude") || key.equals("longitude") ) {
            MainActivity.updateCoordinates();
            SunFragment.updatePrefsAndRefresh();
            MoonFragment.updatePrefsAndRefresh();
        }
        if( key.equals("sync_interval")) {
            SunFragment.updatePrefsAndRefresh();
            MoonFragment.updatePrefsAndRefresh();
        }
    }

}
