package com.apps.agshin.countryquiz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void imageButtonClicked(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_flag:
            {
                getApplicationContext().startActivity(new Intent(getApplicationContext(), TestQuizActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            case R.id.btn_coat_of_arms:
            {

            }
            case R.id.btn_capital:
            {

            }
            case R.id.btn_area:
            {

            }
            case R.id.btn_currency:
            {

            }
            case R.id.btn_domain:
            {

            }
        }
    }
}
