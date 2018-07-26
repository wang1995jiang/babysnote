package com.jpeng.demo.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MainActivity;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;
import com.jpeng.demo.vioce.ActivityCommon;
import com.jpeng.demo.vioce.ActivityOnline;
import com.jpeng.demo.vioce.ActivityUiDialog;
import com.jpeng.demo.vioce.ActivityWakeUp;
import com.jpeng.demo.vioce.mini.ActivityMiniRecog;
import com.jpeng.demo.vioce.mini.ActivityMiniWakeUp;
import com.jpeng.demo.vioce.mini.MiniActivity;

public class ClockSet extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout toolbarLin;
    LinearLayout back,remind;
    TextView title;
    ImageView play,save,add;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_set);
        ActivityCollector.addActivity(this);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();

        if (Tool.getRingtones().size()==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Tool.getRingtoneList(RingtoneManager.TYPE_ALL);
                }
            }).start();
        }

        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), false);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("我的提醒");
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        play=(ImageView)findViewById(R.id.play_toolbar);
        save=(ImageView)findViewById(R.id.save_toolbar);
        remind=(LinearLayout)findViewById(R.id.remind_lin);
        play.setVisibility(View.GONE);
        save.setVisibility(View.GONE);

        add=(ImageView)findViewById(R.id.add_remind);
        add.setOnClickListener(this);

        if (Tool.getRemindNum()!=0){
            for (int i=0;i<Tool.getRemindNum();i++){
                remind.addView(getItemRemindView(remind,Tool.getClocks().get(i)));
            }
        }

        intentFilter=new IntentFilter();
        intentFilter.addAction("com.jpeng.demo.REMIND");
        intentFilter.addAction("com.jpeng.demo.REMINDSUCCESS");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    private View getItemRemindView(LinearLayout root,Clock clock){
        View view= LayoutInflater.from(this).inflate(R.layout.item_remind,root,false);
        RelativeLayout relativeLayout=(RelativeLayout)view.findViewById(R.id.item_rela_remind);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        TextView timeType=(TextView)view.findViewById(R.id.item_remind_timeType);
        TextView time=(TextView)view.findViewById(R.id.item_remind_time);
        TextView remindType=(TextView)view.findViewById(R.id.item_remind_type);
        TextView remindF=(TextView)view.findViewById(R.id.item_remindF);
        TextView cancel=(TextView)view.findViewById(R.id.cancel_remind);
        TextView clockOrRemind=(TextView)view.findViewById(R.id.clock_or_remind);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.show_remind_time);
        TextView stop=(TextView)view.findViewById(R.id.stop_ringtone);

        linearLayout.setVisibility(View.VISIBLE);
        stop.setVisibility(View.GONE);

        cancel.setTag(view);
        cancel.setOnClickListener(this);

        if (Integer.valueOf(clock.getHour())<13){
            timeType.setText("上午");
        }else {
            timeType.setText("下午");
        }
        time.setText(clock.getHour()+":"+clock.getMinute());
        if (clock.isClock()){
            remindType.setText("闹钟");
            clockOrRemind.setText("将会响铃");
            if (clock.getRemindF()==0){
                remindF.setText("每天");
            }else {
                remindF.setText("仅此一次");
            }
        }else {
            remindType.setText("事件");
            clockOrRemind.setText("将会提醒");
            remindF.setText("仅此一次");
        }

        view.setTag(clock.getId());
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_toolbar:
                finish();
                break;
            case R.id.add_remind:
                Intent intent=new Intent(this,AddRemind.class);
                startActivity(intent);
                break;
            case R.id.cancel_remind:
                View view=(View)v.getTag();
                int id=(int)view.getTag();
                remind.removeView(view);
                Tool.getClocks().remove((Clock)Tool.getClock(id));
                Tool.getCancels().add(id);
                Tool.setRemindNum(Tool.getRemindNum()-1);
                break;

            default: break;
        }
    }

    class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.jpeng.demo.REMIND")){
                remind.addView(getItemRemindView(remind,Tool.getClocks().get(Tool.getRemindNum()-1)));
            }
            if (intent.getAction().equals("com.jpeng.demo.REMINDSUCCESS")){
                final Clock clock=Tool.getClock(Tool.getClockId());
                for (int i=0;i<remind.getChildCount();i++){
                    if ((int)remind.getChildAt(i).getTag()==Tool.getClockId()){
                        if (clock.isClock()){
                            LinearLayout linearLayout=(LinearLayout)remind.getChildAt(i).findViewById(R.id.show_remind_time);
                            TextView stop=(TextView)remind.getChildAt(i).findViewById(R.id.stop_ringtone);
                            linearLayout.setVisibility(View.GONE);
                            stop.setVisibility(View.VISIBLE);
                            stop.setTag(i);
                            stop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Tool.setIsStop(true);
                                    clock.getRingtone().stop();
                                    remind.removeView(remind.getChildAt((int)v.getTag()));
                                }
                            });
                        }else {
                            remind.removeView(remind.getChildAt(i));
                        }

                    }
                }
                Tool.getClocks().remove((Clock) clock);
                Tool.setRemindNum(Tool.getRemindNum()-1);
            }
        }
    }
}
