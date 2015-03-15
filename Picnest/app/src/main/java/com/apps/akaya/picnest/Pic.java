package com.apps.akaya.picnest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Pic {
    float x;
    float y;
    float width;
    float height;
    Bitmap bitmap;
    Bitmap picBitmap;
    boolean isBlank = true;

    public Pic(float x, float y,Bitmap bmp, Bitmap picBitmap) {
        this.bitmap = bmp;
        this.picBitmap = picBitmap;
        this.width = picBitmap.getWidth();
        this.height = picBitmap.getHeight();

        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas, Paint paint, float x, float y, float w, float h)
    {
        canvas.drawBitmap(this.bitmap, new Rect(0,0,(int)width,(int)height), new Rect((int)x,(int)y,(int)(w+x),(int)(h+y)), paint);
        canvas.drawBitmap(this.picBitmap, new Rect(0,0,(int)width,(int)height), new Rect((int)x,(int)y,(int)(w+x),(int)(h+y)), paint);
    }

    public void draw(Canvas canvas, Paint paint)
    {
        if(!isBlank)
        {
            canvas.drawBitmap(this.bitmap, new Rect(0,0,(int)width,(int)height), new Rect((int)this.x,(int)this.y,(int)(this.width+this.x),(int)(this.height+this.y)), paint);
        }
        canvas.drawBitmap(this.picBitmap, new Rect(0,0,(int)this.width,(int)this.height), new Rect((int)this.x,(int)this.y,(int)(this.width+this.x),(int)(this.height+this.y)), paint);
    }

}
