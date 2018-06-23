package com.jpeng.demo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.jpeng.demo.add.AddCarmera;
import com.jpeng.demo.add.AddEvent;
import com.jpeng.demo.add.LearningNotes;
import com.jpeng.demo.clock.ClockSet;
import com.jpeng.demo.delete.AllTypeDelete;
import com.jpeng.demo.delete.CarmearDelete;
import com.jpeng.demo.delete.LearnDelete;
import com.jpeng.demo.delete.NoteDelete;
import com.jpeng.demo.diarys.Diary;
import com.jpeng.demo.diarys.DiaryEntity;
import com.jpeng.demo.diarys.Encryption;
import com.jpeng.demo.map.GaoDeMap;
import com.jpeng.demo.notes.Carmera;
import com.jpeng.demo.notes.Learn;
import com.jpeng.demo.notes.Note;
import com.jpeng.demo.set.SetType;
import com.jpeng.demo.vioce.ActivityOnline;
import com.jpeng.demo.vioce.ActivityRecog;
import com.jpeng.demo.vioce.ActivityUiDialog;
import com.jpeng.demo.vioce.mini.ActivityMiniRecog;
import com.jpeng.demo.weather.AirNow;
import com.jpeng.demo.weather.ImageChose;
import com.jpeng.demo.weather.Provide;
import com.jpeng.demo.weather.Weather;
import com.jpeng.demo.weather.WeatherActivity;
import com.jpeng.jptabbar.BadgeDismissListener;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.animate.AnimationType;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.jpeng.demo.R.id.brf;
import static com.jpeng.demo.R.id.camera_show;
import static com.jpeng.demo.R.id.forever;
import static com.jpeng.demo.R.id.swipe_refresh;
import static com.jpeng.demo.R.id.tabbar;


public class MainActivity extends AddressAndWeather implements BadgeDismissListener, OnTabSelectListener, View.OnClickListener, View.OnLongClickListener {

    private PopupWindow popupWindow;
    public static Context context;

    public static List<Note> noteList=new ArrayList<>();
    public static List<Learn> learns=new ArrayList<>();
    public static List<Carmera> carmeras=new ArrayList<>();
    public static List<Note> noteStars=new ArrayList<>();
    public static List<Learn> learnStars=new ArrayList<>();
    public static List<Carmera> carmeraStars=new ArrayList<>();

    public static View.OnLongClickListener onLongClickListener;
    public static View.OnClickListener onClickListener;
    RelativeLayout addNote,addDiray,addLearning;
    Dialog dialog;
    CircleImageView circleImageView;
    LinearLayout layoutNav;
    Activity activity;
    TextView nickName,personalitySignature,address;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    TextView toolbarText,airTemperature;
    Toolbar toolbar;
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    ImageView weathear;

