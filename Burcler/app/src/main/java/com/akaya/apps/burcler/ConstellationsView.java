package com.akaya.apps.burcler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ConstellationsView extends View{

    private Paint paint;
    private Paint textPaint;
    private Paint percPaint;
    private Paint linePaint;

    Vibrator vibrator;

    private PointF fingerPoint;
    private PointF startPoint;

    boolean showAccordance = false;
    private boolean dimensionsIsOk = false;
    private int bgColorId;

    private Bitmap maleBitmap;
    private Bitmap femaleBitmap;

    private ArrayList<Box> boxesM = new ArrayList<Box>();
    private ArrayList<Box> boxesF = new ArrayList<Box>();

    ArrayList<Constellation> maleConstellations;
    ArrayList<Constellation> femaleConstellations;


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

        Random r = new Random();

        int vb = Math.abs(r.nextInt()%6) + 1;

//        R.color.colorBackgroundUygunluq;
        bgColorId = getResources().getIdentifier("colorBackgroundUygunluq"+vb, "color", context.getPackageName());


        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);



        preInitConstellations();
        loadConstellations("uygunluq");

        fingerPoint = new PointF(0, 0);
        startPoint = new PointF(0, 0);


        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 10, 500);
    }


    private void vibrate(long msecs){
        vibrator.vibrate(msecs);
    }
    // Overrides
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(!dimensionsIsOk) return;
        canvas.drawColor(getResources().getColor(bgColorId));

        canvas.drawBitmap(maleBitmap, margin, margin, paint);
        canvas.drawBitmap(femaleBitmap, W - margin - (2*radius) ,margin,  paint);
        int t = 0;
        float cx = (startPoint.x + fingerPoint.x) / 2;
        float cy = (startPoint.y + fingerPoint.y) / 2;
        if((compFid != -1 || compMid != -1) && (startPoint.x > 0) && (fingerPoint.x > 0))
        {
            if(startPoint.x < fingerPoint.x && (compFid != -1)) {
               t = (255 * boxesF.get(compFid).angle / 360);
            }
            if(startPoint.x > fingerPoint.x && (compMid != -1)) {
                t = (255 * boxesM.get(compMid).angle / 360);
            }
            linePaint.setARGB(255, 255 - t, t, t / 2 );
            Path pth = new Path();

            float d1;
            float d2;
            if(startPoint.x < fingerPoint.x) {
                d1 = 7*radius;
                if(startPoint.y > fingerPoint.y){
                    d2 = -4*radius;
                } else {
                    d2 = 4*radius;
                }
            } else {
                d1 = -7*radius;
                if(startPoint.y > fingerPoint.y){
                    d2 = -4*radius;
                } else {
                    d2 = 4*radius;
                }
            }
            {
                pth.moveTo(startPoint.x, startPoint.y);
                pth.cubicTo( startPoint.x+d1, startPoint.y - d2, fingerPoint.x - d1, fingerPoint.y + d2, fingerPoint.x, fingerPoint.y);
            }
            linePaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(pth, linePaint);
        }

        if(compFid != -1 && compMid != -1)
        {

            linePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, 1.5f * radius, linePaint);

            textPaint.setTextSize(radius / 1.8f);
            if( (255 + (t / 2)) > 320  ){
                textPaint.setARGB(255, 0, 0, 0);
            } else {
                textPaint.setARGB(255, 255, 255, 255);
            }
            canvas.drawText(boxesM.get(compMid).text, cx, cy - (textPaint.getTextSize()), textPaint);
            canvas.drawText(boxesF.get(compFid).text, cx, cy + (textPaint.getTextSize()), textPaint);
            textPaint.setTextSize(radius / 2.8f);
            textPaint.setColor(getResources().getColor(R.color.colorButtonText));
        }

        textPaint.setTextSize(radius / 2f);
        for(int i = 0; i < 12; i++)
        {
            if(compMid == i)
            {
                paint.setColor(getResources().getColor(R.color.colorButtonDragging));
            } else {
                paint.setColor(getResources().getColor(R.color.colorButton));
            }

            boxesM.get(i).draw(paint, textPaint, percPaint, canvas);

            if(compFid == i)
            {
                paint.setColor(getResources().getColor(R.color.colorButtonDragging));
            } else {
                paint.setColor(getResources().getColor(R.color.colorButton));
            }
            boxesF.get(i).draw(paint, textPaint, percPaint, canvas);
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

            default: { break;}

        }

        invalidate();
        return true;
    }
    // Overrides.

    private int[] findConOnPoint(float x, float y)
    {
        int[] ret = new int[2];


        float minDist = 5000;
        int minId = -1;
        boolean male = false;
        for(int i = 0; i < 12; i++){
            float dm = dist(boxesM.get(i).center.x, y, x, boxesM.get(i).center.y);
            if( dm <= radius && dm < minDist )
            {
                male = true;
                minId = i;
                minDist = dm;
            }

            dm = dist(boxesF.get(i).center.x, y, x, boxesM.get(i).center.y);
            if( dm <= radius && dm < minDist )
            {
                male = false;
                minId = i;
                minDist = dm;
            }
        }

        ret[0] = minId;
        ret[1] = male?0:1;



        return ret;
    }

    private void actionDown(float x, float y){
        int prevMid = compMid;
        int prevFid = compFid;

        int[] fcp = findConOnPoint(x, y);

        int minId = fcp[0];
        boolean male = fcp[1] == 0 ? true:false;

        if(minId != -1)
        {
            if(male)
            {
                compMid = minId;
                vibrate(30);
                compFid = -1;

                if(compMid != prevMid)
                {
                    if(prevMid != -1)
                    {
                        boxesM.get(prevMid).dragging = false;
                        boxesM.get(prevMid).resetCenter();
                    }
                }

                boxesM.get(compMid).dragging = true;
                startPoint = boxesM.get(compMid).center;
                if(compFid != -1)
                {
                    boxesF.get(compFid).dragging = false;
                }
                setAngles(0);
            }
            else
            {
                compFid = minId;
                vibrate(30);
                compMid = -1;

                if( compFid != prevFid)
                {
                    if(prevFid != -1)
                    {
                        boxesF.get(prevFid).dragging = false;
                        boxesF.get(prevFid).resetCenter();
                    }

                }

                boxesF.get(compFid).dragging = true;
                startPoint = boxesF.get(compFid).center;
                if(compMid != -1)
                {
                    boxesM.get(compMid).dragging = false;
                }
                setAngles(1);
            }
        }
    }

    private void actionMove(float x, float y)
    {
        int pm = compMid;
        int pf = compFid;
        fingerPoint.set(x, y);

        int[] fcp = findConOnPoint(x, y);

        int minId = fcp[0];
        boolean male = fcp[1] == 0 ? true:false;

        if(minId == -1){
            if( startPoint.x < (W/2) ) {
                compFid = -1;
            } else {
                compMid = -1;
            }
        } else {
            for(int i = 0; i < 12; i++){

                if(i == minId)
                {
                    if(!male && (compMid != -1)){
                        if( startPoint.x < (W/2) ) {
                            compFid = i;

                            if( i != pf){
                                vibrate(30);
                            }
                        }

                        fingerPoint.set(boxesF.get(i).center.x, boxesF.get(i).center.y);
                    }

                    if(male && (compFid != -1)){
                        if( startPoint.x > (W/2) ) {
                            compMid = i;

                            if( i != pm){
                                vibrate(30);
                            }
                        }

                        fingerPoint.set(boxesM.get(i).center.x, boxesM.get(i).center.y);
                    }
                }
            }
        }


    }


    private void actionUp(float x, float y)
    {
        showAccordance();

        compFid = -1;
        compMid = -1;

        for(int i = 0; i < 12; i++) {
            boxesF.get(i).angle = 0;
            boxesM.get(i).angle = 0;
        }
    }


    private float dist(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }

    private void initPaints() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getResources().getColor(R.color.colorButtonText));
        textPaint.setTextSize(radius / 2f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorButton));
        paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));


        percPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        percPaint.setStyle(Paint.Style.FILL);
        percPaint.setAntiAlias(true);
        percPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(radius / 10.f);
        linePaint.setAntiAlias(true);
        linePaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

