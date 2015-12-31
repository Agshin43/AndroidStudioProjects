package com.apps.agshin.countryquiz;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tvAppName = (TextView) findViewById(R.id.tvAppName);
        TextView tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
        tvAppName.setText(getResources().getString(R.string.app_name));
        tvAppVersion.setText(appVersionName());

        animateTvCorrect();



        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(1600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    getApplicationContext().startActivity(new Intent(getApplicationContext(), PlayQuizActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    Splash.this.finish();
                }
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    private String appVersionName(){
        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo("com.apps.agshin.countryquiz", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return "version "+info.versionName;
    }

    private void animateTvCorrect()
    {
        ImageView ivLogo = (ImageView) findViewById(R.id.ivLogo);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.bounce);
        a.reset();
        ivLogo.clearAnimation();
        ivLogo.startAnimation(a);
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
