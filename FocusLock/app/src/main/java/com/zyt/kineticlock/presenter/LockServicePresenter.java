package com.zyt.kineticlock.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.contract.LockServiceContract;
import com.zyt.kineticlock.model.LockServiceModel;
import com.zyt.kineticlock.utils.SensorManagerHelper;

import java.util.List;

public class LockServicePresenter implements LockServiceContract.Presenter {


    private final LockServiceContract.View mLockServiceView;
    private final LockServiceModel lockServiceModel;


    public LockServicePresenter (@NonNull LockServiceContract.View lockServiceView){
        this.mLockServiceView=lockServiceView;
        lockServiceModel=new LockServiceModel();
        mLockServiceView.setPresenter(this);
    }


    @Override
    public boolean isWhiteAppRun(Context mContext, String packageName) {
        return lockServiceModel.isWhiteAppRun(mContext,packageName);
    }

    @Override
    public void getWhiteAppData(Context mContext, List<AppInfo> appInfoList) {
        lockServiceModel.getWhiteApp(mContext,appInfoList);
    }

    @Override
    public void toApp(Context mContext, String packageName) {
        lockServiceModel.toApp(mContext,packageName);
    }

    @Override
    public void start() {

    }
}
