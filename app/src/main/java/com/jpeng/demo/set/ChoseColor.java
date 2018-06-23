package com.jpeng.demo.set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;

public class ChoseColor extends AppCompatActivity implements View.OnClickListener {

    ImageView imagePlay;
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    View viewColor;
    int colorData;
    private LocalBroadcastManager localBroadcastManager;
    RelativeLayout toolbarLin;
    LinearLayout back;
    Button yes,no;
    TextView title;
    private TextView txtColor;
    private ColorPickView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_color);
        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        ActivityCollector.addActivity(this);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("调色盘");
        imagePlay=(ImageView)findViewById(R.id.play_toolbar);
        imagePlay.setVisibility(View.GONE);

        myView = (ColorPickView) findViewById(R.id.color_picker_view);
        txtColor = (TextView) findViewById(R.id.txt_color);
        viewColor=(View)findViewById(R.id.view_color);
        yes=(Button) findViewById(R.id.yes_btn);
        yes.setOnClickListener(this);
        no=(Button)findViewById(R.id.no_btn);
        no.setOnClickListener(this);
        myView.setOnColorChangedListener(new ColorPickView.OnColorChangedListener() {

            @Override
            public void onColorChange(final int color) {
                txtColor.setTextColor(color);
                viewColor.setBackgroundColor(color);
                colorData=color;
            }
        });
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
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.yes_btn:
                MyApplication.getPeople().setToolbarColor(colorData);
                SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putInt("colorData",colorData);
                editor.apply();
                Intent intent=new Intent("com.jpeng.demo.CHOSECOLOR");
                localBroadcastManager.sendBroadcast(intent);
                toolbarLin.setBackgroundColor(colorData);
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.no_btn:
                finish();
                ActivityCollector.removeActivity(this);
            default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        ActivityCollector.removeActivity(this);
    }
}
