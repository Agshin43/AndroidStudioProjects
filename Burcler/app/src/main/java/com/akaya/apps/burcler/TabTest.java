package com.akaya.apps.burcler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TabTest extends Fragment {


    boolean isJokeDay = false;
    private MScale m_scale1, m_scale2;
    Timer mtimer;
    TimerTask mtask;
    EditText et_nm1;
    EditText et_nm2;

    int ratioOrder;

    float mresult1, mp1, mp2, mresult2;
    boolean start = false;
    int hrt1, hrt2, ascending1, ascending2, daily_love = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mresult1 = 43;
        mresult2 = 68;

        ratioOrder = 1;


        hrt1 = 110;
        hrt2 = 0;

        ascending1 = 0;
        ascending2 = 0;

        mp1 = 0;
        mp2 = 0;

        mtimer = new Timer();
        mtask = new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(0);

            }
        };
        mtimer.scheduleAtFixedRate(mtask, 0, 50);

        m_scale1 = new MScale(getActivity());
        m_scale2 = new MScale(getActivity());





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_layout, container, false);

        final Button btn_clear = (Button) v.findViewById(R.id.btn_clear);


        LinearLayout lay_res1 = (LinearLayout) v.findViewById(R.id.lay_res1);
        LinearLayout lay_res2 = (LinearLayout) v.findViewById(R.id.lay_res2);

        et_nm1 = (EditText) v.findViewById(R.id.et_name1);
        et_nm2 = (EditText) v.findViewById(R.id.et_name2);


        
        btn_clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                et_nm1.setText("");
                et_nm2.setText("");

            }
        });

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        inputMethodManager.showSoftInput(et_nm1,
                InputMethodManager.SHOW_IMPLICIT);
        inputMethodManager.showSoftInput(et_nm2,
                InputMethodManager.SHOW_IMPLICIT);

        final Button btn_go = (Button) v.findViewById(R.id.btn_go);
        et_nm1.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    // btn_go.performClick();
                }
                return false;
            }
        });
        et_nm2.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btn_go.performClick();
                }
                return false;
            }
        });

        final TextView tv_common = (TextView) v.findViewById(R.id.tv_main_res);
        
        btn_go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mp1 = 0;
                mp2 = 0;

                saveSettings(et_nm1.getText().toString(), et_nm2.getText().toString());

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_nm1.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(et_nm2.getWindowToken(), 0);


                float rat = LoveRatio();
                float x = scaleLove(et_nm1.getText().toString(), et_nm2
                        .getText().toString());

                if (ratioOrder == 1) {
                    if (rat > 1)
                        mresult1 = x / rat;
                    else
                        mresult1 = x * rat;
                    mresult2 = x - mresult1;
                } else if (ratioOrder == 2) {
                    if (rat > 1)
                        mresult2 = x / rat;
                    else
                        mresult2 = x * rat;
                    mresult1 = x - mresult2;
                } else if (ratioOrder == 0) {
//					mresult1 = x / 2;
//					mresult2 = x / 2;
                    if (uniqueChars(et_nm1.getText().toString()).length() > uniqueChars(et_nm2.getText().toString()).length()) {
                        if (rat > 1) {
                            mresult2 = x / rat;
                        } else {
                            mresult2 = x * rat;
                        }
                        mresult1 = x - mresult2;
                    }
                    if (uniqueChars(et_nm1.getText().toString()).length() < uniqueChars(et_nm2.getText().toString()).length()) {
                        if (rat > 1) {
                            mresult1 = x / rat;
                        } else {
                            mresult1 = x * rat;
                        }
                        mresult2 = x - mresult1;
                    }
                    if (uniqueChars(et_nm1.getText().toString()).length() == uniqueChars(et_nm2.getText().toString()).length()) {

                        mresult2 = x / 2;
                        mresult1 = x / 2;
                    }
                }


                String[] jokes = {"O səni sevmir",
                        "Bu günə \r\n 1200% \r\n Bu 1 aprel zarafatıdır",
                        "Bu günə \r\n -90%  , 1 aprel",
                        "Bu günə \r\n " + String.valueOf(x) + "%. Siz 1000 AZN qazandınız"};
                Random r = new Random();

                if (isJokeDay) {
                    tv_common.setText(jokes[Math.abs(r.nextInt() % 3)]);
                } else {
                    tv_common.setText("Bu günə \r\n " + String.valueOf(x)
                            + "%");
                }

                start = true;

            }
        });

        m_scale1.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        m_scale1.setLineCount(101);
        m_scale1.setView(0, 100, 160f, 380f, 0);
        m_scale1.setScalePaintingOrder(10);
        m_scale1.setLinesPaintingOrder(10);
        m_scale1.setBackgroundARGB(0, 0, 0, 33);
        m_scale1.setStrlARGB(255, 20, 20, 255);
        m_scale1.setUnitation(1, " % ", "", "");
        m_scale1.setTextARGB(255, 100, 100, 30);

        m_scale2.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        m_scale2.setLineCount(101);
        m_scale2.setView(0, 100, 160f, 380f, 0);
        m_scale2.setScalePaintingOrder(10);
        m_scale2.setLinesPaintingOrder(10);
        m_scale2.setBackgroundARGB(0, 0, 0, 33);
        m_scale2.setStrlARGB(255, 20, 20, 255);
        m_scale2.setUnitation(1, " % ", "", "");
        m_scale2.setTextARGB(255, 100, 100, 30);

        m_scale1.m_board_paint.setTextSize(m_scale1.m_height / 11);
        m_scale1.setLines(m_scale1.start_angle, m_scale1.end_angle,
                m_scale1.m_width / 2, m_scale1.m_height / 2, 30,
                m_scale1.m_height / 2);

        m_scale2.m_board_paint.setTextSize(m_scale2.m_height / 11);
        m_scale2.setLines(m_scale2.start_angle, m_scale2.end_angle,
                m_scale2.m_width / 2, m_scale2.m_height / 2, 30,
                m_scale2.m_height / 2);
        m_scale2.updateStrl(m_scale2.m_width / 2, m_scale2.m_height / 2,
                m_scale2.m_height / 6, m_scale2.m_height / 2, 30 / 10);

        if(m_scale1.getParent()!=null) {
            ((ViewGroup) m_scale1.getParent()).removeView(m_scale1);
        } else {
            loadSettings();
        }

        if(m_scale2.getParent()!=null)
            ((ViewGroup)m_scale2.getParent()).removeView(m_scale2);

        lay_res1.addView(m_scale1);
        lay_res2.addView(m_scale2);

        m_scale1.invalidate();
        m_scale2.invalidate();
        
        return v;
        

        
    }

    public float LoveRatio() {
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(month == 3 && day == 1){
            isJokeDay = true;
        }

        String dt = String.valueOf(year) + String.valueOf(month)
                + String.valueOf(day);
        int od = 0, sheat = 0;
        for (int i = 0; i < dt.length(); i++) {
            int k = Integer.parseInt(String.valueOf(dt.charAt(i)));
            if (k % 2 == 0) {
                od += k;
            } else {
                sheat += k;
            }
        }

        // Toast.makeText(this, "Date random"+ (od+sheat), 1000).show();
        // Toast.makeText(this, "Date random 2 - "+
        // ((od+sheat)/(Math.abs(od-sheat))), 1000).show();

        daily_love = (od + sheat);

//        final EditText et_nm1 = (EditText) v.findViewById(R.id.et_name1);
//        final EditText et_nm2 = (EditText) v.findViewById(R.id.et_name2);
        if ((od + sheat) % 2 == 0) {
            if (et_nm1.getText().toString().length() > et_nm2.getText()
                    .toString().length()) {
                ratioOrder = 1;
            } else {
                ratioOrder = 2;
            }

            if (et_nm1.getText().toString().length() == et_nm2.getText()
                    .toString().length()) {
                ratioOrder = 0;
            }
        } else {
            if (et_nm1.getText().toString().length() > et_nm2.getText()
                    .toString().length()) {
                ratioOrder = 2;
            } else {
                ratioOrder = 1;
            }

            if (et_nm1.getText().toString().length() == et_nm2.getText()
                    .toString().length()) {
                ratioOrder = 0;
            }
        }

        return (od + sheat + sheat) / (Math.abs(od + od - sheat + 0.01f));

    }
    public String uniqueChars(String str) {
        int sz = str.length();

        char s1;

        String mass = "";
        for (int i = 0; i < sz; i++) {
            s1 = str.charAt(i);
            if (!mass.contains(String.valueOf(s1))) {
                mass += String.valueOf(s1);
            }

        }
        return mass;
    }

    public int scaleLove(String nm1, String nm2) {
        // Toast.makeText(this, "Unique : "+ uniqueChars(nm1), 1000).show();
        nm1 = nm1.toLowerCase();
        nm2 = nm2.toLowerCase();

        int ret = 0;
        if (nm1.length() < 2 || nm2.length() < 2)
            return 0;

        float kv = 100 / (nm1.length() + nm2.length());
        if (nm1.charAt(0) == nm2.charAt(0))
            ret += 17;
        if (nm1.length() == nm2.length()) {
            ret += 6;
        }
        if (nm1.charAt(nm1.length() - 1) == nm2.charAt(nm2.length() - 1)) {
            ret += 7;
        }
        if (nm1.charAt(0) == nm2.charAt(0)) {
            ret += 3;
        }
        if (nm1.charAt(0) == nm2.charAt(nm2.length() - 1)) {
            ret += 2;
        }
        if (nm1.charAt(nm1.length() - 1) == nm2.charAt(0)) {
            ret += 2;
        }

        float sl = 30.f/(Math.abs(nm1.length() - nm2.length())+1) ;

        float hc = 0;

        if( nm1.length() > 4 && nm2.length() > 4 ){
            if(nm1.charAt(nm1.length() - 3) == nm2.charAt(nm2.length() - 3) ){
                if(nm1.charAt(nm1.length() - 2) == nm2.charAt(nm2.length() - 2) ){
                    if(nm1.charAt(nm1.length() - 1) == nm2.charAt(nm2.length() - 1) ){
                        hc += 5;
                    }
                }
            }
        }

        nm1 = uniqueChars(nm1);
        nm2 = uniqueChars(nm2);

        // david 5 ai
        // victoria 8 aii
        //
        // (100 / (5 + 8)) * ( 2 * 2 ) = 30.7 = ( 3 / 5) * 30.7 + ( 2/5 ) * 30.7

        int kt = 0;

        for (int i = 0; i < nm1.length(); i++) {
            for (int j = 0; j < nm2.length(); j++) {
                if (nm1.charAt(i) == nm2.charAt(j))
                    kt += 1;
            }
        }
        // kt = kt/2;

        ret += kv * kt;

        int rr = ret + daily_love+(int)sl + (int) hc;
        if(rr>100){
            rr = 100;
        }
        return rr;
    }

    public static float round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

    private void loadSettings() {
        SharedPreferences settings = getActivity().getSharedPreferences("names", 0);

        String nms = settings.getString("mnames", "no");
        if(nms.equals("no")) { return; }

        String[] ss = nms.split(",");
        if(ss.length < 2) {
            return;
        }
        et_nm1.setText(ss[0]);
        et_nm2.setText(ss[1]);

    }

    private void saveSettings(String nam1, String nam2) {
        SharedPreferences settings = getActivity().getSharedPreferences("names", 0);
        SharedPreferences.Editor editor = settings.edit();


        editor.putString("mnames", nam1+","+nam2);

        editor.commit();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    Random r = new Random();
                    if(isJokeDay)
                    {
                        mresult1 = -3000 + (r.nextInt() % 4000);
                        mresult2 = -3000 + (r.nextInt() % 4000);

                        m_scale1.setStrlARGB(255, hrt1, hrt1, 0);
                        m_scale2.setStrlARGB(255, hrt2, 0, hrt1);
                        m_scale1.setTextARGB(255, 80, hrt1, 80);
                        m_scale2.setTextARGB(255, 80, hrt2, 80);
                    }
                    m_scale1.setStrlARGB(255, hrt1, 0, 0);
                    m_scale2.setStrlARGB(255, hrt2, 0, 0);
                    m_scale1.setTextARGB(255, 80, hrt1, 80);
                    m_scale2.setTextARGB(255, 80, hrt2, 80);

                    if (ascending1 == 0) {
                        if (hrt1 < 245) {
                            hrt1 += 10;
                        } else {
                            ascending1 = 1;
                        }
                    } else {
                        if (hrt1 > 0) {
                            hrt1 -= 10;
                        } else {
                            ascending1 = 0;
                        }
                    }

                    // ////////////////////
                    if (ascending2 == 0) {
                        if (hrt2 < 245) {
                            hrt2 += 10;
                        } else {
                            ascending2 = 1;
                        }
                    } else {
                        if (hrt2 > 0) {
                            hrt2 -= 10;
                        } else {
                            ascending2 = 0;
                        }
                    }

                    if (m_scale1.getIs_redy_to_draw() == 1) {
                        m_scale1.m_board_paint.setTextSize(m_scale1.m_height / 11);
                        m_scale1.setLines(m_scale1.start_angle, m_scale1.end_angle,
                                m_scale1.m_width / 2, m_scale1.m_height / 2,
                                m_scale1.m_height / 15, m_scale1.m_height / 2);
                        m_scale1.setIs_redy_to_draw(2);
                    }
                    if (m_scale2.getIs_redy_to_draw() == 1) {
                        m_scale2.m_board_paint.setTextSize(m_scale2.m_height / 11);
                        m_scale2.setLines(m_scale2.start_angle, m_scale2.end_angle,
                                m_scale2.m_width / 2, m_scale2.m_height / 2,
                                m_scale2.m_height / 15, m_scale2.m_height / 2);
                        m_scale2.setIs_redy_to_draw(2);
                    }

                    if (mp1 < mresult1) {
                        mp1 += (mresult1 - mp1) / 18;
                    }
                    if (mp2 < mresult2) {
                        mp2 += (mresult2 - mp2) / 18;
                    }

                    if (start) {
                        m_scale1.updateStrl(m_scale1.m_width / 2,
                                m_scale1.m_height / 2, m_scale1.m_height / 6,
                                m_scale1.m_height / 2, mp1);
                        m_scale2.updateStrl(m_scale2.m_width / 2,
                                m_scale2.m_height / 2, m_scale2.m_height / 6,
                                m_scale2.m_height / 2, mp2);

                        m_scale1.setValue(round(mp1, 1));
                        m_scale2.setValue(round(mp2, 1));
                    } else {
                        m_scale1.updateStrl(m_scale1.m_width / 2,
                                m_scale1.m_height / 2, m_scale1.m_height / 6,
                                m_scale1.m_height / 2, 0);
                        m_scale2.updateStrl(m_scale2.m_width / 2,
                                m_scale2.m_height / 2, m_scale2.m_height / 6,
                                m_scale2.m_height / 2, 0);
                    }

                    // m_scale1.setUnitation( 1, "  Percent ", "pre", "after");
                    // m_scale2.setUnitation( 1, "  Percent ", "pre", "after");

                    m_scale1.postInvalidate();
                    m_scale2.postInvalidate();
                case 1:
            }
        }
    };






}