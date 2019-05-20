package com.zyt.kineticlock.model;

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

import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.utils.StringAndBitmapHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class LockServiceModel {

    private MyDatabaseHelper dbHelper;

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

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo =mContext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList =mContext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            mContext.startActivity(intent);
        }
    }


    public boolean isWhiteAppRun(Context mContext,String packageName){
        class RecentUseComparator implements Comparator<UsageStats> {
            @Override
            public int compare(UsageStats lhs, UsageStats rhs) {
                return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
            }
        }

        String currentTopPackage = null;
        try {
            RecentUseComparator mRecentComp = new RecentUseComparator();
            long ts = System.currentTimeMillis();
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000 * 10, ts);

            Collections.sort(usageStats, mRecentComp);
            currentTopPackage = usageStats.get(0).getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "isWhiteAppRun: +++++++++当前顶端APP"+currentTopPackage);
        Log.i(TAG, "isWhiteAppRun: +++++++++++启动APP"+packageName);
        if (currentTopPackage.equals(packageName)||(currentTopPackage.equals("com.miui.securitycenter"))) {
            return true;
        } else {
            return false;
        }
    }

}
