package com.apps.idrak.positiontracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ActivityTurnInternet extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_turn_internet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_turn_internet, menu);
        return true;
    }
    @Override
    public void onDestroy()
    {
        setActivityStatusRunning("internet",false);
        super.onDestroy();
    }
    public void btnMobileDataOnClicked(View v)
    {
        Intent callGPSSettingIntent = new Intent(
                Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        callGPSSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(callGPSSettingIntent);
        this.finish();
    }

    public void btnWirelessOnClicked(View v)
    {
        Intent callGPSSettingIntent = new Intent(
                Settings.ACTION_WIRELESS_SETTINGS);
        callGPSSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(callGPSSettingIntent);
        this.finish();
    }

    public void btnWifiOnClicked(View v)
    {
        Intent callGPSSettingIntent = new Intent(
                Settings.ACTION_WIFI_SETTINGS);
        callGPSSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(callGPSSettingIntent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setActivityStatusRunning(String key, boolean value)
    {
        SharedPreferences prefs = this.getSharedPreferences(
                "tracker", this.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }
}
