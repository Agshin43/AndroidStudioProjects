package com.akaya.apps.burcler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ConstellationsView extends View{

    private Paint paint;
    private Paint textPaint;

    private ArrayList<Box> boxesM = new ArrayList<Box>();
    private ArrayList<Box> boxesF = new ArrayList<Box>();
    private float W = -1;
    private float H = -1;

    private int compMid = -1;
    private int compFid = -1;


    private float margin;
    private float radius;

    private TimerTask timerTask;
    private Timer timer;


    private Context context;
    public ConstellationsView(Context context) {
        super(context);
        this.context = context;

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 10, 100);
    }

    // Overrides
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawColor(getResources().getColor(R.color.colorBackgroundUygunluq));
        if(W == -1) return;
        for(int i = 0; i < 12; i++)
        {
            if(compMid == i)
            {
                if(boxesM.get(compMid).dragging)
                {
                    paint.setColor(getResources().getColor(R.color.colorButtonDragging));
                } else {
                    paint.setColor(getResources().getColor(R.color.colorButtonPressed));
                }
            } else {
                paint.setColor(getResources().getColor(R.color.colorButton));
            }
            boxesM.get(i).draw(paint, textPaint, canvas);

            if(compFid == i)
            {
                if(boxesF.get(compFid).dragging)
                {
                    paint.setColor(getResources().getColor(R.color.colorButtonDragging));
                } else {
                    paint.setColor(getResources().getColor(R.color.colorButtonPressed));
                }
            } else {
                paint.setColor(getResources().getColor(R.color.colorButton));
            }
            boxesF.get(i).draw(paint, textPaint, canvas);
        }
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

            default:{ break;}

        }

        invalidate();
        return true;
    }
    // Overrides.


    private void actionDown(float x, float y)
    {

        for(int i = 0; i < 12; i++)
        {
            if( dist(boxesM.get(i).center.x, y, x, boxesM.get(i).center.y) <= radius )
            {
                compMid = i;
                boxesM.get(i).dragging = true;
                return;
            }
        }
        for(int i = 0; i < 12; i++)
        {
            if( dist(boxesF.get(i).center.x, y, x, boxesF.get(i).center.y) <= radius )
            {
                compFid = i;
                boxesF.get(i).dragging = true;
                return;
            }
        }

    }

    private void actionMove(float x, float y)
    {
        if(compMid != -1 )
        {
            if(boxesM.get(compMid).dragging) {
                boxesM.get(compMid).setCenter(x, y, 0, 0);
            }

            boolean f = false;
            for(int i = 0; i < 12; i++)
            {
                if( dist(boxesF.get(i).center.x, y, x, boxesF.get(i).center.y) <= (3 * radius) )
                {
                    f = true;
                    compFid = i;
                    break;
                }
            }

            if(!f)
            {
                compFid = -1;
            }
//            return;
        }

        if(compFid != -1 )
        {
            if(boxesF.get(compFid).dragging) {
                boxesF.get(compFid).setCenter(x, y, 0, 0);
            }

            boolean f = false;

            for(int i = 0; i < 12; i++)
            {
                if( dist(boxesM.get(i).center.x, y, x, boxesM.get(i).center.y) <= ( 3 * radius ) )
                {
                    f = true;
                    compMid = i;
                    break;
                }
            }

            if(!f)
            {
                compMid = -1;
            }

//            return;
        }
    }

    private void actionUp(float x, float y)
    {
        if(compMid != -1)
        {
            boxesM.get(compMid).dragging = false;
            boxesM.get(compMid).resetCenter();
        }

        if(compFid != -1)
        {
            boxesF.get(compFid).dragging = false;
            boxesF.get(compFid).resetCenter();
        }

        compMid = -1;
        compFid = -1;
    }

    private void doComparison(String cn1, String cn2){

    }

    private float dist(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }

    private void initPaints() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getResources().getColor(R.color.colorButtonText));
        textPaint.setTextSize(radius / 2.8f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorButton));
        paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

//        mShadowPaint = new Paint(0);
//        mShadowPaint.setColor(0xff101010);
//        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }
    private String getConName(int i){
        int id = getResources().getIdentifier("str_c"+i, "string", context.getPackageName());
        return getResources().getString(id);
    }
    private Bitmap getConBitmap(int i){
        int id = getResources().getIdentifier("c"+i, "drawable", context.getPackageName());
        return convertToBitmap(getResources().getDrawable(id), (int) (radius*0.95f), (int) (radius*0.95f));
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
    private void initBoxes()
    {
        boxesM.clear();
        boxesF.clear();

        for (int i = 0; i < 12; i++)
        {
            Box bxm = new Box(i, margin + (4*radius), margin + (radius * (i * 2 + 1) ), radius, getConName(i), getConBitmap(i));
            boxesM.add(bxm);

            Box bxf = new Box(i, W - margin - (4*radius), margin + (radius * (i * 2 + 1) ), radius, getConName(i), getConBitmap(i));
            boxesF.add(bxf);
        }
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 0:{
                    if(W == -1)
                    {
                        W = getWidth();
                        H = getHeight();

                        margin = W / 20.f;
                        radius = ((H-(2*margin))/24.f);


                        initBoxes();
                        initPaints();
                        invalidate();
                    }
                    break;
                }
            }
        }
    };

}
class Box
{
    int number;
    String text;
    Bitmap imageBitmap;
    Float radius;
    PointF center;
    Color color1;
    Color color2;
    boolean dragging = false;

    PointF baseCenter;

    Rect imageRect;
    RectF destImgRect;

    public Box(int number, float x, float y, float radius, String text, Bitmap imageBitmap )
    {
        this.number = number;
        this.imageBitmap = imageBitmap;
        this.text = text;
        this.center = new PointF(x, y);
        this.baseCenter = new PointF(x,y);
        this.radius = radius;
    }

    public void SetColor1(Color color1)
    {
        this.color1 = color1;
    }

    public void resetCenter()
    {
        center.set(baseCenter.x, baseCenter.y);
    }

    public void setCenter(float x, float y, float dx, float dy){
        this.center.set(x - dx, y - dy);

    }

    public void draw(Paint pnt, Paint txtPnt, Canvas cn)
    {
        if(dragging)
        {
            cn.drawCircle(center.x, center.y, radius, txtPnt);
        }
        cn.drawCircle(center.x, center.y, radius*0.9f, pnt);
        cn.drawText(text, center.x, center.y + (radius / 2.5f), txtPnt);
        cn.drawBitmap(imageBitmap, center.x - (radius/2), center.y - (radius*0.8f), pnt);
    }
}