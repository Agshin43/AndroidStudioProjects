package com.apps.idrak.positiontracker;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GpsService extends Service implements LocationListener{
    final static int DATA_SEND_INTERVAL  = 1000 * 30;
    final static int STATUS_GET_INTERVAL = 1000 * 20;

    final static int NOTIFICATION_ID_GPS = 9;
    final static int NOTIFICATION_ID_INTERNET = 1;

    boolean locationChanged = false;
    boolean providerEnabledNet = false;
    boolean providerEnabledGps = false;

    boolean isShownInternetNotification = false;


    Timer mTimer;
    TimerTask mTimerTask;

    int statusGotTime = 0;
    int dataSentTime = 0;

    int myRegistrationStatus;
    boolean isTracking;

    private String myDeviceId;
    private String myServerUrl;

    double currentLatitude;
    double currentLongitude;

    Notification barNotif;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate()
    {
    }

    public boolean isGpsAvailableViaNetwork()
    {
        LocationManager locationManagerNetwork = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L,1.0f, this);

        return locationManagerNetwork.isProviderEnabled (LocationManager.NETWORK_PROVIDER);
    }

    public boolean isGpsAvailable()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0f, this);

        return locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
    }


    private String getUserName()
    {
        String mn = wimn();
        String ret = mn;
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for(Account account : accounts)
        {
            possibleEmails.add(account.name);
        }

        if(!possibleEmails.isEmpty() && possibleEmails.get(0) != null)
        {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if(parts.length > 0)
            {
                ret = parts[0]+"'s "+wimn();
            }
        }
        return ret;
    }

    private String wimn()
    {
        return Build.MANUFACTURER+" "+Build.MODEL;
    }
    private String getMyId()
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//        Toast.makeText(getApplicationContext(), "Manufacturer : "+Build.MANUFACTURER, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "Manuf + Model : "+Build.MANUFACTURER+""+Build.MODEL, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Subs id : "+telephonyManager.getSubscriberId(), Toast.LENGTH_SHORT).show();
        return telephonyManager.getDeviceId();
    }

    private String getMyServerUrl()
    {
        return getResources().getString(R.string.server_url);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(!isGpsAvailable())
        {

//            Toast.makeText(getApplicationContext(), "Gps is available on your device", Toast.LENGTH_LONG).show();
        }
        myDeviceId = getMyId();
        myServerUrl = getMyServerUrl();

        myRegistrationStatus = getMyRegistrationStatus();
        if(myRegistrationStatus == -1)
        {
            HttpFunctions.registerMe(myDeviceId +","+getUserName(), myServerUrl);
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

    private int getMyRegistrationStatus()
    {
        return HttpFunctions.getRegistrationStatus(myDeviceId, myServerUrl);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
//        HttpFunctions.setMyConnection(myDeviceId+","+"false",myServerUrl);
//        Toast.makeText(getApplicationContext(),"on Destroy",Toast.LENGTH_LONG);
    }
    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
        HttpFunctions.setMyConnection(myDeviceId+","+"false",myServerUrl);
        Toast.makeText(getApplicationContext(),"on Task removed",Toast.LENGTH_LONG);
    }

    public final boolean isInternetOn() {
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }

    private void reGettingRegStatAndRegstration()
    {
        if( myRegistrationStatus  == -1)
        {

            HttpFunctions.registerMe(myDeviceId +","+getUserName(), myServerUrl);
            myRegistrationStatus = HttpFunctions.getRegistrationStatus(myDeviceId, myServerUrl);
            Log.i("", "reGettingRegStatAngRegstration()");
//            Toast.makeText(getApplicationContext(), "reGettingRegStatAngRegstration() ", Toast.LENGTH_SHORT).show();
//            myRegistrationStatus = getMyRegistrationStatus();
//            if(myRegistrationStatus != -1)
//            {
//                HttpFunctions.registerMe(myDeviceId +","+getUserName(), myServerUrl);
//            }
        }
    }

    private boolean canGetLocation()
    {
        return (providerEnabledGps || providerEnabledNet);
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                {
                    if(!canGetLocation() && myRegistrationStatus != -1)
                    {
//                        Toast.makeText(getApplicationContext(), " cant get location", Toast.LENGTH_SHORT).show();
                        HttpFunctions.setMyConnection(myDeviceId+","+false,myServerUrl);
                        break;
                    }
                    if(!isInternetOn())
                    {
                        Intent callMobileDataSettingIntent = new Intent(GpsService.this,ActivityTurnInternet.class);
                        callMobileDataSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if(!isShownInternetNotification)
                        {
                            createNotification(callMobileDataSettingIntent,getResources().getString(R.string.warning),getResources().getString(R.string.internet_is_off_md),NOTIFICATION_ID_INTERNET, R.drawable.ic_internet_off);

                            isShownInternetNotification = true;
                        }
                    }
                    else
                    {
                        cancelNotification(NOTIFICATION_ID_INTERNET);
                        isShownInternetNotification = false;
                    }
                    reGettingRegStatAndRegstration();

                    if(!isInternetOn() || !locationChanged)
                    {
                        break;
                    }

                    statusGotTime += 1000;
                    if(statusGotTime >= STATUS_GET_INTERVAL)
                    {
                        statusGotTime = 0;
                        isTracking = HttpFunctions.isTracking(myDeviceId, myServerUrl);
                    }

                    if(isTracking)
                    {
                        dataSentTime+=1000;

                        if(dataSentTime >= DATA_SEND_INTERVAL)
                        {
                            dataSentTime = 0;
                            HttpFunctions.postPosition(
                                    myDeviceId,
                                    currentLatitude,
                                    currentLongitude,
                                    String.valueOf(System.currentTimeMillis()),
                                    myServerUrl
                            );
                            locationChanged = false;
//                            Toast.makeText(getApplicationContext(), "Position sent", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(getApplicationContext(), "Location "+location.getLatitude()+", "+location.getLongitude(),Toast.LENGTH_LONG).show();
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        locationChanged = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Toast.makeText(getApplicationContext(), "Status changed "+status, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Toast.makeText(getApplicationContext(), "Provider enabled", Toast.LENGTH_SHORT).show();
//        HttpFunctions.setMyConnection(myDeviceId+","+"true", myServerUrl);

//        Toast.makeText(getApplicationContext(), "Provider enabled : "+ provider, Toast.LENGTH_SHORT).show();

        if( provider.equals(LocationManager.NETWORK_PROVIDER) )
        {
            providerEnabledNet = true;
//            Toast.makeText(getApplicationContext(), (providerEnabledGps?"G: true - ":"G: false - ")+(providerEnabledNet?"N: true":"N: false"), Toast.LENGTH_SHORT).show();
        }
        if( provider.equals(LocationManager.GPS_PROVIDER) )
        {
            providerEnabledGps = true;
//            Toast.makeText(getApplicationContext(), (providerEnabledGps?"G: true - ":"G: false - ")+(providerEnabledNet?"N: true":"N: false"), Toast.LENGTH_SHORT).show();
        }


        if(canGetLocation())
        {
            cancelNotification( NOTIFICATION_ID_GPS );
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Toast.makeText(getApplicationContext(), "Provider disabled", Toast.LENGTH_SHORT).show();
//        HttpFunctions.setMyConnection(myDeviceId+","+"false", myServerUrl);
//        Toast.makeText(getApplicationContext(), "Provider disabled : "+ provider, Toast.LENGTH_SHORT).show();

        if( provider.equals(LocationManager.NETWORK_PROVIDER) )
        {
            providerEnabledNet = false;
        }
        if( provider.equals(LocationManager.GPS_PROVIDER) )
        {
            providerEnabledGps = false;
        }

        if( ! canGetLocation() )
        {
            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            callGPSSettingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            createNotification(callGPSSettingIntent,getResources().getString(R.string.warning),getResources().getString(R.string.gps_is_off),NOTIFICATION_ID_GPS, R.drawable.ic_gps_off);
        }

//        showNotification(getResources().getString(R.string.warning), getResources().getString(R.string.gps_is_off), callGPSSettingIntent, NOTIFICATION_ID_GPS);


//        askForEnableGps();
    }


    /* other functions */
    private void cancelNotification(int id)
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
    }


    public void createNotification(Intent intent, String title, String message, int id, int drawable) {
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(GpsService.VIBRATOR_SERVICE);
//        long[] pattern = new long[]{500L, 400L, 500L, 400L};

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        v.vibrate(800);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message).setSmallIcon(drawable)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(id, noti);

    }

    /* other functions */
}
