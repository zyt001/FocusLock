package com.zyt.kineticlock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_TASK="create table tb_task("
            +"id integer primary key autoincrement not null,"
            +"title text,"
            +"lockTime text,"
            +"unLockMode integer,"
            +"modeNum integer,"
            +"alarmMode integer)";

    public static final String CREATE_WHITEAPP="create table tb_whiteApp("
            +"id integer primary key autoincrement not null,"
            +"appName text,"
            +"packageName text,"
            +"isSelect boolean)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK);
        db.execSQL(CREATE_WHITEAPP);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
