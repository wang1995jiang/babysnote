package com.jpeng.demo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.jpeng.demo.face.FaceDB;
import com.jpeng.demo.vioce.ui.SimpleTransApplication;
import com.jpeng.jptabbar.JPTabBar;

import org.litepal.LitePalApplication;

/**
 * Created by 王将 on 2018/3/7.
 */

public class MyApplication extends SimpleTransApplication {

    private final String TAG = this.getClass().toString();
    public FaceDB mFaceDB;
    public Uri mImage;
    private static People people;
    private static Context context;
    private static JPTabBar jpTabBar;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
        mImage = null;
        Log.e("path+++++++++",this.getExternalCacheDir().getPath());
    }
    public void setCaptureImage(Uri uri) {
        mImage = uri;
    }

    public Uri getCaptureImage() {
        return mImage;
    }
    public static void setPeople(People people) {
        MyApplication.people = people;
    }

    public static People getPeople() {
        return people;
    }


    public static void setJpTabBar(JPTabBar jpTabBar) {
        MyApplication.jpTabBar = jpTabBar;
    }

    public static JPTabBar getJpTabBar() {
        return jpTabBar;
    }

    public static Context getContext(){
        return context;
    }

    /**
     * @param path
     * @return
     */
    public static Bitmap decodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            //op.inMutable = true;
            res = BitmapFactory.decodeFile(path, op);
            //rotate and scale.
            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
