package com.apps.akaya.easytorch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;


public class TorchSettings extends ActionBarActivity {

    private String[] arraySpinner;
//    private int fmCount;
//    private boolean vibrate;
//    private int sensity;

    private CheckBox cb_vibration;
    private CheckBox cb_sounds;
    private Spinner sp_fmc;
    private SeekBar sb_sensity;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch_settings);

        this.arraySpinner = new String[] {
                "3", "4", "5", "6", "7", "8"
        };

        cb_vibration = (CheckBox) findViewById(R.id.cb_vibation);
        cb_sounds = (CheckBox) findViewById(R.id.cb_sounds);
        sb_sensity = (SeekBar) findViewById(R.id.sb_sensity);
        sp_fmc = (Spinner) findViewById(R.id.sp_fac);
        sp_fmc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_fmc.setId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.my_spinner_layout, arraySpinner);
        sp_fmc.setAdapter(adapter);

        cb_vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Vibrator v = (Vibrator) TorchSettings.this.getSystemService(TorchSettings.VIBRATOR_SERVICE);
                    v.vibrate(100);
                }
            }
        });

        cb_sounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mediaPlayer = MediaPlayer.create(TorchSettings.this, R.raw.snd_lights_off);
                    mediaPlayer.start();
                }
            }
        });

        loadPreferences();
        startService(new Intent(TorchSettings.this, TorchService.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_torch_settings, menu);
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

    private void loadPreferences()
    {
        SharedPreferences prefs = this.getSharedPreferences(
                "fmc", this.MODE_MULTI_PROCESS);
        cb_vibration.setChecked(prefs.getBoolean("vibration", true));
        cb_sounds.setChecked(prefs.getBoolean("sounds", true));
        sb_sensity.setProgress(prefs.getInt("sensity",500));
        int fmc = prefs.getInt("fmc",3);
        Log.i("","Loaded fmc, settings >> "+fmc);
        for(int i = 0; i < arraySpinner.length; i++)
        {
            if(arraySpinner[i].equals(String.valueOf(fmc)))
            {
                sp_fmc.setId(i);
                break;
            }
        }
    }

    private void savePreferences()
    {
        SharedPreferences prefs = this.getSharedPreferences(
                "fmc", this.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();

        if(cb_vibration.isChecked())
        {
            editor.putBoolean("vibration", true);
        }
        else
        {
            editor.putBoolean("vibration", false);
        }
        if(cb_sounds.isChecked())
        {
            editor.putBoolean("sounds", true);
        }
        else
        {
            editor.putBoolean("sounds", false);
        }

        editor.putInt("sensity", sb_sensity.getProgress());
        int fm = Integer.parseInt(arraySpinner[sp_fmc.getId()]);
        Log.i("","---TorchSettings-------FMC"+fm);
        editor.putInt("fmc", fm);
        editor.commit();
    }

    private void restorePreferences()
    {
        SharedPreferences prefs = this.getSharedPreferences(
                "fmc", this.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("vibration", true);
        editor.putInt("sensity",500);
        editor.putInt("fmc", 3);
        editor.putBoolean("sounds",true);

        cb_vibration.setChecked(true);
        cb_sounds.setChecked(true);
        sb_sensity.setProgress(500);
        sp_fmc.setId(0);

        editor.commit();

    }

    public void onBtnSaveClicked(View view)
    {
        savePreferences();
        stopService(new Intent(TorchSettings.this, TorchService.class));
        startService(new Intent(TorchSettings.this, TorchService.class));
    }

    public void onBtnRestoreClicked(View view)
    {
        restorePreferences();
        stopService(new Intent(TorchSettings.this, TorchService.class));
        startService(new Intent(TorchSettings.this, TorchService.class));
    }
}
