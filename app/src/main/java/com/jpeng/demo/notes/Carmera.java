package com.jpeng.demo.notes;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 王将 on 2018/3/27.
 */

public class Carmera extends LitePalSupport{
    private int id;
    private String createTime;
    private String labelCarmera;
    private String pathCarmera;
    private String moodCarmera;
    private String addressCarmera;
    private boolean isCollection;

    public int getId() {
        return id;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setAddressCarmera(String addressCarmera) {
        this.addressCarmera = addressCarmera;
    }

    public void setLabelCarmera(String labelCarmera) {
        this.labelCarmera = labelCarmera;
    }

    public void setMoodCarmera(String moodCarmera) {
        this.moodCarmera = moodCarmera;
    }

    public void setPathCarmera(String pathCarmera) {
        this.pathCarmera = pathCarmera;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public String getAddressCarmera() {
        return addressCarmera;
    }

    public String getMoodCarmera() {
        return moodCarmera;
    }

    public String getPathCarmera() {
        return pathCarmera;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getLabelCarmera() {
        return labelCarmera;
    }

}
