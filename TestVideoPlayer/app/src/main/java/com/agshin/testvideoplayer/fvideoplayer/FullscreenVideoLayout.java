package com.agshin.testvideoplayer.fvideoplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.agshin.testvideoplayer.R;

public class FullscreenVideoLayout extends FullscreenVideoView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener, View.OnTouchListener {

    private final static String TAG = "FullscreenVideoLayout";

    protected View videoControlsView;
    protected SeekBar seekBar;
    protected ImageButton imgplay;
    protected ImageButton imgfullscreen;
    protected ImageButton imgLoop;

    protected TextView textTotal, textElapsed;

    protected OnTouchListener touchListener;

    protected static final Handler TIME_THREAD = new Handler();
    protected Runnable updateTimeRunnable = new Runnable() {
        public void run() {
            updateCounter();

            TIME_THREAD.postDelayed(this, 200);
        }
    };

    public FullscreenVideoLayout(Context context) {
        super(context);
    }

    public FullscreenVideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullscreenVideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        Log.d(TAG, "init");

        super.init();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        this.videoControlsView = inflater.inflate(com.agshin.testvideoplayer.R.layout.view_videocontrols, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_BOTTOM);
//        videoControlsView.setLayoutParams(params);
//        videoControlsView.setVisibility(View.VISIBLE);
        addView(videoControlsView, params);

        this.seekBar = (SeekBar) this.videoControlsView.findViewById(com.agshin.testvideoplayer.R.id.vcv_seekbar);
        this.imgfullscreen = (ImageButton) this.videoControlsView.findViewById(com.agshin.testvideoplayer.R.id.vcv_img_fullscreen);
        this.imgLoop = (ImageButton) this.videoControlsView.findViewById(com.agshin.testvideoplayer.R.id.btn_loop);
        this.imgplay = (ImageButton) this.videoControlsView.findViewById(com.agshin.testvideoplayer.R.id.vcv_img_play);
        this.textTotal = (TextView) this.videoControlsView.findViewById(com.agshin.testvideoplayer.R.id.vcv_txt_total);
        this.textElapsed = (TextView) this.videoControlsView.findViewById(com.agshin.testvideoplayer.R.id.vcv_txt_elapsed);

        super.setOnTouchListener(this);

        this.imgplay.setOnClickListener(this);
        this.imgfullscreen.setOnClickListener(this);
        this.seekBar.setOnSeekBarChangeListener(this);
        this.imgLoop.setOnClickListener(this);

        this.videoControlsView.setVisibility(View.INVISIBLE);
    }

    protected void startCounter() {
        Log.d(TAG, "startCounter");

        TIME_THREAD.postDelayed(updateTimeRunnable, 200);
    }

    protected void stopCounter() {
        Log.d(TAG, "stopCounter");

        TIME_THREAD.removeCallbacks(updateTimeRunnable);
    }

    protected void updateCounter() {
        int elapsed = getCurrentPosition();
        // getCurrentPosition is a little bit buggy :(
        if (elapsed > 0 && elapsed < getDuration()) {
            seekBar.setProgress(elapsed);

            elapsed = Math.round(elapsed / 1000.f);
            long s = elapsed % 60;
            long m = (elapsed / 60) % 60;
            long h = (elapsed / (60 * 60)) % 24;

            if (h > 0)
                textElapsed.setText(String.format("%d:%02d:%02d", h, m, s));
            else
                textElapsed.setText(String.format("%02d:%02d", m, s));
        }
    }

    @Override
    public void setOnTouchListener(View.OnTouchListener l) {
        touchListener = l;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");

        super.onCompletion(mp);
        stopCounter();
        updateCounter();
        updateControls();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        boolean result = super.onError(mp, what, extra);
        stopCounter();
        updateControls();
        return result;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getCurrentState() == State.END) {
            stopCounter();
        }
    }

    @Override
    protected void tryToPrepare() {
        super.tryToPrepare();

        if (getCurrentState() == State.PREPARED || getCurrentState() == State.STARTED) {
            int total = getDuration();
            if (total > 0) {
                seekBar.setMax(total);
                seekBar.setProgress(0);

                total = total / 1000;
                long s = total % 60;
                long m = (total / 60) % 60;
                long h = (total / (60 * 60)) % 24;
                if (h > 0) {
                    textElapsed.setText("00:00:00");
                    textTotal.setText(String.format("%d:%02d:%02d", h, m, s));
                } else {
                    textElapsed.setText("00:00");
                    textTotal.setText(String.format("%02d:%02d", m, s));
                }
            }

            videoControlsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void start() throws IllegalStateException {
        Log.d(TAG, "start");

        if (!isPlaying()) {
            super.start();
            startCounter();
            updateControls();
        }
    }

    @Override
    public void pause() throws IllegalStateException {
        Log.d(TAG, "pause");

        if (isPlaying()) {
            stopCounter();
            super.pause();
            updateControls();
        }
    }

    @Override
    public void reset() {
        Log.d(TAG, "reset");

        super.reset();
        stopCounter();
        updateControls();
    }

    @Override
    public void stop() throws IllegalStateException {
        Log.d(TAG, "stop");

        super.stop();
        stopCounter();
        updateControls();
    }


    protected void updateControls() {
        Drawable icon;
        if (getCurrentState() == State.STARTED) {
            icon = context.getResources().getDrawable(com.agshin.testvideoplayer.R.drawable.fvl_selector_pause);
        } else {
            icon = context.getResources().getDrawable(com.agshin.testvideoplayer.R.drawable.fvl_selector_play);
        }
        imgplay.setBackgroundDrawable(icon);
    }

    public void hideControls() {
        Log.d(TAG, "hideControls");
        if (videoControlsView != null) {
            videoControlsView.setVisibility(View.INVISIBLE);
        }
    }

    public void showControls() {
        Log.d(TAG, "showControls");
        if (videoControlsView != null) {
            videoControlsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (videoControlsView != null) {
                if (videoControlsView.getVisibility() == View.VISIBLE)
                    hideControls();
                else
                    showControls();
            }
        }

        if (touchListener != null) {
            return touchListener.onTouch(FullscreenVideoLayout.this, event);
        }

        return false;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == com.agshin.testvideoplayer.R.id.vcv_img_play) {
            if (isPlaying()) {
                pause();
            } else {
                start();
            }

        } else if(v.getId() == com.agshin.testvideoplayer.R.id.btn_loop){
            loop = !loop;
            setLooping(loop);

            Drawable icon;
            if (loop) {
                icon = context.getResources().getDrawable(R.drawable.fvl_selector_loop);
            } else {
                icon = context.getResources().getDrawable(R.drawable.fvl_selector_loop_off);
            }
            imgLoop.setBackgroundDrawable(icon);


        }
        else {
            if (isPlaying()) {
                pause();
                fullscreen();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        start();
                    }
                });
            } else {
                fullscreen();
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "onProgressChanged");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        stopCounter();
        Log.d(TAG, "onStartTrackingTouch");

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        seekTo(progress);
        Log.d(TAG, "onStopTrackingTouch");

    }
}
