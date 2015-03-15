package com.apps.akaya.picnest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PicsView extends View {
    Activity context;
    int horizontalCount;
    int verticalCount;
    int width;
    int height;
    float picWidth;
    float picHeight;
    ArrayList<Pic> pics;
    Paint paint;
    Paint mPaint;
    Paint blurPaint;

    private PointF downPos;
    private PointF movePos;
    private PointF upPos;

    private boolean geomInitialized = false;

    private Timer timer;
    private TimerTask timerTask;
    private Canvas canvas;
    private RelativeLayout parentLayout;

    private RectF rectActs;
    private RectF rectAct1;
    private RectF rectAct2;
    private RectF rectAct3;

    private Bitmap bitmapAct1;
    private Bitmap bitmapAct2;
    private Bitmap bitmapAct3;

    private Path mTriangle;
    PointF ta;
    PointF tb;
    PointF tc;

    private int paddingLeft;
    private int paddingTop;

    private int picsDrawable;

    public Bitmap picsBitmap;
    public int selectedPicId;

    public int actionNumber;




    public PicsView(Activity context, RelativeLayout parentLayout, int horizontalCount, int verticalCount, int picsDrawable) {
        super(context);

        this.parentLayout = parentLayout;
        this.context = context;
        this.horizontalCount = horizontalCount;
        this.verticalCount = verticalCount;
        this.pics = new ArrayList<Pic>();
        this.picsBitmap  = BitmapFactory.decodeResource(context.getResources(),picsDrawable);
        this.picsDrawable = picsDrawable;

        this.bitmapAct1  = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_camera);
        this.bitmapAct2  = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_gallery);
        this.bitmapAct3  = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_edit);

        paint = new Paint();
        mPaint = new Paint();
        blurPaint = new Paint();

        mTriangle = new Path();
        mTriangle.setFillType(Path.FillType.EVEN_ODD);

        ta = new PointF();
        tb = new PointF();
        tc = new PointF();
