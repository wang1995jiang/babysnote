package com.jpeng.demo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 王将 on 2018/3/10.
 */

public class SaveConfiguration extends AppCompatActivity{

    public void saveString(String key,String data){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString(key,data);
        editor.apply();
    }
    public void saveInt(String key,int data){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt(key,data);
        editor.apply();
    }

    public void saveBoolean(String key,boolean data){
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean(key,data);
        editor.apply();
    }
}

