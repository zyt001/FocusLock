package com.zyt.kineticlock.contract;

import com.zyt.kineticlock.BasePresenter;
import com.zyt.kineticlock.BaseView;

public interface LockTomatoContract {

    interface View extends BaseView<Presenter>{

        //初始化
        void initView();

        //展示任务数据
        void showTask();

        void showBackground();

        //退出提示
        void exitDialog();

        //展示倒计时
        void showTime();

    }


    interface Presenter extends BasePresenter{


    }

}
