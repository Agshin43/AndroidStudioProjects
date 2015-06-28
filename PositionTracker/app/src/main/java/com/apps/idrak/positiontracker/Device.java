package com.apps.idrak.positiontracker;

import java.sql.Date;

public class Device {
    public int id;
    public String displayName;
    public Date lastDataReceivedTime;
    public Date lastCommandSentTime;
    public boolean connected;
    public boolean tracking;
    public float lastLongitude;
    public float lastLatitude;
    public int registrationStatus;

    public Device()
    {

    }

}