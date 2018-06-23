package com.jpeng.demo.diarys;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;


public class Encryption extends AppCompatActivity implements View.OnClickListener {

    TextView one,two,three,four,five,six,seven,eight,nine,zero,resultEnc;
    EditText passOne,passTwo,passThree,passFour;
    ImageView deleft,fingerprint;
    int id=1;
    String passWord="";

    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_encryption);
        ActivityCollector.addActivity(this);
        mVibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);


        resultEnc=(TextView)findViewById(R.id.result_enc);
        zero=(TextView)findViewById(R.id.number_0);
        one=(TextView)findViewById(R.id.number_1);
        two=(TextView)findViewById(R.id.number_2);
        three=(TextView)findViewById(R.id.number_3);
        four=(TextView)findViewById(R.id.number_4);
        five=(TextView)findViewById(R.id.number_5);
        six=(TextView)findViewById(R.id.number_6);
        seven=(TextView)findViewById(R.id.number_7);
        eight=(TextView)findViewById(R.id.number_8);
        nine=(TextView)findViewById(R.id.number_9);
        deleft=(ImageView)findViewById(R.id.pass_delete);

        fingerprint=(ImageView)findViewById(R.id.use_fing);
        fingerprint.setOnClickListener(this);

        passOne=(EditText)findViewById(R.id.pass_1);
        passTwo=(EditText)findViewById(R.id.pass_2);
        passThree=(EditText)findViewById(R.id.pass_3);
        passFour=(EditText)findViewById(R.id.pass_4);

        passOne.setFocusable(false);
        passTwo.setFocusable(false);
        passThree.setFocusable(false);
        passFour.setFocusable(false);

        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        deleft.setOnClickListener(this);
    }

    private void setWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 设置竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.number_0:
                putPass("0");
                break;
            case R.id.number_1:
                putPass("1");
                break;
            case R.id.number_2:
                putPass("2");
                break;
            case R.id.number_3:
                putPass("3");
                break;
            case R.id.number_4:
                putPass("4");
                break;
            case R.id.number_5:
                putPass("5");
                break;
            case R.id.number_6:
                putPass("6");
                break;
            case R.id.number_7:
                putPass("7");
                break;
            case R.id.number_8:
                putPass("8");
                break;
            case R.id.number_9:
                putPass("9");
                break;
            case R.id.pass_delete:
                deleftPass();
                break;
            case R.id.use_fing:
                finish();
                ActivityCollector.removeActivity(this);
                Intent intent=new Intent(Encryption.this,Fingerprint.class);
                startActivity(intent);
            default:break;
        }
    }

    private void putPass(String number){
        switch (id){
            case 1:
                passOne.setText(number);
                passWord=passWord+number;
                id++;
                break;
            case 2:
                passTwo.setText(number);
                passWord=passWord+number;
                id++;
                break;
            case 3:
                passThree.setText(number);
                passWord=passWord+number;
                id++;
                break;
            case 4:
                passFour.setText(number);
                passWord=passWord+number;
                id++;
                break;
            default:break;
        }

        if (id>4){
            if (passWord.equals(MyApplication.getPeople().getPassWord())){
                resultEnc.setText("密码正确！");
                finish();
                ActivityCollector.removeActivity(this);
                Intent intent=new Intent(Encryption.this,Diary.class);
                startActivity(intent);
            }else {
                if (mVibrator != null) {
                    mVibrator.vibrate(800);
                }
                resultEnc.setText("密码错误！");
                id=1;
                passWord="";
                passOne.setText("");
                passTwo.setText("");
                passThree.setText("");
                passFour.setText("");
            }
        }
    }

    private void deleftPass(){
        if (passWord.isEmpty()){
            return;
        }else {
            passWord=passWord.substring(0,passWord.length()-1);
            id--;
            switch (id){
                case 1:passOne.setText("");break;
                case 2:passTwo.setText("");break;
                case 3:passThree.setText("");break;
                case 4:passFour.setText("");break;
                default:break;
            }
        }

    }
}
