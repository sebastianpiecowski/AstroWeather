package com.example.seba.astroweather.fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.seba.astroweather.R;

import com.example.seba.astroweather.util.DownLoadImageTask;
import com.example.seba.astroweather.util.WeatherDownloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ForecastFragment extends Fragment {
    List<ImageView> forecastImages=new ArrayList<>();
    List<EditText> forecastEditText=new ArrayList<>();
    SharedPreferences prefs;
    SwipeRefreshLayout swipeRefreshWeather;
    Timer t = new Timer();
    TimerTask mTimerTask;
    Handler handler=new Handler();

    public static Fragment newInstance() {
        ForecastFragment fragment = new ForecastFragment();
        return fragment;
    }

    public ForecastFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forecast, container, false);
        prefs = getActivity().getSharedPreferences("preferences", 0);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (int i=1; i<8; i++) {
            try {
                forecastImages.add((ImageView) view.findViewById(getResources().getIdentifier("forecastImage" + i, "id", getContext().getPackageName())));
                forecastEditText.add((EditText) view.findViewById(getResources().getIdentifier("forecast" + i, "id", getContext().getPackageName())));
            }
            catch (Resources.NotFoundException e) {
                onViewCreated(view, savedInstanceState);
            }
        }

        swipeRefreshWeather = view.findViewById(R.id.swipeRefreshWeather);

        t.purge();

        mTimerTask = new TimerTask() {
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            updateData(prefs.getString("city", ""));
                        }
                    });
                }
                catch(Exception e) {
                    try {
                        synchronized (ForecastFragment.this) {
                            ForecastFragment.this.wait(100);
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
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
                json = WeatherDownloader.getJSON(getContext(), handler, city, prefs.getString("unit", "c"));


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
                            synchronized (ForecastFragment.this){
                                ForecastFragment.this.wait(100);
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
                                JSONArray forecast = _results.getJSONObject("item").getJSONArray("forecast");

                               // JSONObject _info = _results.getJSONObject("item");

                               // Astro2.setTopGeographicalLocation(_info.getString("lat"), _info.getString("long"));

                                for (int i=1; i<8; i++){
                                    JSONObject currentDay=forecast.getJSONObject(i);
                                    int conditionCode=currentDay.getInt("code");
                                    try {
                                        new DownLoadImageTask(forecastImages.get(i-1)).execute("http://l.yimg.com/a/i/us/we/52/" + conditionCode + ".gif");
                                    }catch(Exception e) {
                                        forecastImages.get(i-1).setImageDrawable(Drawable.createFromPath("weather.png"));
                                    }
                                    //forecastEditText.get(0).setText("hahahha");
                                    forecastEditText.get(i-1).setText(currentDay.getString("date")+" "+currentDay.getString("day")+ " "+currentDay.getInt("low")+"   "+currentDay.getInt("high"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                alertMessage(e.getMessage());
                                alertMessage("Unexpected error");

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
