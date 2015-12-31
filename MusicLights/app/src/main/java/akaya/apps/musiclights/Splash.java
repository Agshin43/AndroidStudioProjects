package akaya.apps.musiclights;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Splash.this, MainActivity.class));
            }
        });

    }

}
