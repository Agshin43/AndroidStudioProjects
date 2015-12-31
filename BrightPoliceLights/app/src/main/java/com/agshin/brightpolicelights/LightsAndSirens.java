package com.agshin.brightpolicelights;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;



public class LightsAndSirens extends AppCompatActivity {

    final static String SP_1  = "<NAME-ELEMENTS>";
    final static String SP_2  = "<FEATURES>";
    final static String SP_EL = "<ELEMENT>";

    MediaPlayer mpSiren;

    private PatternPlayer player;
    private boolean nbIsVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);
        nbIsVisible = true;



        LinearLayout layPlayer = (LinearLayout) findViewById(R.id.layPlayer);

        player = new PatternPlayer(getApplicationContext(), readPattern());


        player.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideShowNb();
                return false;
            }
        });

        if(player.getSirenId() > 0){
            mpSiren = MediaPlayer.create(getApplicationContext(), sirenResIdById(player.getSirenId()));

            mpSiren.setVolume(1, 1);
            mpSiren.setLooping(true);
            mpSiren.start();
        }

        layPlayer.addView(player);

        PowerManager pm;
        PowerManager.WakeLock wl;

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FlashActivity");
        wl.acquire();


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);

        hideShowNb();


        ///////////////
        AdView mAdView = (AdView) findViewById(R.id.adView);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        ///////////////
    }

    public int sirenResIdById(int sirenId){
        return getResources().getIdentifier("police"+sirenId, "raw", getPackageName());
    }

    private void hideShowNb(){
        View decorView = getWindow().getDecorView();
        int uiOptions;
        if(nbIsVisible){
            uiOptions= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            uiOptions= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        nbIsVisible = !nbIsVisible;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lights, menu);
        return true;
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

    @Override
    public void onDestroy() {
        if(mpSiren != null) {
            mpSiren.stop();
        }
        super.onDestroy();
    }
    @Override
    public void onPause(){
        if(mpSiren != null) {
            mpSiren.pause();
        }
        super.onPause();
    }
    @Override
    public void onResume()
    {
        if(mpSiren != null) {
            mpSiren.start();
        }
        super.onResume();
    }


    private static ArrayList samplePattern(){
        ArrayList ret = new ArrayList<>();

        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));

        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 255, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 255, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 255, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 255, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));

        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));

        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 200, 0, 255)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 100, 0, 255)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));
        ret.add(new PatternElement(150, PatternElementType.Light, Color.argb(255, 30, 90, 0)));
        ret.add(new PatternElement(150, PatternElementType.Pause, Color.argb(255, 0, 0, 0)));


        return ret;
    }

    private Pattern readPattern(){


        SharedPreferences sp = this.getSharedPreferences("bpl_", MODE_PRIVATE);

        int lastUsedPatternId = sp.getInt("last pattern", 0);
        Log.i("", "LAST PATTERN " + lastUsedPatternId);
        return new Pattern(sp.getString("pattern " + lastUsedPatternId, ""), SP_1, SP_2, SP_EL);


    }
}