//        blurPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.SOLID));

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1000, 100000);

        rectActs = new RectF();
        rectAct1 = new RectF();
        rectAct2 = new RectF();
        rectAct3 = new RectF();
    }

    public void changeTiles(int horizontalCount, int verticalCount)
    {
        this.picsBitmap  = MyGraphicFunctions.drawableToBitmap(getResources().getDrawable(this.picsDrawable));
        float rt = (float)this.verticalCount / (float)this.horizontalCount;

        this.picsBitmap = Bitmap.createScaledBitmap(this.picsBitmap, (int)((float)this.parentLayout.getWidth()/rt), (int)(this.parentLayout.getWidth()*rt), false);
        ///////////////////////////////////////////////////////


        this.horizontalCount = horizontalCount;
        this.verticalCount = verticalCount;

        initGeom(true);

        this.pics.clear();

        this.initBlankPicsBitmaps();

        this.invalidate();
    }
    public void arangeTiles()
    {
        initGeom(true);

        this.pics.clear();

        this.initBlankPicsBitmaps();
        this.invalidate();
    }


    public void setPicsBitmap(int picsDrawable, boolean deletePics)
    {
        System.gc();
        this.picsBitmap  = MyGraphicFunctions.drawableToBitmap(getResources().getDrawable(picsDrawable));
        this.picsDrawable = picsDrawable;
        float rt = (float)this.verticalCount / (float)this.horizontalCount;

        this.picsBitmap = Bitmap.createScaledBitmap(this.picsBitmap, (int)((float)this.parentLayout.getWidth()/rt), (int)(this.parentLayout.getWidth()*rt), false);

        this.picWidth = this.width / MyConstants.MAX_COL_COUNT;//this.horizontalCount;
        this.picHeight = this.picWidth;

        arangeTiles();
        this.invalidate();
    }

    private void blankizePics()
    {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_blank_image);
        for(int i = 0; i < this.pics.size(); i++)
        {
            pics.get(i).bitmap = bmp;
            pics.get(i).isBlank = true;
        }
    }

    private void blankizePic(int i)
    {
        if(i >= this.pics.size()) { return; }
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_blank_image);
        pics.get(i).bitmap = bmp;
        pics.get(i).isBlank = true;
    }

    private void changePicBitmap(int index)
    {

        Point pos = getCoordsById(index);
        float w = (float)this.width / (float)this.horizontalCount;
        float W = this.picWidth;
        System.gc();
        Bitmap pb = Bitmap.createBitmap(this.picsBitmap, (int)(pos.x*W), (int)(pos.y*W), (int)w, (int)w);

        this.pics.get(index).picBitmap = pb;
    }

    private void addBlankPic()
    {
        if( ( this.pics.size() >= this.horizontalCount * this.verticalCount ) ) { return; }

        Point pos = getCoordsById(this.pics.size());
        float t = (float)this.width / (float)MyConstants.MAX_COL_COUNT;//(float)this.horizontalCount;
        float W = this.picWidth;
//        System.gc();
        Bitmap pb = Bitmap.createBitmap(this.picsBitmap, (int)((float)pos.x*W), (int)((float)pos.y*W), (int)t, (int)t);

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_blank_image);
        this.pics.add(new Pic(pos.x*W, pos.y*W, bmp, pb));
    }

    public void initGeom(boolean force) {
        if (!geomInitialized || force) {
            this.width = parentLayout.getWidth();
            this.height = this.width;
            if (this.width > 100) {
                {
                    this.picWidth  = (float)this.width / (float)MyConstants.MAX_COL_COUNT;//this.horizontalCount;
                    this.picHeight = this.picWidth;
                }

                this.paddingLeft = (int)(((float)this.width - (this.picWidth*this.horizontalCount))/2f);
                this.paddingTop = (int)(((float)this.width - (this.picWidth*this.verticalCount))/2f);

                this.paint.setTextSize(this.width/20);

                System.gc();
                this.picsBitmap = Bitmap.createScaledBitmap(this.picsBitmap, (int)this.picHeight*this.horizontalCount, (int)this.picHeight*this.verticalCount, false);
                initBlankPicsBitmaps();

                geomInitialized = true;
                this.invalidate();
            }
        }
    }
    private void initBlankPicsBitmaps()
    {
        for(int i = 0; i < this.horizontalCount * this.verticalCount; i++)
        {
            this.addBlankPic();
        }
    }

    private void initPicsBitmap()
    {
        for(int i = 0; i < this.horizontalCount * this.verticalCount; i++)
        {
            this.changePicBitmap(i);
        }
    }

    public void setPicBitmap(int id, Bitmap bmp)
    {
        if( id >= this.pics.size() || id < 0) return;

        this.pics.get(id).bitmap = bmp;
        this.pics.get(id).isBlank = false;
    }

    private int getPicIdByCoords(float x, float y) {
        float hc = this.horizontalCount;

        float elementW = this.picWidth;//this.width / hc;

        float tx = (int) (x / elementW);
        float ty = (int) (y / elementW);

        return (int) ((ty * horizontalCount) + tx);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.translate(this.paddingLeft,this.paddingTop);
        this.canvas = canvas;
        super.onDraw(canvas);
        int picCount = this.pics.size();

        float x;
        float y;

        paint.setARGB(255,0,0,0);
        canvas.drawRect(0,0,(int)this.width, (int)this.height, paint);
        float picW = this.width/MyConstants.MAX_COL_COUNT; // this.horizontalCount;

        boolean picSelected = false;
        for (int i = 0; i < picCount; i++) {
            Point p = getCoordsById(i);
            x = p.x * picWidth;
            y = p.y * picWidth;
            paint.setStyle(Paint.Style.STROKE);
            this.pics.get(i).draw(canvas, paint);

            if( i == selectedPicId )
            {
                picSelected = true;

                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setARGB(150, 255, 30, 30);
                canvas.drawRect(x, y, x+picW, y+picW, mPaint);
            }

            {
                mPaint.setColor(context.getResources().getColor(R.color.mediumSlateBlue));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(this.width/100);
                canvas.drawRect(x, y, x+picW, y+picW, mPaint);
            }
        }

        if(picSelected)
        {
            float round = 0;//rectAct2.width()/8;
            blurPaint.setStyle(Paint.Style.FILL_AND_STROKE);

            blurPaint.setColor(context.getResources().getColor(R.color.myColor1));
            blurPaint.setAlpha(200);
            if(actionNumber == 1)
            {
                blurPaint.setColor(context.getResources().getColor(R.color.myBlack));
                canvas.drawRoundRect(rectAct1, round, round, blurPaint);
                canvas.drawBitmap(bitmapAct1,new Rect(0,0, bitmapAct1.getWidth(), bitmapAct1.getHeight()), rectAct1, blurPaint);
            }
            else
            if(actionNumber == 2)
            {
                blurPaint.setColor(context.getResources().getColor(R.color.myBlack));
                canvas.drawRoundRect(rectAct2, round, round, blurPaint);
                canvas.drawBitmap(bitmapAct2,new Rect(0,0, bitmapAct2.getWidth(), bitmapAct2.getHeight()), rectAct2, blurPaint);
            }else
            if(actionNumber == 3)
            {
                blurPaint.setColor(context.getResources().getColor(R.color.myBlack));
                canvas.drawRoundRect(rectAct3, round, round, blurPaint);
                canvas.drawBitmap(bitmapAct3,new Rect(0,0, bitmapAct3.getWidth(), bitmapAct3.getHeight()), rectAct3, blurPaint);
            }
            else
            {
                canvas.drawRect(rectAct1,blurPaint);
                canvas.drawRect(rectAct2,blurPaint);
                canvas.drawRect(rectAct3,blurPaint);

                canvas.drawBitmap(bitmapAct1,new Rect(0,0, bitmapAct1.getWidth(), bitmapAct1.getHeight()), rectAct1, blurPaint);
                canvas.drawBitmap(bitmapAct2,new Rect(0,0, bitmapAct2.getWidth(), bitmapAct2.getHeight()), rectAct2, blurPaint);
                canvas.drawBitmap(bitmapAct3,new Rect(0,0, bitmapAct3.getWidth(), bitmapAct3.getHeight()), rectAct3, blurPaint);
                //        mTriangle.reset();
                mTriangle = new Path();
                mTriangle.moveTo(ta.x, ta.y);
                mTriangle.lineTo(tb.x, tb.y);
                mTriangle.lineTo(tc.x, tc.y);
                mTriangle.lineTo(ta.x, ta.y);
                mTriangle.close();
                canvas.drawPath(mTriangle, blurPaint);
            }



        }

        paint.setColor(getResources().getColor(R.color.mediumSlateBlue));
        String xxx = this.horizontalCount + "x"+this.verticalCount;
        canvas.drawText(xxx, this.width / 2 - (paint.measureText(xxx)/2) - paddingLeft, (int)(this.height*1.1) - paddingTop, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        float tx = event.getX() - paddingLeft;
        float ty = event.getY() - paddingTop;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                touchDown(new PointF( tx, ty));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                touchMove(new PointF( tx, ty));
                break;
            }
            case MotionEvent.ACTION_UP: {
                touchUp(new PointF( tx, ty));
                break;
            }
        }
        this.invalidate();
        return true;
    }

    private void touchDown(PointF pos) {
        downPos = pos;
        boolean actSelected = selectAction(pos);
        if(!actSelected)
        {
            int lastPicId = this.selectedPicId;
            this.selectedPicId = this.getPicIdByCoords(pos.x, pos.y);

            if(lastPicId == this.selectedPicId)
            {
                this.selectedPicId = -1;
            }
        }
        else
        {
        }

        this.postInvalidate();
    }

    private void touchMove(PointF pos) {
        movePos = pos;
    }

    private void touchUp(PointF pos) {
        upPos = pos;
        if(actionNumber > 0 && actionNumber < 4)
        {
            hideActions();
            return;
        }

        if(selectAction(pos)) {
            return;
        }
        else
        {
        }

        if(selectedPicId == -1) {return;}

        Point p = getCoordsById(selectedPicId);
        float x = p.x * picWidth + (picWidth/2);
        float y = (p.y+1) * picWidth;



        float bw = this.width/5;
        float by = this.width/5;
        //////////////////////
        ta = new PointF(x, y);
        tb = new PointF(x+(bw/4), y+(bw/2));
        tc = new PointF(x-(bw/4), y+(bw/2));
        if(y > (this.height - (1.5*bw)))
        {
            float ny = y-this.picHeight;
            ta = new PointF(x, ny);
            tb = new PointF(x+(bw/4), ny-(bw/2));
            tc = new PointF(x-(bw/4), ny-(bw/2));
        }
        //////////////////////
        if( x > (this.width + paddingLeft - (1.5*bw)) )
        {
            x = (float)(this.width + paddingLeft - (1.5*bw));
        }
        if( x < (1.5*bw-paddingLeft) )
        {
            x = (float)1.5*bw-paddingLeft;
        }

        float yDiff = 0;
        if( y > (this.height - (1.5*bw)) )
        {
            yDiff = -2f*by - (this.height / this.verticalCount);

        }

        rectAct1.set(x-(float)(bw*1.5),y + (by/2)+yDiff,x-(bw/2),y + (float)(by*1.5)+yDiff);
        rectAct2.set(x-(bw/2),y + (by/2)+yDiff,x+(bw/2),y + (float)(by*1.5)+yDiff);
        rectAct3.set(x+(bw/2),y + (by/2)+yDiff,x+(float)(bw*1.5),y + (float)(by*1.5)+yDiff);

        if( rectAct1.top > rectAct1.bottom )
        {
            float c = rectAct1.top;
            rectAct1.top = rectAct1.bottom;
            rectAct1.bottom = c;
        }

        if( rectAct2.top > rectAct2.bottom )
        {
            float c = rectAct2.top;
            rectAct2.top = rectAct2.bottom;
            rectAct2.bottom = c;
        }

        if( rectAct3.top > rectAct3.bottom )
        {
            float c = rectAct3.top;
            rectAct3.top = rectAct3.bottom;
            rectAct3.bottom = c;
        }
    }

    public void hideActions()
    {
        rectAct1.set(-100,-100,-99,-99);
        rectAct2.set(-100,-100,-99,-99);
        rectAct3.set(-100,-100,-99,-99);

        ta.set(-4,-4);
        tb.set(-4,-4);
        tc.set(-4,-4);
    }

    private boolean selectAction(PointF pos)
    {
        if( rectAct1.contains(pos.x, pos.y) )
        {
            actionNumber = 1;
            return true;
        }
        if( rectAct2.contains(pos.x, pos.y) )
        {
            actionNumber = 2;
            return true;
        }
        if( rectAct3.contains(pos.x, pos.y) )
        {
            actionNumber = 3;
            return true;
        }

        actionNumber = -1;
        return false;
    }

    private int gestureId(Point p1, Point p2, int range) {
        if ((p2.x - p1.x) > range) {
            return 1;
        }
        if ((p1.x - p2.x) > range) {
            return 2;
        }

        if ((p2.y - p1.y) > range) {
            return 3;
        }
        if ((p1.y - p2.y) > range) {
            return 4;
        }

        return 0;
    }

    private Point getCoordsById(int index) {
        int column = index % this.horizontalCount;
        int row = (index - column)/ this.horizontalCount;

        return new Point(column, row);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initGeom(false);
        }
    };
}