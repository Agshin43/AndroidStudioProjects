package com.apps.akaya.picnest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by agshin on 3/15/15.
 */
public class MyTools {

    public static void printI(String tag, String message, boolean print)
    {
        if(print)
        {
            Log.i(tag, message);
        }
    }

    public static void showToast(Context context, String message, boolean show)
    {
        if(show)
        {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
