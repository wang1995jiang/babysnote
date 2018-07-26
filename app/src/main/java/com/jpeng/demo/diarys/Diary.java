package com.jpeng.demo.diarys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.AnimationType;
import com.jpeng.demo.CarmeraAndGall;
import com.jpeng.demo.MainActivity;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Diary extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    public static DiaryEntity diaryEntityClick;
    public static Context context;
    public static List<DiaryEntity> diaryEntities=new ArrayList<>();
    public static List<DiaryEntity> collections=new ArrayList<>();
    public static List<DeleteHandle> deleteHandles=new ArrayList<>();
    public static View.OnClickListener onClickListener;
    public static View.OnLongClickListener onLongClickListener;

    private int count=0;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private IntentFilter intentFilter;
    boolean isPause,isDetail=true;
    MediaPlayer mediaPlayer=new MediaPlayer();
    ImageView imageAll,imageCollection,imageCreate,imageSet,imagePlay;
    RelativeLayout toolbarLin;
    LinearLayout back,fragmentBox,delete,allSelect,cancel;
    TextView title, itemAllDirayOpen, itemCollectionDirayOpen, itemCreateDirayOpen,itemAllDirayClose,itemCollectionDirayClose,itemCreateDirayClose,numberSelect;
    ViewPager vp;
    AllDiary allDiary;
    CollectionDiary collectionDiary;
    CreateDiary createDiary;
    List<Fragment> mFragmentList = new ArrayList<Fragment>();
    FragmentAdapter mFragmentAdapter;

    RelativeLayout itemAllDiray,itemCollectionDiray,itemCreateDiray,deleteBox;
    String[] titles = new String[]{"日记本", "星标日记", "新建日记"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ActivityCollector.addActivity(this);

        context=this;

        diaryEntities= LitePal.findAll(DiaryEntity.class);

        if (diaryEntities.size()>0){
            for (DiaryEntity diaryEntity:diaryEntities){
                if (diaryEntity.isCollection()){
                    collections.add(diaryEntity);
                }
            }
        }

        onClickListener=this;
        onLongClickListener=this;

        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        back=(LinearLayout) findViewById(R.id.back_toolbar);
        back.setOnClickListener(this);
        imageSet=(ImageView)findViewById(R.id.save_toolbar);
        imagePlay=(ImageView)findViewById(R.id.play_toolbar);
        imageSet.setImageResource(R.drawable.ic_settings);
        imagePlay.setImageResource(R.drawable.play);
        imagePlay.setOnClickListener(this);
        imageSet.setOnClickListener(this);
        if (MyApplication.getPeople().isMusic()){
            imagePlay.setVisibility(View.VISIBLE);
            mediaPlayer=new MediaPlayer();
            prepareMusic();
            isPause=true;
        }else {
            imagePlay.setVisibility(View.GONE);
            isPause=false;
        }
        initViews();

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
       // itemAllDiray.setTextColor(Color.parseColor("#66CDAA"));
        itemAllDirayOpen.setAlpha(1);
        itemAllDirayClose.setAlpha(0);

        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPosition = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset>0){
                    if (count==0){
                        itemAllDirayOpen.setAlpha(1-positionOffset);
                        itemAllDirayClose.setAlpha(positionOffset);

                        itemCollectionDirayOpen.setAlpha(positionOffset);
                        itemCollectionDirayClose.setAlpha(1-positionOffset);
                    }
                    if (count==1){
                        if (position<count){
                            itemCollectionDirayOpen.setAlpha(positionOffset);
                            itemCollectionDirayClose.setAlpha(1-positionOffset);

                            itemAllDirayOpen.setAlpha(1-positionOffset);
                            itemAllDirayClose.setAlpha(positionOffset);
                        }else {
                            itemCollectionDirayOpen.setAlpha(1-positionOffset);
                            itemCollectionDirayClose.setAlpha(positionOffset);

                            itemCreateDirayOpen.setAlpha(positionOffset);
                            itemCreateDirayClose.setAlpha(1-positionOffset);
                        }
                    }
                    if (count==2){
                        itemCreateDirayOpen.setAlpha(positionOffset);
                        itemCreateDirayClose.setAlpha(1-positionOffset);

                        itemCollectionDirayOpen.setAlpha(1-positionOffset);
                        itemCollectionDirayClose.setAlpha(positionOffset);
                    }
                }else {
                    setTextAlpha(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                count=position;
                title.setText(titles[position]);
                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });

        //监听音频播放完的代码，实现音频的自动循环播放
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });

        intentFilter=new IntentFilter();
        intentFilter.addAction("com.jpeng.demo.PLAYMUSIC");
        intentFilter.addAction("com.jpeng.demo.CLOSEMUSIC");
        intentFilter.addAction("com.jpeng.demo.ADDDATASUCCESS_DIARY");
        intentFilter.addAction("com.jpeng.demo.UPDATESUCCESS_DIARY");
        intentFilter.addAction("com.jpeng.demo.DELETEALLDIARY");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        ActivityCollector.removeActivity(this);
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    private void changeTextColor(int position) {
        for (int i=0;i<3;i++){
            if (i==position){
                openText(i);
            }else {
                closeText(i);
            }
        }
    }

    private void openText(int id){
        switch (id){
            case 0:
                //itemAllDiray.setTextColor(Color.parseColor("#66CDAA"));
                setAnimation(imageAll,0);
                break;
            case 1:
                //itemCollectionDiray.setTextColor(Color.parseColor("#66CDAA"));
                setAnimation(imageCollection,1);
                break;
            case 2:
                //itemCreateDiray.setTextColor(Color.parseColor("#66CDAA"));
                setAnimation(imageCreate,2);
                break;
            default:break;
        }
    }

    private void setAnimation(ImageView img,int id){
        switch (MyApplication.getPeople().getDiaryAnimation()){
            case 0: AnimationType.setNoType(img,id);break;
            case 1:AnimationType.setScaleType(img,id);break;
            case 2:AnimationType.setRotateType(img,id);break;
            case 3:AnimationType.setTranslateType(img,id);break;
            default:break;
        }
    }

    private void closeText(int id){
        switch (id){
            case 0:
                //itemAllDiray.setTextColor(Color.parseColor("#969696"));
                imageAll.setImageResource(R.drawable.diary_1);
                break;
            case 1:
                //itemCollectionDiray.setTextColor(Color.parseColor("#969696"));
                imageCollection.setImageResource(R.drawable.collection);
                break;
            case 2:
                //itemCreateDiray.setTextColor(Color.parseColor("#969696"));
                imageCreate.setImageResource(R.drawable.write);
                break;
            default:break;
        }
    }

    private void initViews() {
        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        toolbarLin=(RelativeLayout)findViewById(R.id.toolbar_lin);
        fragmentBox=(LinearLayout) findViewById(R.id.fragment_box);
        delete=(LinearLayout) findViewById(R.id.delete_diary);
        allSelect=(LinearLayout) findViewById(R.id.all_select_diary);
        cancel=(LinearLayout) findViewById(R.id.cancel_select_diary);
        deleteBox=(RelativeLayout) findViewById(R.id.delete_operation_box);
        toolbarLin.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        title=(TextView)findViewById(R.id.title_tooolbar);
        numberSelect=(TextView) findViewById(R.id.number_select_diary);
        title.setText("日记本");

        imageAll=(ImageView)findViewById(R.id.all_image);
        imageCollection=(ImageView)findViewById(R.id.collection_image);
        imageCreate=(ImageView)findViewById(R.id.write_image);


        itemAllDiray=(RelativeLayout) findViewById(R.id.item_all_diary);
        itemCollectionDiray=(RelativeLayout) findViewById(R.id.item_collection_diary);
        itemCreateDiray=(RelativeLayout) findViewById(R.id.item_create_diray);

        itemAllDirayOpen=(TextView)findViewById(R.id.item_all_diary_open);
        itemAllDirayClose=(TextView)findViewById(R.id.item_all_diary_close);
        itemCollectionDirayOpen=(TextView)findViewById(R.id.item_collection_diary_open);
        itemCollectionDirayClose=(TextView)findViewById(R.id.item_collection_diary_close);
        itemCreateDirayOpen=(TextView)findViewById(R.id.item_create_diray_open);
        itemCreateDirayClose=(TextView)findViewById(R.id.item_create_diray_close);

        fragmentBox.setVisibility(View.VISIBLE);
        deleteBox.setVisibility(View.GONE);

        itemAllDiray.setOnClickListener(this);
        itemCollectionDiray.setOnClickListener(this);
        itemCreateDiray.setOnClickListener(this);
        delete.setOnClickListener(this);
        allSelect.setOnClickListener(this);
        cancel.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        allDiary=new AllDiary();
        collectionDiary=new CollectionDiary();
        createDiary=new CreateDiary();

        mFragmentList.add(allDiary);
        mFragmentList.add(collectionDiary);
        mFragmentList.add(createDiary);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_all_diary:
                vp.setCurrentItem(0, false);
                break;
            case R.id.item_collection_diary:
                vp.setCurrentItem(1, false);
                break;
            case R.id.item_create_diray:
                vp.setCurrentItem(2, false);
                break;
            case R.id.back_toolbar:
                finish();
                ActivityCollector.removeActivity(this);
                break;
            case R.id.save_toolbar:
                Intent intent=new Intent(Diary.this,SetDiary.class);
                startActivity(intent);
                break;
            case R.id.play_toolbar:
                if (isPause){
                    imagePlay.setImageResource(R.drawable.play);
                    isPause=false;
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                }else {
                    imagePlay.setImageResource(R.drawable.play_fill);
                    isPause=true;
                    if (!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }
                }
                break;
            case R.id.item_diary_star:
                ImageView star=(ImageView) v;
                DiaryEntity diaryEntity=(DiaryEntity) v.getTag();
                setStarImage(diaryEntity,star);
                break;
            case R.id.select_show_diary:
                DeleteHandle deleteHandle=(DeleteHandle) v.getTag();
                ImageView select=(ImageView) v;
                if (deleteHandle.isAscertain()){
                    select.setImageResource(R.drawable.no_select);
                    deleteHandle.setAscertain(false);
                }else {
                    select.setImageResource(R.drawable.yes_select);
                    deleteHandle.setAscertain(true);
                }
                statisticsSelect();
                break;
            case R.id.delete_diary:
                deleteDiary();
                break;
            case R.id.all_select_diary:
                allSelectDelete();
                break;
            case R.id.cancel_select_diary:
                cancelDelete();
                break;
            case R.id.rela_diary:
                if (isDetail){
                    diaryEntityClick=(DiaryEntity) v.getTag();
                    Intent intent1=new Intent(this,DiaryDetails.class);
                    startActivity(intent1);
                }
                break;
            default:break;
        }
    }

    private void deleteDiary(){
        isDetail=true;
        for (DeleteHandle deleteHandle1:deleteHandles){
            if (deleteHandle1.isAscertain()){
                deleteHandle1.getDiaryEntity().delete();
                diaryEntities.remove((DiaryEntity) deleteHandle1.getDiaryEntity());
                if (deleteHandle1.getDiaryEntity().isCollection()){
                    collections.remove((DiaryEntity) deleteHandle1.getDiaryEntity());
                }
            }
        }

        updateDelete();
        allDiary.setToUpdate();
        collectionDiary.setCollectionChange();
        new CancelDeleteBox().execute();
    }

    private void statisticsSelect(){
        int i=0;
        for (DeleteHandle deleteHandle1:deleteHandles){
            if (deleteHandle1.isAscertain()){
                i++;
            }
        }
        numberSelect.setText("已选中："+""+i+"个");
    }

    private void allSelectDelete(){
        for (DeleteHandle deleteHandle1:deleteHandles){
            deleteHandle1.setAscertain(true);
            deleteHandle1.getImageView().setImageResource(R.drawable.yes_select);
            statisticsSelect();
        }
    }

    private void cancelDelete(){
        isDetail=true;
        for (DeleteHandle deleteHandle1:deleteHandles){
            deleteHandle1.getRelativeLayout().setAlpha((float) 1);
            deleteHandle1.getImageView().setImageResource(R.drawable.no_select);
            deleteHandle1.getImageView().setVisibility(View.GONE);
            deleteHandle1.setAscertain(false);
        }

        fragmentBox.setVisibility(View.VISIBLE);
        deleteBox.setVisibility(View.GONE);
    }

    private void setStarImage(DiaryEntity diaryEntity,ImageView star){
        if (diaryEntity.isCollection()){
            star.setImageResource(R.drawable.no_star);
            diaryEntity.setCollection(false);
            collections.remove((DiaryEntity) diaryEntity);
        }else {
            star.setImageResource(R.drawable.yes_star);
            diaryEntity.setCollection(true);
            collections.add(diaryEntity);
        }
        updateDelete();
        collectionDiary.setCollectionChange();
        allDiary.setToUpdate();
        diaryEntity.save();
    }

    private void updateDelete(){
        if (deleteHandles.size()>0){
            deleteHandles.removeAll(deleteHandles);
        }
    }

    private void setTextAlpha(int id){
        switch (id){
            case 0:
                itemAllDirayOpen.setAlpha(1);
                itemAllDirayClose.setAlpha(0);

                itemCollectionDirayOpen.setAlpha(0);
                itemCollectionDirayClose.setAlpha(1);

                itemCreateDirayOpen.setAlpha(0);
                itemCreateDirayClose.setAlpha(1);
                break;
            case 1:
                itemCollectionDirayOpen.setAlpha(1);
                itemCollectionDirayClose.setAlpha(0);

                itemAllDirayOpen.setAlpha(0);
                itemAllDirayClose.setAlpha(1);

                itemCreateDirayOpen.setAlpha(0);
                itemCreateDirayClose.setAlpha(1);
                break;
            case 2:
                itemCreateDirayOpen.setAlpha(1);
                itemCreateDirayClose.setAlpha(0);

                itemAllDirayOpen.setAlpha(0);
                itemAllDirayClose.setAlpha(1);

                itemCollectionDirayOpen.setAlpha(0);
                itemCollectionDirayClose.setAlpha(1);
                break;
            default:break;
        }
    }

    private void prepareMusic(){
        File file=new File(MyApplication.getPeople().getMusicUrl());
        try {
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId()==R.id.rela_diary){
            for (DeleteHandle defaultHandler:deleteHandles){
                isDetail=false;
                defaultHandler.getImageView().setVisibility(View.VISIBLE);
                defaultHandler.getRelativeLayout().setAlpha((float) 0.5);
                fragmentBox.setVisibility(View.GONE);
                deleteBox.setVisibility(View.VISIBLE);
                statisticsSelect();
            }
        }
        return false;
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    class CancelDeleteBox extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            while (true){
                SystemClock.sleep(10);
                if (deleteHandles.size()==diaryEntities.size()){

                    Intent intent=new Intent("com.jpeng.demo.UPDATESUCCESS_DIARY");
                    localBroadcastManager.sendBroadcast(intent);
                    break;
                }
            }
            return true;
        }
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.jpeng.demo.PLAYMUSIC")){
                if (mediaPlayer==null){
                    mediaPlayer=new MediaPlayer();
                }else {
                    mediaPlayer.reset();
                }
                prepareMusic();
                mediaPlayer.start();
                isPause=true;
                imagePlay.setVisibility(View.VISIBLE);
                imagePlay.setImageResource(R.drawable.play_fill);
            }
            if (intent.getAction().equals("com.jpeng.demo.CLOSEMUSIC")){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
                imagePlay.setVisibility(View.GONE);
            }
            if (intent.getAction().equals("com.jpeng.demo.ADDDATASUCCESS_DIARY")){

                diaryEntities.add(LitePal.findLast(DiaryEntity.class));
                updateDelete();
                allDiary.setToUpdate();
            }
            if (intent.getAction().equals("com.jpeng.demo.UPDATESUCCESS_DIARY")){
                cancelDelete();
            }
            if (intent.getAction().equals("com.jpeng.demo.DELETEALLDIARY")){
                if (diaryEntities.size()>0){
                    diaryEntities.removeAll(diaryEntities);
                }
                if (collections.size()>0){
                    collections.removeAll(collections);
                }

                allDiary.setToUpdate();
                collectionDiary.setCollectionChange();
            }
        }
    }
}
