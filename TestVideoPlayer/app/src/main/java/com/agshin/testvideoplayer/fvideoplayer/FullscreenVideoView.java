
package com.agshin.testvideoplayer.fvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;

public class FullscreenVideoView extends RelativeLayout implements SurfaceHolder.Callback, OnPreparedListener, OnErrorListener, OnSeekCompleteListener, OnCompletionListener {


    private final static String TAG = "FullscreenVideoView";

    protected Context context;
    protected Activity activity;

    protected MediaPlayer mediaPlayer;
    protected SurfaceHolder surfaceHolder;
    protected SurfaceView surfaceView;
    protected boolean videoIsReady, surfaceIsReady;
    protected boolean detachedByFullscreen;
    protected State currentState;
    protected State lastState;

    protected boolean loop = false;

    protected View loadingView;

    protected ViewGroup parentView;
    protected ViewGroup.LayoutParams currentLayoutParams;

    protected boolean isFullscreen;
    protected boolean shouldAutoplay;
    protected int initialConfigOrientation;
    protected int initialMovieWidth, initialMovieHeight;

    protected OnErrorListener errorListener;
    protected OnPreparedListener preparedListener;
    protected OnSeekCompleteListener seekCompleteListener;
    protected OnCompletionListener completionListener;


    public enum State {
        IDLE,
        INITIALIZED,
        PREPARED,
        PREPARING,
        STARTED,
        STOPPED,
        PAUSED,
        PLAYBACKCOMPLETED,
        ERROR,
        END
    }

    public FullscreenVideoView(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public FullscreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public FullscreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow - detachedByFullscreen: " + detachedByFullscreen);

        super.onDetachedFromWindow();

        if (!detachedByFullscreen) {
            if (mediaPlayer != null) {
                this.mediaPlayer.setOnPreparedListener(null);
                this.mediaPlayer.setOnErrorListener(null);
                this.mediaPlayer.setOnSeekCompleteListener(null);
                this.mediaPlayer.setOnCompletionListener(null);

                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            videoIsReady = false;
            surfaceIsReady = false;
            currentState = State.END;
        }

        detachedByFullscreen = false;
    }

    @Override
    synchronized public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called = " + currentState);

        mediaPlayer.setDisplay(surfaceHolder);

        if (!surfaceIsReady) {
            surfaceIsReady = true;
            if (currentState != State.PREPARED &&
                    currentState != State.PAUSED &&
                    currentState != State.STARTED &&
                    currentState != State.PLAYBACKCOMPLETED)
                tryToPrepare();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged called");
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                resize();
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed called");
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();

        surfaceIsReady = false;
    }

    @Override
    synchronized public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared called");
        videoIsReady = true;
        tryToPrepare();
    }

    /**
     * Restore the last State before seekTo()
     *
     * @param mp the MediaPlayer that issued the seek operation
     */
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.d(TAG, "onSeekComplete");

        stopLoading();
        if (lastState != null) {
            switch (lastState) {
                case STARTED: {
                    start();
                    break;
                }
                case PAUSED: {
                    pause();
                    break;
                }
                case PLAYBACKCOMPLETED: {
                    currentState = State.PLAYBACKCOMPLETED;
                    break;
                }
                case PREPARED: {
                    currentState = State.PREPARED;
                    break;
                }
            }
        }

        if (this.seekCompleteListener != null)
            this.seekCompleteListener.onSeekComplete(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion");
        if (!this.mediaPlayer.isLooping())
            this.currentState = State.PLAYBACKCOMPLETED;
        else
            start();

        if (this.completionListener != null)
            this.completionListener.onCompletion(mp);
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError called - " + what + " - " + extra);

        stopLoading();
        this.currentState = State.ERROR;

        if (this.errorListener != null)
            return this.errorListener.onError(mp, what, extra);
        return false;
    }

    /**
     * Initializes the UI
     */
    protected void init() {
        if (isInEditMode())
            return;

        this.shouldAutoplay = false;
        this.currentState = State.IDLE;
        this.isFullscreen = false;
        this.initialConfigOrientation = -1;
        this.setBackgroundColor(Color.BLACK);

        this.mediaPlayer = new MediaPlayer();

        this.surfaceView = new SurfaceView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        this.surfaceView.setLayoutParams(layoutParams);
        addView(this.surfaceView);

        this.surfaceHolder = this.surfaceView.getHolder();
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.surfaceHolder.addCallback(this);

        this.loadingView = new ProgressBar(context);
        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        this.loadingView.setLayoutParams(layoutParams);
        addView(this.loadingView);


    }
    
