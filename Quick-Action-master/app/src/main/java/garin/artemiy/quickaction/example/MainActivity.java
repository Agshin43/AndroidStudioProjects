package garin.artemiy.quickaction.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import garin.artemiy.quickaction.R;
import garin.artemiy.quickaction.library.QuickAction;

public class MainActivity extends Activity {

    private QuickAction customQuickAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        @SuppressLint("InflateParams") RelativeLayout customLayout =
                (RelativeLayout) getLayoutInflater().inflate(R.layout.popup_custom_layout, null);
        customQuickAction = new QuickAction(this, R.style.PopupAnimation, customLayout);
    }

    @SuppressWarnings("unused")
    public void onClickTopButton(View view) {
        customQuickAction.show(view);
    }

    @SuppressWarnings("unused")
    public void onClickMiddleButton(View view) {
        customQuickAction.show(view);
    }

    @SuppressWarnings("unused")
    public void onClickBottomButton(View view) {
        customQuickAction.show(view);
    }

}
