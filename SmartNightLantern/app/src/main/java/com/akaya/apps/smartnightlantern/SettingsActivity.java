package com.akaya.apps.smartnightlantern;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akaya.apps.smartnightlantern.colorpicker.ColorPickerView;
import com.akaya.apps.smartnightlantern.colorpicker.OnColorSelectedListener;
import com.akaya.apps.smartnightlantern.colorpicker.builder.ColorPickerClickListener;
import com.akaya.apps.smartnightlantern.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    public final static int PERMISSION_REQUEST_SMS = 0;
    public final static int PERMISSION_REQUEST_CALL_LOG = 1;

    private CheckBox     cbSleep;
    private CheckBox     cbSms;
    private CheckBox     cbMissedCall;
    private CheckBox     cbBattery;
    private CheckBox     cbDigitalClock;
    private CheckBox     cbAwakeViaMotion;
//    private CheckBox     cbAwakeViaSound;
    private CheckBox     cbColorClock;
    private TextView     tvSleepTime;

    private SeekBar     sbBrightness;
    private SeekBar     sbSleepTime;
//    private SeekBar     sbSoundLevel;


    private Button btnScreenColor;
    private int initialColor;
    private int awakeSoundLevel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cbSleep = (CheckBox) findViewById(R.id.cb_sleep);
        cbSms = (CheckBox) findViewById(R.id.cb_sms);
        cbMissedCall = (CheckBox) findViewById(R.id.cb_missed_calls);
        cbBattery = (CheckBox) findViewById(R.id.cb_battery);
        cbDigitalClock = (CheckBox) findViewById(R.id.cb_digital_clock);
        cbAwakeViaMotion = (CheckBox) findViewById(R.id.cb_awake_via_motion);
//        cbAwakeViaSound = (CheckBox) findViewById(R.id.cb_awake_via_sound);
        cbColorClock = (CheckBox) findViewById(R.id.cb_color_clock);
        sbBrightness = (SeekBar) findViewById(R.id.sb_brightness);
        sbSleepTime = (SeekBar) findViewById(R.id.sb_sleep_time);
