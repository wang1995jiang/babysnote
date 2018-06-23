package com.jpeng.demo.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.jpeng.demo.R;


import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by 王将 on 2018/4/18.
 */

public class ImageChose {
    private static final int sunny=R.drawable.sunny;
    private static final int sunnyN=R.drawable.clear;
    private static final int cloudy=R.drawable.cloudy;
    private static final int fewCloudy=R.drawable.few_clouds;
    private static final int partlyCloudy=R.drawable.partly_cloudy;
    private static final int overcast=R.drawable.overcast;
    private static final int calm=R.drawable.calm;
    private static final int cold=R.drawable.cold;
    private static final int drizzleRain=R.drawable.drizzle_rain;
    private static final int dust=R.drawable.dust;
    private static final int duststorm=R.drawable.duststorm;
    private static final int foggy=R.drawable.foggy;
    private static final int freezingRain=R.drawable.freezing_rain;
    private static final int freshBreeze=R.drawable.fresh_breeze;
    private static final int gale=R.drawable.gale;
    private static final int hail=R.drawable.hail;
    private static final int haze=R.drawable.haze;
    private static final int heavyRain=R.drawable.heavy_rain;
    private static final int heavyShowerRain=R.drawable.heavy_shower_rain;
    private static final int heavyShowerRainN=R.drawable.heavy_shower_rain_n;
    private static final int heavySnow=R.drawable.heavy_snow;
    private static final int heavyStorm=R.drawable.heavy_storm;
    private static final int heavyThunderStorm=R.drawable.heavy_thunderstorm;
    private static final int highWind=R.drawable.high_wind;
    private static final int hot=R.drawable.hot;
    private static final int hurricane=R.drawable.hurricane;
    private static final int lightBreeze=R.drawable.light_breeze;
    private static final int lightRain=R.drawable.light_rain;
    private static final int lightSnow=R.drawable.light_snow;
    private static final int mist=R.drawable.mist;
    private static final int moderate=R.drawable.moderate;
    private static final int moderateRain=R.drawable.moderate_rain;
    private static final int moderateSnow=R.drawable.moderate_snow;
    private static final int overcastN=R.drawable.overcast_n;
    private static final int rainAndSnow=R.drawable.rain_and_snow;
    private static final int sand=R.drawable.sand;
    private static final int sandStorm=R.drawable.sandstorm;
    private static final int severeStorm=R.drawable.severe_storm;
    private static final int showerRain=R.drawable.shower_rain;
    private static final int showerRainN=R.drawable.shower_rain_n;
    private static final int showerSnow=R.drawable.shower_snow;
    private static final int showerSnowN=R.drawable.shower_snow_n;
    private static final int sleet=R.drawable.sleet;
    private static final int snowFlurry=R.drawable.snow_flurry;
    private static final int snowFlurryN=R.drawable.snow_flurry_n;
    private static final int snowStorm=R.drawable.snowstorm;
    private static final int storm1=R.drawable.storm;
    private static final int storm=R.drawable.storm_1;
    private static final int strongBreeze=R.drawable.strong_breeze;
    private static final int strongGale=R.drawable.strong_gale;
    private static final int thunderShower=R.drawable.thunder_shower;
    private static final int tornado=R.drawable.tornado;
    private static final int tropicalStorm=R.drawable.tropical_storm;
    private static final int unknown=R.drawable.unknown;
    private static final int violentStorm=R.drawable.violent_storm;
    private static final int windy=R.drawable.windy;

