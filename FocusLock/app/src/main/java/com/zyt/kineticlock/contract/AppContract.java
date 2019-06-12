package com.zyt.kineticlock.contract;

import android.content.Context;

import com.zyt.kineticlock.BasePresenter;
import com.zyt.kineticlock.BaseView;
import com.zyt.kineticlock.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AppContract {


    interface View extends BaseView<Presenter>{

        void showAppList();

    }

    interface Presenter extends BasePresenter {


        //获取本地应用
        void getApp(Context mContext, List<AppInfo> mAppInfo);
        //保存白名单应用
        void saveWhiteApp(Context mContext, List<AppInfo> mAppInfo, int positon);


    }

}
