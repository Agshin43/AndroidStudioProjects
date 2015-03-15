package com.apps.akaya.picnest;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static com.apps.akaya.picnest.R.drawable.ic_menu;

public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = (Toolbar) findViewById(R.id.toolbarv7main);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            // configuring toolbar options
//            toolbar.setLogo(R.drawable.ic_image_launcher);
//            toolbar.setTitle(R.string.app_name);
//            toolbar.setTitleTextColor(Color.rgb(255,255,255));
//
//
//            //------------------------------------------------
//        }else {
//            Toast.makeText(this,"null toolbar",Toast.LENGTH_SHORT).show();
//        }
        final Button btn_create = ( Button ) findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation vanish;
                vanish = AnimationUtils.loadAnimation(MainActivity.this, R.anim.vanish);
                btn_create.startAnimation(vanish);
                MainActivity.this.startActivity(new Intent(MainActivity.this, MakePicnest.class));
//                MainActivity.this.startActivity(new Intent(MainActivity.this, TileSizeAndPicSelect.class));

            }
        });
        final Button btn_open = ( Button ) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake;
                shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                btn_open.startAnimation(shake);
            }
        });

    }

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//
//        return super.onOptionsItemSelected(item);
//    }
}