    public static int getWeatherImage(int id,String str,boolean isHour){
        int imageId = 0;
        switch (id){
            case 100:
                if (isHour){
                    if (str.equals("晚上")||str.equals("凌晨")){
                        imageId=sunnyN;
                    }else {
                        imageId=sunny;
                    }
                }else {
                    imageId=sunny;
                }
                break;
            case 101:imageId=cloudy;break;
            case 102:imageId= fewCloudy;break;
            case 103:imageId= partlyCloudy;break;
            case 104:
                if (isHour){
                    if (str.equals("晚上")||str.equals("凌晨")){
                        imageId=overcastN;
                    }else {
                        imageId=overcast;
                    }
                }else {
                    imageId=overcast;
                }
                break;
            case 200:imageId= windy;break;
            case 201:imageId= calm;break;
            case 202:imageId= lightBreeze;break;
            case 203:imageId= moderate;break;
            case 204:imageId= freshBreeze;break;
            case 205:imageId= strongBreeze;break;
            case 206:imageId=highWind;break;
            case 207:imageId=gale;break;
            case 208:imageId=strongGale;break;
            case 209:imageId=storm1;break;
            case 210:imageId=violentStorm;break;
            case 211:imageId=hurricane;break;
            case 212:imageId=tornado;break;
            case 213:imageId=tropicalStorm;break;
            case 300:
                if (isHour){
                    if (str.equals("晚上")||str.equals("凌晨")){
                        imageId=showerRainN;
                    }else {
                        imageId=showerRain;
                    }
                }else {
                    imageId=showerRain;
                }
                break;
            case 301:
                if (isHour){
                    if (str.equals("晚上")||str.equals("凌晨")){
                        imageId=heavyShowerRainN;
                    }else {
                        imageId=heavyShowerRain;
                    }
                }else {
                    imageId=heavyShowerRain;
                }
                break;
            case 302:imageId=thunderShower;break;
            case 303:imageId=heavyThunderStorm;break;
            case 304:imageId=hail;break;
            case 305:imageId=lightRain;break;
            case 306:imageId=moderateRain;break;
            case 307:imageId=heavyRain;break;
            case 308:imageId=heavyRain;break;
            case 309:imageId=drizzleRain;break;
            case 310:imageId=storm;break;
            case 311:imageId=heavyStorm;break;
            case 312:imageId=severeStorm;break;
            case 313:imageId=freezingRain;break;
            case 400:imageId=lightSnow;break;
            case 401:imageId=moderateSnow;break;
            case 402:imageId=heavySnow;break;
            case 403:imageId=snowStorm;break;
            case 404:imageId=sleet;break;
            case 405:imageId=rainAndSnow;break;
            case 406:
                if (isHour){
                    if (str.equals("晚上")||str.equals("凌晨")){
                        imageId=showerSnowN;
                    }else {
                        imageId=showerSnow;
                    }
                }else {
                    imageId=showerSnow;
                }
                break;
            case 407:
                if (isHour){
                    if (str.equals("晚上")||str.equals("凌晨")){
                        imageId=snowFlurryN;
                    }else {
                        imageId=snowFlurry;
                    }
                }else {
                    imageId=snowFlurry;
                }
                break;
            case 500:imageId=mist;break;
            case 501:imageId=foggy;break;
            case 502:imageId=haze;break;
            case 503:imageId=sand;break;
            case 504:imageId=dust;break;
            case 507:imageId=duststorm;break;
            case 508:imageId=sandStorm;break;
            case 900:imageId=hot;break;
            case 901:imageId=cold;break;
            case 999:imageId=unknown;break;
        }
        return imageId;
    }

    public static String getWeatherName(int id){
        String name = "";
        switch (id){
            case 100:name="晴";break;
            case 101:name="多云";break;
            case 102:name="少云";break;
            case 103:name="晴间多云";break;
            case 104:name="阴";break;
            case 200:name="有风";break;
            case 201:name="平静";break;
            case 202:name="微风";break;
            case 203:name="和风";break;
            case 204:name="清风";break;
            case 205:name="强风";break;
            case 206:name="疾风";break;
            case 207:name="大风";break;
            case 208:name="烈风";break;
            case 209:name="风暴";break;
            case 210:name="狂爆风";break;
            case 211:name="飓风";break;
            case 212:name="龙卷风";break;
            case 213:name="热带风暴";break;
            case 300:name="阵雨";break;
            case 301:name="强阵雨";break;
            case 302:name="雷阵雨";break;
            case 303:name="强雷阵雨";break;
            case 304:name="雷阵雨伴有冰雹";break;
            case 305:name="小雨";break;
            case 306:name="中雨";break;
            case 307:name="大雨";break;
            case 308:name="极端降雨";break;
            case 309:name="毛毛雨";break;
            case 310:name="暴雨";break;
            case 311:name="大暴雨";break;
            case 312:name="特大暴雨";break;
            case 313:name="冻雨";break;
            case 400:name="小雪";break;
            case 401:name="中雪";break;
            case 402:name="大雪";break;
            case 403:name="暴雪";break;
            case 404:name="雨夹雪";break;
            case 405:name="雨雪天气";break;
            case 406:name="阵雨夹雪";break;
            case 407:name="阵雪";break;
            case 500:name="薄雾";break;
            case 501:name="雾";break;
            case 502:name="霾";break;
            case 503:name="扬沙";break;
            case 504:name="浮尘";break;
            case 507:name="沙尘暴";break;
            case 508:name="强沙尘暴";break;
            case 900:name="热";break;
            case 901:name="冷";break;
            case 999:name="未知";break;
        }
        return name;
    }

