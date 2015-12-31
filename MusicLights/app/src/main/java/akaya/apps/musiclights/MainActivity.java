package akaya.apps.musiclights;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

//    TextView freqLights;
    TextView bass;
    TextView red;
    TextView green;
    TextView blue;

    RelativeLayout mainLay;

    boolean recordAudioPermissionGranted;

    static int freq;
    static short maxA;
    Timer timer;
    TimerTask timerTask;
    public static int maxAmplitudeSetter = 0;
    boolean playing = false;
    boolean aaStarted = false;

    final static int PERMISSION_REQUEST_RECORD_AUDIO = 8;
    ImageButton fab ;//= (FloatingActionButton) findViewById(R.id.fab);
    AudioAnalyser aa;

    static short surroundMaxAmplitude = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        PowerManager pm;
        PowerManager.WakeLock wl;

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FlashActivity");
        wl.acquire();


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);




//        freqLights = (TextView) findViewById(R.id.freqLight);
        red = (TextView) findViewById(R.id.red);
        green = (TextView) findViewById(R.id.green);
        blue = (TextView) findViewById(R.id.blue);
        bass = (TextView) findViewById(R.id.bass);

        mainLay = (RelativeLayout) findViewById(R.id.main);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 10, 10);

//        aa = new AudioAnalyser();
//        aa.startStreaming();
//        playing = true;


        fab = (ImageButton) findViewById(R.id.play);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("FAB","FFF 1");
                if(!recordAudioPermissionGranted){
                    return;
                }
                if(!aaStarted){
                    Log.i("FAB","FFF 2");
                    aaStarted = true;
                    AudioAnalyser aa = new AudioAnalyser();
                    aa.startStreaming();
                }

                Log.i("FAB","FFF 3");
                playing = !playing;

                Log.i("FAB","FFF 4");
                fab.setVisibility(View.INVISIBLE);
                Log.i("FAB", "FFF 5");
                hideNb();
                Log.i("FAB", "FFF 6");


            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClicked();
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClicked();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClicked();
            }
        });
//        hideNb();
        requestRecordAudioPermission(this, red);
        int lc = getLaunchCount();
        if(lc > 1){
            ///////////////
            AdView mAdView = (AdView) findViewById(R.id.adView);
            com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().addTestDevice("7CFFA9E6C57CC1E3D565DB32911A71AA").build();
            mAdView.loadAd(adRequest);
            ///////////////
        } else {
            setLaunchCount(lc+1);
        }


    }

    private void tvClicked(){
        if (!recordAudioPermissionGranted) {
            return;
        }
        if (fab.getVisibility() == View.INVISIBLE) {
            playing = false;
            fab.setVisibility(View.VISIBLE);
        }

        red.setBackgroundColor(Color.BLACK);
        green.setBackgroundColor(Color.BLACK);
        blue.setBackgroundColor(Color.BLACK);
        bass.setBackgroundColor(Color.BLACK);
        showNb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            aa.recorder.stop();
            aa = null;
        }catch (NullPointerException e){}

    }

//    @Override
//    public void onBackPressed() {
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private int getLaunchCount(){
        SharedPreferences sp = this.getSharedPreferences("ml_", MODE_PRIVATE);
        return sp.getInt("launched",0);
    }

    private void setLaunchCount(int launchCount){

        Log.i("LC","LC = "+launchCount);
        SharedPreferences sp = this.getSharedPreferences("ml_", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("launched", launchCount);
        ed.commit();
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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){

                if(!playing){
                    return;
                }

                //6000 - 255
                // f   -  x

                int base = 50;

                float f = Math.min((freq*255.f)/6000.f, 255.f);
                int a = (int) Math.max(0,(maxA * 255.f / surroundMaxAmplitude) - base);
//                freqLights.setBackgroundColor(Color.argb(base + a, f , f, 255 - f + base ));
                int[] ret = curColorS(f / 255.f);
                red.setBackgroundColor(Color.argb(ret[0],ret[0],0,0));
                green.setBackgroundColor(Color.argb(ret[1],0,ret[1],0));
                blue.setBackgroundColor(Color.argb(ret[2],0,0,ret[2]));
//                freqLights.setBackgroundColor(curColor(f / 255.f));


                bass.setBackgroundColor(Color.argb(a+base, ret[0], ret[1], ret[2] ));

//                freqLights.setText("Freq = " + f + " , Max ampl. = " + a+" bass Setter "+ maxAmplitudeSetter);
            }
        }
    };

