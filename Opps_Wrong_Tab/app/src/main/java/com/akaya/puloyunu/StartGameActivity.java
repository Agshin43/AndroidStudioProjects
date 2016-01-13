package com.akaya.puloyunu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class StartGameActivity extends Activity
		 {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start_game);
		CustomeSurface.tileHolder.clear();
	}

	protected void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
		VarHolder.IS_APPLICATION_PAUSE=true;
		VarHolder.IS_GAME_PAUSE=true;
	}
	protected void onResume() {
		super.onResume();
		overridePendingTransition(0, 0);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_game, menu);
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
	int GAME_PAUSE_COUNT=0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case (MotionEvent.ACTION_DOWN):
			if(VarHolder.FIRST_TOUCH){
				VarHolder.IS_TOUCHED=true;
			}
			VarHolder.FIRST_TOUCH=true;
			if(VarHolder.IS_GAME_PAUSE) {
				
				if(CustomeSurface.tileHolder.size()>2)
					CustomeSurface.tileHolder.clear();
				if(GAME_PAUSE_COUNT%2==0) {
					VarHolder.IS_GAME_PAUSE=false;
				}
				if(GAME_PAUSE_COUNT!=0)
					GAME_PAUSE_COUNT++;
			}
			break;
		case (MotionEvent.ACTION_UP):
			break;
		}
		VarHolder.Touch.X = event.getX();
		VarHolder.Touch.Y = event.getY();
		return super.onTouchEvent(event);
	}
	public void onBackPressed() {
		if(VarHolder.IS_APPLICATION_PAUSE || VarHolder.IS_GAME_PAUSE){
		Intent i = new Intent(getBaseContext(),OptionMenuActivity.class);
        startActivity(i);
        finish();
		}
	};
}
