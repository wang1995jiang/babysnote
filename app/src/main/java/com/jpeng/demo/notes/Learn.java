package com.jpeng.demo.notes;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 王将 on 2018/3/23.
 */

public class Learn extends LitePalSupport {
    private int id;
    private String createTime="";
    private String labelLearn="";
    private String title="";
    private String contentLearn="";
    private String pricesLearn="";
    private String musicPath="";
    private String videoPath="";
    private String addressLearn="";
    private String sigh="";
    private boolean isCollection;

    public int getId() {
        return id;
    }

    public void setSigh(String sigh) {
        this.sigh = sigh;
    }

    public String getSigh() {
        return sigh;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public void setAddressLearn(String addressLearn) {
        this.addressLearn = addressLearn;
    }

    public void setContentLearn(String contentLearn) {
        this.contentLearn = contentLearn;
    }

    public void setLabelLearn(String labelLearn) {
        this.labelLearn = labelLearn;
    }

    public void setPricesLearn(String pricesLearn) {
        this.pricesLearn = pricesLearn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddressLearn() {
        return addressLearn;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getContentLearn() {
        return contentLearn;
    }

    public String getLabelLearn() {
        return labelLearn;
    }

    public String getPricesLearn() {
        return pricesLearn;
    }

    public String getTitle() {
        return title;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public String getVideoPath() {
        return videoPath;
    }
}
