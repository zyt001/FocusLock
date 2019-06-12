package com.zyt.kineticlock.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.contract.WhiteAppContract;
import com.zyt.kineticlock.model.WhiteAppModel;

import java.util.List;

public class WhiteAppPresenter implements WhiteAppContract.Presenter {

    private WhiteAppContract.View mWhiteAppView;
    private WhiteAppModel whiteAppModel;

    public WhiteAppPresenter(@NonNull WhiteAppContract.View whiteAppView){
        mWhiteAppView=whiteAppView;
        whiteAppModel=new WhiteAppModel();
        whiteAppView.setPresenter(this);
    }

    @Override
    public void getWhiteApp(Context mContext, List<AppInfo> mAppInfo) {
        whiteAppModel.getWhiteApp(mContext,mAppInfo);
    }

    @Override
    public void deleteWhiteApp(Context mContext, List<AppInfo> mAppInfo, int position) {
        whiteAppModel.deleteWhiteApp(mContext,mAppInfo,position);
    }

    @Override
    public void start() {

    }
}
