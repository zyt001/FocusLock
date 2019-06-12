package com.zyt.kineticlock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zyt.kineticlock.R;
import com.zyt.kineticlock.adapter.WhiteAppAdapter;
import com.zyt.kineticlock.bean.AppInfo;
import com.zyt.kineticlock.contract.WhiteAppContract;
import com.zyt.kineticlock.presenter.WhiteAppPresenter;

import java.util.ArrayList;
import java.util.List;

public class WhiteAppActivity extends AppCompatActivity implements WhiteAppContract.View {

    private WhiteAppContract.Presenter mWhiteAppPresenter;
    private ArrayList<AppInfo> whiteAppList = new ArrayList<AppInfo>();

    private RecyclerView recyclerView_whiteApp;
    private WhiteAppAdapter whiteAppAdapter;
    private static final int LoadSuccess=1;
    private LinearLayoutManager layoutManager;
    private Button btn_addWhiteApp;
    private Context mContext;
    private Toolbar toolbar;
    private TextView tv_NoWhiteApp;

    @Override
    public void setPresenter(WhiteAppContract.Presenter presenter) {
        mWhiteAppPresenter=presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_app);
        new WhiteAppPresenter(this);
        mContext=this;
        showWhiteApp();
    }



    @Override
    public void initView() {
        //BindView
        toolbar=findViewById(R.id.toolbar);
        tv_NoWhiteApp=findViewById(R.id.tv_NoWhiteApp);
        recyclerView_whiteApp=findViewById(R.id.recyclerview_whiteApp);
        layoutManager=new LinearLayoutManager(this);
        recyclerView_whiteApp.setLayoutManager(layoutManager);

        if(whiteAppList.size()==0){
            tv_NoWhiteApp.setVisibility(View.VISIBLE);
        }else {
            tv_NoWhiteApp.setVisibility(View.GONE);
        }

        whiteAppAdapter=new WhiteAppAdapter(mContext,whiteAppList);
        recyclerView_whiteApp.setAdapter(whiteAppAdapter);

        btn_addWhiteApp=findViewById(R.id.btn_addWhiteApp);
        btn_addWhiteApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WhiteAppActivity.this,AppActivity.class);
                startActivityForResult(intent,1);
            }
        });
        whiteAppAdapter.setItemOnCheckListener(new WhiteAppAdapter.ItemOnCheckListener() {
            @Override
            public void OnCheck(View view, int position, boolean isChecked) {
                if(isChecked){
                }else {
                    if(!whiteAppAdapter.isSelectMap.containsKey(position)){
                        whiteAppAdapter.isSelectMap.put(position,false);
                    }
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


    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case LoadSuccess:
                    initView();
                    break;
            }
            return false;
        }
    });

    @Override
    public void showWhiteApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mWhiteAppPresenter.getWhiteApp(mContext,whiteAppList);
                Message message=new Message();
                message.what=LoadSuccess;
                handler.sendMessage(message);
            }
        }).start();

    }

    @Override
    public void finish() {
        super.finish();
        for(int position:whiteAppAdapter.isSelectMap.keySet()) {
            mWhiteAppPresenter.deleteWhiteApp(mContext,whiteAppList,position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode==1){
           whiteAppList.clear();
           mWhiteAppPresenter.getWhiteApp(mContext,whiteAppList);
           whiteAppAdapter.notifyDataSetChanged();
           if(whiteAppList.size()==0){
               tv_NoWhiteApp.setVisibility(View.VISIBLE);
           }else {
               tv_NoWhiteApp.setVisibility(View.GONE);
           }
       }
    }

}


