package com.zyt.kineticlock.contract;

import android.content.Context;

import com.zyt.kineticlock.BasePresenter;
import com.zyt.kineticlock.BaseView;

public interface SetTasksContract {

    interface View extends BaseView<Presenter>{
        //显示设置锁机壁纸
        void showPic();
        //显示支持我
        void showSupportMe();
       //显示版本
        void showVersion();
        //检测版本
        void checkVersion();
        //去商店评分
        void showStore();
        //显示开源
        void showOpenSource();
        //显示联系
        void showContactMe();
        //显示关于
        void showAbout();

    }


    interface Presenter extends BasePresenter{
        //设置锁机壁纸
        void setLockPic(Context mContext);
        //获取支持我
        void openSupportMe(Context mContext);
        //获取版本
        String getVersion(Context mContext);
        //打开版本
        void openViersion(Context mContext);
        //进入商店
        void openStore(Context mContext);
        //获取开源
        void openOpenSource(Context mContext);
        void openContactMe(Context mContext);
        //获取关于我
        void openAbout(Context mContext);





    }
}
