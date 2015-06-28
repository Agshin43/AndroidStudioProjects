package com.apps.akaya.easytorch;

import java.util.ArrayList;

/**
 * Created by agshin on 5/23/15.
 */
public class Pattern {
    public ArrayList<Integer> runPauseIntervals;
    public boolean repeat;
    private int repeatCount;
    private int repeatCursor;
    int sensityMSecs;
    private boolean goingOn;
    private Pattern compWith;

    public Pattern(int sensityMSecs, int firstPlay) {
        runPauseIntervals = new ArrayList<Integer>();
        runPauseIntervals.add(firstPlay);
        this.sensityMSecs = sensityMSecs;
        goingOn = true;
    }

    public Pattern(int sensityMSecs, Pattern compWith) {
        this.compWith = compWith;
        runPauseIntervals = new ArrayList<Integer>();
        this.sensityMSecs = sensityMSecs;
        goingOn = true;
    }


    public void addPair(int pause, int run)
    {
        if(!isGoingOn())
            return;

        int size = this.runPauseIntervals.size();
        if( this.runPauseIntervals.size() > compWith.runPauseIntervals.size()-2 )
        {
            return;
        }
        if(compWith.runPauseIntervals.get(size - 2) <= (pause+sensityMSecs) && compWith.runPauseIntervals.get(size - 2) >= (pause-sensityMSecs)
                && compWith.runPauseIntervals.get(size - 2) <= (run+sensityMSecs) && compWith.runPauseIntervals.get(size - 2) >= (run-sensityMSecs))
        {
            runPauseIntervals.add(pause);
            runPauseIntervals.add(run);
            this.setGoingOn(true);
        }
        else
        {
            this.setGoingOn(false);
        }
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public boolean isGoingOn() {
        return goingOn;
    }

    public void setGoingOn(boolean goingOn) {
        this.goingOn = goingOn;
    }

    public void forwardCursor()
    {
        repeatCursor++;
    }
}

