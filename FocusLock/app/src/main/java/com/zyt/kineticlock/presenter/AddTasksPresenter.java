package com.zyt.kineticlock.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;


import com.zyt.kineticlock.contract.AddTasksContract;
import com.zyt.kineticlock.model.AddTasksModel;

public class AddTasksPresenter implements AddTasksContract.Presenter {

    private final AddTasksContract.View mAddTaskView;
    private final AddTasksModel mAddTasksModel;

    public AddTasksPresenter(@NonNull AddTasksContract.View addTaskView){

        this.mAddTaskView=addTaskView;
        this.mAddTasksModel=new AddTasksModel();
        mAddTaskView.setPresenter(this);

    }



    @Override
    public void start() {

    }

    @Override
    public void saveTask(Context mContext, String title, String lockTime, int unLockMode, int modeNum, int alarmMode) {
        mAddTasksModel.saveTask(mContext,title,lockTime,unLockMode,modeNum,alarmMode);

    }



    @Override
    public void searchTaskTitle(Context mContext, String title) {

    }
}
