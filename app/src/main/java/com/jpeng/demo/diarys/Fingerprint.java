package com.jpeng.demo.diarys;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.R;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class Fingerprint extends FragmentActivity {
    boolean isClose=false;
    Activity activity;
    ImageView imageView;
    FingerprintManager manager;
    KeyguardManager mKeyManager;
    private final static int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0;
    private final static String TAG = "finger_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_fingerprint);
        ActivityCollector.addActivity(this);
        activity=this;
        manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        mKeyManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        imageView=(ImageView)findViewById(R.id.finger_id);
        if (isFinger()) {
            Toast.makeText(Fingerprint.this, "请进行指纹识别", Toast.LENGTH_LONG).show();
            Log(TAG, "keyi");
            startListening(null);
        }
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

    public boolean isFinger() {

        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "有指纹权限");
        //判断硬件是否支持指纹识别
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!manager.isHardwareDetected()) {
                Toast.makeText(this, "没有指纹识别模块", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Log(TAG, "有指纹模块");
        //判断 是否开启锁屏密码

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!mKeyManager.isKeyguardSecure()) {
                Toast.makeText(this, "没有开启锁屏密码", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Log(TAG, "已开启锁屏密码");
        //判断是否有指纹录入
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!manager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "没有录入指纹", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Log(TAG, "已录入指纹");

        return true;
    }

    CancellationSignal mCancellationSignal = new CancellationSignal();
    //回调方法
    FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
            Toast.makeText(Fingerprint.this, errString, Toast.LENGTH_SHORT).show();
            showAuthenticationScreen();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

            Toast.makeText(Fingerprint.this, helpString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

            Toast.makeText(Fingerprint.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.drawable.fingerprint_fill);
            finish();
            ActivityCollector.removeActivity(activity);
            Intent intent=new Intent(Fingerprint.this,Diary.class);
            startActivity(intent);

        }

        @Override
        public void onAuthenticationFailed() {
            Toast.makeText(Fingerprint.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
        }
    };


    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, mCancellationSignal, 0, mSelfCancelled, null);
        }


    }

    /**
     * 锁屏密码
     */
    private void showAuthenticationScreen() {

        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            intent = mKeyManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
        }
        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "识别成功", Toast.LENGTH_SHORT).show();
                finish();
                ActivityCollector.removeActivity(activity);
                Intent intent=new Intent(Fingerprint.this,Diary.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
                finish();
                ActivityCollector.removeActivity(activity);
            }
        }
    }

    private void Log(String tag, String msg) {
        if (isClose){
            Log.d(tag, msg);
        }

    }
}
