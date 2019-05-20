package com.zyt.kineticlock.contract;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyt.kineticlock.BasePresenter;
import com.zyt.kineticlock.BaseView;
import com.zyt.kineticlock.bean.AppInfo;

import java.util.List;

public interface LockServiceContract {

    interface View extends BaseView<Presenter>{

        //展示数据
        void showTask();
        void showBackground();
        //开启悬浮窗
       void showFloatingWindow();
       //关闭悬浮窗
       void showCloseWindow();
       //展示倒计时
       void showTime();
        //摇动监听
        void showShakeListener();
        //白名单应用状态监听
        boolean showWhiteAppListener(Context mContext,String packageName);
        //移除悬浮窗
        void showRemoveWindow();
        //添加悬浮窗
        void showAddWindow();

        void isShowAppRun();
    }

    interface Presenter extends BasePresenter{

        //判断白名单应用是否运行
        boolean isWhiteAppRun(Context mContext,String packageName);

        //获取应用白名单数据
        void getWhiteAppData(Context mContext, List<AppInfo> appInfoList);

        void toApp(Context mContext,String packageName);
    }

}
