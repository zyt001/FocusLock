package com.zyt.kineticlock.contract;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zyt.kineticlock.BasePresenter;
import com.zyt.kineticlock.BaseView;
import com.zyt.kineticlock.bean.Task;

import java.util.List;

public interface TasksContract {

    interface  View extends BaseView<Presenter>{

        //展示任务
        void showTask();
        //展示无任务
        void showNoTask();
        //添加任务
        void showAddTask();
        //番茄任务执行
        void openTomatoTask();
        //专注任务执行
        void openLockTask(int position);
        //禅定任务执行
        void openZenTask();
        //任务设置
        void showSetTask();
        //展示信息
        void showMessage(String msg);
        //显示申请权限
        void applyPermission();
        //显示悬浮窗权限申请
        void showOverlayPermissionDialog();
        //显示应用使用量权限申请
        void showAppUsePermissionDialog();
        //检测权限
        void checkPermission();



    }


    interface Presenter extends BasePresenter {

        //添加任务
        void addNewTask(Context mContext);
        //处理任务数据
        void processTasks(Context mContext,List<Task> taskList);
        //删除任务
        void deleteTask(String title);
        //进入设置
        void openTaskSet(Context mContext);
    }

}
