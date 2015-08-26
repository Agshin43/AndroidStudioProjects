package com.akaya.apps.burcler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocatoin")
public class MScale extends View {


    protected int  max_value, min_value;
    private float Radius;
    private float value, my_max;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    private Bitmap bmp_back;

    int m_width, m_height;
    int lines_count;
    float start_angle, end_angle;
    public Paint m_board_paint;
    private float[][][] lines;
    private float[][]  strl;
    private int m_scale_painting_order, m_lines_painting_order;
    private int back_alpha, back_red, back_green, back_blue;
    private int strl_alpha, strl_red, strl_green, strl_blue;
    private int text_alpha, text_red, text_green, text_blue;
    private String m_unit;
    private float m_scale_fraction;
    private String m_prevalue;
    private String m_aftervalue;

    private int is_redy_to_draw;

    public int getIs_redy_to_draw() {
        return is_redy_to_draw;
    }

    public void setIs_redy_to_draw(int is_redy_to_draw) {
        this.is_redy_to_draw = is_redy_to_draw;
    }

    public MScale(Context context) {
        super(context);
        m_board_paint = new Paint();
        m_board_paint.setShadowLayer(12, 0, 0, Color.argb(255, 30, 100, 30));

//		lines_count = 11;
        lines = new float[lines_count][3][2];
        strl = new float[2][2];
        is_redy_to_draw = 0;


    }

    public void setLineCount(int cnt)
    {
        lines_count = cnt;
        lines = new float[lines_count][3][2];
    }

    public void setUnitation( float fr, String unit, String pre, String after )
    {
        m_scale_fraction = fr;
        m_unit = unit;
        m_prevalue = pre;
        m_aftervalue = after;
    }

    public void setScalePaintingOrder(int n)
    {
        m_scale_painting_order = n;
    }
    public void setLinesPaintingOrder(int n)
    {
        m_lines_painting_order = n;
    }

    public void  setView(int min, int max, float ang1, float ang2, int val)
    {
        value = val;
        max_value = max;
        min_value = min;
        start_angle = ang1;
        end_angle = ang2;
    }

    public void setBackgroundARGB( int a, int r, int g, int b )
    {
        back_alpha = a;
        back_red = r;
        back_green = g;
        back_blue = b;
    }

    public void setTextARGB( int a, int r, int g, int b )
    {
        text_alpha = a;
        text_red = r;
        text_green = g;
        text_blue = b;
    }

    public void setStrlARGB( int a, int r, int g, int b )
    {
        strl_alpha = a;
        strl_red = r;
        strl_green = g;
        strl_blue = b;
    }

    public void setLines(  float start, float end, float cen_x, float cen_y,float length, float radius )
    {
        float cur_angle = start;
        float cur_sin, cur_cos;
        float dist1 = radius - length, dist2 = radius, dist3 = (float) (radius- ( 2*length ));
        float angle_diff = ( end - start ) / (lines_count - 1);
        for( int i = 0;i < lines_count; i++ )
        {


            cur_sin = (float) Math.sin(Math.toRadians(cur_angle));
            cur_cos = (float) Math.cos(Math.toRadians(cur_angle));

            lines[i][0][0] = xByCentreXCosAndDist(cen_x, cur_cos, dist1);
            lines[i][0][1] = yByCentreYSinAndDist(cen_y, cur_sin, dist1);
            lines[i][1][0] = xByCentreXCosAndDist(cen_x, cur_cos, dist2);
            lines[i][1][1] = yByCentreYSinAndDist(cen_y, cur_sin, dist2);

            lines[i][2][0] = xByCentreXCosAndDist(cen_x, cur_cos, dist3);
            lines[i][2][1] = yByCentreYSinAndDist(cen_y, cur_sin, dist3);


            cur_angle += angle_diff;

        }
    }

    public void setMaxValue( int val)
    {
        max_value = val;
    }

    public void updateStrl( float cen_x, float cen_y, float r, float R, float val )
    {
        float dif =  ( end_angle - start_angle ) / (max_value - min_value);
        float cur_angle = start_angle + ( dif * val );
        float cur_sin, cur_cos;
        float dist1 = -r, dist2 = R;
        cur_sin = (float) Math.sin( Math.toRadians(cur_angle) );
        cur_cos = (float) Math.cos( Math.toRadians(cur_angle) );

        strl[0][0] = xByCentreXCosAndDist(cen_x, cur_cos, dist1);
        strl[0][1] = yByCentreYSinAndDist(cen_y, cur_sin, dist1);
        strl[1][0] = xByCentreXCosAndDist(cen_x, cur_cos, dist2);
        strl[1][1] = yByCentreYSinAndDist(cen_y, cur_sin, dist2);
    }


    public float xByCentreXCosAndDist( float cx, float cos, float d )
    {
        return (cx + ( cos * d ));
    }
    public float yByCentreYSinAndDist( float cy, float sin, float d )
    {
        return (cy + ( sin * d ));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas )
    {
        super.onDraw(canvas);
        m_board_paint.setTextSize(m_width/20);
        m_board_paint.setARGB(back_alpha, back_red, back_green, back_blue);

        RectF r = new RectF(0, 0, m_width, m_height);
        canvas.drawRoundRect(r, 0, 0, m_board_paint);
        m_board_paint.setStrokeWidth(5);



        if( is_redy_to_draw == 0 )
        {
            m_width  = this.getWidth();
            m_height = this.getHeight();

        }


        if( m_width > 0 && (is_redy_to_draw == 0))
            is_redy_to_draw = 1;


        for( int i = 0; i < lines_count; i++ )
        {

            if( i % m_lines_painting_order == 0 )
            {
                m_board_paint.setARGB(255, i*( 255/lines_count ), 255 - i*( 255/lines_count ), 100);
                canvas.drawLine(lines[i][0][0], lines[i][0][1],
                        lines[i][1][0], lines[i][1][1], m_board_paint);

            }
            if( i % m_scale_painting_order == 0  ){
                m_board_paint.setARGB(text_alpha, text_red, text_green, text_blue);
                canvas.drawText(String.valueOf(i),lines[i][2][0] - (m_board_paint.getTextSize() / 2), lines[i][2][1], m_board_paint);
            }
        }

        m_board_paint.setARGB(strl_alpha, strl_red, strl_green, strl_blue);
        canvas.drawCircle(m_width / 2, m_height / 2, m_height / 25,m_board_paint);


        m_board_paint.setStrokeWidth(10);
//		m_board_paint.setARGB(strl_alpha, 255 - strl_red, 255 - strl_green,255 - strl_blue);
        m_board_paint.setARGB(strl_alpha, 30, 30,30);
        canvas.drawLine(strl[0][0], strl[0][1], strl[1][0], strl[1][1], m_board_paint);

        m_board_paint.setARGB(strl_alpha, strl_red, strl_green,strl_blue);
        m_board_paint.setStrokeWidth(5);
        canvas.drawLine(strl[0][0], strl[0][1], strl[1][0], strl[1][1], m_board_paint);





        m_board_paint.setARGB(255, 255 - back_red, 255 -  back_green, 255 -  back_blue );
        m_board_paint.setTextSize(m_width/15);
//		canvas.drawText( m_prevalue + String.valueOf(my_max/m_scale_fraction) + m_aftervalue, 2 * m_board_paint.getTextSize(), (float) (m_height * 0.9), m_board_paint);
        canvas.drawText(String.valueOf(value/m_scale_fraction) + m_unit, m_width / 2 - 80, (float) (m_height * 0.9), m_board_paint);




    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        super.onTouchEvent(e);


        this.invalidate();
        return true;
    }


}

