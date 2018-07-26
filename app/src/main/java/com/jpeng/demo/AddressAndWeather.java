package com.jpeng.demo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.jpeng.demo.weather.AirNow;
import com.jpeng.demo.weather.ImageChose;
import com.jpeng.demo.weather.Provide;
import com.jpeng.demo.weather.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 王将 on 2018/5/4.
 */

public class AddressAndWeather extends AppCompatActivity {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public String city = "", district = "";
    private LocalBroadcastManager localBroadcastManager;
    public Dialog dialogPro;
    public TextView show;
    static String urlNow,urlAir;
    String jsonStr="",jsonStrAir="";

    public void startAddress() {
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        //showProgress();
    }

    private void showProgress() {
        dialogPro = new Dialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_progress, null);
        ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progress_bar);
        show = (TextView) inflate.findViewById(R.id.show_result);
    }

    public void getWeatherInfo(){
        String []strings= ImageChose.getCityAndDistrict(MyApplication.getPeople().getDistrict(),MyApplication.getPeople().getCity());
        urlNow="https://free-api.heweather.com/s6/weather?" +
                "location="+strings[0]+","+strings[1]+"&" +
                "key=516b156869d3478b9741e63dfcafe6d4"+"&"+
                "lang=en"+"&"+
                "unit=m";
        urlAir = "https://free-api.heweather.com/s6/air/now?" +
                "location="+strings[1]+"&" +
                "key=516b156869d3478b9741e63dfcafe6d4"+"&"+
                "lang=en"+"&"+
                "unit=m";
        Log.i("urlNow++++++",urlNow);
        Log.i("urlAir++++++",urlAir);
        new GetBingPic().execute();
    }

    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    MyApplication.getPeople().setDistrict(amapLocation.getDistrict());
                    MyApplication.getPeople().setAddress(amapLocation.getAddress());
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("address",amapLocation.getAddress());
                    editor.apply();
                    mLocationClient.stopLocation();MyApplication.getPeople().setCity(amapLocation.getCity());

                    getWeatherInfo();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient!=null){
            mLocationClient.onDestroy();
        }
    }

    class GetBingPic extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url("http://guolin.tech/api/bing_pic").build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                Provide.setBingPic(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            if ((Boolean) o){
                Provide.setIsSuccessBing(true);
                new WeatherData().execute();
            }else {
                Toast.makeText(MyApplication.getContext(),"更新失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class WeatherData extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(urlNow).build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                jsonStr=response.body().string();
                Log.i("json++++++++",jsonStr);
                //打印json
                if (!jsonStr.isEmpty()){
                    Gson gson=new Gson();
                    Provide.setWeather(gson.fromJson(jsonStr,Weather.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {

            if ((Boolean) o){
                Provide.setIsSuccessWeaNow(true);
                Intent intent=new Intent("com.jpeng.demo.WEATHERNOW");
                localBroadcastManager.sendBroadcast(intent);
                new AirData().execute();
            }else {
                Toast.makeText(MyApplication.getContext(),"更新失败！",Toast.LENGTH_SHORT).show();
            }

        }
    }
    class AirData extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(urlAir).build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                jsonStrAir=response.body().string();
                Log.i("json++++++++",jsonStrAir);
                //打印json
                if (!jsonStrAir.isEmpty()){
                    Gson gson=new Gson();
                    Provide.setAirNow(gson.fromJson(jsonStrAir,AirNow.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o){

            if ((Boolean) o){
                Provide.setIsSuccessAirNow(true);
                //show.setText("获取成功");
                //dialogPro.dismiss();
                Toast.makeText(MyApplication.getContext(),"更新成功~",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent("com.jpeng.demo.CHANGEADDRESS");
                localBroadcastManager.sendBroadcast(intent);
            }else {
                Toast.makeText(MyApplication.getContext(),"更新失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
