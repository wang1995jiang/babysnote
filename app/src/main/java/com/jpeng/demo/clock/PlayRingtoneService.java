package com.jpeng.demo.clock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class PlayRingtoneService extends Service {
    private LocalBroadcastManager localBroadcastManager;
    public PlayRingtoneService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int clockId=intent.getIntExtra("count",0);
        boolean isCancel=false;
        for (int id:Tool.getCancels()){
            if (id==clockId){
                isCancel=true;
            }
        }
        if (!isCancel){
            final Clock clock=Tool.getClock(intent.getIntExtra("count",0));
            Tool.setClockId(clockId);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Tool.isIsStop()){
                        clock.getRingtone().play();
                    }
                }
            }).start();
            Intent intent2=new Intent("com.jpeng.demo.REMINDSUCCESS");
            localBroadcastManager.sendBroadcast(intent2);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
