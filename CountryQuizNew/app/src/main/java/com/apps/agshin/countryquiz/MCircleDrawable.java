package com.apps.agshin.countryquiz;


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class MCircleDrawable extends Drawable {
    private final Bitmap mBitmap;
    private final Paint mPaint;
    private final Paint mCirclePaint;
    private final RectF mRectF;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private int circleColorRes;

    public MCircleDrawable(Bitmap bitmap, int circleColorRes) {
        this.circleColorRes = circleColorRes;
        mBitmap = bitmap;
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColorRes);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mBitmapWidth/2.f, mBitmapHeight/2.f, mBitmapHeight/2.3f, mCirclePaint);
        canvas.drawOval(mRectF, mPaint);
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float d = bounds.height()/14.f;
        Rect nr = new Rect();
        mCirclePaint.setStrokeWidth(d/2.f);
        nr.set((int) (bounds.left + d), (int) (bounds.top + d), (int) (bounds.right - d), (int) (bounds.bottom - d));
        mRectF.set(nr);
    }

    @Override
    public void setAlpha(int alpha) {
        if (mPaint.getAlpha() != alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public void setAntiAlias(boolean aa) {
        mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mPaint.setDither(dither);
        invalidateSelf();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }



}
