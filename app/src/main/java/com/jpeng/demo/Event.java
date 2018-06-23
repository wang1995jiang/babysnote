package com.jpeng.demo;

/**
 * Created by 王将 on 2018/3/15.
 */

public class Event {
    private int id;
    private String labelEvent;
    private String timeEvent;
    private String contentEvent;
    private String photoEvent;
    private String addressEvent;
    private boolean isCollection;

    public void setId(int id) {
        this.id = id;
    }

    public void setAddressEvent(String addressEvent) {
        this.addressEvent = addressEvent;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public void setContentEvent(String contentEvent) {
        this.contentEvent = contentEvent;
    }

    public void setLabelEvent(String labelEvent) {
        this.labelEvent = labelEvent;
    }

    public void setPhotoEvent(String photoEvent) {
        this.photoEvent = photoEvent;
    }

    public void setTimeEvent(String timeEvent) {
        this.timeEvent = timeEvent;
    }

    public int getId() {
        return id;
    }

    public String getAddressEvent() {
        return addressEvent;
    }

    public String getContentEvent() {
        return contentEvent;
    }

    public String getLabelEvent() {
        return labelEvent;
    }

    public String getPhotoEvent() {
        return photoEvent;
    }

    public String getTimeEvent() {
        return timeEvent;
    }
    public boolean getCollection(){
        return isCollection;
    }
}
