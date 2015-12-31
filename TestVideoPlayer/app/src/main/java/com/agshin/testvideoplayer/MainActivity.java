package com.agshin.testvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.agshin.testvideoplayer.fvideoplayer.FullscreenVideoLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    FullscreenVideoLayout videoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);
        videoLayout.setShouldAutoplay(false);

        Uri videoUri = Uri.parse("storage/sdcard0/DCIM/Camera/20151105_232705.mp4");
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        Button btnFSVPA = (Button) findViewById(R.id.btnFullScreenVP);
//        btnFSVPA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, FSVPActivity.class));
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
            startActivity(new Intent(MainActivity.this, FSVPActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
