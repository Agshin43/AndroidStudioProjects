package livescores.biz.livescores;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Settings extends AppCompatActivity {

    private int langId;
    private boolean notify;
    private int timeOutId;
    private boolean autoRefresh;
    private boolean onStart = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.action_settings));
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btnAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAbout();
            }
        });


        loadSettings();

    }
    private void displayAbout(){
        new AlertDialog.Builder(Settings.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.about)
                .setPositiveButton(R.string.m_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.ic_launcher)
                .show();
    }

    private void saveSettings(){
        SharedPreferences pref = getSharedPreferences("lscores", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("langId",langId);
        editor.putInt("timeOutId", timeOutId);
        editor.putBoolean("notify", notify);
        editor.putBoolean("autoRefresh", autoRefresh);

        editor.commit();
    }
    private void loadSettings(){
        SharedPreferences pref = getSharedPreferences("lscores", MODE_PRIVATE);
        langId     = pref.getInt("langId", 0);
        notify = pref.getBoolean("notify", false);
        timeOutId  = pref.getInt("timeOutId", 4);
        autoRefresh = pref.getBoolean("autoRefresh", true);

        SwitchCompat swAutoRefresh = (SwitchCompat) findViewById(R.id.swAutoRefresh);
        swAutoRefresh.setChecked(autoRefresh);


        final Spinner spTimeout = (Spinner) findViewById(R.id.spTimeout);
        spTimeout.setEnabled(autoRefresh);
        List<String> ts = new ArrayList<>();
        String minute = getResources().getString(R.string.m_min);
        String second = getResources().getString(R.string.m_sec);
        ts.add("30 " + second);
        ts.add("45 " + second);
        ts.add("1 " + minute);
        ts.add("2 "+minute);
        ts.add("3 "+minute);
        ts.add("5 " + minute);
        ts.add("10 " + minute);
        ts.add("30 " + minute);

        ArrayAdapter<String> tad = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item_settings,ts);
        spTimeout.setAdapter(tad);
        spTimeout.setSelection(timeOutId);

        final Spinner spLanguage = (Spinner) findViewById(R.id.spLanguage);
        spTimeout.setEnabled(autoRefresh);
        List<String> ls = new ArrayList<>();

        ls.add("EN");
        ls.add("RU");
        ls.add("AZ");
        ls.add("TR");


        ArrayAdapter<String> lad = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item_settings,ls);
        spLanguage.setAdapter(lad);

        spLanguage.setSelection(langId);

//        spLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                langId = position;
//                saveSettings();
//                setLanguage();
//                displayLangueDialog();
//            }
//        });
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(onStart){
                    onStart = false;
                    return;
                }
                langId = position;
                saveSettings();
                setLanguage();
                displayLangueDialog();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTimeout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeOutId = position;
                saveSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swAutoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spTimeout.setEnabled(isChecked);
                autoRefresh = isChecked;
                saveSettings();
            }
        });


        SwitchCompat swNotify = (SwitchCompat) findViewById(R.id.swNotify);
        swNotify.setChecked(notify);
        swNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notify = isChecked;
                saveSettings();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ok();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ok();
                break;
        }
        return true;
    }

    private void languageChanged(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("finish",true);

        setResult(Activity.RESULT_OK,returnIntent);
    }
    private void ok(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("notify",notify);
        returnIntent.putExtra("autoRefresh",autoRefresh);
        returnIntent.putExtra("timeOutId",timeOutId);
        returnIntent.putExtra("langId", langId);

        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void setLanguage(){
        String[] ls = {"en","ru","az","tr"};

        Locale locale = new Locale(ls[langId]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void displayLangueDialog(){
        new AlertDialog.Builder(Settings.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.need_restart_for_language)
                .setPositiveButton(R.string.m_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        languageChanged();
//                        try {
//                            ((Activity) Class.forName("livescores.biz.livescores.MainActivity").newInstance()).finish();
//                        } catch (InstantiationException e) {
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        Log.i("RESTART","-----1");
                        Intent mStartActivity = new Intent(getApplicationContext(), Splash.class);
                        Log.i("RESTART","-----2");
                        int mPendingIntentId = 123456;
                        Log.i("RESTART","-----3");
                        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        Log.i("RESTART","-----3");
                        AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Log.i("RESTART","-----4");
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 6000, mPendingIntent);
                        Log.i("RESTART", "-----5");
                        finish();
                        Log.i("RESTART", "-----6");
                    }
                })
                .setIcon(R.drawable.ic_launcher)
                .show();
    }


}
