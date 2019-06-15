package com.zyt.kineticlock.service;

import android.app.ActivityManager;
import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.zyt.kineticlock.R;
import com.zyt.kineticlock.activity.TaskActivity;
import com.zyt.kineticlock.adapter.LockAppAdapter;
import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.LockServiceContract;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.presenter.LockServicePresenter;
import com.zyt.kineticlock.utils.SensorManagerHelper;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

import java.util.ArrayList;

import java.util.List;


public class TaskService extends Service implements LockServiceContract.View {

    private LockServiceContract.Presenter mLockServicePresenter;
    private TextView tv_taskTime, tv_taskTitle, tv_taskMode, tv_taskNum;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private  List<AppInfo> appList = new ArrayList<AppInfo>();
    private List<Task>taskList=new ArrayList<Task>();
    private int timeStep=0,shakeNumber;
    private WindowManager windowManager;
    private Context mContext;
    private WindowManager.LayoutParams layoutParams;
    private SharedPreferencesHelper spHelper;
    private CountDownTimer timer;
    private Task task =new Task();
    private View view;

    private ConstraintLayout bg;
    private String pic=null,name,packageName;
    private Boolean isAppRun=false;



    @Override
    public void setPresenter(LockServiceContract.Presenter presenter) {
        this.mLockServicePresenter=presenter;
    }



    public TaskService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        view=View.inflate(getApplicationContext(), R.layout.layout_floatlock,null);


        new LockServicePresenter(this);
        mContext=this;

        initView();
        showTask();
        showBackground();
        showFloatingWindow();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initView(){

        //init FloatView
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN
                |WindowManager.LayoutParams.FIRST_SUB_WINDOW
                |WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;

        //initView
        tv_taskTime =view.findViewById(R.id.tv_time);
        tv_taskTitle =view.findViewById(R.id.tv_name);
        tv_taskMode =view.findViewById(R.id.tv_mode);
        tv_taskNum =view.findViewById(R.id.tv_number);

        bg=view.findViewById(R.id.bg);

        recyclerView=view.findViewById(R.id.recyclerview_app);
        //init WhiteApp
        mLockServicePresenter.getWhiteAppData(mContext,appList);
        layoutManager=new GridLayoutManager(TaskService.this,4);
        recyclerView.setLayoutManager(layoutManager);
        LockAppAdapter lockAppAdapter =new LockAppAdapter(appList);
        recyclerView.setAdapter(lockAppAdapter);
        lockAppAdapter.setItemOnClickListener(new LockAppAdapter.ItemOnClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                mLockServicePresenter.toApp(mContext,appList.get(position).getPackageName());
                packageName=appList.get(position).getPackageName();
                showRemoveWindow();
                isAppRun=true;
            }
        });




    }



    @Override
    public void showTask() {

        mLockServicePresenter.getTaskInfo(mContext,taskList);
        task.setTitle(taskList.get(0).getTitle());
        task.setLockTime(taskList.get(0).getLockTime());
        task.setAlarmMode(taskList.get(0).getAlarmMode());
        task.setModeNum(taskList.get(0).getModeNum());
        switch (taskList.get(0).getTaskMode()){
            case "番茄":
                task.setTaskMode("番茄");
                tv_taskMode.setVisibility(View.GONE);
                tv_taskNum.setVisibility(View.GONE);
                break;
            case "专注":
                task.setTaskMode("专注");
                shakeNumber=task.getModeNum();
                showShakeListener();
                break;
            case "禅定":
                task.setTaskMode("禅定");
                tv_taskMode.setVisibility(View.GONE);
                tv_taskNum.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                break;
        }

        //initData
        timeStep= task.getLockTime()*1000*60;
        tv_taskTitle.setText(String.valueOf(task.getTitle()));
        tv_taskTime.setText(String.valueOf(task.getLockTime()));
        tv_taskMode.setText(task.getTaskMode()+"模式");
        tv_taskNum.setText("剩余"+shakeNumber+"次");

        showTime();

    }

    @Override
    public void showBackground() {
        spHelper = new SharedPreferencesHelper(mContext, "Task");
        pic = (String) spHelper.getValue("pic", "");
        if(pic!=null){
            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    bg.setBackground(resource);
                }
            };

            Glide.with(this).load(pic).into(simpleTarget);
        }

    }

    @Override
    public void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                windowManager.addView(view, layoutParams);
            }
        }else {
            windowManager.addView(view, layoutParams);
        }
    }

    @Override
    public void showCloseWindow() {
        timer.cancel();
        if(view.isShown()){
            windowManager.removeView(view);
        }
        Intent stopIntent = new Intent(this, TaskService.class);
        stopService(stopIntent);
        switch (task.getAlarmMode()){
            case 1:
                break;
            case 2:
                Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                break;
        }
        ActivityManager mAm = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            if (rti.topActivity.getPackageName().equals(getPackageName())) {
                mAm.moveTaskToFront(rti.id, ActivityManager.MOVE_TASK_WITH_HOME);
                return;
            }
        }
        Intent resultIntent = new Intent(TaskService.this, TaskActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(resultIntent);

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

                isShowAppRun();

            }
            @Override
            public void onFinish() {
                showCloseWindow();
            }
        };
        timer.start();
    }

    @Override
    public void showShakeListener() {
        SensorManagerHelper sensorManagerHelper=new SensorManagerHelper(this);
        sensorManagerHelper.setOnShakeListener(new SensorManagerHelper.OnShakeListener() {
            @Override
            public void onShake() {
                if(shakeNumber!=0)
                {
                    shakeNumber--;
                    tv_taskNum.setText("剩余"+shakeNumber+"次");
                    if(shakeNumber<=0)
                    {
                        showCloseWindow();
                    }
                }

            }
        });
    }

    @Override
    public boolean showWhiteAppListener(Context mContext,String packageName) {
      return   mLockServicePresenter.isWhiteAppRun(mContext,packageName);
    }

    @Override
    public void showRemoveWindow() {
        if(view.isShown()){
            windowManager.removeView(view);
        }

    }


    @Override
    public void isShowAppRun() {

        if(isAppRun){
            if(showWhiteAppListener(mContext,packageName)){

            }
            else {
                showFloatingWindow();
                isAppRun=false;
            }
        }

    }

}
