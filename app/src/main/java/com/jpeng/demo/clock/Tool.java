package com.jpeng.demo.clock;

import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.WindowManager;

import com.jpeng.demo.MyApplication;
import com.jpeng.demo.vioce.wakeup.RecogWakeupListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 王将 on 2018/5/13.
 */

public class Tool {

    private static List<Ringtone> ringtones=new ArrayList<>();
    private static boolean isStop=false;
    private static List<Integer> cancels=new ArrayList<>();
    private static int clockR,clockF,remindNum=0,allNum=0,clockId;
    private static List<Clock> clocks=new ArrayList<>();
    public static final String[] items = new String[] { "事件", "闹钟" };
    public static final String[] itemsF = new String[] { "每天", "仅此一次" };
    public static final String[] itemsR = (String[])getRingtoneTitleList(RingtoneManager.TYPE_ALL).toArray(new String[getRingtoneTitleList(RingtoneManager.TYPE_ALL).size()]);

    public static List<Ringtone> getRingtones() {
        return ringtones;
    }

    public static void setIsStop(boolean isStop) {
        Tool.isStop = isStop;
    }

    public static boolean isIsStop() {
        return isStop;
    }

    public static void setClockId(int clockId) {
        Tool.clockId = clockId;
    }

    public static List<Integer> getCancels() {
        return cancels;
    }

    public static int getClockId() {
        return clockId;
    }

    public static void setAllNum(int allNum) {
        Tool.allNum = allNum;
    }

    public static int getAllNum() {
        return allNum;
    }

    public static void setRemindNum(int remindNum) {
        Tool.remindNum = remindNum;
    }

    public static int getRemindNum() {
        return remindNum;
    }

    public static List<Clock> getClocks() {
        return clocks;
    }

    public static void setClockF(int clockF) {
        Tool.clockF = clockF;
    }

    public static void setClockR(int clockR) {
        Tool.clockR = clockR;
    }

    public static int getClockF() {
        return clockF;
    }

    public static int getClockR() {
        return clockR;
    }

    public static Ringtone getDefaultRingtone(int type){

        return RingtoneManager.getRingtone(MyApplication.getContext(), RingtoneManager.getActualDefaultRingtoneUri(MyApplication.getContext(), type));

    }



    public static Uri getDefaultRingtoneUri(int type){

        return RingtoneManager.getActualDefaultRingtoneUri(MyApplication.getContext(), type);

    }



    public static void getRingtoneList(int type){


        RingtoneManager manager = new RingtoneManager(MyApplication.getContext());

        manager.setType(type);

        Cursor cursor = manager.getCursor();

        int count = cursor.getCount();

        for(int i = 0 ; i < count ; i ++){

            ringtones.add(manager.getRingtone(i));

        }
    }



    public static Ringtone getRingtone(int type,int pos){

        RingtoneManager manager = new RingtoneManager(MyApplication.getContext());

        manager.setType(type);

        return manager.getRingtone(pos);

    }



    public static List<String> getRingtoneTitleList(int type){

        List<String> resArr = new ArrayList<String>();

        RingtoneManager manager = new RingtoneManager(MyApplication.getContext());

        manager.setType(type);

        Cursor cursor = manager.getCursor();

        if(cursor.moveToFirst()){

            do{

                resArr.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));

            }while(cursor.moveToNext());

        }

        return resArr;

    }

    public static String getRingtoneUriPath(int type,int pos,String def){

        RingtoneManager manager = new RingtoneManager(MyApplication.getContext());

        manager.setType(type);

        Uri uri = manager.getRingtoneUri(pos);

        return uri==null?def:uri.toString();

    }



    public static Ringtone getRingtoneByUriPath(int type ,String uriPath){

        RingtoneManager manager = new RingtoneManager(MyApplication.getContext());

        manager.setType(type);

        Uri uri = Uri.parse(uriPath);

        return manager.getRingtone(MyApplication.getContext(), uri);
    }

    public static Clock getClock(int id){
        Clock clock = null;
        for (Clock clockItem:Tool.getClocks()){
            if (clockItem.getId()==id){
                clock=clockItem;
            }
        }
        return clock;
    }

    public static String[] getStrNow(){
        String[] strNow=new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new java.util.Date()).split("-");
        return strNow;
    }
}
