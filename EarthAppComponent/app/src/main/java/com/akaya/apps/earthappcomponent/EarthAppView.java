package com.akaya.apps.earthappcomponent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by agshin on 9/1/15.
 */
public class EarthAppView extends View {
    private Timer timer;
    private TimerTask timerTask;
    private float H = 0;
    private float W = 0;

    private PointF sliderP;
    private boolean sliding = false;
    final static int ALLTITUDE = 7;
    boolean altitudeEnabled = false;
    public String EarthAppItemName = "Find it";


    private final static int BUTTON_COUNT = 9;

    private int clickedButton = -1;
    private int pressedButton = -1;
    private boolean initialized;

    private Bitmap bmpAltitudeOn;
    private Bitmap bmpAltitudeOff;

    private int sliderId = -1;
    private ArrayList<Slider> sliders;

    final static int SEX = 3;
    final static int ITEM_NAME = 0;
    private int sexId;

    private Slider currentSlider = null;


    private float r;
    private float R;
    private float marginH;
    private float marginV;
    private float buttonPadding;
    private ArrayList<PointF> buttons;


    private Paint buttonsPaint;
    private Paint buttonsDisabledPaint;
    private Paint buttonsPressPaint;
    private Paint buttonsSliderPaint;
    private Paint buttonsSliderCirclePaint;

    private Paint paint;
    private Paint textPaint;
    private Paint transPaint;


    final static float SCREEN_RATIO = 1.5f;

