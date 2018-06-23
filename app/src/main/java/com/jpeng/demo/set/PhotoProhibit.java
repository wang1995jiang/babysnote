package com.jpeng.demo.set;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;

public class PhotoProhibit extends AppCompatActivity implements View.OnClickListener {

    ImageView imagePlay;
    Activity activity;
    TextView title,show,surplus;
    LinearLayout back;
    RelativeLayout toolbarLin;
    LinearLayout setTime;
    EditText time;
    Button start;
    Dialog dialog;
    int timeNumber,h=0,m=0,s=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_prohibit);
        activity=this;
        ActivityCollector.addActivity(this);
        AlertDialog.Builder dialog=new AlertDialog.Builder(PhotoProhibit.this);
        dialog.setTitle("你需要知道：");
        dialog.setMessage("手机被禁止使用期间无法进行任何操作(包括接打电话发送短信)，直到设置时间结束！如果遇到紧急事情，请长按电源开机键七秒强制关机！");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setNegativeButton("离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                ActivityCollector.removeActivity(activity);
            }
        });
        dialog.show();

        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        back=(LinearLayout)findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("手机禁玩");
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        imagePlay=(ImageView)findViewById(R.id.play_toolbar);
        imagePlay.setVisibility(View.GONE);

        setTime=(LinearLayout)findViewById(R.id.set_prohibit_time);
        time=(EditText)findViewById(R.id.prohibit_time);
        time.setInputType( InputType.TYPE_CLASS_NUMBER);
        show=(TextView)findViewById(R.id.show_prohibit);
        surplus=(TextView)findViewById(R.id.surplus_time);
        start=(Button)findViewById(R.id.start_prohibit);
        start.setVisibility(View.VISIBLE);
        setTime.setVisibility(View.VISIBLE);
        show.setVisibility(View.GONE);
        surplus.setVisibility(View.GONE);
        start.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.start_prohibit:
                dwtNumber();
                break;
            default:break;
        }
    }

    private void startProhibit(){
        View view= LayoutInflater.from(this).inflate(R.layout.nothing_layout,null);
        dialog = new AlertDialog.Builder(this, R.style.TransparentWindowBg)
                .setView(view)
                .create();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);

        dialog.setCancelable(false);
        dialog.show();
        new ClosePhotoAsyncTask().execute();
    }

    private void dwtNumber(){
        int number= Integer.valueOf(time.getText().toString()).intValue();
        timeNumber=number*60;
        if (number>60){
            h=number/60;
            m=number%60;
        }else {
            m=number;
        }
        startProhibit();
    }

    class ClosePhotoAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            show.setVisibility(View.VISIBLE);
            surplus.setVisibility(View.VISIBLE);
            start.setVisibility(View.GONE);
            setTime.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            while (true){
                if (s<0&&m>=0){
                    s=59;
                    m--;
                }
                if (m<0&&h>0){
                    m=59;
                    h--;
                }
                publishProgress(h,m,s);
                timeNumber--;
                s--;
                SystemClock.sleep(1000);
                if (timeNumber<0){
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {

            if ((int)values[0]==0&&(int)values[1]!=0){
                surplus.setText("倒计时："+values[1]+"分"+values[2]+"秒");
            }
            else if((int)values[1]==0&&(int)values[0]==0){
                surplus.setText("倒计时："+values[2]+"秒");
            }else {
                surplus.setText("倒计时："+values[0]+"时"+values[1]+"分"+values[2]+"秒");
            }

        }

        @Override
        protected void onPostExecute(Object o) {
            dialog.dismiss();

            setTime.setVisibility(View.VISIBLE);
            start.setVisibility(View.VISIBLE);
            show.setVisibility(View.GONE);
            surplus.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"手机已恢复正常！为你的坚持鼓掌",Toast.LENGTH_SHORT).show();
        }

    }

}
