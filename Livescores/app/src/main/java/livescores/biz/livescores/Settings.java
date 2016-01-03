package livescores.biz.livescores;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    private int langId;
    private boolean notify;
    private int timeOutId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.action_settings));
        actionBar.setDisplayHomeAsUpEnabled(true);


        loadSettings();

    }

    private void saveSettings(){
        SharedPreferences pref = getSharedPreferences("lscores", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("langId",langId);
        editor.putInt("timeOutId",timeOutId);
        editor.putBoolean("notify", notify);

        editor.commit();
    }
    private void loadSettings(){
        SharedPreferences pref = getSharedPreferences("lscores", MODE_PRIVATE);
        langId     = pref.getInt("langId", 0);
        notify = pref.getBoolean("notify", false);
        timeOutId  = pref.getInt("timeOutId", 0);

        final Spinner spTimeout = (Spinner) findViewById(R.id.spTimeout);
        spTimeout.setEnabled(notify);
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
        spTimeout.setEnabled(notify);
        List<String> ls = new ArrayList<>();

        ls.add("AZ");
        ls.add("EN");
        ls.add("RU");
        ls.add("TR");


        ArrayAdapter<String> lad = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item_settings,ls);
        spLanguage.setAdapter(lad);
        spLanguage.setSelection(langId);

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                langId = position;
                saveSettings();
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

        SwitchCompat swNotify = (SwitchCompat) findViewById(R.id.swNotify);
        swNotify.setChecked(notify);
        swNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spTimeout.setEnabled(isChecked);
                notify = isChecked;
                saveSettings();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }
}
