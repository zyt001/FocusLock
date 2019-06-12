package com.zyt.kineticlock.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

import java.util.List;

public class TasksModel {

    private MyDatabaseHelper dbHelper;
    private SharedPreferencesHelper spHelper;


    private Boolean isFirst;


    public void createDB(Context mContext)
    {

        dbHelper=new MyDatabaseHelper(mContext,"Lock.db",null,1);
        dbHelper.getWritableDatabase();
        spHelper=new SharedPreferencesHelper(mContext,"Task");
        isFirst=(Boolean)spHelper.getValue("isFirst",true);

        if(isFirst)
        {
            try {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.execSQL("insert into tb_task (title,lockTime,unLockMode,modeNum,alarmMode) " +
                        "values('向左滑动删除','1','1','0','1') ");
                db.execSQL("insert into tb_task (title,lockTime,unLockMode,modeNum,alarmMode) " +
                        "values('点我开始','1','2','10','2') ");
                db.close();
                spHelper.putValue("isFirst",false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void deleteTask(String title)
    {
        try {
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            db.execSQL("delete from tb_task where title=?",new  String[]{title});
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getTask(List<Task> taskList)
    {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor =db.rawQuery("select * from tb_task",null);
        if(cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                Task task=new Task();
                task.setTitle(cursor.getString(1));

                task.setLockTime(cursor.getInt(2));
                switch (cursor.getInt(3)){
                    case 0:
                        task.setTaskMode("番茄");
                        break;
                    case 1:
                        task.setTaskMode("番茄");
                        break;
                    case 2:
                        task.setTaskMode("专注");
                        task.setModeNum(cursor.getInt(4));
                        break;
                    case 3:
                        task.setTaskMode("禅定");
                        task.setModeNum(cursor.getInt(4));
                }
                taskList.add(task);

            }
            cursor.close();
        }
        db.close();
    }

}