    public EarthAppView(Context context) {
        super(context);

        sexId = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 30, 200);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!initialized) {
            return;
        }
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);


        for (int i = 0; i < BUTTON_COUNT; i++) {
            float rp = i == 4 ? marginH : 0;
            if(sliderId != -1 ){
                if(sliderId != i){
                    canvas.drawCircle(buttons.get(i).x, buttons.get(i).y, r + rp, buttonsDisabledPaint);
                } else {
                    canvas.drawCircle(buttons.get(sliderId).x, buttons.get(sliderId).y, r + (marginH/2) , buttonsSliderCirclePaint);
                }

            } else {
                float pp = i == pressedButton ? marginH / 2.f : 0;
                if (i == pressedButton) {
                    canvas.drawCircle(buttons.get(i).x, buttons.get(i).y, r + rp + pp, buttonsPressPaint);
                } else {
                    canvas.drawCircle(buttons.get(i).x, buttons.get(i).y, r + rp + pp, buttonsPaint);
                }

            }

//            canvas.dr


            int slid = getSlider(i);
            if(slid != -1){
                textPaint.setTextSize(r/2);
                canvas.drawText(Math.round(sliders.get(getSlider(i)).value)+"", buttons.get(i).x, buttons.get(i).y - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
            }

            if(i == SEX){
                textPaint.setTextSize(r);
                canvas.drawText("" + sexId, buttons.get(i).x, buttons.get(i).y - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
            }
            if(i == ITEM_NAME){
                textPaint.setTextSize(r/3);
                canvas.drawText(EarthAppItemName, buttons.get(i).x, buttons.get(i).y - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
            }
            if(i == ALLTITUDE){
                if(altitudeEnabled){
                    canvas.drawBitmap(bmpAltitudeOn, buttons.get(i).x - (r/3), buttons.get(i).y - (r/3),paint);
                } else {
                    canvas.drawBitmap(bmpAltitudeOff, buttons.get(i).x - (r/3), buttons.get(i).y - (r/3),paint);
                }
            }
        }

        if (sliderId != -1) {
            canvas.drawRect(marginH, sliderP.y - (buttonPadding / 2), W - marginH, sliderP.y + (buttonPadding / 2), buttonsSliderPaint);
            canvas.drawCircle(sliderP.x, sliderP.y, r / 2, buttonsSliderCirclePaint);
            return;

        }


    }

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    }

    private void initPaints() {

        buttonsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonsPaint.setStyle(Paint.Style.FILL);
        buttonsPaint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.colorButton));

        buttonsDisabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonsDisabledPaint.setStyle(Paint.Style.FILL);
        buttonsDisabledPaint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.colorButtonDisabled));

        buttonsPressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonsPressPaint.setStyle(Paint.Style.FILL);
        buttonsPressPaint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.colorButtonPressed));

        buttonsSliderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonsSliderPaint.setStyle(Paint.Style.FILL);
        buttonsSliderPaint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.colorSliderLine));

        buttonsSliderCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonsSliderCirclePaint.setStyle(Paint.Style.FILL);
        buttonsSliderCirclePaint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.colorSliderCircle));

        transPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        transPaint.setStyle(Paint.Style.FILL);
        transPaint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.mySemiTransparentBlue));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.bgColor));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(r);
        textPaint.setColor(getResources().getColor(com.akaya.apps.earthappcomponent.R.color.colorButtonText));
    }

    private void init() {
        if (W > H) {
            marginH = W / 40.f;
        } else {
            marginH = H / 40.f;
        }

        buttonPadding = marginH * 0.8f;
        float Z = W < H ? W : H;
        r = (Z - (6.f * buttonPadding) - (2.f * marginH)) / 6.f;
        marginV = (H - (3 * (2 * (r + buttonPadding)))) / 2.f;

        buttons = new ArrayList<PointF>();
        float mr = r + buttonPadding;

        for (int i = 0; i < BUTTON_COUNT; i++) {
            buttons.add(new PointF(marginH + mr + ((i % 3) * mr * 2), marginV + mr + ((i / 3) * mr * 2)));
        }

        sliders = new ArrayList<Slider>();
        Slider sl1 = new Slider(10, 140, 2, W - (2 * marginH));
        sliders.add(sl1);

        Slider sl2 = new Slider(100, 200, 5, W - (2 * marginH));
        sliders.add(sl2);

        Slider sl3 = new Slider(10, 60, 6, W - (2 * marginH));
        sliders.add(sl3);


        int id = getResources().getIdentifier("ic_alt_on", "drawable", getContext().getPackageName());
        int id1 = getResources().getIdentifier("ic_alt_off", "drawable", getContext().getPackageName());
        bmpAltitudeOn = convertToBitmap(getResources().getDrawable(id), (int) (r / 1.5), (int) (r/1.5));
        bmpAltitudeOff = convertToBitmap(getResources().getDrawable(id1), (int) (r / 1.5), (int) (r/1.5));
    }
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    private int getSlider(int id) {
        for (int i = 0; i < sliders.size(); ++i) {
            if (id == sliders.get(i).id) {
                return i;
            }
        }
        return -1;
    }


    private void itemNameDialog(){
        final EditText textEdit = new EditText(getContext());
        AlertDialog dialog = null;

// Builder
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Enter item name");
        textEdit.setText(EarthAppItemName);
        textEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textEdit.setSelection(0, EarthAppItemName.length());
        alert.setView(textEdit);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EarthAppItemName = textEdit.getText().toString();
                postInvalidate();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(textEdit, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    EarthAppItemName = textEdit.getText().toString();
                    postInvalidate();
                    dialog.dismiss();
                }
                return false;
            }
        });


        dialog.show();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                actionDown(event.getX(), event.getY());
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                actionMove(event.getX(), event.getY());
                invalidate();
                return false;

            }

            case MotionEvent.ACTION_UP: {
                actionUp(event.getX(), event.getY());
                break;
            }

            default: {
                break;
            }

        }

        invalidate();
        return true;
    }

    private void actionDown(float x, float y) {

        if (sliderId != -1 && (Math.abs(sliderP.y - y) < (buttonPadding * 2))) {

            sliding = true;
            float mx = x;
            mx = x < marginH ? marginH : x > (W - marginH) ? W - marginH : x;
            sliderP.set(mx, sliderP.y);

//            sliders.get(curSliderId()).value = sliders.get(curSliderId()).maxValue * ((mx - marginH) / ());
            return;

        }

        for (int i = 0; i < BUTTON_COUNT; i++) {
            if (dist(x, y, buttons.get(i).x, buttons.get(i).y) <= r) {
                pressedButton = i;
                break;
            }
        }

    }

    private void actionMove(float x, float y) {
        if (sliderId != -1 && sliding) {


            float mx = x;
            mx = x < marginH ? marginH : x > (W - marginH) ? W - marginH : x;
            sliderP.set(mx, sliderP.y);

            currentSlider.value = currentSlider.maxValue * ((mx - marginH)/currentSlider.width );
//            sliders.get(curSliderId()).value = sliders.get(curSliderId()).maxValue * ((mx - marginH) / ());
            return;

        }
    }

    private void actionUp(float x, float y) {

        if(sliderId != -1){
            sliderId = -1;
            pressedButton = -1;
            return;
        }

        int rb = -1;
        boolean startSliding = false;
        for (int i = 0; i < BUTTON_COUNT; i++) {
            if (dist(x, y, buttons.get(i).x, buttons.get(i).y) <= r) {
                rb = i;
                break;
            }
        }

        if (rb > -1) {
            if (rb == pressedButton) {
                clickedButton = pressedButton;

                if (sliderId == -1) {
                    for (int i = 0; i < sliders.size(); ++i) {
                        if (sliders.get(i).id == rb) {
                            sliderId = rb;
                            startSliding = true;
                            currentSlider = sliders.get(i);
                            calculateSliderPoint(sliders.get(i));
                            break;
                        }
                    }
                } else {
                    sliderId = -1;
                }

                if(rb == SEX){
                    setSex();
                }

                if(rb == ALLTITUDE){
                    altitudeEnabled = !altitudeEnabled;
                }

                if(rb == ITEM_NAME){
                    itemNameDialog();
                }

                performClick();
                clickedButton = -1;
            }
        } else {
            pressedButton = -1;
            clickedButton = -1;
        }
        if (!startSliding) {
            sliderId = -1;
            sliding = false;
        }
        pressedButton = -1;
    }

    private void calculateSliderPoint(Slider slider) {
        sliderP = new PointF(currentSlider.width * (currentSlider.value / currentSlider.maxValue) + marginH, H - r );
    }

    private void setSex(){
        if(sexId == 2){
            sexId = 0;
        } else
        {
            sexId++;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (!initialized) {
                    if (H == 0) {
                        H = getHeight();
                        W = getWidth();
                    }

                    if (H > 100) {

                        initialized = true;
                        init();
                        initPaints();
                        invalidate();
                    }
                }

            }
        }
    };

    public class Slider {
        public int id;
        public float value;
        public float maxValue;
        public float width;

        public Slider(float value, float maxValue, int id, float width) {
            this.value = value;
            this.maxValue = maxValue;
            this.id = id;
            this.width = width;
        }
    }
}

