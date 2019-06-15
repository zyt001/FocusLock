package com.zyt.kineticlock.presenter;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.LockServiceContract;
import com.zyt.kineticlock.model.LockServiceModel;

import java.util.List;

public class LockServicePresenter implements LockServiceContract.Presenter {


    private final LockServiceContract.View mLockServiceView;
    private final LockServiceModel mlockServiceModel;


    public LockServicePresenter (@NonNull LockServiceContract.View lockServiceView){
        this.mLockServiceView=lockServiceView;
        mlockServiceModel =new LockServiceModel();
        mLockServiceView.setPresenter(this);
    }


    @Override
    public void getTaskInfo(Context mContext, List<Task> taskList) {
        mlockServiceModel.getTaskInfo(mContext,taskList);
    }

    @Override
    public boolean isWhiteAppRun(Context mContext, String packageName) {
        return mlockServiceModel.isWhiteAppRun(mContext,packageName);
    }

    @Override
    public void getWhiteAppData(Context mContext, List<AppInfo> appInfoList) {
        mlockServiceModel.getWhiteApp(mContext,appInfoList);
    }

    @Override
    public void toApp(Context mContext, String packageName) {
        mlockServiceModel.toApp(mContext,packageName);
    }

    @Override
    public void start() {

    }
}
