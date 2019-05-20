package com.zyt.kineticlock.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.zyt.kineticlock.activity.AboutActivity;
import com.zyt.kineticlock.activity.OpenSourceActivity;

import com.zyt.kineticlock.contract.SetTasksContract;


public class SetTasksPresenter implements SetTasksContract.Presenter {

    private final SetTasksContract.View mSetTasksView;

    public SetTasksPresenter(@NonNull SetTasksContract.View setTasksView){
        this.mSetTasksView=setTasksView;
        mSetTasksView.setPresenter(this);
    }

    @Override
    public void setLockPic(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity)mContext).startActivityForResult(intent, 2);

    }

    @Override
    public void openSupportMe(Context mContext) {
        AlertDialog dialog=new AlertDialog.Builder(mContext)
                .setTitle("捐赠")
                .setMessage("捐赠我？\n\n感谢你的支持，捐赠对我来说是一种无形压力，我喜欢随缘开发\n\n所以我目前不打算开通捐赠哦\n\n什么时候开通？\n\n随缘，哈哈哈哈！")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void openViersion(Context mContext) {
        Toast.makeText(mContext,"版本随缘更新！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openStore(Context mContext) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=" + mContext.getPackageName()));
            mContext.startActivity(i);
        } catch (Exception e) {
            Toast.makeText(mContext, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void openOpenSource(Context mContext) {
        Intent intent=new Intent(mContext,OpenSourceActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void openAbout(Context mContext) {
        Intent intent=new Intent(mContext,AboutActivity.class);
        mContext.startActivity(intent);
    }


    @Override
    public void start() {

    }
}
