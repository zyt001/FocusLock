package com.zyt.kineticlock.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

import static android.support.constraint.Constraints.TAG;

public class AddTasksModel {

    private MyDatabaseHelper dbHelper;
    private SharedPreferencesHelper spHelper;


    public void saveTask(Context mContext,String title,String lockTime,int unLockMode,int modeNum,int alarmMode){
            dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
            dbHelper.getWritableDatabase();
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("title",title);
            values.put("lockTime",lockTime);
            values.put("unLockMode",unLockMode);
            values.put("modeNum",modeNum);
            values.put("alarmMode",alarmMode);
            db.insert("tb_task",null,values);
            values.clear();
            db.close();

    }

    public void isTitleExist(Context mContext,String title){
        dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("select * from tb_task where title=?",new String[]{title});
    }

}
