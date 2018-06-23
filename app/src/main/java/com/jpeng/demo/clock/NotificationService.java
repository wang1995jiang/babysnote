package com.jpeng.demo.clock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.jpeng.demo.MainActivity;
import com.jpeng.demo.R;

public class NotificationService extends Service {
    private LocalBroadcastManager localBroadcastManager;
    public NotificationService() {
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
    public int onStartCommand(final Intent intent, int flags, int startId) {
        int clockId=intent.getIntExtra("count",0);
        boolean isCancel=false;
        for (int id:Tool.getCancels()){
            if (id==clockId){
                isCancel=true;
            }
        }
        if (!isCancel){
            final Clock clock=Tool.getClock(clockId);
            Tool.setClockId(intent.getIntExtra("count",0));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent1=new Intent(NotificationService.this,MainActivity.class);
                    PendingIntent pendingIntent=PendingIntent.getActivity(NotificationService.this,0,intent1,0);
                    NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    Notification notification=new NotificationCompat.Builder(NotificationService.this).setContentTitle("时间到啦！")
                            .setContentText(clock.getRemindThing())
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_lanucher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_lanucher))
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .build();
                    manager.notify(1,notification);
                    stopSelf();
                }
            }).start();
            Intent intent2=new Intent("com.jpeng.demo.REMINDSUCCESS");
            localBroadcastManager.sendBroadcast(intent2);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
