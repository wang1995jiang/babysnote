package com.jpeng.demo.add;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MainActivity;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.NoteTool;
import com.jpeng.demo.R;
import com.jpeng.demo.notes.Note;

import java.util.Date;

public class AddEvent extends AppCompatActivity implements View.OnClickListener {

    Dialog dialog;
    RelativeLayout toolbarLin;
    LinearLayout back;
    EditText editText;
    Button button;
    int labelID=1;
    RadioButton tourism,personal,life;
    TextView title;
    TextView time;
    TextView labelText;
    ImageView save;
    ImageView label;
    boolean isAdd;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ActivityCollector.addActivity(this);

        Intent intent=getIntent();
        isAdd=intent.getBooleanExtra("isAdd",false);

        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);

        time=(TextView)findViewById(R.id.time_event);
        Date date = new Date();
        time.setText(date.toLocaleString());
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        labelText=(TextView)findViewById(R.id.label_text);
        label=(ImageView)findViewById(R.id.label_event);
        label.setOnClickListener(this);
        button=(Button)findViewById(R.id.button_event);
        button.setOnClickListener(this);
        editText=(EditText)findViewById(R.id.content_event);

        if (isAdd){
            title.setText("新建记事");

        }else {
            title.setText("记事详情");
            time.setText(NoteTool.note.getCreateTime());
            labelText.setText(NoteTool.note.getLabelNote());
            label.setImageResource(R.drawable.icn_label_two);
            editText.setText(NoteTool.note.getContentNote());
        }
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
            case R.id.button_event:
                Date date = new Date();
                if (isAdd){
                    Note note=new Note();
                    note.setCreateTime(date.toLocaleString());
                    note.setLabelNote(labelText.getText().toString());
                    note.setContentNote(editText.getText().toString());
                    note.save();
                    Intent intent=new Intent("com.jpeng.demo.ADDSUCCESSNOTE");
                    localBroadcastManager.sendBroadcast(intent);
                    Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                }else {
                    NoteTool.note.setCreateTime(date.toLocaleString());
                    NoteTool.note.setLabelNote(labelText.getText().toString());
                    NoteTool.note.setContentNote(editText.getText().toString());
                    NoteTool.note.save();
                    Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();

                    NoteTool.isRevise=true;
                }

                finish();
                ActivityCollector.removeActivity(this);
                break;
            default:break;
        }
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
        dialog.dismiss();
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
    protected void onDestroy() {
        super.onDestroy();
        finish();
        ActivityCollector.removeActivity(this);
    }
}
