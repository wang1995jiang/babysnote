package com.jpeng.demo.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.jpeng.demo.R;


/**
 * Created by 王将 on 2018/4/22.
 */

public class Huayuan extends View {
    private Paint mPaint,mPaint1,mPaint2,mPatint3,mPaint4,mPaint5;
    private static float angle;
    float an=0;
    Bitmap bitmap;
    /**
     * 圆的宽度
     */
    private int mCircleWidth = 3;
    public Huayuan(Context context, float ag) {
        this(context, null);
        this.angle=ag;
        Log.i("angle++++a",""+angle);
    }

    public Huayuan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Huayuan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint1=new Paint();
        mPaint2=new Paint();
        mPatint3=new Paint();
        mPaint4=new Paint();
        mPaint5=new Paint();
        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sun_up)).getBitmap();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (an<=angle){
                    postInvalidate();
                    SystemClock.sleep(50);
                    an++;
                }
            }
        }).start();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //Log.i("angle++++++b",""+an);
        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(Color.WHITE);

        mPaint2.setColor(Color.WHITE);
        mPaint2.setTextSize(30);
        mPaint2.setStrokeWidth(mCircleWidth);

        mPaint4.setColor(Color.WHITE);
        mPaint4.setTextSize(30);
        mPaint4.setStrokeWidth(mCircleWidth);

        mPaint5.setColor(Color.WHITE);
        mPaint5.setTextSize(30);
        mPaint5.setStrokeWidth(mCircleWidth);

        mPatint3.setDither(true);
        mPatint3.setFilterBitmap(true);

        float left=getWidth()/2-400+400*(1-(float)Math.cos(Math.PI*an/180))-getBit().getWidth()/2;
        float top=getHeight()-40-400*(float) Math.sin(Math.PI*an/180)-getBit().getHeight()/2;

        String string="日出日落";
        Rect bounds = new Rect();
        Rect upBound=new Rect();
        Rect downBound=new Rect();

        mPaint2.getTextBounds(string, 0, string.length(), bounds);
        mPaint4.getTextBounds(Provide.getSunUp(),0,Provide.getSunUp().length(),upBound);
        mPaint5.getTextBounds(Provide.getSunDown(),0,Provide.getSunDown().length(),downBound);

        canvas.drawCircle(getWidth()/2,getHeight()-40,400,mPaint);
        if (Provide.isIsDown()||Provide.isIsUp()){
            if (Provide.isIsDown()){
                canvas.drawBitmap(getBit(),getWidth()/2+400-getBit().getWidth()/2,getHeight()-40-getBit().getHeight()/2,mPatint3);
            }
            if (Provide.isIsUp()){
                canvas.drawBitmap(getBit(),getWidth()/2-400-getBit().getWidth()/2,getHeight()-40-getBit().getHeight()/2,mPatint3);
            }
        }else {
            canvas.drawBitmap(getBit(),left,top,mPatint3);
        }
        canvas.drawText(string,getWidth()/2-bounds.width() / 2,getHeight()-40-bounds.height()/2,mPaint2);
        canvas.drawLine(30,getHeight()-40,getWidth()-30,getHeight()-40,mPaint);
        canvas.drawText(Provide.getSunUp(),getWidth()/2-400-upBound.width()/2,getHeight()-10,mPaint4);
        canvas.drawText(Provide.getSunDown(),getWidth()/2+400-downBound.width()/2,getHeight()-10,mPaint5);
    }

    private Bitmap getBit(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        int newWidth = 70;
        int newHeight = 70;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap mbitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return mbitmap;
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

}
