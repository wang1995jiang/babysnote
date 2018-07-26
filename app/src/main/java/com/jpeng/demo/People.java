package com.jpeng.demo;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 王将 on 2018/3/10.
 */

public class People extends LitePalSupport{
    private boolean isDetecter;
    private String nickName;
    private String personalitySignature;
    private String headImage;
    private int animationType=0;
    private int toolbarColor=-12627531;
    private boolean isUserGesture;
    private String learnLabel="";
    private String passWord="";
    private String musicUrl="";
    private String musicTitle="";
    private boolean isMusic;
    private boolean isEncryption;
    private boolean isUseFace;
    private int diaryAnimation=0;
    private String address;
    private String city="";
    private String district="";

    public void setPersonalitySignature(String personalitySignature) {
        this.personalitySignature = personalitySignature;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDiaryAnimation(int diaryAnimation) {
        this.diaryAnimation = diaryAnimation;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public int getDiaryAnimation() {
        return diaryAnimation;
    }

    public void setDetecter(boolean detecter) {
        isDetecter = detecter;
    }

    public boolean isDetecter() {
        return isDetecter;
    }

    public void setUseFace(boolean useFace) {
        isUseFace = useFace;
    }

    public boolean isUseFace() {
        return isUseFace;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setEncryption(boolean encryption) {
        isEncryption = encryption;
    }

    public void setMusic(boolean music) {
        isMusic = music;
    }

    public boolean isEncryption() {
        return isEncryption;
    }

    public boolean isMusic() {
        return isMusic;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getLearnLabel() {
        return learnLabel;
    }

    public void setLearnLabel(String learnLabel) {
        this.learnLabel = learnLabel;
    }

    public void setUserGesture(boolean userGesture) {
        isUserGesture = userGesture;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getHeadImage() {
        return headImage;
    }

    public boolean isUserGesture() {
        return isUserGesture;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }

    public void setToolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public int getAnimationType() {
        return animationType;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPersonalitySignature() {
        return personalitySignature;
    }
}
