package com.example.seba.astroweather;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsFragment extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_main);

    }
}
