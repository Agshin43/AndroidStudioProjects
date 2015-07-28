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

    ArrayList<Constellation> maleConstellations;
    ArrayList<Constellation> femaleConstellations;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preInitConstellations();
        loadConstellations("uygunluq");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.uygunluq_layout, container, false);


        LinearLayout layConstellations = (LinearLayout) v.findViewById(R.id.layConstellations);
        layConstellations.addView(new ConstellationsView(getActivity()));

        return v;
    }


    private void loadConstellations(String fileName)
    {
        try {
            int rID = getResources().getIdentifier(getActivity().getPackageName()+":raw/" + fileName, null, null);
            InputStream inputStream = getResources().openRawResource(rID);

            if (inputStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String l;

                int mc = 0;
                while (( l = bufferedReader.readLine()) != null) {

                    String[] ll = l.split("#");
                    String[] lm = ll[0].split("-");
                    String name1 = lm[0];
                    String name2 = lm[1];

                    Log.i("", "M iterator "+(mc / 12));
                    maleConstellations.get(mc / 12).accordanceArray.add(new Accordance(ll[2], Integer.valueOf(ll[1])));
                    femaleConstellations.get(mc % 12).accordanceArray.get(mc / 12).SetValues(ll[2], Integer.valueOf(ll[1]));
                    mc++;
                }

            }

            inputStream.close(); //close the file
        } catch (java.io.FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0;i < 12; i++)
        {
            Log.i("","MALE "+maleConstellations.get(i).accordanceArray.get(0).accordance);
        }
        for(int i = 0;i < 12; i++)
        {
            Log.i("","FEMALE "+femaleConstellations.get(i).accordanceArray.get(0).accordance);
        }
    }

    private Constellation genConstellation(String line)
    {
        Constellation constellation = new Constellation();
        return  constellation;
    }

    private void preInitConstellations()
    {
        maleConstellations = new ArrayList<Constellation>();
        femaleConstellations = new ArrayList<Constellation>();
        for(int i = 0; i < 12; i++)
        {
            maleConstellations.add(new Constellation());
            femaleConstellations.add(new Constellation());
        }

        for(int i = 0; i < 12; i++)
        {
            for(int j = 0; j < 12; j++)
            {
                femaleConstellations.get(i).accordanceArray.add(new Accordance("",0));
            }
        }

        Log.i("", "0 Fem con acc size "+ femaleConstellations.get(0).accordanceArray.size());
    }
}