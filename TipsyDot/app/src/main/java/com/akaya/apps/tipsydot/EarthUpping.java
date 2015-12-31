package com.akaya.apps.tipsydot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class EarthUpping extends View {
    static final float ANGLE_START = 0.0f;
    static final int BUTTON_COUNT = 6;
    static final int MAX_ENERGY = 500;
    private float H = 0.0f;
    private float W = 0.0f;
    private Paint bPaint;
    private Paint bPressedPaint;
    private Paint backgroundPaint;
    private Paint basePaint;
    private int buttonPressed = -1;
    private Bitmap buttonsBitmap;
    private RectF buttonsPressedRect;
    private float buttonsR;
    private RectF buttonsRect;
    private PointF center;
    private Paint centerBPaint;
    private Paint centerBPressedPaint;
    private Bitmap centerBitmap;
    private float centerButtonMargin;
    private float centerButtonPressedR;
    private float centerButtonR;
    private float centerButtonR2;
    private Bitmap centerInfoBitmap;
    public int clickedButton = -1;
    private int clickedSliderButton = -1;
    private int days;
    private float daysAngle = 0.0f;
    private int energy;
    private float energyAngle = 0.0f;
    private Handler handler;
    private float horizontalMargin;
    private int[] rangedButtons = new int[]{0, 4};
    private float[] ranges = new float[]{0.0f, 52.0f, 0.5f, 10.0f, 120.0f, 5.0f};
    private Paint sliderPaint;
    private RectF sliderRect;
    Timer timer;
    TimerTask timerTask;
    private float[] values = new float[]{27.8f, 13.0f};
    private float verticalMargin;

    public EarthUpping(Context context) {
        super(context);
        this.handler = new Handler() {
             /* * Enabled aggressive block sorting */

            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        return;
                    }
                    case 0: {
                        if (EarthUpping.this.W != 0.0f) return;
                        EarthUpping.this.W = EarthUpping.this.getWidth();
                        EarthUpping.this.H = EarthUpping.this.getHeight();
                        if (EarthUpping.this.W <= 0.0f) return;
                        EarthUpping.this.initButtons();
                        EarthUpping.this.initPaints();
                        EarthUpping.this.invalidate();
                        return;
                    }
                }
            }
        };
        this.timer = new Timer();
        this.timerTask = new TimerTask() {
            

            public void run() {
                EarthUpping.this.handler.sendEmptyMessage(0);
            }
        };
        this.timer.scheduleAtFixedRate(this.timerTask, 0, 200);
    }

     /* * Enabled aggressive block sorting */

    private void actionDown(float f, float f2) {
        if (this.clickedSliderButton != -1) {
            return;
        }
        float f3 = this.distToCenter(f, f2);
        this.clickedButton = f3 < 0.85f * this.centerButtonR ? 100 : (f3 > this.buttonsR ? -1 : this.getPressedButton(this.angle(f, f2)));
        this.clickedSliderButton = this.clickedButton == 0 || this.clickedButton == 1 ? this.clickedButton : -1;
        this.buttonPressed = this.clickedButton;
    }

    

    private void actionMove(float f, float f2) {
        if (this.setAnglesAndValues(f, f2)) {
// empty if block 
        }
    }

    

    private void actionUp(float f, float f2) {
        if (this.clickedSliderButton != -1) {
            this.clickedButton = this.buttonPressed = -1;
            this.clickedSliderButton = -1;
            return;
        }
        this.buttonPressed = -1;
    }

    

    private float angle(float f, float f2) {
        float f3 = (float) Math.toDegrees((double) Math.atan2((double) (f2 - this.center.y), (double) (f - this.center.x)));
        if (f3 < 0.0f) {
            f3 += 360.0f;
        }
        return f3;
    }

    

    private float distToCenter(float f, float f2) {
        return (float) Math.sqrt((double) ((this.center.x - f) * (this.center.x - f) + (this.center.y - f2) * (this.center.y - f2)));
    }

    

    private Bitmap getBitmap(String string, float f, int n, int n2) {
        int n3 = this.getResources().getIdentifier(string, "drawable", this.getContext().getPackageName());
        return this.convertToBitmap(this.getResources().getDrawable(n3), (int) (f * (float) n), (int) (f * (float) n2));
    }

    

    private int getPressedButton(float f) {
        return (int) (f / 60.0f);
    }

     /* * Enabled aggressive block sorting */

    private void initButtons() {
        this.center = new PointF(this.W / 2.0f, this.H / 2.0f);
        if (this.H > this.W) {
            this.centerButtonR = 0.52336687f * (this.W / 2.0f);
            this.centerButtonR2 = 0.6064661f * (this.W / 2.0f);
            this.centerButtonPressedR = 0.52336687f * (this.W / 2.0f);
            this.buttonsR = 0.46f * this.W;
        } else {
            this.centerButtonR = 0.52336687f * (this.H / 2.0f);
            this.centerButtonR2 = 0.6064661f * (this.H / 2.0f);
            this.centerButtonPressedR = 0.52336687f * (this.H / 2.0f);
            this.buttonsR = 0.46f * this.H;
        }
        this.horizontalMargin = this.W / 2.0f - this.buttonsR;
        this.verticalMargin = this.H / 2.0f - this.buttonsR;
        float f = this.W / 40.0f;
        this.buttonsPressedRect = new RectF(this.center.x - (f + this.buttonsR), this.center.y - (f + this.buttonsR), this.center.x + (f + this.buttonsR), this.center.y + (f + this.buttonsR));
        this.buttonsRect = new RectF(this.center.x - this.buttonsR, this.center.y - this.buttonsR, this.center.x + this.buttonsR, this.center.y + this.buttonsR);
        this.sliderRect = new RectF(this.center.x - 0.89f * this.centerButtonR2, this.center.y - 0.89f * this.centerButtonR2, this.center.x + 0.89f * this.centerButtonR2, this.center.y + 0.89f * this.centerButtonR2);
        this.centerBitmap = this.getBitmap("center_button", 1.0f, (int) (2.0f * this.buttonsR), (int) (2.0f * this.buttonsR));
        this.buttonsBitmap = this.getBitmap("around_buttons", 1.0f, (int) (2.0f * this.buttonsR), (int) (2.0f * this.buttonsR));
        this.centerInfoBitmap = this.getBitmap("center_info_button", 1.0f, (int) (2.0f * this.buttonsR), (int) (2.0f * this.buttonsR));
    }

    

    private void initPaints() {
        this.backgroundPaint = new Paint(1);
        this.backgroundPaint.setStyle(Paint.Style.FILL);
        this.backgroundPaint.setAntiAlias(true);
        this.backgroundPaint.setColor(this.getResources().getColor(2131427346));
        this.sliderPaint = new Paint(1);
        this.sliderPaint.setStyle(Paint.Style.FILL);
        this.sliderPaint.setAntiAlias(true);
        this.sliderPaint.setColor(this.getResources().getColor(2131427353));
        this.basePaint = new Paint(1);
        this.basePaint.setStyle(Paint.Style.FILL);
        this.basePaint.setAntiAlias(true);
        this.basePaint.setColor(this.getResources().getColor(2131427352));
        this.centerBPaint = new Paint(1);
        this.centerBPaint.setStyle(Paint.Style.FILL);
        this.centerBPaint.setAntiAlias(true);
        this.centerBPaint.setColor(this.getResources().getColor(2131427350));
        this.centerBPressedPaint = new Paint(1);
        this.centerBPressedPaint.setStyle(Paint.Style.FILL);
        this.centerBPressedPaint.setAntiAlias(true);
        this.centerBPressedPaint.setColor(this.getResources().getColor(2131427351));
        this.bPaint = new Paint(1);
        this.bPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.bPaint.setAntiAlias(true);
        this.bPaint.setStrokeWidth(3.0f);
        this.bPaint.setColor(this.getResources().getColor(2131427348));
        this.bPressedPaint = new Paint(1);
        this.bPressedPaint.setStyle(Paint.Style.FILL);
        this.bPressedPaint.setAntiAlias(true);
        this.bPressedPaint.setStrokeWidth(3.0f);
        this.bPressedPaint.setColor(this.getResources().getColor(2131427349));
    }

     /* * Enabled aggressive block sorting * Lifted jumps to return sites */

    private boolean setAnglesAndValues(float f, float f2) {
        if (this.distToCenter(f, f2) > this.centerButtonR2) {
            return false;
        }
        if (this.clickedSliderButton == 0) {
            this.daysAngle = this.angle(f, f2);
            this.days = (int) (365.0f * (this.daysAngle / 360.0f));
            return true;
        }
        if (this.clickedSliderButton != 1) return false;
        this.energyAngle = this.angle(f, f2);
        this.energy = (int) (500.0f * (this.energyAngle / 360.0f));
        return true;
    }

    

    public Bitmap convertToBitmap(Drawable drawable, int n, int n2) {
        Bitmap bitmap = Bitmap.createBitmap((int) n, (int) n2, (Bitmap.Config) Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, n, n2);
        drawable.draw(canvas);
        return bitmap;
    }

     /* * Enabled aggressive block sorting * Lifted jumps to return sites */

    protected void onDraw(Canvas canvas) {
        if (this.W == 0.0f) {
            return;
        }
        canvas.drawRect(0.0f, 0.0f, this.W, this.H, this.backgroundPaint);
        canvas.drawBitmap(this.buttonsBitmap, this.horizontalMargin, this.verticalMargin, this.basePaint);
        for (int i = 0;
             i < 6;
             ++i) {
            if (i != this.buttonPressed) continue;
            if (this.clickedButton == 0 || this.clickedButton == 1) {
                canvas.drawCircle(this.center.x, this.center.y, this.buttonsR, this.basePaint);
            }
            canvas.drawArc(this.buttonsRect, 60.0f * (float) i + 3.0f / 2.0f, 60.0f - 3.0f, true, this.bPressedPaint);
        }
        canvas.drawCircle(this.center.x, this.center.y, 0.89f * this.centerButtonR2, this.backgroundPaint);
        if (this.clickedButton == 0) {
            canvas.drawArc(this.sliderRect, 0.0f, 0.0f + this.daysAngle, true, this.sliderPaint);
            canvas.drawBitmap(this.centerInfoBitmap, this.horizontalMargin, this.verticalMargin, this.basePaint);
            return;
        }
        if (this.clickedButton == 1) {
            canvas.drawArc(this.sliderRect, 0.0f, 0.0f + this.energyAngle, true, this.sliderPaint);
            canvas.drawBitmap(this.centerInfoBitmap, this.horizontalMargin, this.verticalMargin, this.basePaint);
            return;
        }
        canvas.drawBitmap(this.centerBitmap, this.horizontalMargin, this.verticalMargin, this.basePaint);
        if (this.buttonPressed != 100) return;
        canvas.drawCircle(this.center.x, this.center.y, 0.76f * this.centerButtonPressedR, this.bPressedPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()){
            case MotionEvent.ACTION_DOWN:
                actionDown(evt.getX(), evt.getY());
                break;
            case MotionEvent.ACTION_MOVE:
//                actionDown(evt.getX(), evt.getY());
                break;
            case MotionEvent.ACTION_UP:
                actionUp(evt.getX(), evt.getY());
                break;
            default:break;
        }
        return super.onTouchEvent(evt);
    }
}
//        switch (var1_1.getAction()) {
//            case 0: { this.actionDown(var1_1.getX(), var1_1.getY());
//        break;
//    } case 2: {
//                this.actionMove(var1_1.getX(), var1_1.getY());
//    }
//    default: {
//        this.invalidate();
//        return true;
//    } case 1: { this.actionUp(var1_1.getX(), var1_1.getY());
//        this.invalidate();
//        return this.performClick();
//    }
