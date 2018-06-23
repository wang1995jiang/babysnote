package com.jpeng.demo.clock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;
import com.jpeng.demo.WheelView;

import java.util.ArrayList;
import java.util.List;

public class AddRemind extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout toolbarLin;
    LinearLayout back;
    TextView title,showType,showThing,inContent,showRingtone,showFrequency;
    RelativeLayout remindThing,remindType,remindRingtone,remindFrequency;
    ImageView play,save;
    Dialog dialog;
    WheelView morning,hour,minu;
    Clock clock=new Clock();
    AlarmManager manager;
    boolean isType=false,isThing=false,isR=false,isF=false,isTime=false;
    private LocalBroadcastManager localBroadcastManager;
    private  List<String> hours=new ArrayList<>();
    private  List<String> minus=new ArrayList<>();
    private  List<String> timetypes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remind);
        ActivityCollector.addActivity(this);

        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), false);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("新建提醒");
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        play=(ImageView)findViewById(R.id.play_toolbar);
        save=(ImageView)findViewById(R.id.save_toolbar);
        play.setVisibility(View.GONE);
        save.setImageResource(R.drawable.save_white);
        save.setOnClickListener(this);

        morning=(WheelView)findViewById(R.id.wheel_morning);
        hour=(WheelView)findViewById(R.id.wheel_hour);
        minu=(WheelView)findViewById(R.id.wheel_minute);
        remindThing=(RelativeLayout)findViewById(R.id.remind_rela);
        remindType=(RelativeLayout)findViewById(R.id.remind_type);
        remindRingtone=(RelativeLayout)findViewById(R.id.remind_ringtone);
        remindFrequency=(RelativeLayout)findViewById(R.id.remind_frequency);
        showThing=(TextView)findViewById(R.id.thing_text);
        showType=(TextView)findViewById(R.id.Type_text);
        showRingtone=(TextView)findViewById(R.id.ringtone_text) ;
        showFrequency=(TextView)findViewById(R.id.frequency_text) ;

        remindThing.setOnClickListener(this);
        remindType.setOnClickListener(this);
        remindRingtone.setOnClickListener(this);
        remindFrequency.setOnClickListener(this);

        remindThing.setVisibility(View.GONE);
        remindRingtone.setVisibility(View.GONE);
        remindFrequency.setVisibility(View.GONE);

        setList();

        morning.setData((ArrayList<String>) timetypes);
        hour.setData((ArrayList<String>) hours);
        minu.setData((ArrayList<String>) minus);

        if (Integer.valueOf(Tool.getStrNow()[0])<13){
            morning.setDefault(0);
        }else {
            morning.setDefault(1);
        }
        hour.setDefault(Integer.valueOf(Tool.getStrNow()[0]));
        minu.setDefault(Integer.valueOf(Tool.getStrNow()[1]));

        morning.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {

            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        hour.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                clock.setHour(text);
                isTime=true;
            }

            @Override
            public void selecting(int id, String text) {
                if (id>=13){
                    morning.setDefault(1);
                }else {
                    morning.setDefault(0);
                }
            }
        });
        minu.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                clock.setMinute(text);
                isTime=true;
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }

    private void setList() {
        timetypes.add("上午");
        timetypes.add("下午");
        for (int i=0;i<24;i++){
            if (i<10){
                hours.add("0"+String.valueOf(i));
            }else {
                hours.add(String.valueOf(i));
            }

        }
        for (int i=0;i<60;i++){
            if (i<10){
                minus.add("0"+String.valueOf(i));
            }else {
                minus.add(String.valueOf(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_toolbar:
                ActivityCollector.removeActivity(this);
                finish();
                break;
            case R.id.save_toolbar:
                setSave();
                break;
            case R.id.remind_rela:
                setRemindThing();
                break;
            case R.id.determine_button:
                showThing.setText(inContent.getText().toString());
                clock.setRemindThing(inContent.getText().toString());
                isThing=true;
                dialog.dismiss();
                break;
            case R.id.remind_type:
                setRemindType();
                break;
            case R.id.remind_frequency:
                setRemindFrequency();
                break;
            case R.id.remind_ringtone:
                setRemindRingtone();
                break;
            default: break;
        }
    }

    private void setSave(){
        if (isTime){
            if (clock.getHour()==null){
                clock.setHour(Tool.getStrNow()[0]);
            }
            if (clock.getMinute()==null){
                clock.setMinute(Tool.getStrNow()[1]);
            }
            if (isType){
                if (clock.isClock()){
                    if (isR&&isF)
                    {
                        successSet();
                    }else {
                        Toast.makeText(MyApplication.getContext(),"请完善闹钟信息！",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (isThing){
                        successSet();
                    }else {
                        Toast.makeText(MyApplication.getContext(),"请填写提醒事件！",Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(MyApplication.getContext(),"请选择提醒类型！",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MyApplication.getContext(),"请确定提醒时间！",Toast.LENGTH_SHORT).show();
        }
    }

    private void successSet(){
        clock.setId(Tool.getAllNum());
        Tool.setAllNum(Tool.getAllNum()+1);
        Tool.getClocks().add(clock);
        Tool.setRemindNum(Tool.getRemindNum()+1);
        long time=0;
        int clockHour=Integer.valueOf(clock.getHour());
        int clockMinute=Integer.valueOf(clock.getMinute());

        if (clockHour>=Integer.valueOf(Tool.getStrNow()[0])){
            if (clockHour==Integer.valueOf(Tool.getStrNow()[0])){
                time=SystemClock.elapsedRealtime()+(clockMinute-Integer.valueOf(Tool.getStrNow()[1])-1)*60*1000+(60-Integer.valueOf(Tool.getStrNow()[2]))*1000;
            }else {
                time=SystemClock.elapsedRealtime()+(clockHour-Integer.valueOf(Tool.getStrNow()[0])-1)*3600*1000+(clockMinute+60-Integer.valueOf(Tool.getStrNow()[1])-1)*60*1000+(60-Integer.valueOf(Tool.getStrNow()[2]))*1000;
            }
        }else {
            time=SystemClock.elapsedRealtime()+(clockHour+24-Integer.valueOf(Tool.getStrNow()[0])-1)*3600*1000+(clockMinute+60-Integer.valueOf(Tool.getStrNow()[1])-1)*60*1000+(60-Integer.valueOf(Tool.getStrNow()[2]))*1000;
        }

        if (clock.isClock()){
            setAlarm(time,clock.getId(),false);
        }else {
            setAlarm(time,clock.getId(),true);
        }

        Intent intent=new Intent("com.jpeng.demo.REMIND");
        localBroadcastManager.sendBroadcast(intent);
        ActivityCollector.removeActivity(this);
        finish();
    }

    private void setAlarm(long time,int count,boolean isRemind){
        if (isRemind){
            Intent intent1=new Intent(this,NotificationService.class);
            intent1.putExtra("count",count);
            PendingIntent pi=PendingIntent.getService(this,count,intent1,0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pi);
            }
        }else {
            Intent intent1=new Intent(this,PlayRingtoneService.class);
            intent1.putExtra("count",count);
            PendingIntent pi=PendingIntent.getService(this,count,intent1,0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pi);
            }
        }

    }

    private void setRemindThing() {
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        final View inflate = LayoutInflater.from(this).inflate(R.layout.in_content, null);
        //初始化控件
        final Button determineButton=(Button)inflate.findViewById(R.id.determine_button);
        TextView titleContent=(TextView)inflate.findViewById(R.id.title_content);
        inContent=(EditText)inflate.findViewById(R.id.in_content);
        inContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>10){
                    determineButton.setEnabled(false);
                    Toast.makeText(MyApplication.getContext(),"不可超过十个字",Toast.LENGTH_SHORT).show();
                }else {
                    determineButton.setEnabled(true);
                }
            }
        });
        titleContent.setText("提醒事件");
        determineButton.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void setRemindType(){
        // 创建对话框构建器
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("选择提醒类型")
                .setSingleChoiceItems(Tool.items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyApplication.getContext(), Tool.items[which],
                                Toast.LENGTH_SHORT).show();
                        showType.setText(Tool.items[which]);
                        if (which==0){
                            clock.setClock(false);
                            remindThing.setVisibility(View.VISIBLE);
                            remindRingtone.setVisibility(View.GONE);
                            remindFrequency.setVisibility(View.GONE);
                        }else {
                            clock.setClock(true);
                            remindRingtone.setVisibility(View.VISIBLE);
                            remindFrequency.setVisibility(View.VISIBLE);
                            remindThing.setVisibility(View.GONE);
                        }
                        dialog.dismiss();
                        isType=true;
                    }
                });
        builder.create().show();
    }

    private void setRemindRingtone(){
        // 创建对话框构建器
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("选择闹钟铃声")
                .setSingleChoiceItems(Tool.itemsR, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyApplication.getContext(), Tool.itemsR[which],
                                Toast.LENGTH_SHORT).show();
                        showRingtone.setText(Tool.itemsR[which]);
                        Tool.setClockR(which);
                        clock.setRmindR(which);
                        Ringtone ringtone=Tool.getRingtones().get(which);
                        clock.setRingtone(ringtone);
                        dialog.dismiss();
                        isR=true;
                    }
                });
        builder.create().show();
    }

    private void setRemindFrequency(){
        // 创建对话框构建器
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("选择闹钟频率")
                .setSingleChoiceItems(Tool.itemsF, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyApplication.getContext(), Tool.itemsF[which],
                                Toast.LENGTH_SHORT).show();
                        showFrequency.setText(Tool.itemsF[which]);
                        Tool.setClockF(which);
                        clock.setRemindF(which);
                        dialog.dismiss();
                        isF=true;
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
