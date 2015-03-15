package com.apps.akaya.countryquiz;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;


public class ChooseGame extends ActionBarActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        Button btn_flags = (Button) findViewById(R.id.btn_flags);
        Button btn_coats = (Button) findViewById(R.id.btn_coats);
        Button btn_capitals = (Button) findViewById(R.id.btn_capitals);
        Button btn_populations = (Button) findViewById(R.id.btn_populations);
        Button btn_areas = (Button) findViewById(R.id.btn_areas);
        Button btn_mixed = (Button) findViewById(R.id.btn_mixed);

        btn_mixed.setOnClickListener(this);
        btn_flags.setOnClickListener(this);
        btn_coats.setOnClickListener(this);
        btn_populations.setOnClickListener(this);
        btn_capitals.setOnClickListener(this);
        btn_areas.setOnClickListener(this);
        Animation vanish;
        vanish = AnimationUtils.loadAnimation(this, R.anim.vanish);

        btn_mixed.startAnimation(vanish);
        btn_flags.startAnimation(vanish);
        btn_coats.startAnimation(vanish);
        btn_populations.startAnimation(vanish);
        btn_capitals.startAnimation(vanish);
        btn_areas.startAnimation(vanish);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        Animation shake;
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        int id = v.getId();

        switch (id) {
            case R.id.btn_areas:
                //Your Operation
                findViewById(R.id.btn_areas).startAnimation(shake);
                break;

            case R.id.btn_capitals:
                findViewById(R.id.btn_capitals).startAnimation(shake);

                //Your Operation
                break;
            case R.id.btn_coats:
                findViewById(R.id.btn_coats).startAnimation(shake);

                //Your Operation
                break;
            case R.id.btn_flags:
                findViewById(R.id.btn_flags).startAnimation(shake);

                //Your Operation
                break;
            case R.id.btn_mixed:
                findViewById(R.id.btn_mixed).startAnimation(shake);

                //Your Operation
                break;
            case R.id.btn_populations:
                findViewById(R.id.btn_populations).startAnimation(shake);

                //Your Operation
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_game, menu);
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
