package com.zyt.kineticlock.presenter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.zyt.kineticlock.activity.AddTaskActivity;
import com.zyt.kineticlock.activity.SetTaskActivity;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.TasksContract;
import com.zyt.kineticlock.model.TasksModel;
import com.zyt.kineticlock.service.TaskService;

import java.io.Serializable;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class TasksPresenter implements TasksContract.Presenter {

    private final TasksContract.View mTasksView;
    private final TasksModel mTasksModel;



    public TasksPresenter(@NonNull TasksContract.View tasksView) {
        this.mTasksView=tasksView;
        this.mTasksModel=new TasksModel();
        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void addNewTask(Context mContext) {
        Intent intent=new Intent(mContext,AddTaskActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void processTasks(Context mContext,List<Task> taskList) {
        mTasksModel.createDB(mContext);
        mTasksModel.getTask(taskList);
    }




    @Override
    public void deleteTask(String title) {
        mTasksModel.deleteTask(title);
    }



    @Override
    public void openTaskSet(Context mContext) {
        Intent intent=new Intent(mContext,SetTaskActivity.class);
        mContext.startActivity(intent);
    }


}
