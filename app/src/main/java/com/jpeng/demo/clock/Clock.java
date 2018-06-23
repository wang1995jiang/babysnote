package com.jpeng.demo.clock;

import android.media.Ringtone;

/**
 * Created by 王将 on 2018/5/14.
 */

public class Clock {
    private boolean isClock;
    private String hour,minute,remindThing="";
    private int rmindR,remindF,id;
    private Ringtone ringtone;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setClock(boolean clock) {
        isClock = clock;
    }

    public void setRemindF(int remindF) {
        this.remindF = remindF;
    }

    public void setRemindThing(String remindThing) {
        this.remindThing = remindThing;
    }

    public void setRmindR(int rmindR) {
        this.rmindR = rmindR;
    }

    public int getRemindF() {
        return remindF;
    }

    public int getRmindR() {
        return rmindR;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getRemindThing() {
        return remindThing;
    }

    public boolean isClock() {
        return isClock;
    }

    public void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
    }

    public Ringtone getRingtone() {
        return ringtone;
    }
}
