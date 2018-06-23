package com.jpeng.demo.notes;

import org.litepal.crud.DataSupport;

/**
 * Created by 王将 on 2018/3/23.
 */

public class Note extends DataSupport {
    private int id;
    private String createTime="";
    private String labelNote="";
    private String contentNote="";
    private String addressNote="";
    private boolean isCollection=false;

    public int getId() {
        return id;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public void setAddressNote(String addressNote) {
        this.addressNote = addressNote;
    }

    public void setContentNote(String contentNote) {
        this.contentNote = contentNote;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setLabelNote(String labelNote) {
        this.labelNote = labelNote;
    }

    public String getAddressNote() {
        return addressNote;
    }

    public String getContentNote() {
        return contentNote;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getLabelNote() {
        return labelNote;
    }

    public boolean isCollection() {
        return isCollection;
    }
}
