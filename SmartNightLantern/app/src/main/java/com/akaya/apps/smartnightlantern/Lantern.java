package com.akaya.apps.smartnightlantern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class Lantern extends View {
    private Timer timer;
    private TimerTask timerTask;

    private Timer soundTimer;
    private TimerTask soundTimerTask;

    private float H = -1;
    private float W = -1;
    private float W2;
    private float H2;
    private float batteryPercentage;
    private boolean batteryPlugged;

    private boolean bgIsBright = false;

//    SoundMeter soundMeter;

    private Paint textPaint;
    private Paint batteryPaint;
    private Paint paint;
    private Paint brightPaint;
    private Paint secondaryTextPaint;
    private Paint blackPaint;

    final static float MIN_BRIGHTNESS = 0.05F;

    private PointF prevMovePos;
    private PointF prevPressPos;

    private float radiusDiff;
    private  String[] dateTimeString;
    private float textPaintHeight2;
    private float secondaryTextPaintHeight2;

    boolean screenIsWake = true;

    int sleepTimeCounter;

    int     sleepTime;
    int     brightness;
    int     soundLevel;
    boolean sleep;
    boolean colorClock;
    boolean awakeViaMotion;
    boolean awakeViaSound;
    boolean battery;
    boolean digitalClock;
    boolean missedCalls;
    boolean sms;
    int     screenColor;
    Bitmap  smsBitmap;
    Bitmap  callBitmap;

    Bitmap  smsBitmapBlack;
    Bitmap  callBitmapBlack;

    Activity activity;

    Bitmap settingsBitmap;

    int smsCount;
    int callCount;

    private boolean settingsPressed = false;

    public boolean geomIsOk = false;

    final static String M_DATE_FORMAT_AP = "dd/MM/yyyy hh:mm aa";
    final static String M_DATE_FORMAT_24 = "dd/MM/yyyy HH:mm";
    final static int HANDLER_PERIOD = 20000;
    final static int SOUND_HANDLER_PERIOD = 1000;
    private String mDateFormat;
    private boolean is24HourFormat;


    public Lantern(Context context, Activity activity) {
        super(context);

        this.activity = activity;

        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        is24HourFormat= dateFormat.is24HourFormat(getContext());
        if(!is24HourFormat){
            mDateFormat = M_DATE_FORMAT_AP;
        } else {
            mDateFormat = M_DATE_FORMAT_24;
        }

        prevMovePos = new PointF();
        prevPressPos = new PointF();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 500, HANDLER_PERIOD);

        soundTimer = new Timer();
        soundTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        soundTimer.scheduleAtFixedRate(soundTimerTask, 500, SOUND_HANDLER_PERIOD);

//        soundMeter = new SoundMeter();

    }


    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0)
            {
                if(H < 10 || W < 10) {
                    H = getHeight();
                    W = getWidth();
                    H2 = H / 2;
                    W2 = W / 2;
                }

                if(!geomIsOk){
                    if(H > 0 && W > 0){
                        geomIsOk = true;
                        initPaints();
                        initIcons();
                        update();
                    }
                }

                dateTimeString = getDate(System.currentTimeMillis(), mDateFormat).split(" ");
                if(geomIsOk){
                    refreshNotifications();
                    if(colorClock){
                        setCurTimeColor(dateTimeString[1], paint);
                    }
                }

//                boolean wakeBefore = screenIsWake;
                if(sleep && screenIsWake){
                    sleepTimeCounter++;
                    if((sleepTimeCounter/3) >= sleepTime){
                        if(awakeViaSound){
//                            soundMeter.start();
                        } else {
                            Log.i("SOUND", "Awake via sound is false");
                        }

                        screenIsWake = false;
                        turnScreenBrightness("handler");
                    }
                }
                setTextPaints();
                invalidate();
//                if(wakeBefore != screenIsWake){
//                }

            }