//        sbSoundLevel = (SeekBar) findViewById(R.id.sb_sound_level);
        tvSleepTime = (TextView) findViewById(R.id.tv_sleep_time);
        sbSleepTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSleepTime.setText(minutesToReadable(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cbSleep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sbSleepTime.setEnabled(isChecked);
            }
        });

        cbColorClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnScreenColor.setEnabled(!isChecked);
            }
        });

        cbSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    requestSMSPermission(SettingsActivity.this, btnScreenColor);
                }
            }
        });

        cbMissedCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    requestCallPermission(SettingsActivity.this, btnScreenColor);
                }
            }
        });

        btnScreenColor = (Button) findViewById(R.id.btn_color);


        readSettings();
        btnScreenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ColorPickerDialogBuilder
                        .with(SettingsActivity.this)
                        .setTitle("Choose color")
                        .initialColor(initialColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(8)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {

                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                btnScreenColor.setBackgroundColor(selectedColor);
                                initialColor = selectedColor;
                                if (allColors != null) {
                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                    }

                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        Button btnRestore = (Button) findViewById(R.id.btn_restore);
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeDefaultSettings();
                readSettings();
            }
        });
        Button btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeSettings();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private String minutesToReadable(int minutes){
        int hours = minutes / 60;
        int mins = minutes % 60;
        return (hours > 0? String.valueOf(hours)+" ":" ") +
                (hours == 1? getResources().getString(R.string.hour)+" " : (hours == 0?" " : getResources().getString(R.string.hours)+" ")) +
                (mins > 0? String.valueOf(mins)+" ":" ") +
                (mins == 1? getResources().getString(R.string.minute) : (mins == 0?" " : getResources().getString(R.string.minutes)));
    }

    private void readSettings(){
        SharedPreferences sp = this.getSharedPreferences("lantern_", MODE_PRIVATE);

        sbSleepTime.setProgress(sp.getInt("sleep time", sbSleepTime.getSecondaryProgress()));
        tvSleepTime.setText(minutesToReadable(sbSleepTime.getProgress() + 1));

        sbBrightness.setProgress(sp.getInt("brightness", sbBrightness.getSecondaryProgress()));

        cbSleep.setChecked(sp.getBoolean("sleep", true));
        cbColorClock.setChecked(sp.getBoolean("color clock", true));
        cbAwakeViaMotion.setChecked(sp.getBoolean("awake via motion", true));
//        cbAwakeViaSound.setChecked(sp.getBoolean("awake via sound", false));
        cbBattery.setChecked(sp.getBoolean("battery", true));
        cbDigitalClock.setChecked(sp.getBoolean("digital clock", true));
        cbMissedCall.setChecked(sp.getBoolean("missed calls", false));
        cbSms.setChecked(sp.getBoolean("sms", false));

        sbSleepTime.setEnabled(cbSleep.isChecked());
        btnScreenColor.setEnabled(!cbColorClock.isChecked());

        initialColor = sp.getInt("screen color",Color.GREEN);
        btnScreenColor.setBackgroundColor(initialColor);
//        sbSoundLevel.setProgress(sp.getInt("sound level",sbSoundLevel.getSecondaryProgress()));
    }

    private void writeSettings(){
        SharedPreferences sp = this.getSharedPreferences("lantern_", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt("sleep time", sbSleepTime.getProgress() + 1);
        editor.putInt("brightness", sbBrightness.getProgress());

        editor.putBoolean("sleep", cbSleep.isChecked());
        editor.putBoolean("color clock", cbColorClock.isChecked());
        editor.putBoolean("awake via motion", cbAwakeViaMotion.isChecked());
//        editor.putBoolean("awake via sound", cbAwakeViaSound.isChecked());
        editor.putBoolean("battery", cbBattery.isChecked());
        editor.putBoolean("digital clock", cbDigitalClock.isChecked());
        editor.putBoolean("missed calls", cbMissedCall.isChecked());
        editor.putBoolean("sms", cbSms.isChecked());
        editor.putInt("screen color", initialColor);
//        editor.putInt("sound level", sbSoundLevel.getProgress());

        editor.commit();
    }

    private void writeDefaultSettings(){
        SharedPreferences sp = this.getSharedPreferences("lantern_", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt("sleep time", 10);
        editor.putInt("brightness", 80);

        editor.putBoolean("sleep", true);
        editor.putBoolean("color clock", true);
        editor.putBoolean("awake via motion", true);
//        editor.putBoolean("awake via sound", cbAwakeViaSound.isChecked());
        editor.putBoolean("battery", true);
        editor.putBoolean("digital clock", true);
        editor.putBoolean("missed calls", false);
        editor.putBoolean("sms", false);
        editor.putInt("screen color", Color.GREEN);
//        editor.putInt("sound level", sbSoundLevel.getProgress());

        editor.commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            writeSettings();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", true);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void requestPermissions(Activity activity, String[] permissions){

        boolean needReq = false;
        List reqPers = new ArrayList();
        for(String permission:permissions){
            if (ContextCompat.checkSelfPermission(activity,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        permission)) {
                } else {
                    needReq = true;
                    reqPers.add(permission);
                }
            }
        }

        if(needReq){
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG},
                    0);
        }
    }

    public static void requestSMSPermission(final Activity activity, View view){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_SMS)) {

                Snackbar.make(view, R.string.permission_sms_explanation,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                    final Activity act = activity;
                    @Override
                    public void onClick(View view) {
                        // Request the permission
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.READ_SMS},
                                PERMISSION_REQUEST_SMS);
                    }
                }).show();

            } else {

                Snackbar.make(view,
                        R.string.requesting_permission,
                        Snackbar.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_SMS},
                        PERMISSION_REQUEST_SMS);
            }
        }
    }

    public static void requestCallPermission(final Activity activity, View view){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CALL_LOG)) {

                Snackbar.make(view, R.string.permission_call_log_explanation,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                    final Activity act = activity;
                    @Override
                    public void onClick(View view) {
                        // Request the permission
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.READ_CALL_LOG},
                                PERMISSION_REQUEST_CALL_LOG);
                    }
                }).show();

            } else {

                Snackbar.make(view,
                        R.string.requesting_permission,
                        Snackbar.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_CALL_LOG},
                        PERMISSION_REQUEST_CALL_LOG);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_SMS) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(btnScreenColor, R.string.sms_permission_was_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(btnScreenColor, R.string.sms_permission_was_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();

                cbSms.setChecked(false);
            }
        }

        if (requestCode == PERMISSION_REQUEST_CALL_LOG) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(btnScreenColor, R.string.call_log_permission_was_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(btnScreenColor, R.string.call_log_permission_was_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
                cbMissedCall.setChecked(false);
            }
        }
    }


}
