package com.apps.akaya.countryquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_play = (Button) findViewById(R.id.btn_play);
        Button btn_learn = (Button) findViewById(R.id.btn_learn);
        Button btn_high_scores = (Button) findViewById(R.id.btn_high_scores);

        btn_play.setOnClickListener(this);
        btn_high_scores.setOnClickListener(this);
        btn_learn.setOnClickListener(this);

        Animation vanish;
        vanish = AnimationUtils.loadAnimation(this, R.anim.vanish);
        btn_play.startAnimation(vanish);
        btn_high_scores.startAnimation(vanish);
        btn_learn.startAnimation(vanish);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        Animation shake;
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        int id = v.getId();

        switch (id) {
            case R.id.btn_play:
                //Your Operation
                findViewById(R.id.btn_play).startAnimation(shake);
                startActivity(new Intent(MainActivity.this, ChooseGame.class));
                break;

            case R.id.btn_learn:
                findViewById(R.id.btn_learn).startAnimation(shake);

                //Your Operation
                break;
            case R.id.btn_high_scores:
                findViewById(R.id.btn_high_scores).startAnimation(shake);
                //Your Operation
                break;
            default:
                break;
        }
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
