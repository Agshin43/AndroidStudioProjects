package com.akaya.puloyunu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


public class LauncherActivity extends Activity {
	int totalOperations=7;
	float operationDone=1;
	ProgressBar pb;
	TextView tv;
	int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_launcher);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		tv = (TextView) findViewById(R.id.textView1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(VarHolder.LOADING_BITMAP_COUNT!=totalOperations){
					//Log.e("Inside",": "+operationDone + " : " + Variable.LOADING_BITMAP_COUNT + " : " + (Variable.LOADING_BITMAP_COUNT*100)/totalOperations);
					try {
						Thread.sleep(10);
						operationDone=((VarHolder.LOADING_BITMAP_COUNT*100)/totalOperations);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Log.e("Launcher","Running");
								tv.setText("Loaded " + operationDone + "%");
							}
						});	
						pb.setProgress((int)operationDone);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    k++;
				}
			}
		}).start();
		new Thread(new Runnable() {		
			@Override
			public void run() {
				if(!VarHolder.EXIT_APPLICATION){
						long time = System.currentTimeMillis();
						init();
						while(System.currentTimeMillis()-time<=3000);
						Intent i = new Intent(getApplicationContext(),OptionMenuActivity.class);
						startActivity(i);
						finish();			
				}
			}
		}).start();
		
    }
    @Override
    protected void onPause() {
    	super.onResume();
    	overridePendingTransition(0, 0);
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	overridePendingTransition(0, 0);
    }
	public void updateProgressBar(){
		VarHolder.LOADING_BITMAP_COUNT++;	
	}
    @SuppressLint("NewApi")
	public void init(){
		if(!VarHolder.customeSurfaceBitmapLoaded){	
	
		VarHolder.SHARED_PREFERENCES= getSharedPreferences(VarHolder.PREFERENCES, Context.MODE_PRIVATE);
		VarHolder.EDITOR = VarHolder.SHARED_PREFERENCES.edit();
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		VarHolder.SCREEN_WIDTH = size.x;
		VarHolder.SCREEN_HEIGHT = size.y;
		updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		updateProgressBar();
			ImageHolder.BACKGROUND = Bitmap.createScaledBitmap(VarHolder.TEMP, VarHolder.SCREEN_WIDTH, VarHolder.SCREEN_HEIGHT, false);
		updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.restart);
		updateProgressBar();
			ImageHolder.RESTART_BUTTON = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH*0.2), (int) (VarHolder.SCREEN_WIDTH*0.2), false);
		updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.danger);
		updateProgressBar();
			ImageHolder.DANGER = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH * 0.25), (int) (VarHolder.SCREEN_WIDTH * 0.25), false);
		updateProgressBar();

			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.manat1);
		updateProgressBar();
			ImageHolder.MANAT1 = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH * 0.25), (int) (VarHolder.SCREEN_HEIGHT * 0.25), false);

			updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.manat5);
			updateProgressBar();
			ImageHolder.MANAT5 = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH * 0.25), (int) (VarHolder.SCREEN_HEIGHT * 0.25), false);

			updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.manat10);
			updateProgressBar();
			ImageHolder.MANAT10 = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH * 0.25), (int) (VarHolder.SCREEN_HEIGHT * 0.25), false);

			updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.manat20);
			updateProgressBar();
			ImageHolder.MANAT20 = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH * 0.25), (int) (VarHolder.SCREEN_HEIGHT * 0.25), false);


			updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.manat50);
			updateProgressBar();
			ImageHolder.MANAT50 = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH * 0.25), (int) (VarHolder.SCREEN_HEIGHT * 0.25), false);

			updateProgressBar();
			VarHolder.TEMP = BitmapFactory.decodeResource(getResources(), R.drawable.manat100);
			updateProgressBar();
			ImageHolder.MANAT100 = Bitmap.createScaledBitmap(VarHolder.TEMP, (int) (VarHolder.SCREEN_WIDTH * 0.25), (int) (VarHolder.SCREEN_HEIGHT * 0.25), false);
		
		VarHolder.TEMP.recycle();
		VarHolder.TEMP=null;
		VarHolder.customeSurfaceBitmapLoaded=true;
		updateProgressBar();
		
		
		
		}
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
