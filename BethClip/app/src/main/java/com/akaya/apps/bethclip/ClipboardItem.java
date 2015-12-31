package com.akaya.apps.bethclip;

import android.graphics.drawable.Drawable;

public class ClipboardItem {
    private String deviceName;
    private String text;
    private int type;
    private String date;

    public ClipboardItem(String text, int type, String deviceName, String date) {
        this.text = text;
        this.type = type;
        this.deviceName = deviceName;
        this.date = date;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getText() {
        return text;
    }


    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
