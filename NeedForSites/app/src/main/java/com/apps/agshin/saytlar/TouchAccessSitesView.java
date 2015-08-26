package com.apps.agshin.saytlar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.apps.agshin.saytlar.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TouchAccessSitesView extends View {

    private double siteCount;
    private int padding;

    public String siteName;

    private Timer timer;
    private TimerTask timerTask;

    private boolean isConstantsSet = false;

    private int siteId;

    private float fingerPSpeedX;
    private float fingerPSpeedY;

    MDbHelper myDbHelper;

    // my constants(
    private int th;
    private int tw;

    private double horPointCount;
    private double verPointCount;

    private double horSiteCount;
    private double verSiteCount;

    private double horRandRange;
    private double verRandRange;

    private boolean animatingRandom;

    public static LinkedList<String> availableDatabases;
    public static LinkedList<String> mDataBaseList;
    // ) my constants

    // onDraw variables (
    private Paint paint;
    private Paint animPaint;
    private RectF pressedSiteRect;
    private Bitmap backgroundBitmap;
    private PointF fingerPoint;
    private float animationCircleRadius;
    // ) onDraw variables

    public TouchAccessSitesView(Context context, int padding) throws IOException, SQLException {
        super(context);

//        Intent ci = new Intent(getContext(), CopyingDataBase.class);
//        ci.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getContext().startActivity(ci);

        try {
            initDbHelper();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.siteCount = myDbHelper.nameCount();
        this.padding = padding;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        animPaint = new Paint();
        animPaint.setAntiAlias(true);
        animPaint.setTextAlign(Paint.Align.CENTER);
        animPaint.setStyle(Paint.Style.STROKE);
        animPaint.setStrokeCap(Paint.Cap.ROUND);


        pressedSiteRect = new RectF();

        siteName = new String();
        fingerPoint = new PointF();

        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 100, 50);
        mDataBaseList = new LinkedList<String>(Arrays.asList(getResources().getString(R.string.hello_world).split(",")));

        Intent intent = new Intent("progress");
        intent.putExtra("action", "close");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private void initView()
    {
        this.siteCount = myDbHelper.nameCount();
        this.padding = padding;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        animPaint = new Paint();
        animPaint.setAntiAlias(true);
        animPaint.setTextAlign(Paint.Align.CENTER);
        animPaint.setStyle(Paint.Style.STROKE);
        animPaint.setStrokeCap(Paint.Cap.ROUND);


        pressedSiteRect = new RectF();

        siteName = new String();
        fingerPoint = new PointF();

        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 100, 50);
        mDataBaseList = new LinkedList<String>(Arrays.asList(getResources().getString(R.string.hello_world).split(",")));
    }

    private void initDbHelper() throws SQLException {

//        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", getResources().getString(R.string.copying_files));

//        new AsyncTask<Void, Void, Void>()
//        {
//            @Override
//            protected Void doInBackground(Void... v)
//            {
//
//
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void s)
//            {
//
//            }
//
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



        myDbHelper = new MDbHelper(getContext());
        availableDatabases = myDbHelper.isDbAvailable();
//        initView();


    }

    public int numberUnderFinger()
    {
        Random r = new Random();
        double x = fingerPoint.x - padding;
        double y = (fingerPoint.y - padding) * verSiteCount / (th - (2 * padding));

        double ret = (y * horSiteCount + x) + (r.nextInt() % (int)horRandRange);
        if(ret < 0) ret = 0;
        if(ret > siteCount-1) ret = siteCount - 1;

        return (int)ret;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void calculateMyConstants()
    {
        if(isConstantsSet)
        {
            return;
        }

        if(getWidth() > 20)
        {
            th = getHeight();
            tw = getWidth();

            horPointCount = tw - (2 * padding);
            verPointCount = th - (2 * padding);

            horSiteCount = Math.sqrt( siteCount / (verPointCount / horPointCount) );
            verSiteCount = siteCount / horSiteCount;

            horRandRange = (horSiteCount - horPointCount) / 2.f;
            verRandRange = (verSiteCount - verPointCount) / 2.f;

            isConstantsSet = true;

            paint.setTextSize(this.getHeight() / 25.f);

            if(!readBackground())
            {
                createBackgroundBitmap(tw, th);
                saveBackground();
            }
            Drawable d = new BitmapDrawable(getResources(), backgroundBitmap);

            Random r = new Random();

            fingerPoint.set(tw / 2 + r.nextInt() % 200, th / 2 + r.nextInt() % 200);

            animPaint.setStrokeWidth(2.f);
            animationCircleRadius = tw / 20.f;

            paint.setStrokeWidth(tw / 220.f);

            int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(d);
            } else {
                setBackground(d);
            }
//            this.setBackgroundDrawable(d);

            resetFingerPoint();
            processFingerPoint();

            this.invalidate();
        }
    }


    private int parabolicGradient(int range, int maxValue, int iterator, int secIter, int secMax)
    {
        float m0 = range/2.f;
        float m1 = maxValue;
        float k = m0*m0 / m1;


        double secGr = Math.sin(Math.PI * ((float)secIter/(float)secMax));
        return (int)((m1 - ((iterator - m0)*(iterator - m0)/k)) * secGr);
    }

    private void createBackgroundBitmap(int bw, int bh)
    {
        int[] colors = new int[bh * bw];
        Random rand = new Random();
        int a;
        int r;

        int[] baseLine = new int[bw];

        for(int i = 0; i < bh; i++)
        {
            for(int j = 0; j < bw; j++)
            {
                a = parabolicGradient((int) bw, 255, j, i, (int) bh);
                if( a < 1 )
                    a = 1;
                int ka = Math.abs(rand.nextInt() % (a));
                r = ka + Math.abs(rand.nextInt() % (255 - ka));
//                g = 30 + Math.abs(rand.nextInt() % 200);
//                b = 150 + Math.abs(rand.nextInt() % 100);

                if(i == bh/2)
                {
                    baseLine[j] = a;
                }

                if(i == bh-1)
                {
                    colors[ i * bw + j ] = (baseLine[j] << 24) | (255 << 16) | (255 << 8) | 255;
                }
                else
                {
                    colors[ i * bw + j ] = (ka << 24) | (r << 16) | (r << 8) | r;
                }
            }
        }
        backgroundBitmap = Bitmap.createBitmap(colors, bw, bh,
                Bitmap.Config.ARGB_8888);
    }

    private Point calculateTextPoint()
    {
        float[] nameWidth = new float[siteName.length()];
        paint.getTextWidths(siteName, nameWidth);

        float sum = 0;
        for(int i = 0; i < nameWidth.length; i++)
        {
            sum += nameWidth[i];
        }

        int pp = (int) ((sum  + padding) / 2.f);
        int tp = (int) ((paint.getTextSize() + padding) / 2.f);
        int tr = (int) ((paint.getTextSize()) / 6.f );
        int tt = (int) paint.getTextSize() / 8;

        int textX = (int) (fingerPoint.x < pp ? pp:fingerPoint.x > (tw - pp) ? (tw - pp):fingerPoint.x);
        int textY = (int) (fingerPoint.y < (2*tp) ? 2 * tp:fingerPoint.y > (th - tp) ? (th - (2*tp)):fingerPoint.y-tp);

        pressedSiteRect.set(textX - pp, textY - tp - tt, textX + pp, textY + tr + tt);

        return new Point(textX, textY);
    }
    @Override
    public void onDraw(Canvas canvas)
    {
        if(!isConstantsSet)
        {
            return;
        }

        if(animatingRandom) {

            int diff = (int) (animationCircleRadius / 3.f);

            animPaint.setColor(getResources().getColor(R.color.myBColorYellow));
            canvas.drawCircle(fingerPoint.x, fingerPoint.y, animationCircleRadius / 2, animPaint);
            canvas.drawCircle(fingerPoint.x, fingerPoint.y, 1, animPaint);

            canvas.drawCircle(fingerPoint.x, fingerPoint.y, animationCircleRadius, animPaint);

            canvas.drawLine(fingerPoint.x - animationCircleRadius - diff, fingerPoint.y, fingerPoint.x - diff, fingerPoint.y, animPaint);
            canvas.drawLine(fingerPoint.x, fingerPoint.y - animationCircleRadius - diff, fingerPoint.x, fingerPoint.y - diff, animPaint);

            canvas.drawLine(fingerPoint.x + animationCircleRadius + diff, fingerPoint.y, fingerPoint.x + diff, fingerPoint.y, animPaint);
            canvas.drawLine(fingerPoint.x, fingerPoint.y  + animationCircleRadius + diff, fingerPoint.x, fingerPoint.y + diff, animPaint);

            return;
        }

        Point txtPoint = calculateTextPoint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.myBlack));
        canvas.drawRoundRect(pressedSiteRect, paint.getTextSize() / 4, paint.getTextSize() / 4, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.myWhite));
        canvas.drawRoundRect(pressedSiteRect, paint.getTextSize() / 4, paint.getTextSize() / 4, paint);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(fingerPoint.x, fingerPoint.y, 2, paint);
        paint.setColor(getResources().getColor(R.color.myBColorYellow));
        canvas.drawText(siteName, txtPoint.x, txtPoint.y, paint);
    }
    private void processFingerPoint()
    {
        siteId = numberUnderFinger();
        siteName = humanizeName(myDbHelper.readName(siteId));
        Log.i("", "Site id : " + siteId + ", Name " + siteName + " Pos(" + fingerPoint.x + ", " + fingerPoint.y + ")");
    }
    private void resetFingerPoint()
    {
        if(fingerPoint.x < padding)
        {
            fingerPoint.x = padding;
        }
        if(fingerPoint.x > (tw - padding))
        {
            fingerPoint.x = (tw - padding);
        }

        if(fingerPoint.y < padding)
        {
            fingerPoint.y = padding;
        }
        if(fingerPoint.y > (th - padding))
        {
            fingerPoint.y = (th - padding);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        Point evtPoint = new Point((int)event.getX(), (int)event.getY());
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                fingerPoint.set(evtPoint.x, evtPoint.y);
                animatingRandom = false;
                resetFingerPoint();
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                processFingerPoint();
                invalidate();
                break;
            }
        }

        return true;
    }

    public static String humanizeName(String entry)
    {
        for(  int j = 0; j < availableDatabases.size(); j++)
        {
            entry = entry.replace(availableDatabases.get(j), mDataBaseList.get(j));
        }
        return entry;
    }

    public String heyvanizeName(String entry)
    {
        for(  int j = 0; j < availableDatabases.size(); j++)
        {
            entry = entry.replace(mDataBaseList.get(j), availableDatabases.get(j) );
        }
        return entry;
    }

    private void processRandomAnimation1()
    {
        if(!animatingRandom) {
            return;
        }

        fingerPSpeedX *= 0.95;
        fingerPSpeedY *= 0.95;

        float mPx = padding + fingerPSpeedX;
        float mPy = padding + fingerPSpeedY;

        if( fingerPSpeedX < 0 )
        {
            if( fingerPoint.x < mPx)
            {
                fingerPoint.x = padding;
                fingerPSpeedX *= -1;
            }
            else
            {
                fingerPoint.x += fingerPSpeedX;
            }
        } else if( fingerPSpeedX > 0 )
        {
            if( fingerPoint.x > (tw - mPx))
            {
                fingerPoint.x = tw - padding;
                fingerPSpeedX *= -1;
            }
            else
            {
                fingerPoint.x += fingerPSpeedX;
            }
        }

        if( fingerPSpeedY < 0 )
        {
            if( fingerPoint.y < mPy)
            {
                fingerPoint.y = padding;
                fingerPSpeedY *= -1;
            }
            else
            {
                fingerPoint.y += fingerPSpeedY;
            }
        } else if( fingerPSpeedY > 0 )
        {
            if( fingerPoint.y > (th - mPy))
            {
                fingerPoint.y = th - padding;
                fingerPSpeedY *= -1;
            }
            else
            {
                fingerPoint.y += fingerPSpeedY;
            }
        }

        if( Math.abs(fingerPSpeedX) < 0.1 && Math.abs(fingerPSpeedY) < 0.1 )
        {
            fingerPSpeedX = 0;
            fingerPSpeedY = 0;

            animatingRandom = false;
            resetFingerPoint();
            processFingerPoint();
        }

        this.invalidate();

        return;
    }
    public void beginRandomAnimation()
    {
        Random r = new Random();

        fingerPSpeedX = (r.nextInt() % (th / 15)) - (20*(r.nextBoolean()?-1:1)) + (20*(r.nextBoolean()?-1:1));
        fingerPSpeedY = (r.nextInt() % (th / 15)) + (20*(r.nextBoolean()?-1:1)) - (20*(r.nextBoolean()?-1:1));
        animatingRandom = true;
    }

    private void saveBackground()
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getContext().getFilesDir()+"/background");
            backgroundBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean readBackground()
    {
        File bFile = new File(getContext().getFilesDir()+"/background");
        if(!bFile.exists())
        {
            Log.i("", "File is not exist :"+getContext().getFilesDir()+"/background < < < < < }}}}}}}}}}}}");
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try
        {
            backgroundBitmap = BitmapFactory.decodeFile(getContext().getFilesDir()+"/background", options);
            Log.i("", "File read ---------------------");
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                processRandomAnimation1();
                calculateMyConstants();
            }
        }
    };
}