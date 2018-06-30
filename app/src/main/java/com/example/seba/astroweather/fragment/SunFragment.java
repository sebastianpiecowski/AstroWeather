package com.example.seba.astroweather.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.seba.astroweather.R;

import java.util.Calendar;

public class SunFragment extends Fragment {
    static Calendar calendar= Calendar.getInstance();
    static TextView sunrise;
    static TextView sundown;
    static TextView twilight;
    static ImageView image;
    static String syncInterval;
    static SharedPreferences preferences;
    static AstroDateTime datetime;
    public static SunFragment newInstance() {
            return new SunFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.sun_main, container, false);

        image=view.findViewById(R.id.imageView);
        sunrise=view.findViewById(R.id.sunrise);
        sundown=view.findViewById(R.id.sundown);
        twilight=view.findViewById(R.id.twilight);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        datetime=new AstroDateTime();
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
                    }
                }
            }
        }.start();
        return view;
    }
    public static void updatePrefsAndRefresh(){
        fetchInter();
        updateValues();
    }

    private static void updateValues(){
        double latitude=Double.parseDouble(preferences.getString("latitude", "0"));
        double longitude=Double.parseDouble(preferences.getString("longitude", "0"));
        setAstroDateTime();

        AstroCalculator astroCalculator=new AstroCalculator(datetime, new AstroCalculator.Location(latitude,longitude));
        String rise=formatAstroDateTime(astroCalculator.getSunInfo().getSunrise().toString())+"\n azimuth: "+astroCalculator.getSunInfo().getAzimuthRise();
        String down=formatAstroDateTime(astroCalculator.getSunInfo().getSunset().toString())+"\n azimuth: "+astroCalculator.getSunInfo().getAzimuthSet();
        String dusk=formatAstroDateTime(astroCalculator.getSunInfo().getTwilightEvening().toString());
        String daylight=formatAstroDateTime(astroCalculator.getSunInfo().getTwilightMorning().toString());
        sunrise.setText(rise);
        sundown.setText(down);
        twilight.setText("dusk:"+ dusk+"\ndaylight:"+daylight);
    }
    private static void fetchInter(){
        syncInterval=preferences.getString("sync_interval", "60000");
    }
    private static String formatAstroDateTime(String dateString){
        return dateString.substring(10,19);
    }
    private static void setAstroDateTime(){
        datetime.setYear(calendar.get(Calendar.YEAR));
        datetime.setMonth(calendar.get(Calendar.MONTH)+1);
        datetime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        datetime.setHour(calendar.get(Calendar.HOUR));
        datetime.setMinute(calendar.get(Calendar.MINUTE));
        datetime.setSecond(calendar.get(Calendar.SECOND));
    }
}
