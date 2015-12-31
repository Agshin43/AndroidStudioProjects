package com.agshin.brightpolicelights;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PatternPlayer extends View {
    private Timer timer;
    private TimerTask timerTask;
    private float H = -1;
    private float W = -1;

    private Pattern pattern;
    private int played;
    private Paint paint;
    private ArrayList<Integer> timeStops;
    public PatternPlayer(Context context, Pattern pattern) {
        super(context);

        this.pattern = pattern;
        initTimes();
        played = 0;

        paint = new Paint();


        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 50);
    }

    public int getSirenId(){
        return pattern.getSirenId();
    }

    private void initTimes(){
        timeStops = new ArrayList<>();

        if(pattern.getElements().size() == 0){
            return;
        }
        timeStops.add(pattern.getElements().get(0).getDuration());
//        int st = 0;
        for(int i = 1; i < pattern.getElements().size(); ++i){
            int ts = timeStops.get(i - 1) + pattern.getElements().get(i).getDuration();

            timeStops.add(ts);
        }
//        timeStops.add(st);
    }

    public void setPattern(Pattern pattern){
        this.pattern = pattern;
        initTimes();
    }

    private void updatePaint(){

        if(played > timeStops.get(timeStops.size()-1)){
            played = 0;
        }

        for(int i = 0; i < timeStops.size();i++){
            if(played < timeStops.get(i)){
                if(pattern.getElements().get(i).getType() == PatternElementType.Light){
                    paint.setColor(pattern.getElements().get(i).getColor());
                } else {
                    paint.setColor(Color.rgb(0,0,0));
                }
                break;
            }
        }

    }


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                if(H < 10 || W < 10) {
                    H = getHeight();
                    W = getWidth();
                } else {
                    updatePaint();
                    invalidate();
                    played += 50;
                }

            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0, W, H, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                actionDown(event.getX(), event.getY());
                break;

            }

            case MotionEvent.ACTION_MOVE: {
                actionMove(event.getX(), event.getY());
                break;
            }

            case MotionEvent.ACTION_UP: {
                actionUp(event.getX(), event.getY());
                break;
            }

            default: { break;}

        }

        invalidate();
        return true;
    }

    private void actionDown(float x, float y){

    }

    private void actionMove(float x, float y){

    }

    private void actionUp(float x, float y){

    }


}