package com.apps.idrak.positiontracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class autostart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context arg0, Intent arg1)
    {
        Intent intent = new Intent(arg0,GpsService.class);
        arg0.startService(intent);
    }
}