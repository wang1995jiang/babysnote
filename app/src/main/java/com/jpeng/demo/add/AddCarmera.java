package com.jpeng.demo.add;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.CarmeraAndGall;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;
import com.jpeng.demo.notes.Carmera;

import java.util.Date;

public class AddCarmera extends CarmeraAndGall implements View.OnClickListener {

    RelativeLayout toolbarLin;
    LinearLayout back;
    TextView title,time,labelText,address;
    ImageView label,addCarmear,save;
    RadioButton tourism,personal,life;
    Dialog dialog;
    int labelID=1;
    EditText moodText;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carmera);

        ActivityCollector.addActivity(this);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);

        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("新建拍摄");
        save=(ImageView)findViewById(R.id.save_toolbar);
        save.setImageResource(R.drawable.save_white);
        save.setOnClickListener(this);

        time=(TextView)findViewById(R.id.time_event);
        Date date = new Date();
        time.setText(date.toLocaleString());
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        labelText=(TextView)findViewById(R.id.label_text);
        label=(ImageView)findViewById(R.id.label_event);
        label.setOnClickListener(this);
        addCarmear=(ImageView)findViewById(R.id.add_camera);
        addCarmear.setOnClickListener(this);
        moodText=(EditText)findViewById(R.id.mood_carmera);
        address=(TextView)findViewById(R.id.address_carmera);
        address.setText(MyApplication.getPeople().getAddress());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.label_event:
                createLebelDialog();
                break;
            case R.id.tourism_label:
                dialogRadio(1);
                break;
            case R.id.personal_label:
                dialogRadio(2);
                break;
            case R.id.life_label:
                dialogRadio(3);
                break;
            case R.id.add_camera:
                goCarmera();
                break;
            case R.id.save_toolbar:
                saveCarmear();
                Intent intent=new Intent("com.jpeng.demo.ADDSUCCESSCARMEAR");
                localBroadcastManager.sendBroadcast(intent);
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                finish();
                ActivityCollector.removeActivity(this);
                break;
            default:break;
        }
    }

    private void saveCarmear(){
        Date date = new Date();
        Carmera carmera=new Carmera();

        carmera.setCreateTime(date.toLocaleString());
        carmera.setLabelCarmera(labelText.getText().toString());
        carmera.setPathCarmera(path);
        carmera.setMoodCarmera(moodText.getText().toString());
        carmera.setAddressCarmera(MyApplication.getPeople().getAddress());

        carmera.save();
    }

    private void dialogRadio(int id){
        labelID=id;
        label.setImageResource(R.drawable.icn_label_two);
        switch (id){
            case 1:labelText.setText("旅游");break;
            case 2:labelText.setText("个人");break;
            case 3:labelText.setText("生活");break;
            default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void createLebelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.label_event_layout, null);

        tourism=(RadioButton)v.findViewById(R.id.tourism_label);
        personal=(RadioButton)v.findViewById(R.id.personal_label);
        life=(RadioButton)v.findViewById(R.id.life_label);
        tourism.setOnClickListener(this);
        personal.setOnClickListener(this);
        life.setOnClickListener(this);

        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).load(path).placeholder(R.drawable.add_camera).into(addCarmear);
    }
}
