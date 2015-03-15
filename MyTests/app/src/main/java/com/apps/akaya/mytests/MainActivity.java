package com.apps.akaya.mytests;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button btnShowDialog = ( Button ) findViewById(R.id.btn_show_dialog);
//        btnShowDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(MainActivity.this, CustomSpinner.class));
//                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//                View layout = inflater.inflate(R.layout.layout_dialog, null);
//
//                RelativeLayout mLayout = (RelativeLayout) layout.findViewById(R.id.lay_m_view);
//                TileSizeMenu tileSizeMenu = new TileSizeMenu(MainActivity.this, 4, 4, mLayout,R.drawable.pic_animal16);
//
//                mLayout.addView(tileSizeMenu);
//
//                AlertDialog MyDialog;
//                AlertDialog.Builder MyBuilder = new AlertDialog.Builder(MainActivity.this);
//                MyBuilder.setTitle("title");
//                MyBuilder.setView(layout);
//                MyDialog = MyBuilder.create();
//                MyDialog.show();
//
//            }
//        });
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
