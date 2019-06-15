package com.zyt.kineticlock.model;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.service.TaskService;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;
import com.zyt.kineticlock.utils.StringAndBitmapHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class LockServiceModel {

    private MyDatabaseHelper dbHelper;
    private SharedPreferencesHelper spHelper;
    private String pic,name;
    private Task task = new Task();
    private String currentTopPackage = null;


    public void getTaskInfo(Context mContext, List<Task> taskList) {

        spHelper = new SharedPreferencesHelper(mContext, "Task");

        name = (String) spHelper.getValue("title", "");
        dbHelper = new MyDatabaseHelper(mContext, "Lock.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_task where title=?", new String[]{name});
        while (cursor.moveToNext()) {
            task.setTitle(cursor.getString(1));
            task.setLockTime(cursor.getInt(2));
            task.setModeNum(cursor.getInt(4));
            task.setAlarmMode(cursor.getInt(5));
            switch (cursor.getInt(3)) {
                case 0:
                    task.setTaskMode("番茄");
                    break;
                case 1:
                    task.setTaskMode("番茄");
                    break;
                case 2:
                    task.setTaskMode("专注");
                    break;
                case 3:
                    task.setTaskMode("禅定");
            }

            taskList.add(task);

        }
        cursor.close();
        db.close();
    }


    public void getWhiteApp(Context mContext, List<AppInfo> appInfoList){
        PackageManager packageManager=mContext.getPackageManager();
        dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_whiteApp",null);
        if(cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
              AppInfo appInfo=new AppInfo();
              appInfo.setAppName(cursor.getString(1));
              appInfo.setPackageName(cursor.getString(2));
                try {
                    PackageInfo packageInfo=packageManager.getPackageInfo(cursor.getString(2),0);
                    appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                appInfoList.add(appInfo);
            }
        }
        cursor.close();
        db.close();


    }


    public void toApp(Context mContext,String packagename)
    {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name
        PackageInfo packageinfo = null;
        try {
            packageinfo =mContext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        List<ResolveInfo> resolveinfoList =mContext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String packageName = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            mContext.startActivity(intent);
        }
    }


    public boolean isWhiteAppRun(Context mContext,String packageName){

        UsageStatsManager sUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                currentTopPackage = event.getPackageName();
            }
        }
       // Log.i(TAG, "当前顶层APP: ++++++++++"+currentTopPackage);
         if (currentTopPackage.equals(packageName)||(currentTopPackage.equals("com.miui.securitycenter"))) {
            return true;
        } else if(currentTopPackage.equals(null)) {
            return false;
        }else {
            return false;
        }
    }

}
