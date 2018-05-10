package com.example.seba.astroweather;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.concurrent.TimeUnit;

public class SunFragment extends Fragment {
    static TextView time;
    static TextView sunrise;
    static TextView latitude;
    static TextView longitude;
    static TextView sundown;
    static TextView twilight;
    String syncInterval;
    static SharedPreferences preferences;
    static SunFragment newInstance() {
            return new SunFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.sun_main, container, false);
        time=view.findViewById(R.id.time);
        latitude=view.findViewById(R.id.latitude);
        longitude=view.findViewById(R.id.longitude);
        sunrise=view.findViewById(R.id.sunrise);
        sundown=view.findViewById(R.id.sundown);
        twilight=view.findViewById(R.id.twilight);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        setTime();
        fetchInter();
        updateValues();

        new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(Long.parseLong(syncInterval));  //1000ms = 1 sec
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fetchInter();
                                    updateValues();
                                }
                            });
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    };
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec
                        if(getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    long upTime=SystemClock.elapsedRealtime();
                                    time.setText(getTime(upTime));
                                }
                            });
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    };
                }
            }
        }.start();
        return view;
    }
    private static String getTime(long millis){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

        return hms;
    }
    private static void setTime(){
        long upTime=SystemClock.elapsedRealtime();
        time.setText(getTime(upTime));
    }
    private static void updateValues(){
        double latitude=Double.parseDouble(preferences.getString("latitude", "0"));
        double longitude=Double.parseDouble(preferences.getString("longitude", "0"));
        AstroCalculator astroCalculator=new AstroCalculator(new AstroDateTime(), new AstroCalculator.Location(latitude,longitude));
        String rise=astroCalculator.getSunInfo().getSunrise().getHour()+":"+astroCalculator.getSunInfo().getSunrise().getMinute()+":"+astroCalculator.getSunInfo().getSunrise().getSecond()+" azymut: "+astroCalculator.getSunInfo().getAzimuthRise();
        String down=astroCalculator.getSunInfo().getSunset().getHour()+":"+astroCalculator.getSunInfo().getSunset().getMinute()+":"+astroCalculator.getSunInfo().getSunset().getSecond()+" azymut: "+astroCalculator.getSunInfo().getAzimuthSet();
        sunrise.setText(rise);
        sundown.setText(down);
        twilight.setText("chuj nie dziala");
    }
    private void fetchInter(){
        syncInterval=preferences.getString("sync_interval", "60000");
    }
    public static void setLatiLongitude(String x, String y){
        latitude.setText(x);
        longitude.setText(y);
    }
}
