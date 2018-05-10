package com.example.seba.astroweather;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MoonFragment extends Fragment {

    static MoonFragment newInstance() {
        MoonFragment f = new MoonFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.moon_main, container, false);
        TextView time = view.findViewById(R.id.time);
        TextView moonrise = view.findViewById(R.id.moonrise);
        TextView harvestMoon=view.findViewById(R.id.harvestMoon);
        TextView phaseMoon=view.findViewById(R.id.phaseMoon);
        TextView daySynodic=view.findViewById(R.id.daySynodic);
        time.setText("Device up time: ");
        moonrise.setText("Moonrise/Moondown: ");
        harvestMoon.setText("Harvest of moon: ");
        phaseMoon.setText("Phase of moon: ");
        daySynodic.setText("Synodic day: ");
        return view;
    }

}
