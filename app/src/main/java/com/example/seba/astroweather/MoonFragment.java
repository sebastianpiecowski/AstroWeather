package com.example.seba.astroweather;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MoonFragment extends Fragment {
    static Calendar calendar= Calendar.getInstance();
    static TextView moonrise;
    static TextView harvest;
    static TextView phase;
    static TextView synodic;
    static String syncInterval;
    static SharedPreferences preferences;
    static AstroDateTime datetime;
    static MoonFragment newInstance() {
        MoonFragment f = new MoonFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.moon_main, container, false);
        moonrise = view.findViewById(R.id.moonrise);
        harvest=view.findViewById(R.id.harvestMoon);
        phase=view.findViewById(R.id.phaseMoon);
        synodic=view.findViewById(R.id.daySynodic);

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
        DecimalFormat dec = new DecimalFormat("#0.00");
        AstroCalculator astroCalculator=new AstroCalculator(datetime, new AstroCalculator.Location(latitude,longitude));
        String rise="rise:"+formatAstroDateTime(astroCalculator.getMoonInfo().getMoonrise().toString())+"   down:"+formatAstroDateTime(astroCalculator.getMoonInfo().getMoonset().toString());
        String newMoon=formatAstroGetDate(astroCalculator.getMoonInfo().getNextNewMoon().toString());
        String har=formatAstroGetDate(astroCalculator.getMoonInfo().getNextFullMoon().toString());
        String ph=dec.format(astroCalculator.getMoonInfo().getIllumination())+"%";
        String age=dec.format(astroCalculator.getMoonInfo().getAge());
        moonrise.setText(rise);
        harvest.setText("New moon: "+newMoon+"\nFull moon: "+har);
        phase.setText(ph);
        synodic.setText(age);


    }
    private static void fetchInter(){
        syncInterval=preferences.getString("sync_interval", "60000");
    }
    private static String formatAstroDateTime(String dateString){
        return dateString.substring(10,19);
    }
    private static String formatAstroGetDate(String dateString){
        return dateString.substring(0,10);
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
