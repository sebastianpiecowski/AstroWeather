package com.example.seba.astroweather.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDownloader {

    public static JSONObject getJSON(Context context, final Handler handler, String urlPath) {
        try {
            URL url = new URL(urlPath);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            BufferedReader rd;
            JSONObject json;
            rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            if (rd.equals("")) {
                json = null;
            }
            else {
                System.out.println("Reading json");
                String jsonText = readAll(rd);
                json = (new JSONObject(jsonText)).getJSONObject("query");
            }
            if (json.isNull("results")) return null;
            File.save(context, json, "weather");

            return json;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            final Context context1 = context;
            handler.post(new Runnable() {
                public void run() {
                    if(!DownLoadImageTask.isOnline())
                        try {
                            if (context1 != null) {
                                Toast.makeText(context1, "No internet", Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (NullPointerException e) {
                        e.printStackTrace();
                        }
                    //handler.removeMessages(0);
                }
            });

            JSONObject json;
            try {
                json = File.read(context, "weather");
            } catch (Exception err){
                e.printStackTrace();
                return null;
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSON(Context context, Handler handler, String city, String unit) {
        String unitShort;
        if (unit.equals("metric")) unitShort = "c";
        else unitShort = "f";
        System.out.println("CITY:"+city);
        String fullUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20u=\'" + unitShort + "\'%20and%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text=\"" + city + "\")&format=json&env=store://datatables.org/alltableswithkeys";
        return getJSON(context, handler, fullUrl);
    }

    public static JSONObject getJSON(Context context, Handler handler, String longitude, String latitude, String unit) {
        String unitShort;
        if (unit.equals("metric")) unitShort = "c";
        else unitShort = "f";
        String fullUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20u=\'" + unitShort + "\'%20and%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text=\"(" + latitude + "," + longitude + ")\")&format=json&env=store://datatables.org/alltableswithkeys";
        return getJSON(context, handler, fullUrl);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