//        mShadowPaint = new Paint(0);
//        mShadowPaint.setColor(0xff101010);
//        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }
    private String getConName(int i){
        int id = getResources().getIdentifier("str_c"+i, "string", context.getPackageName());
        return getResources().getString(id);
    }

    private Bitmap getConBitmap(int i, float factor){
        int id = getResources().getIdentifier("c"+i, "drawable", context.getPackageName());
        return convertToBitmap(getResources().getDrawable(id), (int) (radius * factor), (int) (radius * factor));
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

        maleBitmap = getConBitmap(13, 2);
        femaleBitmap = getConBitmap(14, 2);

        float dd;

        for (int i = 0; i < 12; i++)
        {
            if(i%2 == 0 )
            {
                dd = radius*0.6f;
            } else {
                dd = -radius*0.6f;
            }
            Box bxm = new Box(i, margin + (4*radius) + dd, margin + (radius * (i * 2 + 1) ), radius, getConName(i), getConBitmap(i, 0.95f));
            boxesM.add(bxm);

            Box bxf = new Box(i, W - margin - (4*radius) - dd, margin + (radius * (i * 2 + 1) ), radius, getConName(i), getConBitmap(i, 0.95f));
            boxesF.add(bxf);
        }
    }

    private void setAngles(int mf)
    {
        if(mf == 0) {
            for( int i = 0; i < boxesF.size(); i++)
            {
                boxesF.get(i).angle = 360 * maleConstellations.get(compMid).accordanceArray.get(i).accordance / 10;
                boxesM.get(i).angle = 0;
            }
        } else {
            for( int i = 0; i < boxesM.size(); i++)
            {
                boxesM.get(i).angle = 360 * femaleConstellations.get(compFid).accordanceArray.get(i).accordance / 10;
                boxesF.get(i).angle = 0;
            }
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
                    if(W <= 100)
                    {

                        W = getWidth();
                        H = getHeight();

                        margin = W / 20.f;
                        radius = ((H-(2*margin))/24.f);
                        Log.i("", "INIT BOXES WITH W = "+W+", H = "+H);

                    } else {

                        if(!dimensionsIsOk){
                            initBoxes();
                            initPaints();
                            invalidate();
                            dimensionsIsOk = true;
                        }
                    }
                    break;
                }
            }
        }
    };

    ////////////////////////////////////////////////////
    private void loadConstellations(String fileName)
    {
        try {
            int rID = getResources().getIdentifier(context.getPackageName()+":raw/" + fileName, null, null);
            InputStream inputStream = getResources().openRawResource(rID);

            if (inputStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String l;

                int mc = 0;
                while (( l = bufferedReader.readLine()) != null) {

                    String[] ll = l.split("#");
                    String[] lm = ll[0].split("-");
                    String name1 = lm[0];
                    String name2 = lm[1];

                    maleConstellations.get(mc / 12).accordanceArray.add(new Accordance(ll[2], Integer.valueOf(ll[1])));
                    femaleConstellations.get(mc % 12).accordanceArray.get(mc / 12).SetValues(ll[2], Integer.valueOf(ll[1]));
                    mc++;
                }

            }

            inputStream.close(); //close the file
        } catch (java.io.FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Constellation genConstellation(String line)
    {
        Constellation constellation = new Constellation();
        return  constellation;
    }

    private void preInitConstellations()
    {
        maleConstellations = new ArrayList<Constellation>();
        femaleConstellations = new ArrayList<Constellation>();
        for(int i = 0; i < 12; i++)
        {
            maleConstellations.add(new Constellation());
            femaleConstellations.add(new Constellation());
        }

        for(int i = 0; i < 12; i++)
        {
            for(int j = 0; j < 12; j++)
            {
                femaleConstellations.get(i).accordanceArray.add(new Accordance("", 0));
            }
        }

        Log.i("", "0 Fem con acc size "+ femaleConstellations.get(0).accordanceArray.size());
    }

    private void showAccordance(){
        if(compFid == -1 || compMid == -1){
            return;
        }

        String stars = "★"+maleConstellations.get(compMid).accordanceArray.get(compFid).accordance;
        final String text = " (Kişi)"+boxesM.get(compMid).text + " -  (Qadın)" +boxesF.get(compFid).text + "  \n  "+maleConstellations.get(compMid).accordanceArray.get(compFid).text;

//        for(int i = 0;i < 10; i++  ) {
//            if(i < maleConstellations.get(compMid).accordanceArray.get(compFid).accordance){
//                stars += "★";
//            } else {
////                stars += "☆";
//            }
//        }


        new AlertDialog.Builder(context)
                .setTitle(stars)
                .setMessage(text)
                .setPositiveButton(R.string.str_close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNeutralButton(R.string.str_share, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        String text = "( "+boxesM.get(compMid).text + " - " +boxesF.get(compFid).text + " ) \n  "+maleConstellations.get(compMid).accordanceArray.get(compFid).text;
                        String ad = "\n Google Play: https://play.google.com/store/apps/details?id=com.akaya.apps.burcler";
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, text+ad);
                        sendIntent.setType("text/plain");
                        context.startActivity(sendIntent);
                    }
                })
                .setIcon(R.drawable.ic_accordance)
                .show();
    }


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
    int angle;

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

    public void draw(Paint pnt, Paint txtPnt, Paint percPaint, Canvas cn)
    {
//        if(dragging)
//        {
//            cn.drawCircle(center.x, center.y, radius, txtPnt);
//        }

        if(this.angle > 0)
        {
            int t = (255 * this.angle / 360);
            percPaint.setARGB(255, 255 - t, t, t / 2);

            float r = radius * 1.1f;
            RectF rcf = new RectF(this.center.x - r, this.center.y - r, this.center.x + r, this.center.y + r);
            cn.drawArc(rcf, 90, (float)angle, true, percPaint);
        }
        cn.drawCircle(center.x, center.y, radius, pnt);
        cn.drawText(text, center.x, center.y + (radius / 2.5f), txtPnt);
        cn.drawBitmap(imageBitmap, center.x - (radius/2), center.y - (radius*0.9f), pnt);
    }
}