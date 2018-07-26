package com.jpeng.demo.add;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.CarmeraAndGall;
import com.jpeng.demo.Image;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.NoteTool;
import com.jpeng.demo.R;
import com.jpeng.demo.TextChange;
import com.jpeng.demo.diarys.Music;
import com.jpeng.demo.notes.Learn;
import com.jpeng.demo.notes.Note;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LearningNotes extends CarmeraAndGall implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    RelativeLayout toolbarLin;
    EditText inContent;
    Dialog dialog,dialog1,dialog2;
    LinearLayout back,linearLayout;
    TextView titleText;
    TextView title;
    ImageView save;
    TextView time;
    TextView labelText;
    ImageView label;
    List<String> labels=new ArrayList<>();
    ImageView add,galleryOpen,carmaOpen,video,music;
    View gallery,carma,videoView,musicView;
    boolean isOpen=true,isAdd;
    private InputMethodManager mInputMethodManager;
    int musicId=-1,videoId=-1;
    MediaPlayer mediaPlayer=new MediaPlayer();
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_notes);

        final Context context=this;

        Intent intent=getIntent();
        isAdd=intent.getBooleanExtra("isAdd",false);

        cancel();

        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        ActivityCollector.addActivity(this);

        MediaScannerConnection.scanFile(this, new String[] { Environment.getExternalStorageDirectory().getAbsolutePath() }, null, null);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);

        if (NoteTool.getMusics().size()==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NoteTool.scanAllAudioFiles(context);
                }
            }).start();
        }

        if (NoteTool.getVideoInfos().size()==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NoteTool.getVideoFile(NoteTool.getVideoInfos(),Environment.getExternalStorageDirectory());// 获得视频文件
                }
            }).start();
        }

        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);
        save=(ImageView)findViewById(R.id.save_toolbar);
        save.setImageResource(R.drawable.save_white);
        save.setOnClickListener(this);
        time=(TextView)findViewById(R.id.time_event);
        Date date = new Date();
        time.setText(date.toLocaleString());
        titleText=(TextView)findViewById(R.id.title_text);
        titleText.setOnClickListener(this);
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        labelText=(TextView)findViewById(R.id.label_text);
        label=(ImageView)findViewById(R.id.label_event);
        label.setOnClickListener(this);

        if (!MyApplication.getPeople().getLearnLabel().isEmpty()){
            for (String str:MyApplication.getPeople().getLearnLabel().split(",")){
                labels.add(str);
            }
        }

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        add=(ImageView)findViewById(R.id.add);
        galleryOpen=(ImageView) findViewById(R.id.gallery_open);
        carmaOpen=(ImageView) findViewById(R.id.carma_open);
        video=(ImageView) findViewById(R.id.video);
        music=(ImageView) findViewById(R.id.music);
        gallery=(View) findViewById(R.id.openG);
        carma=(View) findViewById(R.id.openC);
        videoView=(View) findViewById(R.id.openV);
        musicView=(View) findViewById(R.id.openM);

        gallery.setVisibility(View.GONE);
        carma.setVisibility(View.GONE);
        galleryOpen.setVisibility(View.GONE);
        carmaOpen.setVisibility(View.GONE);
        video.setVisibility(View.GONE);
        music.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        musicView.setVisibility(View.GONE);

        linearLayout=(LinearLayout)findViewById(R.id.linear);

        add.setOnClickListener(this);
        gallery.setOnClickListener(this);
        carma.setOnClickListener(this);
        videoView.setOnClickListener(this);
        musicView.setOnClickListener(this);

        if (isAdd){
            title.setText("新建笔记");
            linearLayout.addView(getEditext(linearLayout,""));
        }else {
            title.setText("笔记详情");
            detailDisplay();
        }

    }

    private void detailDisplay(){
        String []sigh=NoteTool.learn.getSigh().split("&");
        String []content=NoteTool.learn.getContentLearn().split("&");
        String []picture=NoteTool.learn.getPricesLearn().split(";");
        String []music=NoteTool.learn.getMusicPath().split(";");
        String []video=NoteTool.learn.getVideoPath().split(";");

        List<String> contents= new ArrayList<>(Arrays.asList(content));
        List<String> pictures= new ArrayList<>(Arrays.asList(picture));
        List<String> musics= new ArrayList<>(Arrays.asList(music));
        List<String> videos= new ArrayList<>(Arrays.asList(video));

        time.setText(NoteTool.learn.getCreateTime());
        titleText.setText(NoteTool.learn.getTitle());
        labelText.setText(NoteTool.learn.getLabelLearn());
        if (!NoteTool.learn.getLabelLearn().equals("未选择")){
            label.setImageResource(R.drawable.icn_label_two);
        }


        for (int i=0;i<sigh.length;i++){
            switch (Integer.valueOf(sigh[i])){
                case 0:
                    if (contents.size()==0){
                        linearLayout.addView(getEditext(linearLayout,""));
                    }else {
                        linearLayout.addView(getEditext(linearLayout,contents.get(0)));
                        contents.remove(0);
                    }
                    break;
                case 1:
                    linearLayout.addView(getImageView(linearLayout,pictures.get(0)));
                    pictures.remove(0);
                    break;
                case 2:
                    String []musicInfo=musics.get(0).split(",");
                    Music music1=new Music(0,musicInfo[1],"",musicInfo[2],musicInfo[0],0,null,null);
                    linearLayout.addView(getMusicView(linearLayout,music1,NoteTool.getMusicView().size()));
                    musics.remove(0);
                    break;
                case 3:
                    VideoInfo videoInfo=new VideoInfo();
                    videoInfo.setPath(videos.get(0));
                    videoInfo.setBitmap(NoteTool.getVideoPricute(videos.get(0)));
                    linearLayout.addView(getVideoView(linearLayout,videoInfo,NoteTool.getVideoView().size()));
                    videos.remove(0);
                    break;
                default:break;

            }
        }
    }

    public View getEditext(LinearLayout root,String content){
        View view= LayoutInflater.from(this).inflate(R.layout.editext_layout,root,false);
        EditText editText=(EditText) view.findViewById(R.id.editext);

        if (!content.isEmpty()){
            editText.setText(content);
        }

        editText.setFocusable(true);//设置输入框可聚集
        editText.setFocusableInTouchMode(true);//设置触摸聚焦
        editText.requestFocus();//请求焦点
        editText.findFocus();//获取焦点
        mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 显示输入法

        editText.addTextChangedListener(new TextChange(editText,root));
        editText.setTag(NoteTool.getIdEditext());
        NoteTool.getEdis().add(editText);

        TypeSign typeSign=new TypeSign();
        typeSign.setId(0);
        typeSign.setIdEditext(NoteTool.getIdEditext());
        view.setTag(typeSign);
        NoteTool.getEditTexts().add(view);

        NoteTool.setIdEditext(NoteTool.getIdEditext()+1);
        NoteTool.setNumberE(NoteTool.getNumberE()+1);

        return view;
    }

    public View getImageView(LinearLayout root,String url){
        View view= LayoutInflater.from(this).inflate(R.layout.image_layout,root,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.image);
        Glide.with(this).load(url).into(imageView);

        Image image=new Image(NoteTool.getIdImage(),url);

        TypeSign typeSign=new TypeSign();
        typeSign.setId(1);
        typeSign.setImage(image);
        view.setTag(typeSign);
        NoteTool.getImages().add(view);

        NoteTool.setIdImage(NoteTool.getIdImage()+1);
        NoteTool.setNumberI(NoteTool.getNumberI()+1);
        return view;
    }

    public View getMusicView(LinearLayout root, Music music, int id){
        View view= LayoutInflater.from(this).inflate(R.layout.music_layout,root,false);
        ImageView control=(ImageView) view.findViewById(R.id.control_music);
        TextView name=(TextView) view.findViewById(R.id.name_music);
        TextView singer=(TextView) view.findViewById(R.id.singer_music);

        name.setText(music.getTitle());
        singer.setText(music.getArtist());
        control.setOnClickListener(this);

        control.setTag(id);

        TypeSign typeSign=new TypeSign();
        typeSign.setId(2);
        typeSign.setMusic(music);
        view.setTag(typeSign);

        NoteTool.getMusicView().add(view);

        return view;
    }

    public View getVideoView(LinearLayout root,VideoInfo videoInfo,int id){
        View view= LayoutInflater.from(this).inflate(R.layout.video_layout,root,false);

        View stop=(View) view.findViewById(R.id.stop_video);
        VideoView videoView=(VideoView) view.findViewById(R.id.video);
        ImageView imageView=(ImageView) view.findViewById(R.id.cover_video);
        ImageView play=(ImageView) view.findViewById(R.id.video_play);
        ImageView full=(ImageView) view.findViewById(R.id.full_screen_play);

        videoView.setOnCompletionListener(this);
        imageView.setImageBitmap(videoInfo.getBitmap());

        play.setTag(id);
        stop.setTag(id);
        full.setTag(id);
        full.setOnClickListener(this);
        stop.setOnClickListener(this);
        play.setOnClickListener(this);

        stop.setVisibility(View.GONE);
        full.setVisibility(View.GONE);

        TypeSign typeSign=new TypeSign();
        typeSign.setId(3);
        typeSign.setVideoInfo(videoInfo);
        view.setTag(typeSign);

        NoteTool.getVideoView().add(view);
        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.title_text:
                setTitleTextDialog();
                break;
            case R.id.label_event:
                labelChoiceDialog();
                break;
            case R.id.add:
                setAdd();
                break;
            case R.id.openG:
                goGallery();
                break;
            case R.id.openC:
                goCarmera();
                break;
            case R.id.openV:
                if (NoteTool.getMusics().size()==0){
                    Toast.makeText(this,"还没有搜索到相应的视频文件",Toast.LENGTH_SHORT).show();
                }else {
                    choiceDialog(false);
                }
                break;
            case R.id.openM:
                if (NoteTool.getMusics().size()==0){
                    Toast.makeText(this,"还没有搜索到相应的音乐文件",Toast.LENGTH_SHORT).show();
                }else {
                    choiceDialog(true);
                }

                break;
            case R.id.linear_iteam_music:
                linearLayout.addView(getMusicView(linearLayout,NoteTool.getMusics().get((int) v.getTag()),NoteTool.getMusicView().size()));
                linearLayout.addView(getEditext(linearLayout,""));
                dialog.dismiss();
                break;
            case R.id.rela_video:
                linearLayout.addView(getVideoView(linearLayout,NoteTool.getVideoInfos().get((int) v.getTag()),NoteTool.getVideoView().size()));
                linearLayout.addView(getEditext(linearLayout,""));
                dialog.dismiss();
                break;
            case R.id.control_music:
                startMusic((int) v.getTag());
                break;
            case R.id.stop_video:
            case R.id.video_play:
                startVideo((int) v.getTag());
                break;
            case R.id.full_screen_play:
                setFullPlay((int) v.getTag());
                break;
            case R.id.determine_button:
                titleText.setText(inContent.getText().toString());
                dialog2.dismiss();
                break;
            case R.id.child_radio:
                labelText.setText(labels.get((int) v.getTag()));
                label.setImageResource(R.drawable.icn_label_two);
                dialog1.dismiss();
                break;
            case R.id.save_toolbar:
                saveLearn();
                finish();
                ActivityCollector.removeActivity(this);
                break;
            default:break;
        }
    }

    private void saveLearn(){
        Date date = new Date();

        String content = "",prices="",musicPath="",videoPath="",sign="";
        for (EditText editText:NoteTool.getEdis()){
            content=content+editText.getText().toString()+"&";
        }

        for (View view:NoteTool.getImages()){
            TypeSign typeSign=(TypeSign) view.getTag();
            prices=prices+typeSign.getImage().getPath()+";";
        }


        for (int i=1;i<linearLayout.getChildCount();i++){
            TypeSign typeSign=(TypeSign) linearLayout.getChildAt(i).getTag();
            switch (typeSign.getId()){
                case 2:
                    Music music=typeSign.getMusic();
                    musicPath=musicPath+typeSign.getMusic().getUrl()+","+music.getTitle()+","+music.getArtist()+";";
                    break;
                case 3:
                    VideoInfo videoInfo=typeSign.getVideoInfo();
                    videoPath=videoPath+videoInfo.getPath()+";";
                    break;
                default:break;
            }
            sign=sign+String.valueOf(typeSign.getId())+"&";
        }

        if (!sign.isEmpty()){
            sign=sign.substring(0,sign.length()-1);
        }
        if (!content.isEmpty()){
            content=content.substring(0,content.length()-1);
        }
        if (!musicPath.isEmpty()){
            musicPath=musicPath.substring(0,musicPath.length()-1);
        }
        if (!videoPath.isEmpty()){
            videoPath=videoPath.substring(0,videoPath.length()-1);
        }
        if (!prices.isEmpty()){
            prices=prices.substring(0,prices.length()-1);
        }

        if (isAdd){
            Learn learn=new Learn();

            learn.setCreateTime(date.toLocaleString());
            learn.setLabelLearn(labelText.getText().toString());
            learn.setTitle(titleText.getText().toString());
            learn.setContentLearn(content);
            learn.setPricesLearn(prices);
            learn.setMusicPath(musicPath);
            learn.setVideoPath(videoPath);
            learn.setSigh(sign);

            learn.save();
            Intent intent=new Intent("com.jpeng.demo.ADDSUCCESSLEARN");
            localBroadcastManager.sendBroadcast(intent);

            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        }else {
            NoteTool.learn.setCreateTime(date.toLocaleString());
            NoteTool.learn.setLabelLearn(labelText.getText().toString());
            NoteTool.learn.setTitle(titleText.getText().toString());
            NoteTool.learn.setContentLearn(content);
            NoteTool.learn.setPricesLearn(prices);
            NoteTool.learn.setMusicPath(musicPath);
            NoteTool.learn.setVideoPath(videoPath);
            NoteTool.learn.setSigh(sign);

            NoteTool.learn.save();
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();

            NoteTool.isRevise=true;
        }

    }

    public void cancel(){
        NoteTool.setIdEditext(0);
        NoteTool.setIdImage(0);
        NoteTool.setNumberE(0);
        NoteTool.setNumberI(0);

        if (NoteTool.getEditTexts().size()>0){
            NoteTool.getEditTexts().removeAll(NoteTool.getEditTexts());
        }
        if (NoteTool.getImages().size()>0){
            NoteTool.getImages().removeAll(NoteTool.getImages());
        }
        if (NoteTool.getEdis().size()>0){
            NoteTool.getEdis().removeAll(NoteTool.getEdis());
        }
        if (NoteTool.getMusics().size()>0){
            NoteTool.getMusics().removeAll(NoteTool.getMusics());
        }
        if (NoteTool.getVideoInfos().size()>0){
            NoteTool.getVideoInfos().removeAll(NoteTool.getVideoInfos());
        }
        if (NoteTool.getMusicView().size()>0){
            NoteTool.getMusicView().removeAll(NoteTool.getMusicView());
        }
        if (NoteTool.getVideoView().size()>0){
            NoteTool.getVideoView().removeAll(NoteTool.getVideoView());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!path.isEmpty()){
            linearLayout.addView(getImageView(linearLayout,path));
            linearLayout.addView(getEditext(linearLayout,""));
            path="";
        }
    }

    private void setAdd(){
        if (isOpen){
            galleryOpen.setVisibility(View.VISIBLE);
            carmaOpen.setVisibility(View.VISIBLE);
            gallery.setVisibility(View.VISIBLE);
            carma.setVisibility(View.VISIBLE);
            video.setVisibility(View.VISIBLE);
            music.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            musicView.setVisibility(View.VISIBLE);
            NoteTool.setTranslateAnimation(galleryOpen,carmaOpen,video,music,add);
            isOpen=false;
        }else {
            galleryOpen.setVisibility(View.GONE);
            carmaOpen.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
            carma.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            music.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            musicView.setVisibility(View.GONE);

            NoteTool.clearAnimate(galleryOpen);
            NoteTool.clearAnimate(carmaOpen);
            NoteTool.clearAnimate(video);
            NoteTool.clearAnimate(music);
            NoteTool.clearAnimate(add);

            isOpen=true;
        }
    }

    private void setFullPlay(int id){
        TypeSign typeSign=(TypeSign)  NoteTool.getVideoView().get(id).getTag();
        String videoUrl=typeSign.getVideoInfo().getPath();
        Intent openVideo = new Intent(Intent.ACTION_VIEW);
        openVideo.setDataAndType(Uri.parse(videoUrl), "video/*");
        startActivityForResult(openVideo,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){

                    if (Build.VERSION.SDK_INT>=19){
                        path= handleImageOnKitKat(photoUri);

                    }else {
                        path=handleImageBeforeKitKat(photoUri);

                    }
                }
                break;
            case 2:
                if (resultCode==RESULT_OK){
                    photoUri=data.getData();
                    if (Build.VERSION.SDK_INT>=19){
                        path=handleImageOnKitKat(photoUri);

                    }else {
                        path=handleImageBeforeKitKat(photoUri);

                    }

                }
                break;
            case 3:
                if (resultCode==RESULT_OK){
                    handleFrontVideo();
                }
            default:break;

        }
    }

    private void handleFrontVideo()
    {
        View viewFront=NoteTool.getVideoView().get(videoId);
        ImageView playFront=(ImageView) viewFront.findViewById(R.id.video_play);
        ImageView coverFront=(ImageView) viewFront.findViewById(R.id.cover_video);
        VideoView videoViewFront=(VideoView) viewFront.findViewById(R.id.video);
        View stopFront=(View) viewFront.findViewById(R.id.stop_video);
        ImageView fullFront=(ImageView) viewFront.findViewById(R.id.full_screen_play);

        videoViewFront.suspend();
        playFront.setVisibility(View.VISIBLE);
        coverFront.setVisibility(View.VISIBLE);
        stopFront.setVisibility(View.GONE);
        fullFront.setVisibility(View.GONE);
    }
    private void startMusic(int id){
        View view=NoteTool.getMusicView().get(id);
        ImageView imageView=(ImageView) view.findViewById(R.id.control_music);

        TypeSign typeSign=(TypeSign) view.getTag();
        Music music=typeSign.getMusic();

        if (musicId==-1){
            imageView.setImageResource(R.drawable.stop_music);
            prepareMusic(music.getUrl());
            mediaPlayer.start();
        }else {
            if (musicId==id){
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imageView.setImageResource(R.drawable.play_music);
                }else {
                    mediaPlayer.start();
                    imageView.setImageResource(R.drawable.stop_music);
                }
            }else {
                mediaPlayer.reset();
                prepareMusic(music.getUrl());
                mediaPlayer.start();
                imageView.setImageResource(R.drawable.stop_music);
                ImageView imageView1=(ImageView) NoteTool.getMusicView().get(musicId).findViewById(R.id.control_music);
                imageView1.setImageResource(R.drawable.play_music);
            }
        }
        musicId=id;

    }

    private void startVideo(int id){
        View view=NoteTool.getVideoView().get(id);
        View stop=(View) view.findViewById(R.id.stop_video);
        ImageView full=(ImageView) view.findViewById(R.id.full_screen_play);
        ImageView play=(ImageView) view.findViewById(R.id.video_play);
        ImageView cover=(ImageView) view.findViewById(R.id.cover_video);
        VideoView videoView=(VideoView)view.findViewById(R.id.video);
        TypeSign typeSign=(TypeSign) view.getTag();
        VideoInfo videoInfo=typeSign.getVideoInfo();

        if (videoId==-1){
            videoView.setVideoPath(videoInfo.getPath());
            play.setVisibility(View.GONE);
            cover.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            full.setVisibility(View.VISIBLE);
            videoView.start();
        }else {
            if (videoId==id){
                if (videoView.isPlaying()){
                    videoView.pause();
                    play.setVisibility(View.VISIBLE);
                    cover.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.GONE);
                    full.setVisibility(View.GONE);
                }else {
                    videoView.start();

                    play.setVisibility(View.GONE);
                    cover.setVisibility(View.GONE);
                    stop.setVisibility(View.VISIBLE);
                    full.setVisibility(View.VISIBLE);
                }
            }else {
                handleFrontVideo();

                videoView.setVideoPath(videoInfo.getPath());
                play.setVisibility(View.GONE);
                cover.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                full.setVisibility(View.VISIBLE);
                videoView.start();
            }
        }

        videoId=id;

    }

    private void prepareMusic(String url){
        File file=new File(url);
        try {
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void labelChoiceDialog() {
        dialog1=new Dialog(this,R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.learn_label_choice, null);
        LinearLayout linearLayout=(LinearLayout)inflate.findViewById(R.id.linear_choice);
        int i=0;
        if (labels.size()>0){
            for (String str:labels){
                linearLayout.addView(getChildView(str,i));
                i++;
            }
        }else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(this);

            layoutParams.setMargins(0, 5, 10, 5);
            textView.setTextSize(20);
            textView.setText("你还没有一个笔记标签~请去添加一个");
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(textView);
        }
        //将布局设置给Dialog
        dialog1.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog1.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog1.show();//显示对话框
    }

    private View getChildView(String strName,int id){
        View view= LayoutInflater.from(this).inflate(R.layout.child_learn_choice,null);
        RadioButton radioButton=(RadioButton)view.findViewById(R.id.child_radio);
        radioButton.setText(strName);
        radioButton.setTag(id);
        radioButton.setOnClickListener(this);

        return view;
    }

    private void setTitleTextDialog() {
        dialog2 = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.in_content, null);
        //初始化控件
        TextView titleContent=(TextView)inflate.findViewById(R.id.title_content);
        inContent=(EditText)inflate.findViewById(R.id.in_content);
        Button determineButton=(Button)inflate.findViewById(R.id.determine_button);
        titleContent.setText("设置标题");
        determineButton.setOnClickListener(this);
        //将布局设置给Dialog
        dialog2.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog2.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog2.show();//显示对话框
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void choiceDialog(boolean isMusic){
        dialog=new Dialog(this,R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.all_music_create, null);

        LinearLayout linearLayout=(LinearLayout) view.findViewById(R.id.linear_create);
        int i=0;

        if (isMusic){
            for (Music music:NoteTool.getMusics()){
                linearLayout.addView(NoteTool.getIteamMusic(this,linearLayout,music,i,this));
                i++;
            }
        }else {
            for (VideoInfo videoInfo:NoteTool.getVideoInfos()){
                linearLayout.addView(NoteTool.getIteamVideo(this,linearLayout,videoInfo,i,this));
                i++;
            }
        }

        //将布局设置给Dialog
        dialog.setContentView(view);
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
    public void onCompletion(MediaPlayer mp) {
        View view=NoteTool.getVideoView().get(videoId);
        ImageView play=(ImageView) view.findViewById(R.id.video_play);
        ImageView cover=(ImageView) view.findViewById(R.id.cover_video);
        VideoView videoView=(VideoView)view.findViewById(R.id.video);

        play.setVisibility(View.VISIBLE);
        cover.setVisibility(View.VISIBLE);
        videoView.suspend();
    }
}
