package com.zyt.kineticlock.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.utils.StringAndBitmapHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class WhiteAppModel {

    private MyDatabaseHelper dbHelper;
    private Map<String,Boolean> mapSelect =new HashMap<String, Boolean>();


    public void getWhiteApp(Context mContext,List<AppInfo> mAppInfo){
        PackageManager packageManager=mContext.getPackageManager();
        dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from tb_whiteApp",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                AppInfo appInfo=new AppInfo();
                appInfo.setAppName(cursor.getString(1));
                appInfo.setPackageName(cursor.getString(2));
                appInfo.setSelect(true);
                mapSelect.put(cursor.getString(2),true);
                try {
                    PackageInfo packageInfo=packageManager.getPackageInfo(cursor.getString(2),0);
                    appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                mAppInfo.add(appInfo);
            }
        }
        cursor.close();
        dbHelper.close();
    }

    public void deleteWhiteApp(Context mContext,List<AppInfo> mAppInfo,int position){

        if(mAppInfo!=null){
            dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
            dbHelper.getWritableDatabase();
            SQLiteDatabase database=dbHelper.getReadableDatabase();
            database.execSQL("delete  from tb_whiteApp where packageName=?",new String[]{mAppInfo.get(position).getPackageName()});
            dbHelper.close();

        }
    }


}
