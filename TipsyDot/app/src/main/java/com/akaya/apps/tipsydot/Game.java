package com.akaya.apps.tipsydot;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Game extends View {
    private Timer timer;
    private TimerTask timerTask;
    private float H = -1;
    private float W = -1;
    final static float SCREEN_RATIO = 1.5f;
    public Game(Context context) {
        super(context);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };



    }


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                if(H < 0) {
                    H = getHeight();
                    W = SCREEN_RATIO * H;
                }




            }
        }
    };
}