    public static final String getDivisionString(String str,boolean isSpace){
        String strResult="";

        if (isSpace){
            String []strResults=str.split("\\s+");
            strResult=getTimeSection(strResults[1])+strResults[1];
        }else {
            String []strResults=str.split("-");
            strResult=strResults[1]+"月"+strResults[2]+"日";
        }
        return strResult;
    }

    public static final String getTimeSection(String str){
        String[] strResult=str.split(":");
        return getTimeSectionH(Integer.valueOf(strResult[0]));
    }

    public static final String getTimeSectionH(int hour){
        String string="";
        if (hour>0&&hour<=6){
            string="凌晨";
        }
        if (hour>6&&hour<=12){
            string="上午";
        }
        if (hour>12&&hour<=18){
            string="下午";
        }
        if (hour>18&&hour<24){
            string="晚上";
        }
        return string;
    }

    public static final String getAqiTxt(int aqi){
        String string="";
        if (aqi>0&&aqi<=50){
            string="优";
        }else if (aqi>50&&aqi<=100){
            string="良";
        }else if (aqi>100&&aqi<=150){
            string="轻度污染";
        }else if (aqi>150&&aqi<=200){
            string="中度污染";
        }else if (aqi>200&&aqi<=300){
            string="重度污染";
        }else if (aqi>300){
            string="严重污染";
        }
        return string;
    }

    public static final int getAqiGrade(int aqi){
        int grade=0;
        if (aqi>0&&aqi<=50){
            grade=1;
        }else if (aqi>50&&aqi<=100){
            grade=2;
        }else if (aqi>100&&aqi<=150){
            grade=3;
        }else if (aqi>150&&aqi<=200){
            grade=4;
        }else if (aqi>200&&aqi<=300){
            grade=5;
        }else if (aqi>300){
            grade=6;
        }
        return grade;
    }

    public static final String getBrfTxt(String type){
        String string="";
        switch (type){
            case "comf":string="舒适度指数";break;
            case "cw":string="洗车指数";break;
            case "drsg":string="穿衣指数";break;
            case "flu":string="感冒指数";break;
            case "sport":string="运动指数";break;
            case "trav":string="旅游指数";break;
            case "uv":string="紫外线指数";break;
            case "air":string="空气污染扩散条件指数";break;
            default:break;
        }
        return string;
    }

    public static final String getWindDir(String str){
        String string="";
        switch (str){
            case "rotation":string="旋转风";break;
            case "N":string="北";break;
            case "NE":string="东北";break;
            case "E":string="东";break;
            case "SE":string="东南";break;
            case "S":string="南";break;
            case "SW":string="西南";break;
            case "W":string="西";break;
            case  "NW":string="西北";break;
            default:string="无持续风向";break;
        }
        return string;
    }

    public static float getCircularDegrees(String strUp,String strDown){
        float degress = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss", Locale.getDefault());
        String dateTime = sdf.format(new java.util.Date());
        String[] strResultUp=strUp.split(":");
        String[] strResultDown=strDown.split(":");
        String[] strResultNow=dateTime.split("-");

        int upHour=Integer.valueOf(strResultUp[0]);
        int upMinu=Integer.valueOf(strResultUp[1]);
        int downHour=Integer.valueOf(strResultDown[0]);
        int downMinu=Integer.valueOf(strResultDown[1]);
        int nowHour=Integer.valueOf(strResultNow[0]);
        int nowMinu=Integer.valueOf(strResultNow[1]);

        int nowTime=nowHour*60*60+nowMinu*60;
        int downTime=downHour*60*60+downMinu*60;
        int upTime=upHour*60*60+upMinu*60;
        if (nowTime>upTime&&nowTime<downTime){
            degress=((float) (nowTime-upTime)/(float) (downTime-upTime))*180;
        }else {
            if (nowTime<=upTime){
                Provide.setIsUp(true);
            }
            if (nowTime>=downTime){
                Provide.setIsDown(true);
            }
            degress=0;
        }
        Log.i("degress+++++++",""+degress);
        return degress;
    }

    public static final boolean getNetInfor(ConnectivityManager connectivityManager) {
        //首先是获取网络连接管理者
        ConnectivityManager manager = connectivityManager;
        NetworkInfo info = manager.getActiveNetworkInfo();
        //网络状态存在并且是已连接状态
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static final String []getCityAndDistrict(String district,String city){
        String []strings=new String[2];
        strings[0]=district.substring(0,district.length()-1);
        strings[1]=city.substring(0,city.length()-1);
        return strings;
    }

}