//            else if(msg.what == 1){
//                if(soundMeter != null && !screenIsWake){
//                    Log.i("SOUND LEVEL", "SOUND LEVEL >=<  "+(1000+(soundLevel * 100))+", "+soundMeter.getAmplitude());
//                    if((soundMeter.getAmplitude() > 1000)){
//                        screenIsWake = true;
//                        sleepTimeCounter = 0;
//                        invalidate();
//                        soundMeter.stop();
//                    }
//                } else if(soundMeter == null){
//                    Log.i("SOUND", "Sound Meter is null");
//                }
//            }
        }
    };

    public void update(){
        if(!geomIsOk) {
            return;
        }
        int alp = paint.getAlpha();
        radiusDiff = alp / 4;

        if(!colorClock){
            paint.setColor(screenColor);
            brightPaint.setColor(screenColor);
        } else {
//            setCurTimeColor(dateTimeString[1],paint);
        }
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
        invalidate();
    }

    public void setBatteryPlugged(boolean batteryPlugged) {
        this.batteryPlugged = batteryPlugged;
        invalidate();
    }

    private void initIcons(){
        Drawable sDrawable = getDrawable(getContext(), R.drawable.ic_sms);
        Drawable cDrawable = getDrawable(getContext(), R.drawable.ic_missed_call);

        Drawable sDrawableB = getDrawable(getContext(), R.drawable.ic_sms_black);
        Drawable cDrawableB = getDrawable(getContext(), R.drawable.ic_missed_call_black);

        Drawable stDrawable =  getDrawable(getContext(), R.drawable.ic_settings);
        smsBitmap  = convertToBitmap(sDrawable, (int)(W/12.f),(int)(W/12.f));
        callBitmap = convertToBitmap(cDrawable, (int)(W/12.f),(int)(W/12.f));

        smsBitmapBlack  = convertToBitmap(sDrawableB, (int)(W/12.f),(int)(W/12.f));
        callBitmapBlack = convertToBitmap(cDrawableB, (int)(W/12.f),(int)(W/12.f));

        settingsBitmap = convertToBitmap(stDrawable, (int)(W/12.f),(int)(W/12.f));
    }

    private boolean refreshNotifications(){
        int sc = sms?MainActivity.getUnreadSmsCount(activity, this):0;
        int cc = missedCalls?MainActivity.getMissedCallCount(activity, this):0;

        if(sc == 0 && cc == 0){
            smsCount = sc;
            callCount = cc;
            return false;
        }

        if(smsCount == sc && callCount == cc){
            return false;
        }

        smsCount = sc;
        callCount = cc;

        return true;
    }

    private void initPaints() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getColor(getContext(), R.color.myWhite));
        textPaint.setTextSize(H > W ? (H / 10.f) : (W / 10.f));

        Rect r = new Rect();
        textPaint.getTextBounds("65:65", 0, 5, r);

        textPaintHeight2 = (Math.abs(r.height()))/2;
        textPaint.setTextAlign(Paint.Align.CENTER);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getColor(getContext(), R.color.colorPrimaryColor));

        secondaryTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondaryTextPaint.setStyle(Paint.Style.FILL);
        secondaryTextPaint.setAntiAlias(true);
        secondaryTextPaint.setTextSize(H > W ? (H / 26.f) : (W / 26.f));
        Rect r2 = new Rect();
        secondaryTextPaint.getTextBounds("65:65", 0, 5, r);

        secondaryTextPaintHeight2 = (Math.abs(r.height()))/2;

        secondaryTextPaint.setTextAlign(Paint.Align.CENTER);
        secondaryTextPaint.setColor(getColor(getContext(), R.color.colorSecondaryColor));

        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setStyle(Paint.Style.FILL);
        blackPaint.setAntiAlias(true);
        blackPaint.setTextAlign(Paint.Align.CENTER);
        blackPaint.setTextSize(H > W ? (H / 26.f) : (W / 26.f));
        blackPaint.setColor(getColor(getContext(), R.color.myDarkGreen));


        batteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        batteryPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        batteryPaint.setAntiAlias(true);
        batteryPaint.setColor(getColor(getContext(), R.color.colorSecondaryColor));

        brightPaint = new Paint();
        brightPaint.setStyle(Paint.Style.FILL);
        brightPaint.setARGB(125,0,0,0);
        brightPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }
    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private void openMessages(){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
    }

    private void openMCalls(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:0123456789"));
        activity.startActivity(intent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(getColor(getContext(), R.color.myBlack));

//        Toast.makeText(getContext(), "Sleep cond. "+(sleepTimeCounter/3) +"/"+ sleepTime, Toast.LENGTH_LONG).show();

        if(!screenIsWake){
            return;
        }

        if (!geomIsOk) {
            return;
        }



        canvas.drawRect(0, 0, W, H, brightPaint);

        canvas.drawCircle(W2, H2, (W / 3.5f) + radiusDiff, paint);

        if(digitalClock){
            if (!is24HourFormat) {
                canvas.drawText(dateTimeString[1] + " " + dateTimeString[2], W2, H2 + textPaintHeight2, textPaint);
            } else {
                canvas.drawText(dateTimeString[1], W2, H2 + textPaintHeight2, textPaint);
            }
        }


        float sbh = smsBitmap.getHeight();
        float sbw = smsBitmap.getWidth();

        float mpad = textPaintHeight2 * 2;

        float rr = H2 + (textPaintHeight2 * 2) + sbh + (secondaryTextPaintHeight2 * 2);
        if(missedCalls){
            if(callCount > 0){
                if(!bgIsBright) {
                    canvas.drawBitmap(callBitmap, W2 - (sbw * 1.2f), H2 + mpad, textPaint);
                } else {
                    canvas.drawBitmap(callBitmapBlack, W2 - (sbw * 1.2f), H2 + mpad, textPaint);
                }
                canvas.drawText(String.valueOf(callCount), W2 - (sbw * 0.7f), rr, secondaryTextPaint);
            }
        }

        if(sms){
            if(smsCount > 0){
                if(!bgIsBright) {
                    canvas.drawBitmap(smsBitmap, W2 + (sbw * 0.2f), H2 + mpad, textPaint);
                } else {
                    canvas.drawBitmap(smsBitmapBlack, W2 + (sbw * 0.2f), H2 + mpad, textPaint);
                }
                canvas.drawText(String.valueOf(smsCount), W2 + (sbw * 0.7f), rr, secondaryTextPaint);
            }
        }


        if(battery){
            float mr = W2 / 80.f;
            float ll = (batteryPercentage / 100f) * ((textPaintHeight2 - mr) * 2);
            canvas.drawRect(W2 - textPaintHeight2, H2 - (1.5f * textPaintHeight2)-mpad,W2 + textPaintHeight2,H2 - mpad,textPaint );
            canvas.drawRect(W2 + textPaintHeight2, H2 - textPaintHeight2 - mpad, W2 + (textPaintHeight2 * 1.2f), H2 - (textPaintHeight2 / 2) - mpad, textPaint);
            batteryPaint.setARGB(255, (int) ((1.f - (batteryPercentage / 100f)) * 255), 0, (int) (255 * (batteryPercentage / 100f)));
            canvas.drawRect(W2 - textPaintHeight2 + mr, H2 - (1.5f * textPaintHeight2) - mpad + mr, W2 - textPaintHeight2 + mr + ll, H2 - mpad - mr, batteryPaint);
            if(batteryPlugged){
                canvas.drawText("~", W2, H2 - mpad - 0.75f * textPaintHeight2 + secondaryTextPaintHeight2, blackPaint);
            }
        }


        if(settingsPressed){
            canvas.drawCircle( W - (1.5f * settingsBitmap.getWidth()), 1.5f * settingsBitmap.getHeight(),  settingsBitmap.getHeight(), blackPaint);
        } else {
            canvas.drawCircle( W - (1.5f * settingsBitmap.getWidth()), 1.5f * settingsBitmap.getHeight(),  0.8f * settingsBitmap.getHeight(), blackPaint);
        }
        canvas.drawBitmap(settingsBitmap, W - (2 * settingsBitmap.getWidth()), settingsBitmap.getHeight(), textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!geomIsOk){
            return false;
        }

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

//        if(screenIsWake)
//        {
//        }
        invalidate();
        return true;
    }

    public void writeColor(){
        if(colorClock){
            return;
        }

        SharedPreferences sp = activity.getSharedPreferences("lantern_", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt("screen color", paint.getColor());
        editor.commit();
    }

    private void actionDown(float x, float y){
        prevMovePos.set(x, y);
        prevPressPos.set(x, y);


        if(x > (W - (settingsBitmap.getWidth() * 2)) && y < (settingsBitmap.getWidth() * 2)){
            settingsPressed = true;
        }
    }

    private void actionMove(float x, float y){
        if(!screenIsWake){
            return;
        }
        int p = y >= prevMovePos.y?-1:1;
        double yDiff = p * Math.sqrt(((prevMovePos.y - y) * (prevMovePos.y - y)) + ((prevMovePos.x - x) * (prevMovePos.x - x)));
        int br = brightPaint.getAlpha() + (int)(yDiff);
        if(br > 255){
            br = 255;
        } else if(br < 0){
            br = 0;
        }

        radiusDiff = br / 4;
        brightPaint.setAlpha(br);
        paint.setAlpha(br);
        prevMovePos.set(x, y);

        int cl = paint.getColor();


//        float z = 255 - ((Color.red(cl) + Color.green(cl) + Color.blue(cl) + Color.alpha(cl)) / 4);
//        int k = (int)(Math.abs(125.5 - z) < 10? 255: z);
//        textPaint.setARGB(255, k, k, k);
//        secondaryTextPaint.setARGB(255, k, k, k);
    }


    private void actionUp(float x, float y){
        settingsPressed = false;
        float diff = (float)Math.sqrt(((prevPressPos.x - x)*(prevPressPos.x - x)) + ((prevPressPos.y - y)*(prevPressPos.y - y)));
        if(diff < 10 ){
            if(x > (W - (settingsBitmap.getWidth() * 2)) && y < (settingsBitmap.getWidth() * 2)){
                Intent intent = new Intent(activity, SettingsActivity.class);
                activity.startActivity(intent);

            } else {
                if(!screenIsWake){
                    sleepTimeCounter = 0;
                    screenIsWake = true;
                    turnScreenBrightness("action up ");
                }
                else if(sleep){
//                    if(awakeViaSound){
//                        soundMeter.start();
//                    }
                    screenIsWake = false;
                    turnScreenBrightness("action up ");
                }
            }
        } else {
            setTextPaints();
            writeColor();
        }

    }

    private void setTextPaints(){
        if(!geomIsOk){
            return;
        }
        int cl = paint.getColor();
//        setTextPaints();
        int z = (int)(255 - (((Color.red(cl) + Color.green(cl) + Color.blue(cl)) / 3f) * (Color.alpha(cl)/255.f))) > 125? 255:0;
        bgIsBright = (z == 0);
        textPaint.setARGB(255, z, z, z);
        secondaryTextPaint.setARGB(255, z, z, z);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void setCurTimeColor(String time, Paint paint){
        String[] tl = time.split(":");
        float c = (((float)Integer.valueOf(tl[0]) % 12) * 60 + (Float.valueOf(tl[1]))) / 720.f;

        float red = 0;
        float green = 0;
        float blue = 0;

        if(c >= 0 && c <= (1/6.f)){
            red = 255;
            green = 1530 * c;
        } else if( c > (1/6.f) && c <= (1/3.f) ){
            red = 255 - (1530 * (c - 1/6f));
            green = 255;
        } else if( c > (1/3.f) && c <= (1/2.f)){
            green = 255;
            blue = 1530 * (c - 1/3f);
        } else if(c > (1/2f) && c <= (2/3f)) {
            green = 255 - ((c - 0.5f) * 1530);
            blue = 255;
        } else if( c > (2/3f) && c <= (5/6f) ){
            red = (c - (2/3f)) * 1530;
            blue = 255;
        } else if(c > (5/6f) && c <= 1 ){
            red = 255;
            blue = 255 - ((c - (5/6f)) * 1530);
        }
        paint.setARGB(paint.getAlpha(), (int) red, (int) green, (int) blue);
        brightPaint.setARGB(brightPaint.getAlpha(), (int) red, (int) green, (int) blue);
    }







    public static final Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);
        return mutableBitmap;
    }

    @SuppressLint("LongLogTag")
    public void turnScreenBrightness(String sender){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = screenIsWake?(Math.max((float) (brightness) / 100.f, MIN_BRIGHTNESS)):MIN_BRIGHTNESS;
        Log.i("TURN SCREEN BRIGHTNESS : "+sender+" WAKE = "+screenIsWake, lp.screenBrightness+"");
        activity.getWindow().setAttributes(lp);
    }

}