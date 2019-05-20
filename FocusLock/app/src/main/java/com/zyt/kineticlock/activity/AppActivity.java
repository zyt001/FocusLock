package com.zyt.kineticlock.activity;

import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;


import com.zyt.kineticlock.R;
import com.zyt.kineticlock.adapter.AppAdapter;
import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.contract.AppContract;
import com.zyt.kineticlock.database.MyDatabaseHelper;
import com.zyt.kineticlock.presenter.AppPresenter;

import java.util.ArrayList;

public class AppActivity extends AppCompatActivity implements AppContract.View {

    private AppContract.Presenter mAppPresenter;
    private  ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
    private Context mContext;
    private MyDatabaseHelper dbHelper;
    private static final int ReadSuccess=1;
    private AppAdapter appAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ProgressBar pb_loading;


    @Override
    public void setPresenter(AppContract.Presenter presenter) {
        mAppPresenter =presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        mContext=this;
        new AppPresenter(this);
        getAppList();

    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case ReadSuccess:
                    initView();
                    pb_loading.setVisibility(View.GONE);
                    break;
            }
            return false;
        }
    });


    private void initView(){
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerview);
        pb_loading=findViewById(R.id.pb_loading);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        appAdapter =new AppAdapter(mContext,appList);
        recyclerView.setAdapter(appAdapter);
       appAdapter.setItemOnCheckListener(new AppAdapter.ItemOnCheckListener() {
            @Override
            public void OnCheck(View view, int position, boolean isChecked) {
                if(isChecked){
                    appAdapter.isSelectMap.put(position,true);
                    appList.get(position).setSelect(true);

                }else if(appAdapter.isSelectMap.containsKey(position)){
                    appAdapter.isSelectMap.remove(position);
                    appList.get(position).setSelect(false);
                }
            }

        });


        //init toolBar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //SetClickListener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getAppList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppPresenter.getWhiteApp(mContext,appList);
                Message message=new Message();
                message.what=ReadSuccess;
                handler.sendMessage(message);
            }
        }).start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(Integer position:appAdapter.isSelectMap.keySet()){
            mAppPresenter.saveWhiteApp(mContext,appList,position);
        }
    }
}
