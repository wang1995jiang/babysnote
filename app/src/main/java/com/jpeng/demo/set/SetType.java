package com.jpeng.demo.set;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.CarmeraAndGall;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;
import com.jpeng.demo.face.FaceRecognition;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.animate.AnimationType;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetType extends CarmeraAndGall implements View.OnClickListener {

    ImageView imagePlay;
    RelativeLayout closePhoto;
    RelativeLayout learnLabel;
    Dialog dialog;
    private Uri imageUri;
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    TextView switchText;
    Activity activity;
    private LocalReceiver localReceiver;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    LinearLayout back;
    RelativeLayout toolbarLin;
    RelativeLayout navigationbarTextView;
    RelativeLayout userInformation;
    RelativeLayout setHeadPortrait;
    RelativeLayout toolbarColor;
    RelativeLayout faceRe;
    private JPTabBar mTabBar;
    TextView setTitle,showText,inforText,faceText;
    CircleImageView imgShow;
    View colorShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_layout);
        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        activity=this;
        imagePlay=(ImageView)findViewById(R.id.play_toolbar);
        imagePlay.setVisibility(View.GONE);

        switchText=(TextView)findViewById(R.id.switch_button_text);
        if (MyApplication.getPeople().isUserGesture()){
            switchText.setText("关闭手势操作");
        }else {
            switchText.setText("开启手势操作");
        }

        SwitchButton mSwitchButton = (SwitchButton) findViewById(R.id.switch_button);
        mSwitchButton.setChecked(MyApplication.getPeople().isUserGesture());
        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    switchText.setText("关闭手势操作");
                    MyApplication.getPeople().setUserGesture(true);
                    saveSet(2,"isUserGesture","",true,0);
                }else {
                    switchText.setText("开启手势操作");
                    MyApplication.getPeople().setUserGesture(false);
                    saveSet(2,"isUserGesture","",false,0);
                }

            }
        });


        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        ActivityCollector.addActivity(this);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        back=(LinearLayout)findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        setTitle=(TextView)findViewById(R.id.title_tooolbar);
        setTitle.setText("设置");
        navigationbarTextView=(RelativeLayout) findViewById(R.id.navigationbar);
        userInformation=(RelativeLayout)findViewById(R.id.user_information);
        setHeadPortrait=(RelativeLayout)findViewById(R.id.set_head_portrait);
        toolbarColor=(RelativeLayout)findViewById(R.id.toolbar_color);
        closePhoto=(RelativeLayout)findViewById(R.id.close_phone);
        learnLabel=(RelativeLayout)findViewById(R.id.learn_label_set);
        faceRe=(RelativeLayout)findViewById(R.id.face_recognition);
        showText=(TextView)findViewById(R.id.Type_show_text);
        inforText=(TextView)findViewById(R.id.information_show_text);
        faceText=(TextView)findViewById(R.id.face_show_text);
        imgShow=(CircleImageView)findViewById(R.id.icon_image);
        colorShow=(View)findViewById(R.id.color_show);
        closePhoto.setOnClickListener(this);
        learnLabel.setOnClickListener(this);
        toolbarColor.setOnClickListener(this);
        userInformation.setOnClickListener(this);
        setHeadPortrait.setOnClickListener(this);
        navigationbarTextView.setOnClickListener(this);
        faceRe.setOnClickListener(this);

        setShowText(MyApplication.getPeople().getAnimationType());
        inforText.setText(MyApplication.getPeople().getNickName());
        Glide.with(this).load(MyApplication.getPeople().getHeadImage()).error(R.drawable.imageview).into(imgShow);
        setFaceShow(MyApplication.getPeople().isUseFace());
        colorShow.setBackgroundColor(MyApplication.getPeople().getToolbarColor());

        intentFilter=new IntentFilter();
        intentFilter.addAction("com.jpeng.demo.CHOSECOLOR");
        intentFilter.addAction("com.jpeng.demo.PEOPLEINFO");
        intentFilter.addAction("com.jpeng.demo.FACECHANGE");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    private void setShowText(int id){
        switch (id){
            case 0:showText.setText("普通缩放");break;
            case 1:showText.setText("增强缩放");break;
            case 2:showText.setText("跳跃");break;
            case 3:showText.setText("旋转");break;
            default:break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (MyApplication.getPeople().isUserGesture()){
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 = event.getX();
                y1 = event.getY();
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                x2 = event.getX();
                y2 = event.getY();
                if(y1 - y2  > 50) {//向上滑

                } else if(y2 - y1 > 50) {//向下滑

                } else if(x1 - x2 > 50) {//向左滑

                } else if(x2 - x1 > 50) {//向右滑
                    finish();
                    ActivityCollector.removeActivity(this);
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigationbar:
                creatDialog();
                break;
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.user_information:
                Intent intent=new Intent(SetType.this,PeopleInformation.class);
                startActivity(intent);
                break;
            case R.id.toolbar_color:
                Intent intent1=new Intent(SetType.this,ChoseColor.class);
                startActivity(intent1);
                break;
            case R.id.set_head_portrait:
                creatChoiceDialog();
                break;
            case R.id.close_phone:
                Intent intent3=new Intent(SetType.this,PhotoProhibit.class);
                startActivity(intent3);
                break;
            case R.id.learn_label_set:
                Intent intent2=new Intent(SetType.this,LearnLabel.class);
                startActivity(intent2);
                break;
            case R.id.face_recognition:
                Intent intent4=new Intent(SetType.this, FaceRecognition.class);
                startActivity(intent4);
            default:
        }
    }

    private void creatChoiceDialog() {
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.set_headimage, null);
        //初始化控件

        final TextView goCamera=(TextView)inflate.findViewById(R.id.go_camera);
        TextView goGallery=(TextView)inflate.findViewById(R.id.go_gallery);

        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

        goCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCarmera();
                dialog.dismiss();
            }
        });
        goGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goGallery();
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void creatDialog(){
        // 创建数据
        final String[] items = new String[] { "普通缩放", "增强缩放", "跳跃", "旋转" };
        // 创建对话框构建器
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("选择动画类型")
                .setSingleChoiceItems(items, MyApplication.getPeople().getAnimationType(), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyApplication.getContext(), items[which],
                                Toast.LENGTH_SHORT).show();
                        MyApplication.getPeople().setAnimationType(which);
                        saveSet(1,"checkedItem","",false,which);
                        changeTabBar(which);
                        setShowText(which);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void setFaceShow(boolean is){
        if (is){
            faceText.setText("已开启刷脸");
        }else {
            faceText.setText("已关闭刷脸");
        }
    }

    private void changeTabBar(int id){
        switch (id)
        {
            case 0:
                MyApplication.getJpTabBar().setAnimation(AnimationType.SCALE);
                break;
            case 1:
                MyApplication.getJpTabBar().setAnimation(AnimationType.SCALE2);
                break;
            case 2:
                MyApplication.getJpTabBar().setAnimation(AnimationType.JUMP);
                break;
            case 3:
                MyApplication.getJpTabBar().setAnimation(AnimationType.ROTATE);
                break;
            default:break;
        }
    }
    class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.jpeng.demo.CHOSECOLOR")){
                toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
                StatusBarCompat.setStatusBarColor(getActivity(), MyApplication.getPeople().getToolbarColor(), true);
                colorShow.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
            }
            if (intent.getAction().equals("com.jpeng.demo.PEOPLEINFO")){
                inforText.setText(MyApplication.getPeople().getNickName());
            }
            if (intent.getAction().equals("com.jpeng.demo.FACECHANGE")){
                setFaceShow(MyApplication.getPeople().isUseFace());
            }

        }
    }

    private void saveSet(int isInt,String key,String data,boolean isUserGesture,int AnimationType){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        switch(isInt){
            case 1:
                editor.putInt(key,AnimationType);break;
            case 2:
                editor.putBoolean(key,isUserGesture);break;
            case 3:
                editor.putString(key,data);break;
            default:break;
        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!path.isEmpty()){
            MyApplication.getPeople().setHeadImage(path);
            saveSet(3,"headImage",path,false,0);
            Glide.with(this).load(MyApplication.getPeople().getHeadImage()).error(R.drawable.imageview).into(imgShow);
            Intent intent=new Intent("com.jpeng.demo.HEDAIMAGE");
            localBroadcastManager.sendBroadcast(intent);
        }

    }
}
