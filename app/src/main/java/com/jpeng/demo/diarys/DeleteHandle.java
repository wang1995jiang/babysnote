package com.jpeng.demo.diarys;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by 王将 on 2018/6/5.
 */

public class DeleteHandle {
    private RelativeLayout relativeLayout;
    private DiaryEntity diaryEntity;
    private ImageView imageView;
    private boolean isAscertain;


    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }

    public void setAscertain(boolean ascertain) {
        isAscertain = ascertain;
    }

    public void setDiaryEntity(DiaryEntity diaryEntity) {
        this.diaryEntity = diaryEntity;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public DiaryEntity getDiaryEntity() {
        return diaryEntity;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isAscertain() {
        return isAscertain;
    }
}
