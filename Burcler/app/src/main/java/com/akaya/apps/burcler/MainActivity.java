package com.akaya.apps.burcler;

import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {


    private FragmentTabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        String burcler = getApplicationContext().getResources().getString(R.string.str_burcler);
        String uygunluq = getApplicationContext().getResources().getString(R.string.str_uygunluq);
        String qoroskop = getApplicationContext().getResources().getString(R.string.str_qoroskop);

        mTabHost.addTab(
                mTabHost.newTabSpec(burcler).setIndicator(burcler, null),
                TabBurcler.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec(uygunluq).setIndicator(uygunluq, null),
                TabUygunluq.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec(qoroskop).setIndicator(qoroskop, null),
                TabBurcler.class, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
