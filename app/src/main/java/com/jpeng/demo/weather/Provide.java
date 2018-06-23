package com.jpeng.demo.weather;

/**
 * Created by 王将 on 2018/4/24.
 */

public class Provide {
    private static AirNow airNow;
    private static Weather weather;
    private static String bingPic="";
    private static String sunUp="";
    private static String sunDown="";
    private static boolean isUp=false;
    private static boolean isDown=false;
    private static boolean isLinkNet=true;

    private static boolean isSuccessBing=false;
    private static boolean isSuccessWeaNow=false;
    private static boolean isSuccessAirNow=false;


    public static void setIsLinkNet(boolean isLinkNet) {
        Provide.isLinkNet = isLinkNet;
    }

    public static boolean isIsLinkNet() {
        return isLinkNet;
    }

    public static void setIsSuccessAirNow(boolean isSuccessAirNow) {
        Provide.isSuccessAirNow = isSuccessAirNow;
    }

    public static void setIsSuccessBing(boolean isSuccessBing) {
        Provide.isSuccessBing = isSuccessBing;
    }

    public static void setIsSuccessWeaNow(boolean isSuccessWeaNow) {
        Provide.isSuccessWeaNow = isSuccessWeaNow;
    }

    public static boolean isIsSuccessAirNow() {
        return isSuccessAirNow;
    }

    public static boolean isIsSuccessBing() {
        return isSuccessBing;
    }

    public static boolean isIsSuccessWeaNow() {
        return isSuccessWeaNow;
    }

    public static void setAirNow(AirNow airNow) {
        Provide.airNow = airNow;
    }

    public static AirNow getAirNow() {
        return airNow;
    }

    public static void setWeather(Weather weather) {
        Provide.weather = weather;
    }

    public static Weather getWeather() {
        return weather;
    }

    public static void setBingPic(String bingPic) {
        Provide.bingPic = bingPic;
    }

    public static String getBingPic() {
        return bingPic;
    }

    public static void setIsDown(boolean isDown) {
        Provide.isDown = isDown;
    }

    public static void setIsUp(boolean isUp) {
        Provide.isUp = isUp;
    }

    public static boolean isIsDown() {
        return isDown;
    }

    public static boolean isIsUp() {
        return isUp;
    }

    public static void setSunDown(String sunDown) {
        Provide.sunDown = sunDown;
    }

    public static void setSunUp(String sunUp) {
        Provide.sunUp = sunUp;
    }

    public static String getSunDown() {
        return sunDown;
    }

    public static String getSunUp() {
        return sunUp;
    }

}
