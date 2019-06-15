package com.zyt.kineticlock.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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


            String intentFullUrl = "intent://platformapi/startapp?saId=10000007&" +
                    "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx03641ktsh1x35ebovf73?t=1560002618998%3F_s" +  //这里把｛URLcode｝替换成第一步扫描的结果
                    "%3Dweb-other&_t=1472443966571#Intent;" +
                    "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";
            try {
                Intent intent = Intent.parseUri(intentFullUrl, Intent.URI_INTENT_SCHEME);
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }



    }

    @Override
    public String  getVersion(Context mContext) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = mContext.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
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
    public void openContactMe(Context mContext) {
        AlertDialog dialog=new AlertDialog.Builder(mContext)
                .setTitle("联系开发者")
                .setMessage("\nQQ:779230506\n\n邮箱：779230506@qq.com")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
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
