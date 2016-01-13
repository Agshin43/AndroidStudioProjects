package az.amf.ipotekakalkulyatoru;


import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Looper.prepare();
					while (true) {
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						
						Thread.sleep(2000);
						finish();
						startActivity(intent);
						return;

				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.splash, menu);
//		return true;
//	}

}
