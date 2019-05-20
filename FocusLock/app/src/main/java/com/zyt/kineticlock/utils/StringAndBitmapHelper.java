package com.zyt.kineticlock.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.bumptech.glide.load.engine.Resource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class StringAndBitmapHelper {

    private Bitmap bitmap;
    private String string;

    public Bitmap stringToBitmap(String string){
        if(string!=null){
            byte[]bytes=Base64.decode(string,Base64.DEFAULT);
            bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bitmap;
        }
        else {
            return null;
        }
    }


    public String bitmapToString(Bitmap bitmap){
        if(bitmap!=null){
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[]bytes=stream.toByteArray();
            string=Base64.encodeToString(bytes,Base64.DEFAULT);
            return string;
        }
        else {
            return "";
        }
    }


    //将drawable转换成可以用来存储的byte[]类型
    public byte[] toBlob(Drawable drawable) {
        if(drawable == null) {
            return null;
        }
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public Drawable changeToDrawable(Context mContext, byte[] blob){
        ArrayList<Drawable> drawables=new ArrayList<Drawable>();
        Bitmap bitmap=BitmapFactory.decodeByteArray(blob,0, blob.length,null);
        BitmapDrawable bitmapDrawable=new BitmapDrawable(mContext.getResources(),bitmap);
        Drawable drawable=bitmapDrawable;
       // drawables.add(drawable);

        return drawable;

    }




}
