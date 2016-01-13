package com.akaya.puloyunu;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;

public class OptionMenuActivity extends Activity {
	ObjectAnimator animY ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_option_menu);
		
	}
	public void startGameActivity(final View view){
		animY = ObjectAnimator.ofFloat(view, "translationY", -200f, 0f);
		animY.setDuration(500);
		animY.setInterpolator(new BounceInterpolator());
		
		animY.setRepeatCount(0);
		animY.start();
		view.setBackgroundColor(Color.RED);
		animY.addListener(new AnimatorListener() {	
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if(view.getTag().equals("normal")){
					VarHolder.GAME_TYPE=1;
				}else if(view.getTag().equals("thunder")){
					VarHolder.GAME_TYPE=2;
				}else if(view.getTag().equals("danger")){
					VarHolder.GAME_TYPE=3;
				}else if(view.getTag().equals("zen")){
					VarHolder.GAME_TYPE=4;
				}else if(view.getTag().equals("rush")){
					VarHolder.GAME_TYPE=5;
				}
				Intent i = new Intent(getApplicationContext(),StartGameActivity.class);
				startActivity(i);
				finish();
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	public void startAboutUs(final View view){
		animY = ObjectAnimator.ofFloat(view, "translationY", -200f, 0f);
		animY.setDuration(500);
		animY.setInterpolator(new BounceInterpolator());
		animY.setRepeatCount(0);
		animY.start();
		view.setBackgroundColor(Color.RED);
		animY.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if(view.getTag().equals("about")) {
				Intent i = new Intent(getApplicationContext(),AboutActivity.class);
				startActivity(i);
				finish();
				}else if(view.getTag().equals("score")) {
					Intent i = new Intent(getApplicationContext(),ScoreActivity.class);
					startActivity(i);
					finish();
				}
			}
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
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
	public void onBackPressed() {
		
	};
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
}
