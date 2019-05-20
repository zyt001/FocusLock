package com.zyt.kineticlock.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zyt.kineticlock.R;
import com.zyt.kineticlock.activity.TaskActivity;
import com.zyt.kineticlock.adapter.WhiteAppAdapter;
import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.LockServiceContract;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.presenter.LockServicePresenter;
import com.zyt.kineticlock.utils.SensorManagerHelper;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class TaskService extends Service implements LockServiceContract.View {

    private LockServiceContract.Presenter mLockServicePresenter;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private  List<AppInfo> appList = new ArrayList<AppInfo>();
    private int timeStep=0,shakeNumber;
    private WindowManager windowManager;
    private Context mContext;
    private WindowManager.LayoutParams layoutParams;
    private SharedPreferencesHelper spHelper;
    private MyDatabaseHelper dbHelper;
    private CountDownTimer timer;
    private Task task =new Task();
    private View view,whiteAppView;
    private TextView tv_taskTime, tv_taskTitle, tv_taskMode, tv_taskNum;
    private ConstraintLayout bg;
    private String pic,name,packageName;
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
        whiteAppView=View.inflate(TaskService.this,R.layout.layout_appdialog,null);

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
                //|WindowManager.LayoutParams.TYPE_PRIORITY_PHONE
                |WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                //|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN;
                //|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        // layoutParams.width = layoutParams.MATCH_PARENT;
        // layoutParams.height = layoutParams.MATCH_PARENT;

        //initView
        tv_taskTime =view.findViewById(R.id.tv_time);
        tv_taskTitle =view.findViewById(R.id.tv_name);
        tv_taskMode =view.findViewById(R.id.tv_mode);
        tv_taskNum =view.findViewById(R.id.tv_number);

        bg=view.findViewById(R.id.bg);

        recyclerView=view.findViewById(R.id.recyclerview);
        //init WhiteApp
        mLockServicePresenter.getWhiteAppData(mContext,appList);
        layoutManager=new GridLayoutManager(TaskService.this,4);
        recyclerView.setLayoutManager(layoutManager);
        WhiteAppAdapter whiteAppAdapter =new WhiteAppAdapter(appList);
        recyclerView.setAdapter(whiteAppAdapter);
        whiteAppAdapter.setItemOnClickListener(new WhiteAppAdapter.ItemOnClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                mLockServicePresenter.toApp(mContext,appList.get(position).getPackageName());
                packageName=appList.get(position).getPackageName();
                showRemoveWindow();
                isAppRun=true;
            }
        });


    }

    private void getTask(){
        spHelper=new SharedPreferencesHelper(TaskService.this,"Task");
        pic=(String) spHelper.getValue("pic","");
        name=(String)spHelper.getValue("title","");
        dbHelper=new MyDatabaseHelper(this,"Lock.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor =db.rawQuery("select * from tb_task where title=?",new String[]{name});

        if(cursor.moveToNext()){

            task.setTitle(cursor.getString(1));
            task.setLockTime(cursor.getInt(2));
            task.setAlarmMode(cursor.getInt(5));

            switch (cursor.getInt(3)){
                case 0:
                    task.setTaskMode("时间");
                    tv_taskMode.setVisibility(View.GONE);
                    tv_taskNum.setVisibility(View.GONE);


                    break;
                case 1:
                    task.setTaskMode("时间");
                    tv_taskMode.setVisibility(View.GONE);
                    tv_taskNum.setVisibility(View.GONE);

                    break;
                case 2:
                    task.setTaskMode("摇动手机");
                    shakeNumber=cursor.getInt(4);
                    showShakeListener();
                    break;
            }
        }
        cursor.close();
        db.close();


        //initData
        timeStep= task.getLockTime()*1000*60;
        tv_taskTime.setText(String.valueOf(task.getLockTime()));
        tv_taskMode.setText(task.getTaskMode());
        tv_taskNum.setText("剩余"+shakeNumber+"次");

        showTime();

    }

    @Override
    public void showTask() {
        getTask();
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
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity.getPackageName().equals(getPackageName())) {
                mAm.moveTaskToFront(rti.id, ActivityManager.MOVE_TASK_WITH_HOME);
                return;
            }
        }
        //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
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
    public void showAddWindow() {
        windowManager.addView(view,layoutParams);
    }

    @Override
    public void isShowAppRun() {



        Log.i(TAG, "当前APP+++++++++++"+packageName);
        if(isAppRun){
            if(showWhiteAppListener(mContext,packageName)){
               Log.i(TAG, "isShowAppRun: 匹配+++++++++++++++++++");
            }
            else {
                showFloatingWindow();
                isAppRun=false;
            }
        }
    }


}
