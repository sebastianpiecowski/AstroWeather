package com.example.seba.astroweather.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.seba.astroweather.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

/**
 * Created by seba on 13.06.2018.
 */

public class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;
    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
        catch (OutOfMemoryError e){
            return false;
        }
    }
    public DownLoadImageTask(ImageView imageView){
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap logo;
        try{
            if(isOnline()) {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }
            else {
                logo = BitmapFactory.decodeResource(imageView.getResources(), R.drawable.weather);
            }
        }catch(Exception e){ // Catch the download exception
            cancel(true);
            logo = BitmapFactory.decodeResource(imageView.getResources(), R.drawable.weather);
        }
        catch (OutOfMemoryError e) {
            cancel(true);
            logo = BitmapFactory.decodeResource(imageView.getResources(), R.drawable.weather);
        }
        return logo;
    }

    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}
