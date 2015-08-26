package com.akaya.apps.burcler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TabBurcler extends Fragment {

    String[] cons;
    ArrayList<Integer> imageId;
    ListView list;
    CustomList adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView list;
        cons = new String[12];
        for( int i = 0; i < 12; i++ ) {
            cons[i] = getConName(i);
        }

        imageId = new ArrayList<Integer>();

        imageId.add(R.drawable.c0);
        imageId.add(R.drawable.c1);
        imageId.add(R.drawable.c2);
        imageId.add(R.drawable.c3);
        imageId.add(R.drawable.c4);
        imageId.add(R.drawable.c5);
        imageId.add(R.drawable.c6);
        imageId.add(R.drawable.c7);
        imageId.add(R.drawable.c8);
        imageId.add(R.drawable.c9);
        imageId.add(R.drawable.c10);
        imageId.add(R.drawable.c11);

        adapter = new
                CustomList(getActivity(), cons, imageId);
//        imageId = {
//                R.drawable.c0,
//                R.drawable.c1,
//                R.drawable.c2,
//                R.drawable.c3,
//                R.drawable.c4,
//                R.drawable.c5,
//                R.drawable.c6,
//                R.drawable.c7,
//                R.drawable.c8,
//                R.drawable.c9,
//                R.drawable.c10,
//                R.drawable.c11,
//        };



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.burcler_layout, container, false);


        list=(ListView) v.findViewById(R.id.lv_constellations);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });

        TextView tv = (TextView) v.findViewById(R.id.text);
        String test = "";
//        tv.setText(Html.fromHtml(readFile("test")));
        return v;
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    private String getConName(int i){
        int id = getResources().getIdentifier("str_c"+i, "string", getActivity().getPackageName());
        int idd = getResources().getIdentifier("str_d"+i, "string", getActivity().getPackageName());
        return getResources().getString(id) + " " +getResources().getString(idd);
    }



}