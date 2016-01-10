package com.akaya.apps.burcler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TabQoroskop extends Fragment {

    TextView tvQoro;
    HTTPFunctions http;
    String[] horoscopes;
    Spinner spMine;
    RadioButton rbD;
    RadioButton rbW;

    private Context context;
    private int curCons;
    private int curType;
    private boolean onStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        onStart  = true;
        this.context = getActivity().getApplicationContext();
        http = new HTTPFunctions(context);
        horoscopes = new String[24];



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.qoroskop_layout, container, false);
        tvQoro = (TextView) v.findViewById(R.id.tv_qoro);

        rbD = (RadioButton) v.findViewById(R.id.rbD);
        rbW = (RadioButton) v.findViewById(R.id.rbW);

        rbD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQoro.setText(R.string.str_loading);

                curType = rbD.isChecked()? 0 : 1;
                saveHoroSettings();
                getHoroscope(curCons, curType);
            }
        });

        rbW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQoro.setText(R.string.str_loading);

                curType = rbW.isChecked() ? 1 : 0;
                saveHoroSettings();
                getHoroscope(curCons, curType);
            }
        });


        ///////////// MAD
        TextView tvMad = (TextView) v.findViewById(R.id.tvMad);
        tvMad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mad();
            }
        });
        ImageButton btnMad = (ImageButton) v.findViewById(R.id.btnMad);
        btnMad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mad();
            }
        });
        ///////////////



        spMine = (Spinner) v.findViewById(R.id.spMine);
        loadConstellations();
        spMine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvQoro.setText(R.string.str_loading);

                if (onStart) {
                    onStart = false;
                    return;
                }


                curCons = position;
                getHoroscope(curCons, curType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        loadHoroSettings();
        getHoroscope(curCons, curType);
        return v;
    }

    private boolean getHoroscope(int id, int type){
        final int[] ret = {0};
        AsyncTask task = new AsyncTask<String,String,String> (){

            ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity().getApplicationContext());
                dialog.setMessage(getResources().getString(R.string.str_loading));
//                dialog.show();
            }
            @Override
            protected String doInBackground(String... urls) {

                if(horoscopes[curCons+(curType * 12)] != null && horoscopes[curCons+(curType * 12)].length() > 50) {
                    return "";
                }
                horoscopes[curCons+(curType * 12)] = http.goroscopeByConstellationId(curCons, curType);
                ret[0] = horoscopes[curCons+(curType * 12)].length() > 0? 1 : 0;

                return "";
            }

            @Override
            protected void onPostExecute(String result){
                tvQoro.setText(horoscopes[curCons+(curType * 12)]);
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

        return ret[0] == 1;
    }

    private void loadConstellations(){

        String[] ee = getActivity().getResources().getStringArray(R.array.con_array);
        List<String> p23 = new ArrayList<>();
        for (int i = 0; i < ee.length; i++){
            p23.add(ee[i]+" bürcü");
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(context, R.layout.spinner_item, p23);
        spMine.setAdapter(dataAdapter2);
    }

    private void loadHoroSettings(){
        SharedPreferences pref = context.getSharedPreferences("horos", Context.MODE_PRIVATE);

        Random r = new Random();

        curCons = pref.getInt("curCons", Math.abs(r.nextInt() % 12));
        curType = pref.getInt("curType", 0);

        spMine.setSelection(curCons);
        rbD.setChecked(curType == 0);
        rbW.setChecked(curType == 1);
    }

    private void saveHoroSettings(){
        SharedPreferences pref = context.getSharedPreferences("horos",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        edit.putInt("curCons", curCons);
        edit.putInt("curType", curType);
    }


    private void mad(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "akaya.apps.musiclights")));
        }
        catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "akaya.apps.musiclights")));
        }
    }

}