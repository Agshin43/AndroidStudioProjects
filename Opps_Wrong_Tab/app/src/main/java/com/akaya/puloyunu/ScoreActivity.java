package com.akaya.puloyunu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ScoreActivity extends Activity {
	TextView normal,danger,thunder,zen,rush;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_score);
		normal = (TextView) findViewById(R.id.normalScore);
		danger = (TextView) findViewById(R.id.dangerScore);
		thunder = (TextView) findViewById(R.id.thunderScore);
		zen = (TextView) findViewById(R.id.zenScore);
		rush = (TextView) findViewById(R.id.rushScore);
		
		normal.setText(PermanentScoreHolder.getScore("1")+"");
		danger.setText(PermanentScoreHolder.getScore("2")+"");
		thunder.setText(PermanentScoreHolder.getScore("3")+"");
		zen.setText(PermanentScoreHolder.getScore("4")+"S");
		rush.setText(PermanentScoreHolder.getScore("5")+"");
	}
	public void onBackPressed() {
		Intent i = new Intent(getBaseContext(),OptionMenuActivity.class);
        startActivity(i);
        finish();
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score, menu);
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
