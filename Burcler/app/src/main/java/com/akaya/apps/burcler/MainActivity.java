package com.akaya.apps.burcler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {


    private FragmentTabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        String burcler = getApplicationContext().getResources().getString(R.string.str_burcler);
        String uygunluq = getApplicationContext().getResources().getString(R.string.str_uygunluq);
        String qoroskop = getApplicationContext().getResources().getString(R.string.str_qoroskop);
        String test = getApplicationContext().getResources().getString(R.string.str_test);

        Drawable dBurcler = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_consts);
        Drawable dUygunluq = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_accordance);
        Drawable dTest = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_meter);
        Drawable dQoroskop = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_horoscope);

        mTabHost.addTab(
                mTabHost.newTabSpec(uygunluq).setIndicator("", dUygunluq),
                TabUygunluq.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec(burcler).setIndicator("", dBurcler),
                TabBurcler.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec(test).setIndicator("", dTest),
                TabTest.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec(qoroskop).setIndicator("", dQoroskop),
                TabQoroskop.class, null);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
