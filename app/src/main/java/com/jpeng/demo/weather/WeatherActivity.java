package com.jpeng.demo.weather;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;
import com.jpeng.demo.ActivityCollector;
import com.jpeng.demo.AddressAndWeather;
import com.jpeng.demo.MainActivity;
import com.jpeng.demo.MyApplication;
import com.jpeng.demo.R;

import java.util.List;


public class WeatherActivity extends AddressAndWeather implements MyScrollView.OnScrollChangeListener{

    int aqiGrade=0;
    private Boolean isAirFirst = true;
    private Boolean isSunFirst = true;
    View air1,air2,air3,air4,air5,air6;
    MyScrollView scrollView;
    LinearLayout linearLayout,toolbar,toolbarTwo;
    Huayuan huayuan;
    ImageView imageView;
    Weather.HeWeather6Bean.DailyForecastBean dailyForecastBean;
    TextView address,address1;
    private SwipeRefreshLayout refreshLayout;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    View air;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_weather);
        ActivityCollector.addActivity(this);

        localBroadcastManager=LocalBroadcastManager.getInstance(this);

        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshWeather();
            }
        });
        linearLayout=(LinearLayout)findViewById(R.id.weather_all);

        inspectIntent(false);

        intentFilter=new IntentFilter();
        intentFilter.addAction("com.jpeng.demo.CHANGEADDRESS");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);

    }

    private void refreshWeather() {
        inspectIntent(true);
    }

    private void inspectIntent(boolean isRefresh){
        if (ImageChose.getNetInfor((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))){
            if (isRefresh){
                startAddress();
            }else {
                startLin();
            }
        }else {
            if (isRefresh){
                refreshLayout.setRefreshing(false);
            }
            Toast.makeText(this,"网络未连接，天气信息关闭",Toast.LENGTH_SHORT).show();
        }
    }

    private void startLin(){
        address=(TextView)findViewById(R.id.weather_address);
        address1=(TextView)findViewById(R.id.weather_address_1);
        address.setText(MyApplication.getPeople().getDistrict()+"-"+MyApplication.getPeople().getCity());
        address1.setText(MyApplication.getPeople().getDistrict()+"-"+MyApplication.getPeople().getCity());
        toolbar=(LinearLayout)findViewById(R.id.toolbar);
        toolbarTwo=(LinearLayout)findViewById(R.id.toolbar);
        scrollView=(MyScrollView)findViewById(R.id.scro_view);
        scrollView.setOnScrollChangeListener(this);
        imageView=(ImageView)findViewById(R.id.bing_pic);

        StatusBarCompat.setStatusBarColor(this, MyApplication.getPeople().getToolbarColor(), true);
        toolbar.setBackgroundColor(MyApplication.getPeople().getToolbarColor());
        if (Provide.isIsSuccessBing()&&Provide.isIsSuccessWeaNow()&&Provide.isIsSuccessAirNow()){
            Glide.with(this).load(Provide.getBingPic()).into(imageView);

            linearLayout.addView(getNowView(linearLayout,Provide.getWeather().getHeWeather6().get(0).getNow(),Provide.getWeather().getHeWeather6().get(0).getDaily_forecast().get(0)));
            linearLayout.addView(getLineView());
            linearLayout.addView(getHourlyView(linearLayout,Provide.getWeather().getHeWeather6().get(0).getHourly()));
            linearLayout.addView(getLineView());
            dailyForecastBean=Provide.getWeather().getHeWeather6().get(0).getDaily_forecast().get(0);
            Provide.getWeather().getHeWeather6().get(0).getDaily_forecast().remove(0);
            for (Weather.HeWeather6Bean.DailyForecastBean dailyForecastBean:Provide.getWeather().getHeWeather6().get(0).getDaily_forecast()){
                linearLayout.addView(getDailyView(linearLayout,dailyForecastBean));
                linearLayout.addView(getLineView());
            }
            linearLayout.addView(getAirNowView(linearLayout,Provide.getAirNow().getHeWeather6().get(0).getAir_now_city()));
            linearLayout.addView(getLineView());
            linearLayout.addView(getLifeView(linearLayout, Provide.getWeather().getHeWeather6().get(0).getLifestyle()));
            linearLayout.addView(getLineView());
            linearLayout.addView(getWindyView(linearLayout,Provide.getWeather().getHeWeather6().get(0).getNow()));
            linearLayout.addView(getLineView());
        }else {
            startAddress();
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
        ActivityCollector.removeActivity(this);
    }

    private View getNowView(LinearLayout root,Weather.HeWeather6Bean.NowBean nowBean, Weather.HeWeather6Bean.DailyForecastBean dailyForecastBean){
        View view= LayoutInflater.from(this).inflate(R.layout.weather_now,root,false);
        TextView tem=(TextView)view.findViewById(R.id.air_temperature);
        TextView condTxt=(TextView)view.findViewById(R.id.cond_txt);
        TextView tmp=(TextView)view.findViewById(R.id.tmp);
        TextView hum=(TextView)view.findViewById(R.id.hum);

        hum.setText("湿度"+nowBean.getHum());
        tem.setText(nowBean.getFl());
        condTxt.setText(ImageChose.getWeatherName(Integer.valueOf(nowBean.getCond_code())));
        tmp.setText(dailyForecastBean.getTmp_max()+"°C/"+dailyForecastBean.getTmp_min()+"°C");

        return view;
    }
    private View getHourlyView(LinearLayout root,List<Weather.HeWeather6Bean.HourlyBean> hourlyBeans){
        View view= LayoutInflater.from(this).inflate(R.layout.weather_hour,root,false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recy);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        HourAdapter hourAdapter=new HourAdapter(hourlyBeans);
        recyclerView.setAdapter(hourAdapter);

        return view;
    }
    private View getLineView(){
        View view=LayoutInflater.from(this).inflate(R.layout.line,null,false);
        return view;
    }
    private View getDailyView(LinearLayout root,Weather.HeWeather6Bean.DailyForecastBean daily){
        View view=LayoutInflater.from(this).inflate(R.layout.weather_daily_forecast,root,false);
        TextView date=(TextView)view.findViewById(R.id.date_daily);
        ImageView icon=(ImageView)view.findViewById(R.id.icon_daily);
        TextView tmp=(TextView)view.findViewById(R.id.tmp_daily);
        date.setText(ImageChose.getDivisionString(daily.getDate(),false));
        icon.setImageResource(ImageChose.getWeatherImage(Integer.valueOf(daily.getCond_code_d()),null,false));
        tmp.setText(daily.getTmp_max()+"°C/"+daily.getTmp_min()+"°C");
        return view;
    }
    private View getAirNowView(LinearLayout root,AirNow.HeWeather6Bean.AirNowCityBean airNowCityBean){
        air=LayoutInflater.from(this).inflate(R.layout.air_quality,root,false);

        air1=(View)air.findViewById(R.id.air_1);
        air2=(View)air.findViewById(R.id.air_2);
        air3=(View)air.findViewById(R.id.air_3);
        air4=(View)air.findViewById(R.id.air_4);
        air5=(View)air.findViewById(R.id.air_5);
        air6=(View)air.findViewById(R.id.air_6);

        TextView aqiTxt=(TextView)air.findViewById(R.id.aqi_text);
        TextView aqi=(TextView)air.findViewById(R.id.aqi);
        TextView main=(TextView)air.findViewById(R.id.main);
        TextView pm10=(TextView)air.findViewById(R.id.pm10);
        TextView pm25=(TextView)air.findViewById(R.id.pm2_5);
        TextView no2=(TextView)air.findViewById(R.id.no2);
        TextView so2=(TextView)air.findViewById(R.id.so2);
        TextView o3=(TextView)air.findViewById(R.id.o3);
        TextView co=(TextView)air.findViewById(R.id.co);

        aqiTxt.setText(ImageChose.getAqiTxt(Integer.valueOf(airNowCityBean.getAqi())));
        aqi.setText(airNowCityBean.getAqi());
        main.setText(airNowCityBean.getMain());
        pm10.setText(airNowCityBean.getPm10());
        pm25.setText(airNowCityBean.getPm25());
        no2.setText(airNowCityBean.getNo2());
        so2.setText(airNowCityBean.getSo2());
        o3.setText(airNowCityBean.getO3());
        co.setText(airNowCityBean.getCo());

        aqiGrade=ImageChose.getAqiGrade(Integer.valueOf(airNowCityBean.getAqi()));
        return air;
    }

    private View getLifeView(LinearLayout root,List<Weather.HeWeather6Bean.LifestyleBean> lifestyleBeans){
        View view=LayoutInflater.from(this).inflate(R.layout.weather_lifestyle,root,false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recy_life);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        LifeAdapter lifeAdapter=new LifeAdapter(lifestyleBeans);
        recyclerView.setAdapter(lifeAdapter);

        return view;
    }

    private View getWindyView(LinearLayout root,Weather.HeWeather6Bean.NowBean nowBean){
        View view=LayoutInflater.from(this).inflate(R.layout.weather_win,root,false);

        ImageView gif=(ImageView)view.findViewById(R.id.windy_gif);
        TextView deg=(TextView)view.findViewById(R.id.wind_deg);
        TextView dir=(TextView)view.findViewById(R.id.wind_dir);
        TextView sc=(TextView)view.findViewById(R.id.wind_sc);
        TextView spd=(TextView)view.findViewById(R.id.wind_spd);

        Glide.with(this).load(R.drawable.wind).into(gif);
        deg.setText(nowBean.getWind_deg());
        dir.setText(ImageChose.getWindDir(nowBean.getWind_dir()));
        sc.setText(nowBean.getWind_sc()+"级");
        spd.setText(nowBean.getWind_spd());
        return view;
    }

    private View getSunView(LinearLayout root, Weather.HeWeather6Bean.DailyForecastBean dailyForecastBean){
        View sun=LayoutInflater.from(this).inflate(R.layout.weather_sun,root,false);
        Provide.setSunUp("日出 "+dailyForecastBean.getSr());
        Provide.setSunDown("日落 "+dailyForecastBean.getSs());
        huayuan=new Huayuan(this,ImageChose.getCircularDegrees(dailyForecastBean.getSr(),dailyForecastBean.getSs()));
        return sun;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void onScrollChange(MyScrollView view, int x, int y, int oldx, int oldy) {
        if (y<=255){
            toolbar.setAlpha((float)(255-y)/255);
        }
        if (checkIsVisible(this,air)){
            if (isAirFirst){
                isAirFirst=false;
                new ViewChange().execute();
            }
        }
    }

    @Override
    public void onScrollBottomListener() {
        if (Provide.isIsSuccessBing()&&Provide.isIsSuccessWeaNow()&&Provide.isIsSuccessAirNow()){
            if (isSunFirst){
                isSunFirst=false;
                linearLayout.addView(getSunView(linearLayout,dailyForecastBean));
            }
        }
    }

    @Override
    public void onScrollTopListener() {

    }

    public static Boolean checkIsVisible(Context context, View view) {
        // 如果已经加载了，判断广告view是否显示出来，然后曝光
        int screenWidth = getScreenMetrics(context).x;
        int screenHeight = getScreenMetrics(context).y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            return true;
        } else {
            //view已不在屏幕可见区域;
            return false;
        }
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    class ViewChange extends AsyncTask {

        @Override
        protected void onPreExecute() {
            air1.setAlpha(0);
            air2.setAlpha(0);
            air3.setAlpha(0);
            air4.setAlpha(0);
            air5.setAlpha(0);
            air6.setAlpha(0);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            int i=0;
            while (true){
                SystemClock.sleep(100);
                i++;
                publishProgress(i);
                if (i>aqiGrade*10){
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            int grade=(int)values[0];
            if (grade>0&&grade<=10){
                air1.setAlpha((float)0.1*grade);
            }else if (grade>10&&grade<=20){
                air2.setAlpha((float)0.1*(grade-10));
            }else if (grade>20&&grade<=30){
                air3.setAlpha((float)0.1*(grade-20));
            }else if (grade>30&&grade<=40){
                air4.setAlpha((float)0.1*(grade-30));
            }else if (grade>40&&grade<=50){
                air5.setAlpha((float)0.1*(grade-40));
            }else if (grade>50&&grade<=60){
                air6.setAlpha((float)0.1*(grade-50));
            }
        }

        @Override
        protected void onPostExecute(Object o) {
        }
    }
    class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.jpeng.demo.CHANGEADDRESS")){
                refreshLayout.setRefreshing(false);
                if (linearLayout.getChildCount()==0){

                    startLin();
                }else {
                    linearLayout.removeAllViews();
                    startLin();
                }
            }
        }
    }
    /*class CancelDeleteBox extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            while (true){
                SystemClock.sleep(10);
                if ()
            }
            return true;
        }
    }*/
}