//    private int[] curColorS(float value ){
//        float c = value;//(((float)Integer.valueOf(tl[0]) % 12) * 60 + (Float.valueOf(tl[1]))) / 720.f;
//
//        int[] ret = {0,0,0};
//
//        float red = 0;
//        float green = 0;
//        float blue = 0;
//
//        if(c >= 0 && c <= (1/6.f)){
//            red = 255;
//            green = 1530 * c;
//        } else if( c > (1/6.f) && c <= (1/3.f) ){
//            red = 255 - (1530 * (c - 1/6f));
//            green = 255;
//        } else if( c > (1/3.f) && c <= (1/2.f)){
//            green = 255;
//            blue = 1530 * (c - 1/3f);
//        } else if(c > (1/2f) && c <= (2/3f)) {
//            green = 255 - ((c - 0.5f) * 1530);
//            blue = 255;
//        } else if( c > (2/3f) && c <= (5/6f) ){
//            red = (c - (2/3f)) * 1530;
//            blue = 255;
//        } else if(c > (5/6f) && c <= 1 ){
//            red = 255;
//            blue = 255 - ((c - (5/6f)) * 1530);
//        }
//
//        ret[0] = (int) red;
//        ret[1] = (int) green;
//        ret[2] = (int) blue;
//
//        return ret;
//    }

    private int[] curColorS(float c ){

        int[] ret = {0,0,0};

        float red = 0;
        float green = 0;
        float blue = 0;

        //------------------
        //------------------
        //------------------

        int decGreen = 100;

        if(c >= 0 && c <= (1/6.f)){
            red = 255;
            green = 1530 * c;
        } else if( c > (1/6.f) && c <= (1/3.f) ){
            red = Math.min(255,255 - (1530 * (c - 1/6f)) + decGreen);
            green = 255 - decGreen;
        } else if( c > (1/3.f) && c <= (1/2.f)){
            green = 255 - decGreen;
            blue = Math.min(255,1530 * (c - 1/3f) + decGreen);
        } else if(c > (1/2f) && c <= (2/3f)) {
            green = 255 - ((c - 0.5f) * 1530);
            blue = 255;
        } else if( c > (2/3f) && c <= (5/6f) ){
            red = (c - (2/3f)) * 1530;
            blue = 255;
        } else if(c > (5/6f) && c <= 1 ){
            red = 255;
            blue = 255 - ((c - (5/6f)) * 1530);
        }

        ret[0] = (int) red;
        ret[1] = (int) green;
        ret[2] = (int) blue;

        return ret;
    }

    private int curColor(float value ){
        float c = value;//(((float)Integer.valueOf(tl[0]) % 12) * 60 + (Float.valueOf(tl[1]))) / 720.f;

        float red = 0;
        float green = 0;
        float blue = 0;

        if(c >= 0 && c <= (1/6.f)){
            red = 255;
            green = 1530 * c;
        } else if( c > (1/6.f) && c <= (1/3.f) ){
            red = 255 - (1530 * (c - 1/6f));
            green = 255;
        } else if( c > (1/3.f) && c <= (1/2.f)){
            green = 255;
            blue = 1530 * (c - 1/3f);
        } else if(c > (1/2f) && c <= (2/3f)) {
            green = 255 - ((c - 0.5f) * 1530);
            blue = 255;
        } else if( c > (2/3f) && c <= (5/6f) ){
            red = (c - (2/3f)) * 1530;
            blue = 255;
        } else if(c > (5/6f) && c <= 1 ){
            red = 255;
            blue = 255 - ((c - (5/6f)) * 1530);
        }

        return Color.argb(180,(int) red, (int) green, (int) blue);
    }


    static class AudioAnalyser{





        public static AudioRecord recorder;

        private int sampleRate = 44100;//44100; //for music
        private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        private boolean status = true;







        public int calculate(int sampleRate, short [] audioData){


            int numSamples = audioData.length;
            int numCrossing = 0;
            for (int p = 0; p < numSamples-1; p++)
            {
                if ((audioData[p] > 0 && audioData[p + 1] <= 0) ||
                        (audioData[p] < 0 && audioData[p + 1] >= 0))
                {
                    numCrossing++;
                }
            }

            float numSecondsRecorded = (float)numSamples/(float)sampleRate;
            float numCycles = numCrossing/2;
            float frequency = numCycles/numSecondsRecorded;

            return (int)frequency;
        }



        public void startStreaming() {


            Thread streamThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    short[] buffer = new short[minBufSize];
                    recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*10);

                    recorder.startRecording();


                    while(status == true) {


                        //reading data from MIC into buffer
                        minBufSize = recorder.read(buffer, 0, buffer.length);


                        //putting buffer in the packet
                        short max = 0;
//
                        for(int i=0; i< minBufSize;i++)
                        {
                            if(buffer[i] > max){
                                max = buffer[i];
                            }
                        }

                        if(maxAmplitudeSetter < 10){
                            maxAmplitudeSetter++;
                        }
                        else if(maxAmplitudeSetter > 50){
                            if(maxA > surroundMaxAmplitude){
                                surroundMaxAmplitude = maxA;
                            }
                            maxAmplitudeSetter--;
                        }

//                        Log.i("FREQ","Freq "+);

                        maxA = (short)(max / 100.f);
                        freq = calculate(sampleRate, buffer);

//                        freq = buffer.length;
//                        Log.i("mmm", "buffer:"+s);
//                        System.out.println("MinBufferSize: " +minBufSize);


                    }

                }

            });
            streamThread.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_RECORD_AUDIO) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(red, R.string.record_audio_permission_was_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
                recordAudioPermissionGranted = true;
                fab.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(red, R.string.record_audio_permission_was_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
                recordAudioPermissionGranted = false;
                fab.setVisibility(View.INVISIBLE);
                this.finish();
            }
        }

    }


    public  void requestRecordAudioPermission(final Activity activity, View view){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.RECORD_AUDIO)) {

                Snackbar.make(view, R.string.permission_record_audio_explanation,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                    final Activity act = activity;
                    @Override
                    public void onClick(View view) {
                        // Request the permission
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                PERMISSION_REQUEST_RECORD_AUDIO);
                    }
                }).show();

            } else {

                Snackbar.make(view,
                        R.string.requesting_permission,
                        Snackbar.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_REQUEST_RECORD_AUDIO);
            }
        } else {
            recordAudioPermissionGranted = true;
        }
    }

    private void hideNb(){
        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

//        View decorView = getWindow().getDecorView();
//        int uiOptions;
//
//        uiOptions= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//        decorView.setSystemUiVisibility(uiOptions);


    }

    private void showNb(){
        View decorView = getWindow().getDecorView();
        int uiOptions= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);
    }

//    public void FullScreencall() {
//        if(Build.VERSION.SDK_INT < 19) {//19 or above api
//            View v = this.getWindow().getDecorView();
//        v.setSystemUiVisibility(View.GONE);
//    } else {
//        //for lower api versions.
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        decorView.setSystemUiVisibility(uiOptions);
//    }
//}


}
