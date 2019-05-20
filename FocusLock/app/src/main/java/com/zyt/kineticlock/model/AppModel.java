package com.zyt.kineticlock.model;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.utils.StringAndBitmapHelper;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class AppModel {

    private StringAndBitmapHelper sabHelper=new StringAndBitmapHelper();
    private MyDatabaseHelper dbHelper;
    private Drawable drawable;


    public void getAppList(Context mContext,List<AppInfo> mAppInfo) {
       PackageManager packageManager=mContext.getPackageManager();

        List<PackageInfo>packages=packageManager.getInstalledPackages(0);
        for(int i=0;i<packages.size();i++){
            PackageInfo packageInfo=packages.get(i);
            AppInfo appInfo=new AppInfo();

            appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
            appInfo.setAppName(packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
            appInfo.setPackageName(packageInfo.packageName);

            dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
            dbHelper.getWritableDatabase();
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            Cursor cursor=db.rawQuery("select * from tb_whiteApp",null);
            if(cursor!=null&&cursor.getCount()>0){
                while (cursor.moveToNext()){
                    Log.i(TAG, "getAppList: 白名单aaaaaaa"+packageInfo.packageName);
                    if(packageInfo.packageName==cursor.getString(2)){
                        appInfo.setSelect(true);
                        Log.i(TAG, "getAppList: 加入名单aaaaaaaaaaa");
                    }
                    else {
                        appInfo.setSelect(false);
                        Log.i(TAG, "getAppList: 继续aaaaaaaaaaa");
                    }
                }
            }


            mAppInfo.add(appInfo);

        }

    }


    public void saveWhiteApp(Context mContext, List<AppInfo> mAppInfo, int positon){

        if(mAppInfo!=null){
            dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
            dbHelper.getWritableDatabase();
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("appName",mAppInfo.get(positon).getAppName());
            values.put("packageName",mAppInfo.get(positon).getPackageName());
            values.put("isSelect",mAppInfo.get(positon).isSelect());
            db.insert("tb_whiteApp",null,values);
            values.clear();
            db.close();
        }

    }




}
