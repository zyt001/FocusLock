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

public class AppModel {

    private MyDatabaseHelper dbHelper;
    private Map<String,Boolean> mapSelect =new HashMap<String, Boolean>();


    public void getWhiteApp(Context mContext){
        dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from tb_whiteApp",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                mapSelect.put(cursor.getString(2),true);
            }
        }
        cursor.close();
        dbHelper.close();
    }

    public void getAppList(Context mContext,List<AppInfo> mAppInfo) {
        getWhiteApp(mContext);
       PackageManager packageManager=mContext.getPackageManager();

        List<PackageInfo>packages=packageManager.getInstalledPackages(0);
        for(int i=0;i<packages.size();i++){
            PackageInfo packageInfo=packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 ){
                AppInfo appInfo=new AppInfo();
                if(!mapSelect.isEmpty()&&mapSelect.containsKey(packageInfo.packageName)){

                }else {
                    appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                    appInfo.setAppName(packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
                    appInfo.setPackageName(packageInfo.packageName);
                    mAppInfo.add(appInfo);
                }

            }

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
