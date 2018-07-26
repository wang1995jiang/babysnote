package com.jpeng.demo.diarys;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 王将 on 2018/6/3.
 */

public class DiaryEntity extends LitePalSupport {
    private int id;
    private String time="";
    private String mood="";
    private String content="";
    private String weather="";
    private boolean isCollection=false;

    public int getId() {
        return id;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }



    public String getMood() {
        return mood;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public boolean isCollection() {
        return isCollection;
    }
}
