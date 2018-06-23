package com.jpeng.demo.set;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LearnLabel extends AppCompatActivity implements View.OnClickListener {
    String strLabels="";
    View viewBrush;
    boolean isBrush=true;
    Button addLearnLabel;
    RelativeLayout toolbarLin;
    TextView title;
    TextView stateLabelshow;
    LinearLayout linearLearnLabel,back;
    List <String> labels = new ArrayList<>();
    Dialog dialog;
    int numberLabel=0;
    EditText inContent;
    ImageView saveLabel,imagePlay;
    List <View> views=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_label);

        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        ActivityCollector.addActivity(this);

        back=(LinearLayout)findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("笔记标签");
        saveLabel=(ImageView)findViewById(R.id.save_toolbar);
        saveLabel.setImageResource(R.drawable.save_white);
        saveLabel.setOnClickListener(this);
        imagePlay=(ImageView)findViewById(R.id.play_toolbar);
        imagePlay.setVisibility(View.GONE);

        linearLearnLabel=(LinearLayout)findViewById(R.id.linear_learn_label);
        addLearnLabel=(Button)findViewById(R.id.add_learn_label);
        addLearnLabel.setOnClickListener(this);
        stateLabelshow=(TextView)findViewById(R.id.state_labelshow);
        if (!MyApplication.getPeople().getLearnLabel().isEmpty()){

            for (String str:MyApplication.getPeople().getLearnLabel().split(",")){
                labels.add(str);
                linearLearnLabel.addView(getLabelView(numberLabel,str));
                numberLabel++;
            }
            stateLabelshow.setText("你共有"+labels.size()+"条标签~");

        }else {
            stateLabelshow.setText("你还没有添加标签哦~快去添加一个");
        }
    }

    private View getLabelView(int i,String label){
        View view= LayoutInflater.from(this).inflate(R.layout.label_show_layout,null);
        TextView labelShow=(TextView)view.findViewById(R.id.label_show);
        labelShow.setText(label);
        ImageView labelBrush=(ImageView)view.findViewById(R.id.label_brush);
        ImageView labelDelete=(ImageView)view.findViewById(R.id.label_delete);
        labelBrush.setOnClickListener(this);
        labelDelete.setOnClickListener(this);
        view.setTag(i);
        labelBrush.setTag(view);
        labelDelete.setTag(view);
        views.add(view);
        return view;
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
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.label_brush:
                isBrush=true;
                viewBrush=(View)v.getTag();
                setLabelTextDialog();

                break;
            case R.id.save_toolbar:
                if (labels.size()>0){
                    for (String str:labels){
                        strLabels=strLabels+str+",";
                    }
                    strLabels=strLabels.substring(0,strLabels.length()-1);
                    MyApplication.getPeople().setLearnLabel(strLabels);
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("learnLabel",strLabels);
                    editor.apply();
                    Toast.makeText(this,"已经保存标签~",Toast.LENGTH_SHORT).show();
                }else {
                    MyApplication.getPeople().setLearnLabel("");
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("learnLabel","");
                    editor.apply();
                    Toast.makeText(this,"你已经删除了所有标签~",Toast.LENGTH_SHORT).show();
                }
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.label_delete:
                View view1=(View)v.getTag();
                linearLearnLabel.removeView(view1);
                views.remove((int)view1.getTag());
                labels.remove((int)view1.getTag());
                int i=0;
                for (View view2:views){
                    view2.setTag(i);
                    i++;
                }
                numberLabel--;
                if(numberLabel==0){
                    stateLabelshow.setText("你还没有添加标签哦~快去添加一个");
                }else {
                    stateLabelshow.setText("你共有"+numberLabel+"条标签~");
                }

                break;
            case  R.id.add_learn_label:
                isBrush=false;
                setLabelTextDialog();
                break;
            case R.id.determine_button:
                if (!isBrush){
                    linearLearnLabel.addView(getLabelView(numberLabel,inContent.getText().toString()));

                    labels.add(inContent.getText().toString());
                    numberLabel++;
                    stateLabelshow.setText("你共有"+numberLabel+"条标签~");
                }else {
                    TextView textView=(TextView) viewBrush.findViewById(R.id.label_show);
                    textView.setText(inContent.getText().toString());
                    labels.set((int)viewBrush.getTag(),inContent.getText().toString());
                }
                dialog.dismiss();

            default:break;
        }
    }

    private void setLabelTextDialog() {
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
                if (s.toString().contains(",")){
                    determineButton.setEnabled(false);
                    Snackbar.make(inflate,"不可输入逗号",Snackbar.LENGTH_SHORT).show();
                }else {
                    determineButton.setEnabled(true);
                }
            }
        });
        titleContent.setText("新标签");
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

}
