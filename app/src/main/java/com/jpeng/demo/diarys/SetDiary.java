package com.jpeng.demo.diarys;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王将 on 2018/3/27.
 */

public class SetDiary extends AppCompatActivity implements View.OnClickListener {
    boolean isCheM=false,isCheE=false,iserro=false;
    ImageView imagePlay;
    private LocalBroadcastManager localBroadcastManager;
    RelativeLayout relativeLayout,animationRela,toolbarLin;
    Dialog dialog;
    LinearLayout back;
    TextView title,textMusic,textEncryption,textChange,chMusicTitle,showText,deleteAllDiary;
    Switch music,encryption;
    List<Music> musicList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_diary);

        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        ActivityCollector.addActivity(this);
        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);
        title.setText("日记设置");
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        imagePlay=(ImageView)findViewById(R.id.play_toolbar);
        imagePlay.setVisibility(View.GONE);

        textMusic=(TextView)findViewById(R.id.text_music);
        textEncryption=(TextView)findViewById(R.id.text_encryption);
        relativeLayout=(RelativeLayout)findViewById(R.id.show_music);
        animationRela=(RelativeLayout)findViewById(R.id.animation_type);
        textChange=(TextView)findViewById(R.id.music_change);
        chMusicTitle=(TextView)findViewById(R.id.choice_music_title);
        showText=(TextView)findViewById(R.id.animation_show_text);
        deleteAllDiary=(TextView) findViewById(R.id.delete_all_diary);

        deleteAllDiary.setOnClickListener(this);
        textChange.setOnClickListener(this);
        animationRela.setOnClickListener(this);

        setShowText(MyApplication.getPeople().getDiaryAnimation());
        music=(Switch)findViewById(R.id.switch_music);
        encryption=(Switch)findViewById(R.id.switch_encryption);

        if (!MyApplication.getPeople().getMusicTitle().isEmpty()){
            chMusicTitle.setText(MyApplication.getPeople().getMusicTitle());
        }
        if (MyApplication.getPeople().isMusic()){
            relativeLayout.setVisibility(View.VISIBLE);
        }else {
            relativeLayout.setVisibility(View.GONE);
        }
        music.setChecked(MyApplication.getPeople().isMusic());

        encryption.setChecked(MyApplication.getPeople().isEncryption());

        music.setOnClickListener(this);
        encryption.setOnClickListener(this);

    }

    private void openEncryption(final boolean isOpen) {
        dialog=new Dialog(this,R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.in_content, null);
        TextView title=(TextView)inflate.findViewById(R.id.title_content);
        final EditText editText=(EditText)inflate.findViewById(R.id.in_content);
        final Button button=(Button)inflate.findViewById(R.id.determine_button);

        if (isOpen){
            title.setText("请设置访问密码");
        }else {
            title.setText("请输入访问密码");
        }

        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>4){
                    button.setEnabled(false);
                    Toast.makeText(MyApplication.getContext(),"密码只能输入四位！",Toast.LENGTH_SHORT).show();
                }else {
                    if (s.toString().length()==4){
                        button.setEnabled(true);
                    }else {
                        button.setEnabled(false);
                    }
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen){
                    MyApplication.getPeople().setPassWord(editText.getText().toString());
                    isCheE=true;
                    saveSet(2,"passWord",editText.getText().toString(),0,false);
                    Toast.makeText(MyApplication.getContext(),"访问密码设置成功！",Toast.LENGTH_SHORT).show();
                    nextOpenDialog(false);
                }else {
                    if (editText.getText().toString().equals(MyApplication.getPeople().getPassWord())){
                        iserro=false;
                        Toast.makeText(MyApplication.getContext(),"已关闭隐私保护！",Toast.LENGTH_SHORT).show();
                    }else {
                        iserro=true;
                        Toast.makeText(MyApplication.getContext(),"访问密码输入错误！",Toast.LENGTH_SHORT).show();
                    }
                    nextCloseDialog(false);
                }
                dialog.dismiss();
            }
        });

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

    private void choiceMusicDialog(){
        dialog=new Dialog(this,R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.all_music, null);

        RecyclerView recyclerView=(RecyclerView)inflate.findViewById(R.id.recycler_music);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MusicAdapter musicAdapter=new MusicAdapter(musicList,this);
        recyclerView.setAdapter(musicAdapter);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.music_change:
                if (musicList.size()==0){
                    scanAllAudioFiles();
                }
                choiceMusicDialog();
                break;
            case R.id.music_id:
                isCheM=true;
                nextOpenDialog(true);
                int id=(int)v.getTag();
                changeUi(id);
                dialog.dismiss();
                break;
            case R.id.switch_music:
                if (MyApplication.getPeople().isMusic()){
                    nextCloseDialog(true);
                }else {
                    scanAllAudioFiles();
                    choiceMusicDialog();
                    nextOpenDialog(true);
                }
                break;
            case R.id.switch_encryption:
                if (MyApplication.getPeople().isEncryption()){
                    openEncryption(false);
                }else {
                    openEncryption(true);
                    nextOpenDialog(false);
                }
                break;
            case R.id.animation_type:
                creatAnimationDialog();
                break;
            case R.id.delete_all_diary:
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("说明");
                dialog.setMessage("此操作将会删除所有的日记数据，请确定是否继续？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(DiaryEntity.class);
                        Intent intent=new Intent("com.jpeng.demo.DELETEALLDIARY");
                        localBroadcastManager.sendBroadcast(intent);
                        Toast.makeText(MyApplication.getContext(),"日记已全部删除",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            default:break;
        }
    }

    private void creatAnimationDialog(){
        // 创建数据
        final String[] items = new String[] { "普通", "缩放", "旋转", "平移" };
        // 创建对话框构建器
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("选择动画类型")
                .setSingleChoiceItems(items, MyApplication.getPeople().getDiaryAnimation(), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MyApplication.getContext(), items[which],
                                Toast.LENGTH_SHORT).show();
                        MyApplication.getPeople().setDiaryAnimation(which);
                        saveSet(3,"diaryAnimation","",which,false);
                        setShowText(which);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void setShowText(int id){
        switch (id){
            case 0:showText.setText("普通");break;
            case 1:showText.setText("缩放");break;
            case 2:showText.setText("旋转");break;
            case 3:showText.setText("平移");break;
        }
    }

    private void nextOpenDialog(boolean isMusic){
        if (isMusic){
            if (isCheM){
                textMusic.setText("关闭背景音乐");
                saveSet(1,"isMusic","",0,true);
                MyApplication.getPeople().setMusic(true);
                music.setChecked(true);
            }else {
                music.setChecked(false);
            }
        }else {
            if (isCheE){
                textEncryption.setText("关闭隐私保护");
                saveSet(1,"isEncryption","",0,true);
                MyApplication.getPeople().setEncryption(true);
                encryption.setChecked(true);
            }else {
                encryption.setChecked(false);
            }
        }
    }

    private void nextCloseDialog(boolean isMusic){
        if (isMusic){
            textMusic.setText("开启背景音乐");
            relativeLayout.setVisibility(View.GONE);
            saveSet(1,"isMusic","",0,false);
            saveSet(2,"musicUrl","",0,false);
            MyApplication.getPeople().setMusic(false);
            MyApplication.getPeople().setMusicUrl("");
            Intent intent=new Intent("com.jpeng.demo.CLOSEMUSIC");
            localBroadcastManager.sendBroadcast(intent);
            music.setChecked(false);
            isCheM=false;
        }else {
            if (iserro){
                encryption.setChecked(true);
            }else {
                textEncryption.setText("开启隐私保护");
                saveSet(1,"isEncryption","",0,false);
                saveSet(2,"passWord","",0,false);
                MyApplication.getPeople().setEncryption(false);
                encryption.setChecked(false);
                isCheE=false;
            }
        }
    }

    public void changeUi(int id){
        relativeLayout.setVisibility(View.VISIBLE);
        chMusicTitle.setText("已设置背景音乐："+musicList.get(id).getTitle());
        saveSet(2,"musicUrl",musicList.get(id).getUrl(),0,false);
        saveSet(2,"musicTitle",musicList.get(id).getTitle(),0,false);
        MyApplication.getPeople().setMusicUrl(musicList.get(id).getUrl());
        MyApplication.getPeople().setMusicTitle(musicList.get(id).getTitle());
        Intent intent=new Intent("com.jpeng.demo.PLAYMUSIC");
        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        ActivityCollector.removeActivity(this);
    }

    public void scanAllAudioFiles() {
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (size > 1024 * 800) {//大于800K
                    //歌曲编号
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    //歌曲标题
                    String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                    String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                    int album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    Music music = new Music(id, tilte, album, artist, url, duration, size,getArtwork(this,url));
                    musicList.add(music);
                }
                cursor.moveToNext();
            }
        }
    }

    public Bitmap getArtwork(Context context, String url) {
        Uri selectedAudio = Uri.parse(url);
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
        myRetriever.setDataSource(context, selectedAudio); // the URI of audio file
        byte[] artwork;

        artwork = myRetriever.getEmbeddedPicture();

        if (artwork != null) {
            Bitmap bMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);

            return bMap;
        } else {

            return BitmapFactory.decodeResource(context.getResources(), R.drawable.no_picture);
        }
    }

    private void saveSet(int isStr,String key,String url,int which,boolean is){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        switch(isStr){
            case 1:
                editor.putBoolean(key,is);break;
            case 2:
                editor.putString(key,url);break;
            case 3:
                editor.putInt(key,which);break;
            default:break;
        }
        editor.apply();
    }
}
