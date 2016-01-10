package livescores.biz.livescores;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class Splash extends AppCompatActivity {


    final static int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
    private ImageButton icon;

    private boolean readPhoneStatePermissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        icon = (ImageButton) findViewById(R.id.icon);
//        hideNB();


        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    requestReadPhoneStatePermission(Splash.this, icon);
                }
            }
        }.start();
    }


    //////////////////////////////// Permissions

    public  void requestReadPhoneStatePermission(final Activity activity, View view){
        Log.i("PERMISSION", "req per.");
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_PHONE_STATE)) {

                Snackbar.make(view, R.string.permission_read_phone_state_explanation,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.m_ok, new View.OnClickListener() {
                    final Activity act = activity;
                    @Override
                    public void onClick(View view) {
                        // Request the permission
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                PERMISSION_REQUEST_READ_PHONE_STATE);
                    }
                }).show();

            } else {

                Snackbar.make(view,
                        R.string.requesting_permission,
                        Snackbar.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSION_REQUEST_READ_PHONE_STATE);
            }
        } else {
            readPhoneStatePermissionGranted = true;
            getApplicationContext().startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            Splash.this.finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(icon, R.string.read_phone_state_permission_was_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();

                readPhoneStatePermissionGranted = true;
                getApplicationContext().startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Splash.this.finish();
            } else {
                Snackbar.make(icon, R.string.read_phone_state_permission_was_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
                readPhoneStatePermissionGranted = false;
            }
        }

    }

    private void hideNB(){
        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
    }

}