    @Titles
    private static final int[] mTitles = {R.string.tab1,R.string.tab2,R.string.tab3,R.string.tab4};

    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.document_fill,R.mipmap.collection_fill,R.mipmap.order_fill,R.mipmap.camera_fill};

    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.document, R.mipmap.collection, R.mipmap.order, R.mipmap.camera};

    private List<Fragment> list = new ArrayList<>();

    private ViewPager mPager;

    private JPTabBar mTabbar;

    private Tab1Pager mTab1;

    private Tab2Pager mTab2;

    private Tab3Pager mTab3;

    private Tab4Pager mTab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //isMap=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);

        activity=this;
        context=this;
        onLongClickListener=this;
        onClickListener=this;

        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.INTERNET);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CHANGE_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CHANGE_WIFI_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.BLUETOOTH);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_ADMIN)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MODIFY_AUDIO_SETTINGS)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        }
        if (!permissionList.isEmpty()){
            String [] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {
            try {
                init();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private void init() throws FileNotFoundException {
        People peopleIno=new People();
        SharedPreferences preferences=getSharedPreferences("data",MODE_PRIVATE);
        peopleIno.setAnimationType(preferences.getInt("checkedItem",0));
        peopleIno.setPersonalitySignature(preferences.getString("personalitySignature","暂无"));
        peopleIno.setNickName(preferences.getString("nickName","暂无"));
        peopleIno.setToolbarColor(preferences.getInt("colorData",-12627531));
        peopleIno.setUserGesture(preferences.getBoolean("isUserGesture",false));
        peopleIno.setHeadImage(preferences.getString("headImage",""));
        peopleIno.setLearnLabel(preferences.getString("learnLabel",""));
        peopleIno.setMusicUrl(preferences.getString("musicUrl",""));
        peopleIno.setMusic(preferences.getBoolean("isMusic",false));
        peopleIno.setMusicTitle(preferences.getString("musicTitle",""));
        peopleIno.setEncryption(preferences.getBoolean("isEncryption",false));
        peopleIno.setPassWord(preferences.getString("passWord",""));
        peopleIno.setUseFace(preferences.getBoolean("isUseFace",false));
        peopleIno.setDiaryAnimation(preferences.getInt("diaryAnimation",0));
        peopleIno.setAddress(preferences.getString("address",""));
        MyApplication.setPeople(peopleIno);

        //Connector.getDatabase();
        //DataSupport.deleteAll(Carmera.class);

        if (peopleIno.isUseFace()){
            MyApplication.getPeople().setDetecter(true);
            Intent intent=new Intent(MainActivity.this, StartFace.class);
            startActivityForResult(intent,1);
        }else {
            if (ImageChose.getNetInfor((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
                Toast.makeText(this,"正在为你更新位置和天气信息...",Toast.LENGTH_SHORT).show();
                startAddress();
            }else {
                Toast.makeText(this,"未连接网络，地址和天气信息将停止更新",Toast.LENGTH_SHORT).show();
                Provide.setIsSuccessBing(false);
                Provide.setIsSuccessWeaNow(false);
                Provide.setIsSuccessAirNow(false);
            }

        }

        cancel();

        noteList=DataSupport.findAll(Note.class);
        learns=DataSupport.findAll(Learn.class);
        carmeras=DataSupport.findAll(Carmera.class);

        if (noteList.size()>0){
            for(Note note:noteList){
                if (note.isCollection()){
                    noteStars.add(note);
                }
            }
        }
        if (learns.size()>0){
            for (Learn learn:learns){
                if (learn.isCollection()){
                    learnStars.add(learn);
                }
            }
        }
        if (carmeras.size()>0){
            for (Carmera carmera:carmeras){
                if (carmera.isCollection()){
                    carmeraStars.add(carmera);
                }
            }
        }

        StatusBarCompat.setStatusBarColor(this, peopleIno.getToolbarColor(), true);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbarText=(TextView)findViewById(R.id.toolbar_text);
        toolbarText.setText("记事簿");
        //toolbar.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        setSupportActionBar(toolbar);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //滑动过程中不断回调 slideOffset:0~1
                View content = mDrawerLayout.getChildAt(0);
                View menu = drawerView;

                float scale = 1 - slideOffset;//1~0
                content.setTranslationX(menu.getMeasuredWidth() * (1 - scale));//0~width
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navigationView=(NavigationView)findViewById(R.id.nav_view);

        layoutNav=(LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.rela_nav);
        nickName=(TextView)navigationView.getHeaderView(0).findViewById(R.id.nickname_nav);
        personalitySignature=(TextView)navigationView.getHeaderView(0).findViewById(R.id.personality_signature_nav);
        circleImageView=(CircleImageView)navigationView.getHeaderView(0).findViewById(R.id.icon_image);
        Glide.with(this).load(MyApplication.getPeople().getHeadImage()).error(R.drawable.imageview).into(circleImageView);
        address=(TextView)navigationView.getHeaderView(0).findViewById(R.id.address_nav);
        weathear=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.weather_nav);
        airTemperature=(TextView)navigationView.getHeaderView(0).findViewById(R.id.air_temperature);
        weathear.setOnClickListener(this);

        layoutNav.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        nickName.setText(MyApplication.getPeople().getNickName());
        address.setText(MyApplication.getPeople().getAddress());
        personalitySignature.setText(MyApplication.getPeople().getPersonalitySignature());

        startNav();
        mTabbar = (JPTabBar) findViewById(tabbar);
        MyApplication.setJpTabBar(mTabbar);
        mPager = (ViewPager) findViewById(R.id.view_pager);
//        mTabbar.setTitles("asd","页面二","页面三","页面四").setNormalIcons(R.mipmap.tab1_normal,R.mipmap.tab2_normal,R.mipmap.tab3_normal,R.mipmap.tab4_normal)
//                .setSelectedIcons(R.mipmap.tab1_selected,R.mipmap.tab2_selected,R.mipmap.tab3_selected,R.mipmap.tab4_selected).generate();
        mTabbar.setTabTypeFace("fonts/Jaden.ttf");
        mTab1 = new Tab1Pager();
        mTab2 = new Tab2Pager();
        mTab3 = new Tab3Pager();
        mTab4 = new Tab4Pager();
        mTabbar.setGradientEnable(true);
        mTabbar.setPageAnimateEnable(true);
        mTabbar.setTabListener(this);
        list.add(mTab1);
        list.add(mTab2);
        list.add(mTab3);
        list.add(mTab4);

        mPager.setAdapter(new Adapter(getSupportFragmentManager(),list));
        mTabbar.setContainer(mPager);
        //设置Badge消失的代理
        mTabbar.setDismissListener(this);
        mTabbar.setTabListener(this);
        if(mTabbar.getMiddleView()!=null)
            mTabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//中间按钮点击！！！
                    createAddDialog();

                }
            });

        changeTabBar(MyApplication.getPeople().getAnimationType());

        intentFilter=new IntentFilter();
        intentFilter.addAction("com.jpeng.demo.CHOSECOLOR");
        intentFilter.addAction("com.jpeng.demo.PEOPLEINFO");
        intentFilter.addAction("com.jpeng.demo.HEDAIMAGE");
        intentFilter.addAction("com.jpeng.demo.CHANGEADDRESS");
        intentFilter.addAction("com.jpeng.demo.WEATHERNOW");
        intentFilter.addAction("com.jpeng.demo.GETERROR");
        intentFilter.addAction("com.jpeng.demo.ADDSUCCESSNOTE");
        intentFilter.addAction("com.jpeng.demo.ADDSUCCESSLEARN");
        intentFilter.addAction("com.jpeng.demo.ADDSUCCESSCARMEAR");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 0) {
                if (ImageChose.getNetInfor((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
                    startAddress();
                }else {
                    Toast.makeText(this,"未连接网络，地址和天气信息将停止更新",Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 2){
            if (resultCode == 0){
                if (NoteTool.isRevise){
                    mTab1.toUpdate();
                    if (noteStars.contains(NoteTool.note)){
                        mTab2.handleRecycler(0,false);
                    }
                    NoteTool.isRevise=false;
                }
            }
        }
        if (requestCode == 3){
            if (resultCode == 0){
                if (NoteTool.isRevise){
                    mTab3.toUpdate();
                    if (learnStars.contains(NoteTool.learn)){
                        mTab2.handleRecycler(1,false);
                    }
                    NoteTool.isRevise=false;
                }
            }
        }
    }

    private void createAddDialog() {
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.add_dialog_layout, null);

        addNote=(RelativeLayout)v.findViewById(R.id.add_note);
        addDiray=(RelativeLayout)v.findViewById(R.id.add_diary);
        addLearning=(RelativeLayout)v.findViewById(R.id.add_learning);
        addNote.setOnClickListener(this);
        addDiray.setOnClickListener(this);
        addLearning.setOnClickListener(this);

        dialog.setContentView(v);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用该应用",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    try {
                        init();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(this,"发生未知错误！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void changeTabBar(int id){
        switch (id)
        {
            case 0:
                MyApplication.getJpTabBar().setAnimation(AnimationType.SCALE);
                break;
            case 1:
                MyApplication.getJpTabBar().setAnimation(AnimationType.SCALE2);
                break;
            case 2:
                MyApplication.getJpTabBar().setAnimation(AnimationType.JUMP);
                break;
            case 3:
                MyApplication.getJpTabBar().setAnimation(AnimationType.ROTATE);
                break;
            default:break;
        }
    }

    private void startNav(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.diary_nav:
                        if (MyApplication.getPeople().isEncryption()){
                            Intent intent=new Intent(MainActivity.this, Encryption.class);
                            startActivity(intent);
                        }else {
                            Intent intent=new Intent(MainActivity.this, Diary.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.address_nav:
                        Intent intent=new Intent(MainActivity.this, GaoDeMap.class);
                        startActivity(intent);
                        break;
                    case R.id.clock_nav:
                        Intent intent1=new Intent(MainActivity.this, ClockSet.class);
                        startActivity(intent1);
                    default:break;
                }
                //mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.set:
                Intent intent=new Intent(MainActivity.this, SetType.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.enteralpha,R.anim.exitalpha);
            break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

            default:break;
        }
        return true;
    }



    @Override
    public void onTabSelect(int index) {
        toolbarText.setText(mTitles[index]);
        //Toast.makeText(MainActivity.this,"choose the tab index is "+index,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onInterruptSelect(int index) {
//        if(index==2){
//            //如果这里有需要阻止Tab被选中的话,可以return true
//            return true;
//        }
        return false;
    }

    public JPTabBar getTabbar() {
        return mTabbar;
    }


    @Override
    public void onDismiss(int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_note:
                Intent intent=new Intent(MainActivity.this,AddEvent.class);
                intent.putExtra("isAdd",true);
                startActivity(intent);
                dialog.dismiss();
                break;
            case R.id.add_diary:
                Intent intent2=new Intent(MainActivity.this, AddCarmera.class);
                startActivity(intent2);
                dialog.dismiss();
                break;
            case  R.id.add_learning:
                Intent intent1=new Intent(MainActivity.this, LearningNotes.class);
                intent1.putExtra("isAdd",true);
                startActivity(intent1);
                dialog.dismiss();
                break;
            case R.id.weather_nav:
                Intent intent3=new Intent(this, WeatherActivity.class);
                startActivity(intent3);
                break;
            case R.id.star_note:
                ImageView imageView=(ImageView) v;
                Note note=(Note) v.getTag();
                setStarImage(0,imageView,note,null,null);
                break;
            case R.id.delete_note:
                Toast.makeText(this,"删除",Toast.LENGTH_SHORT).show();
                AllTypeDelete allTypeDelete=(AllTypeDelete) v.getTag();
                deleteNote(allTypeDelete);
                popupWindow.dismiss();
                break;
            case R.id.delete_all_note:
                Toast.makeText(this,"全部删除",Toast.LENGTH_SHORT).show();
                int id=(int) v.getTag();
                deleteAll(id);
                popupWindow.dismiss();
                break;
            case R.id.star_learn:
                ImageView imageView1=(ImageView) v;
                Learn learn=(Learn) v.getTag();
                setStarImage(1,imageView1,null,learn,null);
                break;
            case R.id.star_carmear:
                ImageView imageView2=(ImageView) v;
                Carmera carmera=(Carmera) v.getTag();
                setStarImage(2,imageView2,null,null,carmera);
                break;
            case R.id.item_note_linear:
                NoteDelete noteDelete=(NoteDelete) v.getTag();
                NoteTool.setNote(noteDelete.getNote());
                Intent intent4=new Intent(MainActivity.this,AddEvent.class);
                intent4.putExtra("isAdd",false);
                startActivityForResult(intent4,2);
                break;
            case R.id.item_rela_learn:
                LearnDelete learnDelete=(LearnDelete) v.getTag();
                NoteTool.setLearn(learnDelete.getLearn());
                Intent intent5=new Intent(MainActivity.this,LearningNotes.class);
                intent5.putExtra("isAdd",false);
                startActivityForResult(intent5,3);
            default:break;
        }
    }

    private void setStarImage(int id,ImageView star,Note note,Learn learn,Carmera carmera){
        switch (id){
            case 0:
                if (note.isCollection()){
                    star.setImageResource(R.drawable.no_star);
                    note.setCollection(false);
                    note.save();
                    noteStars.remove((Note) note);
                    mTab2.handleRecycler(0,false);
                }else {
                    star.setImageResource(R.drawable.yes_star);
                    note.setCollection(true);
                    note.save();
                    noteStars.add(note);
                    mTab2.handleRecycler(0,true);
                }
                mTab1.toUpdate();
                break;
            case 1:
                if (learn.isCollection()){
                    star.setImageResource(R.drawable.no_star);
                    learn.setCollection(false);
                    learn.save();
                    learnStars.remove((Learn) learn);
                    mTab2.handleRecycler(1,false);
                }else {
                    star.setImageResource(R.drawable.yes_star);
                    learn.setCollection(true);
                    learn.save();
                    learnStars.add(learn);
                    mTab2.handleRecycler(1,true);
                }
                mTab3.toUpdate();
                break;
            case 2:
                if (carmera.isCollection()){
                    star.setImageResource(R.drawable.no_star);
                    carmera.setCollection(false);
                    carmera.save();
                    carmeraStars.remove((Carmera) carmera);
                    mTab2.handleRecycler(2,false);
                }else {
                    star.setImageResource(R.drawable.yes_star);
                    carmera.setCollection(true);
                    carmera.save();
                    carmeraStars.add(carmera);
                    mTab2.handleRecycler(2,true);
                }
                mTab4.toUpdate();
                break;

            default:break;
        }

    }

    private void deleteNote(AllTypeDelete allTypeDelete){
        switch (allTypeDelete.getId()){
            case 0:
                Note note=allTypeDelete.getNote();
                note.delete();
                noteList.remove((Note) note);
                if (noteStars.contains(note)){
                    noteStars.remove(note);
                    mTab2.handleRecycler(0,false);
                }
                mTab1.toUpdate();
                break;
            case 1:
                Learn learn=allTypeDelete.getLearn();
                learn.delete();
                learns.remove((Learn) learn);
                if (learnStars.contains(learn)){
                    learnStars.remove(learn);
                    mTab2.handleRecycler(1,false);
                }
                mTab3.toUpdate();
                break;
            case 2:
                Carmera carmera=allTypeDelete.getCarmera();
                carmera.delete();
                carmeras.remove((Carmera) carmera);
                if (carmeraStars.contains(carmera)){
                    carmeraStars.remove(carmera);
                    mTab2.handleRecycler(2,false);
                }
                mTab4.toUpdate();
                break;
            default:break;
        }
    }

    private void deleteAll(int id){
        switch (id){
            case 0:
                DataSupport.deleteAll(Note.class);
                if (noteList.size()>0){
                    noteList.removeAll(noteList);
                }
                if (noteStars.size()>0){
                    noteStars.removeAll(noteStars);
                }
                mTab2.handleRecycler(0,false);
                mTab1.toUpdate();
                break;
            case 1:
                DataSupport.deleteAll(Learn.class);
                if (learns.size()>0){
                    learns.removeAll(learns);
                }
                if (learnStars.size()>0){
                    learnStars.removeAll(learnStars);
                }
                mTab2.handleRecycler(1,false);
                mTab3.toUpdate();
                break;
            case 2:
                DataSupport.deleteAll(Carmera.class);
                if (carmeras.size()>0){
                    carmeras.removeAll(carmeras);
                }
               if (carmeraStars.size()>0){
                    carmeraStars.removeAll(carmeraStars);
               }
                mTab2.handleRecycler(2,false);
                mTab4.toUpdate();
                break;
            default:break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.item_note_linear:
                NoteDelete noteDelete=(NoteDelete) v.getTag();
                popWindowsShow(0,noteDelete,null,null);
                break;
            case R.id.item_rela_learn:
                LearnDelete learnDelete=(LearnDelete) v.getTag();
                popWindowsShow(1,null,learnDelete,null);
                break;
            case R.id.linear_carmear:
                CarmearDelete carmearDelete=(CarmearDelete) v.getTag();
                popWindowsShow(2,null,null,carmearDelete);
            default:break;
        }
        return false;
    }

    private void popWindowsShow(int id, NoteDelete noteDelete, LearnDelete learnDelete,CarmearDelete carmearDelete){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.popwindows_layout, null);
        TextView delete=(TextView) view.findViewById(R.id.delete_note);
        TextView deleteAll=(TextView) view.findViewById(R.id.delete_all_note);
        popupWindow=new PopupWindow(view,250,300,true);

        View item=null;
        switch (id){
            case 0:
                item=mTab1.recyclerView.getLayoutManager().getChildAt(noteDelete.getPosition());
                AllTypeDelete allTypeDelete=new AllTypeDelete();
                allTypeDelete.setId(0);
                allTypeDelete.setNote(noteDelete.getNote());
                delete.setTag(allTypeDelete);
                deleteAll.setTag(0);
                break;
            case 1:
                item=mTab3.recyclerView.getLayoutManager().getChildAt(learnDelete.getPosition());
                AllTypeDelete allTypeDelete1=new AllTypeDelete();
                allTypeDelete1.setId(1);
                allTypeDelete1.setLearn(learnDelete.getLearn());
                delete.setTag(allTypeDelete1);
                deleteAll.setTag(1);
                break;
            case 2:
                item=mTab4.recyclerView.getLayoutManager().getChildAt(carmearDelete.getPosition());
                AllTypeDelete allTypeDelete2=new AllTypeDelete();
                allTypeDelete2.setId(2);
                allTypeDelete2.setCarmera(carmearDelete.getCarmera());
                delete.setTag(allTypeDelete2);
                deleteAll.setTag(2);
                break;
            default:break;
        }

        delete.setOnClickListener(this);
        deleteAll.setOnClickListener(this);

        int X=(int) item.getX();
        int Y=(int) item.getY();
        int width=X+100;
        int hight=0;

        WindowManager wm = this.getWindowManager();

        if (Y<0){
            if ((popupWindow.getHeight()+Y)>=15){
                hight=Y+popupWindow.getHeight();
            }else {
                hight=15+popupWindow.getHeight();
            }
        }else {
            if ((wm.getDefaultDisplay().getHeight()-Y-200)<popupWindow.getHeight()){
                hight=Y;
            }else {
                hight=Y+popupWindow.getHeight();
            }
        }

        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,width,hight);
    }

    private void cancel(){
        if (noteList.size()>0){
            noteList.removeAll(noteList);
        }
        if (learns.size()>0){
            learns.removeAll(learns);
        }
        if (carmeras.size()>0){
            carmeras.removeAll(carmeras);
        }

        if (noteStars.size()>0){
            noteStars.removeAll(noteStars);
        }
        if (learnStars.size()>0){
            learnStars.removeAll(learnStars);
        }
        if (carmeraStars.size()>0){
            carmeraStars.removeAll(carmeraStars);
        }
    }


    class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.jpeng.demo.CHOSECOLOR")){
                toolbar.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
                StatusBarCompat.setStatusBarColor(getActivity(), MyApplication.getPeople().getToolbarColor(), true);
                layoutNav.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
            }
            if (intent.getAction().equals("com.jpeng.demo.PEOPLEINFO")){
                nickName.setText(MyApplication.getPeople().getNickName());
                personalitySignature.setText(MyApplication.getPeople().getPersonalitySignature());
            }
            if (intent.getAction().equals("com.jpeng.demo.HEDAIMAGE")){
                Glide.with(MyApplication.getContext()).load(MyApplication.getPeople().getHeadImage()).error(R.drawable.imageview).into(circleImageView);
            }
            if (intent.getAction().equals("com.jpeng.demo.CHANGEADDRESS")){
                address.setText(MyApplication.getPeople().getAddress());
                //.makeText(getApplicatiToastonContext(),""+MyApplication.getPeople().getAddress(),Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("com.jpeng.demo.WEATHERNOW")){
                SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss", Locale.getDefault());
                String [] dateTime = sdf.format(new java.util.Date()).split("-");
                weathear.setImageResource(ImageChose.getWeatherImage(Integer.valueOf(Provide.getWeather().getHeWeather6().get(0).getNow().getCond_code()),ImageChose.getTimeSectionH(Integer.valueOf(dateTime[0])),true));
                airTemperature.setText(Provide.getWeather().getHeWeather6().get(0).getNow().getFl()+"°C");
            }
            if (intent.getAction().equals("com.jpeng.demo.ADDSUCCESSNOTE")){
                noteList.add(DataSupport.findLast(Note.class));
                mTab1.toUpdate();
            }
            if (intent.getAction().equals("com.jpeng.demo.ADDSUCCESSLEARN")){
                learns.add(DataSupport.findLast(Learn.class));
                mTab3.toUpdate();
            }
            if (intent.getAction().equals("com.jpeng.demo.ADDSUCCESSCARMEAR")) {
                carmeras.add(DataSupport.findLast(Carmera.class));
                mTab4.toUpdate();
            }
        }
    }

}
