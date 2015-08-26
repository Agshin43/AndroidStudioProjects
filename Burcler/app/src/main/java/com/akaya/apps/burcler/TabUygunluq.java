package com.akaya.apps.burcler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TabUygunluq extends Fragment {


    ConstellationsView constellationsView;
    boolean conViewAdded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        constellationsView = new ConstellationsView(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.uygunluq_layout, container, false);

        LinearLayout layConstellations = (LinearLayout) v.findViewById(R.id.layConstellations);

        if(constellationsView.getParent()!=null)
            ((ViewGroup)constellationsView.getParent()).removeView(constellationsView);
        layConstellations.addView(constellationsView);

        return v;
    }
    @Override
    public void onResume(){
        super.onResume();

    }





}