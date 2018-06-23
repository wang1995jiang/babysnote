package com.jpeng.demo.add;

import com.jpeng.demo.Image;
import com.jpeng.demo.diarys.Music;

/**
 * Created by 王将 on 2018/6/12.
 */

public class TypeSign {
    private int id;
    private int idEditext;
    private Image image;
    private Music music;
    private VideoInfo videoInfo;

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEditext(int idEditext) {
        this.idEditext = idEditext;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }

    public int getId() {
        return id;
    }

    public int getIdEditext() {
        return idEditext;
    }

    public Image getImage() {
        return image;
    }

    public Music getMusic() {
        return music;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }
}
