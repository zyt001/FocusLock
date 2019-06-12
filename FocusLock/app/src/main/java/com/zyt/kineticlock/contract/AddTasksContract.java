package com.zyt.kineticlock.contract;

import android.content.Context;

import com.zyt.kineticlock.BasePresenter;
import com.zyt.kineticlock.BaseView;

public interface AddTasksContract {

    interface View extends BaseView<Presenter>{

        void showMessage(int msgId);


        void showAboutMode();


    }

    interface Presenter extends BasePresenter {

        void saveTask(Context mContext, String title, String lockTime, int unLockMode, int modeNum, int alarmMode);


        boolean searchTaskTitle(Context mContext,String title);


    }

}
