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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zyt.kineticlock.R;
import com.zyt.kineticlock.TomatoActivity;
import com.zyt.kineticlock.adapter.TaskAdapter;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.TasksContract;
import com.zyt.kineticlock.presenter.TasksPresenter;
import com.zyt.kineticlock.service.TaskService;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements TasksContract.View{

    private TasksContract.Presenter mTaskPresenter;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRecyclerView swipeRecyclerView;
    private LinearLayoutManager layoutManager;
    private SharedPreferencesHelper spHelper;
    private TaskAdapter taskAdapter;
    private List<Task> taskList=new ArrayList<>();
    public Context mContext;


    private TextView tv_noTask;
    private Toolbar toolbar;
    private Button btn_setTask;
    private FloatingActionButton fab_addTask;
    private Boolean isFirst;

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
    }

    private void initView(){

        //Bind View
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        btn_setTask=findViewById(R.id.btn_setTask);
        fab_addTask=findViewById(R.id.fab_addTask);
        tv_noTask=findViewById(R.id.tv_noTask);

        //init ToolBar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.btn_setTask:
                        showSetTask();
                        break;
                }
                return false;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
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
            showMessage("deleteSuccess");
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
    public void showTask() {

        mTaskPresenter.processTasks(mContext,taskList);
        taskAdapter=new TaskAdapter(taskList);
        swipeRecyclerView.setAdapter(taskAdapter);
        taskAdapter.setItemOnClickListener(new TaskAdapter.ItemOnClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
            switch (taskList.get(position).getTaskMode()){

                case "番茄":
                    openTomatoTask();
                 break;
                case "专注":
                    openLockTask(position);
                    break;
                case "禅定":
                    openZenTask();
                    break;


            }
                Log.i("a00", "00000000000000OnItemClick: "+taskList.get(position).getTaskMode());

            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        taskAdapter.notifyDataSetChanged();
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
    public void openTomatoTask() {
        Intent intent=new Intent(TaskActivity.this,TomatoActivity.class);
        startActivity(intent);
    }

    @Override
    public void openLockTask(int position) {
        spHelper=new SharedPreferencesHelper(mContext,"Task");
        isFirst=(Boolean)spHelper.getValue("isFirst",true);
        spHelper.putValue("title",taskList.get(position).getTitle());
        if (isFirst)
        {
            AlertDialog dialog=new AlertDialog.Builder(mContext)
                    .setTitle("锁机温馨提示")
                    .setMessage("为了更好的体验，请将软件锁定后台,点击确定后手机将锁定\n(注：本提示只显示一次，以后点击任务将直接进入锁机)")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startService(new Intent(mContext, TaskService.class));
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                        }
                    }).create();
            dialog.show();
        }else {

            startService(new Intent(mContext, TaskService.class));
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

        }

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
                Snackbar.make(coordinatorLayout,"任务添加成功",Snackbar.LENGTH_SHORT).show();
                break;
            case "deleteSuccess":
                Snackbar.make(coordinatorLayout,"删除成功",Snackbar.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void applyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showOverlayPermissionDialog();
            } else {

            }
        }

        checkPermission();

        if(isAppUsePermission(mContext))
        {
        }else {
            showAppUsePermissionDialog();
        }
    }

    @Override
    public void showOverlayPermissionDialog() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage("\n专注需要您授权开启悬浮窗,不开启将无法使用专注功能\n")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去开启授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getPackageName())),0);
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void showAppUsePermissionDialog() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("应用使用情况权限申请")
                .setMessage("\n专注需要您授权开启查看使用情况权限\n注意：不开启将无法使用专注功能\n操作：请找到 专注 并开启允许查看使用情况\n")
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
                        mContext.startActivity(intent);
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_LONG).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        } else {



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



}
