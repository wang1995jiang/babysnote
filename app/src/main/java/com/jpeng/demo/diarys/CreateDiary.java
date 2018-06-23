package com.jpeng.demo.diarys;


import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jpeng.demo.Image;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.NoteTool;
import com.jpeng.demo.R;
import com.jpeng.demo.TextChange;
import com.jpeng.demo.weather.ImageChose;
import com.jpeng.demo.weather.Provide;

import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 王将 on 2018/3/27.
 */

public class CreateDiary extends Fragment implements View.OnClickListener {
    String stringMood="";
    TextView time,mood;
    EditText editText;
    Button button;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.create_diary, container, false);

        localBroadcastManager=LocalBroadcastManager.getInstance(Diary.context);

        time=(TextView) view.findViewById(R.id.time_diary);
        mood=(TextView) view.findViewById(R.id.mood_diary);
        editText=(EditText) view.findViewById(R.id.editext_diary);
        button=(Button) view.findViewById(R.id.save_diary);

        Date dateStart = new Date();
        time.setText(dateStart.toLocaleString());
        mood.setOnClickListener(this);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mood_diary:
                choiceMoodDialog();
                break;
            case R.id.save_diary:

                Date dateEnd=new Date();
                DiaryEntity diaryEntity=new DiaryEntity();

                diaryEntity.setMood(stringMood);
                diaryEntity.setTime(dateEnd.toLocaleString());
                diaryEntity.setContent(editText.getText().toString());
                diaryEntity.setWeather(ImageChose.getWeatherName(Integer.valueOf(Provide.getWeather().getHeWeather6().get(0).getNow().getCond_code())));
                diaryEntity.save();

                editText.setText("");
                mood.setText("");
                time.setText(dateEnd.toLocaleString());

                Intent intent=new Intent("com.jpeng.demo.ADDDATASUCCESS_DIARY");
                localBroadcastManager.sendBroadcast(intent);
            default:break;
        }
    }

    private void choiceMoodDialog(){
        // 创建数据
        final String[] items = new String[] { "快乐高兴", "悲伤难过", "焦虑紧张", "愤怒气愤" ,"感动温暖" ,"后悔内疚" ,"害羞激动" };
        // 创建对话框构建器
        final AlertDialog.Builder builder = new AlertDialog.Builder(Diary.context);
        // 设置参数
        builder.setTitle("选择心情")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyApplication.getContext(), items[which],
                                Toast.LENGTH_SHORT).show();
                        mood.setText(items[which]);
                        stringMood=items[which];
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
