package com.akaya.apps.smartnightlantern;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    Lantern lantern;

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    boolean GSensorUpdateEnabled;



    private BroadcastReceiver mBatInfoReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout lanternLayout = (LinearLayout) findViewById(R.id.lantern_view);
        lantern = new Lantern(this, this);
        lanternLayout.addView(lantern);


        AdView mAdView = (AdView) findViewById(R.id.adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        mBatInfoReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context ctxt, Intent intent) {
                lantern.setBatteryPercentage(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                lantern.setBatteryPlugged((plugged == BatteryManager.BATTERY_PLUGGED_AC) || (plugged == BatteryManager.BATTERY_PLUGGED_USB));
            }
        };

        readSettings();

//        if(lantern.sms){
//            SettingsActivity.requestSMSPermission(MainActivity.this, lantern);
//        }
//
//        if(lantern.missedCalls){
//            SettingsActivity.requestCallPermission(MainActivity.this, lantern);
//        }

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        try {
            manageReceivers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateBrightness();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void manageReceivers() throws IOException {
        if(lantern.battery){
            this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } else {
            try{
                this.unregisterReceiver(this.mBatInfoReceiver);
            } catch (IllegalArgumentException e){

            }
        }

        if(lantern.awakeViaMotion){
            GSensorUpdateEnabled = true;
            sensorMan.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        } else {
            GSensorUpdateEnabled = false;
            try{
                sensorMan.unregisterListener(this, accelerometer);
            } catch (IllegalArgumentException e){
            }
        }

//        if(lantern.awakeViaSound){
//            if(lantern.soundMeter != null){
//                lantern.soundMeter.start();
//            } else
//            {
//                lantern.soundMeter = new SoundMeter();
//                lantern.soundMeter.start();
//            }
//        } else {
//            if(lantern.soundMeter != null){
//                lantern.soundMeter.stop();
//            }
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void updateBrightness(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = Math.max((lantern.brightness) / 100.f, lantern.MIN_BRIGHTNESS );
        Log.i("UPDATE BRIGHTNESS", "BRIGHTNESS = " + lp.screenBrightness + " = " + Math.max((float) (lantern.brightness) / 100.f, lantern.MIN_BRIGHTNESS));
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        lantern.writeColor();
        try {
            this.unregisterReceiver(this.mBatInfoReceiver);
        } catch (IllegalArgumentException e){

        }
    }


    //////////////////////

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!GSensorUpdateEnabled){
            try{
                sensorMan.unregisterListener(this, accelerometer);
            } catch (IllegalArgumentException e){
                Toast.makeText(getApplicationContext(), "Can't unreg listener, e = "+e, Toast.LENGTH_LONG).show();
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 0.8 ){
                wakeScreen();
            }
        }

    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void readSettings(){
        SharedPreferences sp = this.getSharedPreferences("lantern_", MODE_PRIVATE);

        lantern.sleepTime = sp.getInt("sleep time", 10);
        lantern.brightness = sp.getInt("brightness", 80);
        Log.i("READ SETTING","BRIGHTNESS = "+lantern.brightness);
        lantern.sleep = sp.getBoolean("sleep", true);
        lantern.colorClock = sp.getBoolean("color clock", true);
        lantern.awakeViaMotion = sp.getBoolean("awake via motion", true);
        lantern.awakeViaSound = sp.getBoolean("awake via sound", true);
        lantern.battery = sp.getBoolean("battery", true);
        lantern.digitalClock = sp.getBoolean("digital clock", true);
        lantern.missedCalls = sp.getBoolean("missed calls", false);
        Log.i("READ SETTINGS", "MISSED CALLS is "+lantern.missedCalls);
        lantern.sms = sp.getBoolean("sms", false);
        if(!lantern.colorClock){
            lantern.screenColor = sp.getInt("screen color", Color.GREEN);
        }
        lantern.soundLevel = sp.getInt("sound level",50);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        readSettings();
        try {
            manageReceivers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateBrightness();
        lantern.handler.sendEmptyMessage(0);
        lantern.update();
        lantern.invalidate();
    }

    public static int getMissedCallCount(Activity activity, View view){

//        SettingsActivity.requestCallPermission(activity, view);
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor cursor = null;
        int ret = 0;
        try{
            cursor = activity.getContentResolver().query(
                    Uri.parse("content://call_log/calls"),
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);
            while (cursor.moveToNext()) {
                String callNumber = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                String callType = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
                String isCallNew = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NEW));
                if(Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE && (Integer.parseInt(isCallNew) > 0) ){
                    ret++;
                }
            }
        }catch(Exception ex){
        }finally{
            cursor.close();
        }
        return ret;

    }

    public static int getUnreadSmsCount(Activity activity, View view){

//        SettingsActivity.requestSMSPermission(activity, view);

        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

        Cursor c = activity.getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
        int unreadMessagesCount = c.getCount();
        c.close();
        return unreadMessagesCount;
    }

    private void wakeScreen(){
        lantern.screenIsWake = true;
        Log.i("WAKE SCREEN", "TURN BRIGHTNESS");
        lantern.turnScreenBrightness("wake screen");
        lantern.sleepTimeCounter = 0;
        lantern.invalidate();
    }

    private void unlockScreen(){

//        WindowManager wm = getSystemService(getApplicationContext().WINDOW_SERVICE);
//        Window window = getWindow();
//
//        window.addFlags(wm.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        if (requestCode == SettingsActivity.PERMISSION_REQUEST_SMS) {
//            // Request for camera permission.
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission has been granted. Start camera preview Activity.
//                Snackbar.make(lantern, R.string.sms_permission_was_granted,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
//            } else {
//                Snackbar.make(lantern, R.string.sms_permission_was_denied,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
//            }
//        }
//
//        if (requestCode == SettingsActivity.PERMISSION_REQUEST_CALL_LOG) {
//            // Request for camera permission.
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission has been granted. Start camera preview Activity.
//                Snackbar.make(lantern, R.string.call_log_permission_was_granted,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
//            } else {
//                Snackbar.make(lantern, R.string.call_log_permission_was_denied,
//                        Snackbar.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }


}
