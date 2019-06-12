package com.zyt.kineticlock.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyf.immersionbar.ImmersionBar;
import com.zyt.kineticlock.R;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.LockTomatoContract;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.service.TaskService;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

public class TomatoActivity extends AppCompatActivity implements LockTomatoContract.View {

    private LockTomatoContract.Presenter mTomatoPresenter;
    private SharedPreferencesHelper spHelper;
    private MyDatabaseHelper dbHelper;
    private ConstraintLayout bg;
    private CountDownTimer timer;
    private TextView tv_taskTime;
    private TextView tv_name;
    private String name,pic;
    private Context mContext;
    private int  timeStep=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato);
        mContext=this;

        initView();
        showTask();
        showBackground();
    }


    @Override
    public void initView() {

        tv_name=findViewById(R.id.tv_name);
        tv_taskTime=findViewById(R.id.tv_time);
        bg=findViewById(R.id.bg);

        ImmersionBar.with(this).init();

    }

    @Override
    public void showTask() {
        spHelper=new SharedPreferencesHelper(TomatoActivity.this,"Task");
        pic=(String) spHelper.getValue("pic","");
        name=(String)spHelper.getValue("title","");
        dbHelper=new MyDatabaseHelper(this,"Lock.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor =db.rawQuery("select * from tb_task where title=?",new String[]{name});
        if(cursor.moveToNext()){
            tv_name.setText(cursor.getString(1));
            tv_taskTime.setText(String.valueOf(cursor.getInt(2)));
            timeStep= cursor.getInt(2)*1000*60;
        }
        cursor.close();
        dbHelper.close();

        showTime();
    }

    @Override
    public void showBackground() {
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                bg.setBackground(resource);
            }
        };

        Glide.with(this).load(pic).into(simpleTarget);
    }

    @Override
    public void exitDialog() {
        AlertDialog dialog=new AlertDialog.Builder(mContext)
                .setTitle("番茄钟")
                .setMessage("倒计时没结束，你确定要退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public void showTime() {
        if (timer!=null){
            timer.cancel();
        }
        timer=new CountDownTimer(timeStep,1000) {
            @Override
            public void onTick(long t) {
                long day = t / (1000 * 24 * 60 * 60);
                long hour = (t - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60);
                long minute = (t - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60);
                long second = (t - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;
                if(hour>0)
                {
                    tv_taskTime.setText(hour+"小时"+minute+"分"+second+"秒");
                }else if(hour<=0&&minute>0)
                {
                    tv_taskTime.setText(minute+"分"+second+"秒");
                }else if(hour<=0&&minute<=0)
                {
                    tv_taskTime.setText(String.valueOf(second)+"秒");
                }

            }
            @Override
            public void onFinish() {
                finish();
            }
        };
        timer.start();
    }

    @Override
    public void onBackPressed() {
        exitDialog();
    }



    @Override
    public void setPresenter(LockTomatoContract.Presenter presenter) {
       this.mTomatoPresenter=presenter;
    }
}

