package com.example.seba.astroweather.fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seba.astroweather.Astro2;
import com.example.seba.astroweather.R;
import com.example.seba.astroweather.SettingsFragment;
import com.example.seba.astroweather.util.Database;
import com.example.seba.astroweather.util.DownLoadImageTask;
import com.example.seba.astroweather.util.WeatherDownloader;

import org.json.JSONObject;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherFragment extends Fragment{

    SharedPreferences prefs;

    Timer t = new Timer();

    TimerTask mTimerTask;
    Handler handler = new Handler();
    ImageView imageView;
    TextView cityWeather;
    TextView temperature;
    TextView condition;
    EditText humidity;
    EditText pressure;
    EditText windSpeed;
    EditText visibility;

    SwipeRefreshLayout swipeRefreshWeather;

    public WeatherFragment() {}

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        prefs = getActivity().getSharedPreferences("preferences", 0);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        imageView = v.findViewById(R.id.imageView);
        cityWeather = v.findViewById(R.id.cityweather);
        temperature = v.findViewById(R.id.temperature);
        condition = v.findViewById(R.id.condition);
        humidity = v.findViewById(R.id.humidity);
        pressure = v.findViewById(R.id.pressure);
        windSpeed = v.findViewById(R.id.windspeed);
        visibility = v.findViewById(R.id.visibility);

        swipeRefreshWeather = v.findViewById(R.id.swipeRefreshWeather);

        t.purge();
        mTimerTask = new TimerTask() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                            updateData(prefs.getString("city", ""));
                    }
                });
            }
        };
        t = new Timer();
        t.scheduleAtFixedRate(mTimerTask, 0, (prefs.getInt("refreshRate", 1) * 1000 * 60));

        swipeRefreshWeather.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateData(prefs.getString("city", ""));
                    }
                }
        );
    }

    public void updateData(final String city) {
        new Thread() {
            public void run() {
                JSONObject json;
                if(prefs.getString("latitude", "1").equals("1") && prefs.getString("longitude", "1").equals("1")) {
                    json = WeatherDownloader.getJSON(getContext(), handler, city, prefs.getString("unit", "c"));
                }
                else {
                    json = WeatherDownloader.getJSON(getContext(), handler, prefs.getString("longitude", "1"),prefs.getString("latitude", "1"), prefs.getString("unit", "c"));
                    final JSONObject prepareJSON = json;
                    try{
                    JSONObject _results = prepareJSON.getJSONObject("results").getJSONObject("channel");
                    JSONObject _location = _results.getJSONObject("location");
                    SettingsFragment.test(prefs.getString("city", ""), _location.getString("city"));
                    }
                    catch (Exception e) {
                        alertMessage("Unexpected exception during replace city name");
                    }
                }

                    if (json == null) {
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    alertMessage("Location not found");

                                }
                            });
                        }
                        catch (Exception e) {
                            try {
                                synchronized (WeatherFragment.this) {
                                    WeatherFragment.this.wait(100);
                                }
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        final JSONObject prepareJSON = json;
                        handler.post(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void run() {
                                try {

                                    JSONObject _results = prepareJSON.getJSONObject("results").getJSONObject("channel");
                                    JSONObject _units = _results.getJSONObject("units");
                                    JSONObject _info = _results.getJSONObject("item");
                                    JSONObject _condition = _info.getJSONObject("condition");
                                    JSONObject _location = _results.getJSONObject("location");
                                    JSONObject _wind = _results.getJSONObject("wind");
                                    JSONObject _atmosphere = _results.getJSONObject("atmosphere");

                                    SharedPreferences.Editor spEditor = prefs.edit();

                                    spEditor.putString("longitude", _info.getString("long")).apply();
                                    spEditor.putString("latitude", _info.getString("lat")).apply();
                                    spEditor.putString("city", _location.getString("country"));
                                    Astro2.setTopGeographicalLocation(_info.getString("lat"), _info.getString("long"));
                                    int conditionCode=_condition.getInt("code");
                                    try {
                                        new DownLoadImageTask(imageView).execute("http://l.yimg.com/a/i/us/we/52/" + conditionCode + ".gif");
                                    }catch(Exception e) {
                                        imageView.setImageDrawable(Drawable.createFromPath("weather.png"));
                                    }

                                    cityWeather.setText((_location.getString("city") + ", " + _location.getString("country")));
                                    temperature.setText((_condition.getString("temp") + _units.getString("temperature")));
                                    condition.setText(_condition.getString("text"));

                                    humidity.setText((_atmosphere.getString("humidity") + "%"));
                                    pressure.setText((_atmosphere.getString("pressure") + _units.getString("pressure")));
                                    windSpeed.setText((_wind.getString("speed") + _units.getString("speed")));
                                    visibility.setText((_atmosphere.getString("visibility") + "%"));
                                } catch (Exception e) {
                                    alertMessage("Unexpected error");
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
            }
        }.start();
        swipeRefreshWeather.setRefreshing(false);
    }

    @Override
    public void onResume () {
        super.onResume();
        updateData(prefs.getString("city", ""));
    }

    private void alertMessage(String Message) {
        Toast.makeText(this.getContext(), Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause () {
        super.onPause();
        t.cancel();
    }
}