    protected void prepare() throws IllegalStateException {
        startLoading();

        this.videoIsReady = false;
        this.initialMovieHeight = -1;
        this.initialMovieWidth = -1;

        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnErrorListener(this);
        this.mediaPlayer.setOnSeekCompleteListener(this);
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        this.currentState = State.PREPARING;
        this.mediaPlayer.prepareAsync();
    }

    /**
     * Try to call state PREPARED
     * Only if SurfaceView is already created and MediaPlayer is prepared
     * Video is loaded and is ok to play.
     */
    protected void tryToPrepare() {
        if (this.surfaceIsReady && this.videoIsReady) {
            if (this.mediaPlayer != null) {
                this.initialMovieWidth = this.mediaPlayer.getVideoWidth();
                this.initialMovieHeight = this.mediaPlayer.getVideoHeight();
            }

            resize();
            stopLoading();
            currentState = State.PREPARED;

            if (shouldAutoplay)
                start();

            if (this.preparedListener != null)
                this.preparedListener.onPrepared(mediaPlayer);
        }
    }

    protected void startLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
    }

    protected void stopLoading() {
        this.loadingView.setVisibility(View.GONE);
    }

    /**
     * Get the current {@link State}.
     *
     * @return
     */
    synchronized public State getCurrentState() {
        return currentState;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        this.initialConfigOrientation = activity.getRequestedOrientation();
    }

    public void resize() {
        if (initialMovieHeight == -1 || initialMovieWidth == -1)
            return;

        View currentParent = (View) getParent();
        if (currentParent != null) {
            float videoProportion = (float) initialMovieWidth / (float) initialMovieHeight;

            int screenWidth = currentParent.getWidth();
            int screenHeight = currentParent.getHeight();
            float screenProportion = (float) screenWidth / (float) screenHeight;

            int newWidth, newHeight;
            if (videoProportion > screenProportion) {
                newWidth = screenWidth;
                newHeight = (int) ((float) screenWidth / videoProportion);
            } else {
                newWidth = (int) (videoProportion * (float) screenHeight);
                newHeight = screenHeight;
            }

            ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
            if (lp.width != newWidth || lp.height != newHeight) {
                lp.width = newWidth;
                lp.height = newHeight;
                surfaceView.setLayoutParams(lp);
            }

        }
    }

    public boolean isShouldAutoplay() {
        return shouldAutoplay;
    }


    public void setShouldAutoplay(boolean shouldAutoplay) {
        this.shouldAutoplay = shouldAutoplay;
    }

    public void fullscreen() throws IllegalStateException {
        if (mediaPlayer == null) throw new RuntimeException("Media Player is not initialized");

        boolean wasPlaying = mediaPlayer.isPlaying();
        if (wasPlaying)
            pause();

        if (!isFullscreen) {
            isFullscreen = true;

            if (activity != null)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            View rootView = getRootView();
            View v = rootView.findViewById(android.R.id.content);
            ViewParent viewParent = getParent();
            if (viewParent instanceof ViewGroup) {
                if (parentView == null)
                    parentView = (ViewGroup) viewParent;

                detachedByFullscreen = true;

                currentLayoutParams = this.getLayoutParams();

                parentView.removeView(this);
            } else
                Log.e(TAG, "Parent View is not a ViewGroup");

            if (v instanceof ViewGroup) {
                ((ViewGroup) v).addView(this);
            } else
                Log.e(TAG, "RootView is not a ViewGroup");
        } else {
            isFullscreen = false;

            if (activity != null)
                activity.setRequestedOrientation(initialConfigOrientation);

            ViewParent viewParent = getParent();
            if (viewParent instanceof ViewGroup) {
                boolean parentHasParent = false;
                if (parentView != null && parentView.getParent() != null) {
                    parentHasParent = true;
                    detachedByFullscreen = true;
                }

                ((ViewGroup) viewParent).removeView(this);
                if (parentHasParent) {
                    parentView.addView(this);
                    this.setLayoutParams(currentLayoutParams);
                }
            }
        }

        resize();

        if (wasPlaying && mediaPlayer != null)
            start();
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        else throw new RuntimeException("Media Player is not initialized");
    }

    public int getDuration() {
        if (mediaPlayer != null)
            return mediaPlayer.getDuration();
        else throw new RuntimeException("Media Player is not initialized");
    }


    public int getVideoHeight() {
        if (mediaPlayer != null)
            return mediaPlayer.getVideoHeight();
        else throw new RuntimeException("Media Player is not initialized");
    }


    public int getVideoWidth() {
        if (mediaPlayer != null)
            return mediaPlayer.getVideoWidth();
        else throw new RuntimeException("Media Player is not initialized");
    }

    public boolean isLooping() {
        if (mediaPlayer != null)
            return mediaPlayer.isLooping();
        else throw new RuntimeException("Media Player is not initialized");
    }

    public boolean isPlaying() throws IllegalStateException {
        if (mediaPlayer != null)
            return mediaPlayer.isPlaying();
        else throw new RuntimeException("Media Player is not initialized");
    }


    public void pause() throws IllegalStateException {
        Log.d(TAG, "pause");
        if (mediaPlayer != null) {
            currentState = State.PAUSED;
            mediaPlayer.pause();
        } else throw new RuntimeException("Media Player is not initialized");
    }

    public void reset() {
        Log.d(TAG, "reset");

        if (mediaPlayer != null) {
            currentState = State.IDLE;
            mediaPlayer.reset();
        } else throw new RuntimeException("Media Player is not initialized");
    }

    public void start() throws IllegalStateException {
        Log.d(TAG, "start");

        if (mediaPlayer != null) {
            currentState = State.STARTED;
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        } else throw new RuntimeException("Media Player is not initialized");
    }

    public void stop() throws IllegalStateException {
        Log.d(TAG, "stop");

        if (mediaPlayer != null) {
            currentState = State.STOPPED;
            mediaPlayer.stop();
        } else throw new RuntimeException("Media Player is not initialized");
    }

    public void seekTo(int msec) throws IllegalStateException {
        Log.d(TAG, "seekTo = " + msec);

        if (mediaPlayer != null) {
            if (mediaPlayer.getDuration() > -1 && msec <= mediaPlayer.getDuration()) {
                lastState = currentState;
                pause();
                mediaPlayer.seekTo(msec);

                startLoading();
            }
        } else throw new RuntimeException("Media Player is not initialized");
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        if (mediaPlayer != null)
            this.completionListener = l;
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setOnErrorListener(OnErrorListener l) {
        if (mediaPlayer != null)
            errorListener = l;
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
        if (mediaPlayer != null)
            mediaPlayer.setOnBufferingUpdateListener(l);
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setOnInfoListener(OnInfoListener l) {
        if (mediaPlayer != null)
            mediaPlayer.setOnInfoListener(l);
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener l) {
        if (mediaPlayer != null)
            this.seekCompleteListener = l;
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener l) {
        if (mediaPlayer != null)
            mediaPlayer.setOnVideoSizeChangedListener(l);
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setOnPreparedListener(OnPreparedListener l) {
        if (mediaPlayer != null)
            this.preparedListener = l;
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setLooping(boolean looping) {
        if (mediaPlayer != null)
            mediaPlayer.setLooping(looping);
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (mediaPlayer != null)
            mediaPlayer.setVolume(leftVolume, rightVolume);
        else throw new RuntimeException("Media Player is not initialized");
    }

    public void setVideoPath(String path) throws IOException, IllegalStateException, SecurityException, IllegalArgumentException, RuntimeException {
        if (mediaPlayer != null) {
            if (currentState != State.IDLE)
                throw new IllegalStateException("FullscreenVideoView Invalid State: " + currentState);

            mediaPlayer.setDataSource(path);

            currentState = State.INITIALIZED;
            prepare();
        } else throw new RuntimeException("Media Player is not initialized");
    }
    public void setVideoURI(Uri uri) throws IOException, IllegalStateException, SecurityException, IllegalArgumentException, RuntimeException {
        if (mediaPlayer != null) {
            if (currentState != State.IDLE)
                throw new IllegalStateException("FullscreenVideoView Invalid State: " + currentState);

            mediaPlayer.setDataSource(context, uri);

            currentState = State.INITIALIZED;
            prepare();
        } else throw new RuntimeException("Media Player is not initialized");
    }
}
