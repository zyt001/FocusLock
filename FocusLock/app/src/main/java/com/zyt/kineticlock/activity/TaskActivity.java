package com.zyt.kineticlock.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gyf.immersionbar.ImmersionBar;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zyt.kineticlock.R;
import com.zyt.kineticlock.adapter.TaskAdapter;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.TasksContract;
import com.zyt.kineticlock.presenter.TasksPresenter;
import com.zyt.kineticlock.service.TaskService;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements TasksContract.View{

    private TasksContract.Presenter mTaskPresenter;
    private SwipeRecyclerView swipeRecyclerView;
    private LinearLayoutManager layoutManager;
    private LinearLayout ll_top;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferencesHelper spHelper;
    private TaskAdapter taskAdapter;
    private List<Task> taskList=new ArrayList<>();
    public Context mContext;


    private ImageButton btn_setTask;
    private FloatingActionButton fab_addTask;
    private Boolean isFirst;
    private TextView tv_year_month,tv_weak,tv_day,tv_noTask;

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mTaskPresenter=presenter;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        new TasksPresenter(this);
        mContext=this;
        applyPermission();
        initView();
        showTask();
        showNoTask();
        showTopTime();

    }

    private void initView(){




        //Bind View
        ll_top=findViewById(R.id.ll_top);
        coordinatorLayout=findViewById(R.id.coordinator);
        btn_setTask=findViewById(R.id.btn_setTask);
        fab_addTask=findViewById(R.id.fab_addTask);
        tv_year_month =findViewById(R.id.tv_year_mouth);
        tv_weak=findViewById(R.id.tv_weak);
        tv_day=findViewById(R.id.tv_day);
        tv_noTask=findViewById(R.id.tv_NoTask);

        btn_setTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetTask();
            }
        });


        //init SwipRecyclerView
        swipeRecyclerView=findViewById(R.id.swipRecyclerView);
        layoutManager=new LinearLayoutManager(this);
        swipeRecyclerView.setLayoutManager(layoutManager);
        swipeRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
        swipeRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        swipeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //bindClick
        fab_addTask.setOnClickListener(onClickListener);

        ImmersionBar.with(this)
                .titleBar(ll_top) .init();
    }



    SwipeMenuCreator mSwipeMenuCreator=new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            deleteItem.setText("删除");
            deleteItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            deleteItem.setTextColor(getResources().getColor(R.color.colorPrimary));
            deleteItem.setHeight(170);
            deleteItem.setWidth(200);
            rightMenu.addMenuItem(deleteItem);

        }
    };

    OnItemMenuClickListener mMenuItemClickListener=new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();
            TextView title=layoutManager.findViewByPosition(position).findViewById(R.id.tv_name);
            mTaskPresenter.deleteTask(title.getText().toString());
            taskAdapter.removeData(position);
            showNoTask();

        }
    };

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case  R.id.fab_addTask:
                   showAddTask();
                    break;
            }

        }
    };

    @Override
    public void showTopTime() {
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        String weak=String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if("1".equals(weak)){
            tv_weak.setText("星期日");
        }else if("2".equals(weak)){
            tv_weak.setText("星期一");
        }else if("3".equals(weak)){
            tv_weak.setText("星期二");
        }else if("4".equals(weak)){
            tv_weak.setText("星期三");
        }else if("5".equals(weak)){
            tv_weak.setText("星期四");
        }else if("6".equals(weak)){
            tv_weak.setText("星期五");
        }else if("7".equals(weak)){
            tv_weak.setText("星期六");
        }
        tv_year_month.setText(""+month+"/"+year);
        tv_day.setText(String.valueOf(day));
    }

    @Override
    public void showTask() {

        mTaskPresenter.processTasks(mContext,taskList);
        taskAdapter=new TaskAdapter(taskList);
        swipeRecyclerView.setAdapter(taskAdapter);
        taskAdapter.setItemOnClickListener(new TaskAdapter.ItemOnClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
            switch (taskList.get(position).getTaskMode()){

                case "番茄":
                    openTomatoTask(position);
                 break;
                case "专注":
                    openLockTask(position);
                    break;
                case "禅定":
                    openLockTask(position);
                    break;
            }

            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });

    }


    @Override
    public void showNoTask() {
        if(taskList.isEmpty())
        {
            tv_noTask.setVisibility(View.VISIBLE);
        }else {
            tv_noTask.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAddTask() {
        mTaskPresenter.addNewTask(mContext);
    }

    @Override
    public void openTomatoTask(int position) {
        spHelper=new SharedPreferencesHelper(mContext,"Task");
        spHelper.putValue("title",taskList.get(position).getTitle());
        Intent intent=new Intent(TaskActivity.this,TomatoActivity.class);
        startActivity(intent);
    }

    @Override
    public void openLockTask(int position) {
        spHelper=new SharedPreferencesHelper(mContext,"Task");
        spHelper.putValue("title",taskList.get(position).getTitle());
            startService(new Intent(mContext, TaskService.class));
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);



    }

    @Override
    public void openZenTask() {

    }


    @Override
    public void showSetTask() {
        mTaskPresenter.openTaskSet(mContext);
    }

    @Override
    public void showMessage(String msg) {
        switch (msg)
        {
            case "addSuccess":
                Snackbar.make(coordinatorLayout,"任务添加成功！",Snackbar.LENGTH_SHORT).show();
                break;
            case "deleteSuccess":
                Snackbar.make(coordinatorLayout,"任务已删除！",Snackbar.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void applyPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            showStoragePermission();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    showOverlayPermissionDialog();
                }
            }else {
                if(!isAppUsePermission(mContext)){
                    showAppUsePermissionDialog();
                }
            }
        }


    }

    @Override
    public void showOverlayPermissionDialog() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("1.悬浮窗权限申请")
                .setMessage("\n专注需要您授权开启显示悬浮窗,不开启将无法使用专注功能\n操作：权限管理→显示悬浮窗→允许\n")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去开启授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Build.VERSION.SDK_INT>23){
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 1);
                        }else{
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 1);
                        }

                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }



    @Override
    public void showAppUsePermissionDialog() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("2.应用使用情况权限申请")
                .setMessage("\n专注需要您授权开启查看使用情况权限\n注意：不开启将无法使用专注功能\n操作：在列表中请找到 专注 并开启允许查看使用情况\n")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去开启授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent,2);
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void showOpenAppPermissionDialog() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("3.应用后台弹出界面权限申请")
                .setMessage("\n部分机型若无该权限设置可不用设置\n（小米手机必须设置）\n\n专注需要您授权开启应用后台弹出权限\n注意：不开启将无法使用白名单功能\n操作：权限管理→后台弹出界面→允许\n")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去开启授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void showStoragePermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_LONG).show();
            }
            //申请存储权限
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean isAppUsePermission(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(!isAppUsePermission(mContext)){
                    showAppUsePermissionDialog();
                }
                break;
            case 2:
                showOpenAppPermissionDialog();
                break;
            case 3:
                taskList.clear();
                mTaskPresenter.processTasks(mContext,taskList);
                taskAdapter.notifyDataSetChanged();
                showNoTask();
                // showMessage("addSuccess");
                break;
                default:
                    applyPermission();
                    break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    showOverlayPermissionDialog();
                }
            }
        } else {
            applyPermission();
        }
    }
}
