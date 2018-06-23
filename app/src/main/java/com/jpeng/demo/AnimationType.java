package com.jpeng.demo;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by 王将 on 2018/4/9.
 */
public class AnimationType {
    private static final int []opens={R.drawable.diary_fill,R.drawable.collection_fill,R.drawable.write_fill};

    public static void setScaleType(final ImageView imageView,final int id){
        Animation animation= AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.back);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageResource(opens[id]);
                imageView.startAnimation(AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.front));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);
    }
    public static void setRotateType(final ImageView imageView,final int id){
        final RotateAnimation animation=new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(1000);
        imageView.setImageResource(opens[id]);
        imageView.startAnimation(animation);
    }
    public static void setTranslateType(final ImageView imageView,final int id){
        final TranslateAnimation animation=new TranslateAnimation(0,30,0,0);
        animation.setDuration(500);
        imageView.setImageResource(opens[id]);
        imageView.startAnimation(animation);
    }
    public static void setNoType(final ImageView imageView,final int id){
        imageView.setImageResource(opens[id]);
    }
}
