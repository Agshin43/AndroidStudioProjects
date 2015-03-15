package com.apps.akaya.mytests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by agshin on 2/28/15.
 */
public class TileSizeMenu extends View {
    public int columnCount;
    public int rowCount;

    final static int MAX_COL_COUNT = 8;
    final static int MAX_ROW_COUNT = 8;

    private PointF downPos;
    private PointF movePos;
    private PointF upPos;

    private Canvas canvas;
    private Paint paint;
    Bitmap picsBitmap;

    int width;
    int height;

    RelativeLayout parentLayout;

    private boolean geomInitialized = false;

    private Timer timer;
    private TimerTask timerTask;

    public TileSizeMenu(Context context, int columnCount, int rowCount, RelativeLayout parentLayout, int drawable) {
        super(context);
        this.columnCount = columnCount;
        this.rowCount = rowCount;

        this.parentLayout = parentLayout;

        this.picsBitmap  = BitmapFactory.decodeResource(context.getResources(), drawable);

        paint = new Paint();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 100000);
    }


    @Override
    public void onDraw(Canvas canvas)
    {
        this.canvas = canvas;
        super.onDraw(canvas);

        paint.setARGB(255, 30, 30, 255);

        float hg = this.width;
        float wd = this.height;
        // Drawing lines
        float w = wd / MAX_COL_COUNT;
        for(int i = 0; i < MAX_COL_COUNT+1; i++)
        {
            canvas.drawLine(i*w,0,i*w, hg, paint);
        }
        for(int i = 0; i < MAX_ROW_COUNT+1; i++)
        {
            canvas.drawLine(0,i*w,wd,i*w, paint);
        }

        // Drawing tiles
        paint.setARGB(100,255,30,30);
        canvas.drawRect(0,0, columnCount*w, rowCount*w, paint);

        canvas.drawBitmap(this.picsBitmap,
                new Rect(0,0,picsBitmap.getWidth(),picsBitmap.getHeight()),
                new Rect(0,0, (int)(columnCount*w), (int)(rowCount*w)), paint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                touchDown(new PointF( event.getX(), event.getY()));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                touchMove(new PointF( event.getX(), event.getY()));
                break;
            }
            case MotionEvent.ACTION_UP: {
                touchUp(new PointF( event.getX(),  event.getY()));
                break;
            }
        }
        this.invalidate();
        return true;
    }

    private void calculateColRow(PointF pos)
    {
        columnCount = (int) (MAX_COL_COUNT * ( pos.x / this.width ))+1;
        rowCount    = (int) (MAX_ROW_COUNT * ( pos.y / this.height ))+1;

        if(columnCount > MAX_COL_COUNT)
        {
            columnCount = MAX_COL_COUNT;
        }
        if(rowCount > MAX_ROW_COUNT)
        {
            rowCount = MAX_ROW_COUNT;
        }

    }

    private void touchDown(PointF pos) {
        downPos = pos;
        calculateColRow(pos);
    }

    private void touchMove(PointF pos) {
        movePos = pos;
        calculateColRow(pos);
    }

    private void touchUp(PointF pos) {
        upPos = pos;
        calculateColRow(pos);
    }

    private void initGeom() {
        if (!geomInitialized) {
            this.width = parentLayout.getWidth();
            this.height = this.width;
            if (this.width > 100) {

                geomInitialized = true;
                this.invalidate();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initGeom();
        }
    };
}
