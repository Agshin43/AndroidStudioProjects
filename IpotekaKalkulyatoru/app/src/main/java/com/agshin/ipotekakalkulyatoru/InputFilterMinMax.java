package com.agshin.ipotekakalkulyatoru;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {

    private float min, max; //paramets that you send to class

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Float.parseFloat(min);
        this.max = Float.parseFloat(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
        try {
            //int input = Integer.parseInt(dest.toString() + source.toString());
            String startString = dest.toString().substring(0, dstart);
            String insert = source.toString();
            String endString = dest.toString().substring(dend);
            String parseThis = startString+insert+endString;
            float input = Float.parseFloat (parseThis);
            if (isInRange(min, max, input))
	            {
	                return insert;
	            }else 
	            {
	            	return "";
	            }
            }
        catch (NumberFormatException nfe) { }     
        return "";
    }

    private boolean isInRange(float a, float b, float c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}