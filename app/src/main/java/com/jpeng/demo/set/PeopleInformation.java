package com.jpeng.demo.set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;


public class PeopleInformation extends AppCompatActivity implements View.OnClickListener {

    ImageView imagePlay;
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    private LocalBroadcastManager localBroadcastManager;
    Button yes,no;
    EditText nickNameText;
    EditText personalitySignatureText;
    TextView title;
    LinearLayout back;
    RelativeLayout toolbarLin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);
        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        ActivityCollector.addActivity(this);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        back=(LinearLayout)findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("个人信息");
        imagePlay=(ImageView)findViewById(R.id.play_toolbar);
        imagePlay.setVisibility(View.GONE);

        yes=(Button)findViewById(R.id.yes_all_btn);
        no=(Button)findViewById(R.id.no_all_btn);
        nickNameText=(EditText)findViewById(R.id.nickname_user);
        personalitySignatureText=(EditText)findViewById(R.id.personality_signature_user);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
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
                if(y1 - y2 > 50) {//向上滑

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes_all_btn:
                if (!nickNameText.getText().toString().isEmpty()&&!personalitySignatureText.getText().toString().isEmpty()){
                    yesSet();
                }else {
                    Snackbar.make(v,"输入框不可为空",Snackbar.LENGTH_SHORT)
                            .setAction("好的", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    return;
                                }
                            }).show();
                    return ;
                }

                break;
            case R.id.no_all_btn:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
            default:break;
        }
    }

    private void yesSet(){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("nickName",nickNameText.getText().toString());
        editor.putString("personalitySignature",personalitySignatureText.getText().toString());
        editor.apply();
        MyApplication.getPeople().setNickName(nickNameText.getText().toString());
        MyApplication.getPeople().setPersonalitySignature(personalitySignatureText.getText().toString());
        Intent intent=new Intent("com.jpeng.demo.PEOPLEINFO");
        localBroadcastManager.sendBroadcast(intent);
        finish();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
