package com.zyt.kineticlock.contract;

import android.content.Context;

import com.zyt.kineticlock.BasePresenter;
import com.zyt.kineticlock.BaseView;
import com.zyt.kineticlock.bean.AppInfo;

import java.util.List;

public interface WhiteAppContract {

    interface View extends BaseView<Presenter>{
        void initView();
        void showWhiteApp();

    }

    interface Presenter extends BasePresenter{
        void getWhiteApp(Context mContext, List<AppInfo> mAppInfo);
        void deleteWhiteApp(Context mContext,List<AppInfo> mAppInfo,int position);
    }
}
