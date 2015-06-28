package com.apps.akaya.easytorch;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by agshin on 5/14/15.
 */
public class TorchService20plus extends Service implements SensorEventListener{

    Sensor proxSensor;
    Sensor gravitySensor;
    SensorManager sm;
    Notification barNotif;
    boolean hasFlash;
    private boolean isFlashOn;
    private static long nearTime;
    private static long farTime;
    private int sensity = 300;
    private int fingerActCount = 0;
    private boolean vibrate;
    private boolean sounds;
    private int fmc;
    private MediaPlayer mediaPlayer;


    private CameraCaptureSession mSession;
    private CaptureRequest.Builder mBuilder;
    private CameraDevice mCameraDevice;

    TimerTask mTimerTask;
    Timer mTimer;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    public void onCreate() {//onCreat shouldn't be used for sensor u should use onStartCommand
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


    private void handleActions(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if(fingerActCount > (fmc-1))
            {
                if (isFlashOn) {
                    fingerActCount = 0;
                    Log.i("", "turning flash off");
                    // turn off flash
                    turnOffFlash();
                } else {
                    fingerActCount = 0;
                    Log.i("", "turning flash on");
                    // turn on flash
                    turnOnFlash();
                }

                if(vibrate)
                {
                    Vibrator v = (Vibrator) TorchService20plus.this.getSystemService(TorchService20plus.VIBRATOR_SERVICE);
                    v.vibrate(40);
                }
                return;
            }


            float val = event.values[0];
            if(val == 0)
            {
                Log.i("", "Near ----");
                nearTime = System.currentTimeMillis();
            }
            else
            {
                farTime = System.currentTimeMillis();
                if(nearTime+sensity > farTime)
                {
                    Log.i("", "Far ++++");
                    fingerActCount++;
                }
            }
            if(nearTime > (farTime+(600)))
            {
                Log.i("","Over time for finger action *****");
                fingerActCount = 0;
            }


        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        handleActions(event);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        turnOffFlash();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        sm.unregisterListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        loadPreferences();




        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            AlertDialog alert = new AlertDialog.Builder(TorchService20plus.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton(AlertDialog.BUTTON_NEGATIVE,"OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                }
            });
            alert.show();
            return Service.START_STICKY;
        }

        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        proxSensor=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        gravitySensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent bIntent = new Intent(TorchService20plus.this, TorchSettings.class);
        PendingIntent pbIntent = PendingIntent.getActivity(TorchService20plus.this, 0 , bIntent, 0);
        NotificationCompat.Builder bBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Subtitle")
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setContentIntent(pbIntent);
        barNotif = bBuilder.build();
        this.startForeground(1, barNotif);

        return Service.START_NOT_STICKY;
    }



    private void turnOnFlash() {
        if (!isFlashOn) {

            if(sounds)
            {
                mediaPlayer = MediaPlayer.create(TorchService20plus.this, R.raw.snd_lights_on);
                mediaPlayer.start();
            }


            isFlashOn = true;

            // changing button/switch image
//            toggleButtonImage();
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {

            if(sounds)
            {
                mediaPlayer = MediaPlayer.create(TorchService20plus.this, R.raw.snd_lights_off);
                mediaPlayer.start();
            }


            isFlashOn = false;

        }
    }

    private void loadPreferences()
    {
        SharedPreferences prefs = this.getSharedPreferences(
                "fmc", this.MODE_MULTI_PROCESS);
        vibrate = prefs.getBoolean("vibration", true);
        sounds = prefs.getBoolean("sounds", true);
        sensity = prefs.getInt("sensity",500);
        fmc = prefs.getInt("fmc",3);
    }


}

