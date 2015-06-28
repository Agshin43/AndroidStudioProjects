package com.apps.akaya.touchwebsites;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String ss = "saman:\"123456\"";
        Log.i("",getValueFrom("sebzul:x123456x"));
        Log.i("",getValueFrom(ss));
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

    private String getValueFrom(String jstring)
    {
        if(!jstring.contains(":"))
        {
            return jstring;
        }
        String ret = jstring.split(":")[1];
        ret = ret.replace("{","");
        ret = ret.replace("}","");

        ret = ret.substring(1, ret.length()-1);

        //"1234"

        return ret;
    }
}
