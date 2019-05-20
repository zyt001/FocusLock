package com.zyt.kineticlock.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.contract.AppContract;
import com.zyt.kineticlock.model.AppModel;

import java.util.List;

public class AppPresenter implements AppContract.Presenter {

    private AppContract.View mWhiteAppView;
    private AppModel appModel;

    public AppPresenter(@NonNull AppContract.View whiteAppView){
        mWhiteAppView=whiteAppView;
        appModel =new AppModel();
        whiteAppView.setPresenter(this);
    }

    @Override
    public void getWhiteApp(Context mContext, List<AppInfo> mAppInfo) {
        appModel.getAppList(mContext,mAppInfo);
    }

    @Override
    public void saveWhiteApp(Context mContext, List<AppInfo> mAppInfo, int positon) {
        appModel.saveWhiteApp(mContext,mAppInfo,positon);
    }



    @Override
    public void start() {

    }
}
