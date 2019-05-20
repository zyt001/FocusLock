package com.zyt.kineticlock.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.kineticlock.R;
import com.zyt.kineticlock.adapter.TaskAdapter;
import com.zyt.kineticlock.bean.Task;
import com.zyt.kineticlock.contract.AddTasksContract;
import com.zyt.kineticlock.presenter.AddTasksPresenter;

import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements AddTasksContract.View {

    private Context mContext;
    private AddTasksContract.Presenter mAddTaskPresenter;
    private TaskAdapter taskAdapter;
    private TextView tv_aboutMode;
    private LinearLayout layout_selectShake;
    private EditText et_setName,et_setTime,et_setShake;
    private int shakeValue=0,lockModeValue=1,alarmValue=1;
    private Toolbar toolbar;
    private FloatingActionButton btn_save;
    private RadioButton rb_timeMode,rb_shakeMode,rb_zenMode;
    private RadioButton rb_alarmShake,rb_alarmNo;
    private RadioGroup rg_lockMode,rg_alarmMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        new AddTasksPresenter(this);
        mContext=this;
        initView();

    }

    @Override
    public void setPresenter(AddTasksContract.Presenter presenter) {
        mAddTaskPresenter=presenter;
    }

    private void initView(){

        //init Toolbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //BindView
        layout_selectShake =findViewById(R.id.layout_selectShake);
        et_setName=findViewById(R.id.et_setName);
        et_setTime=findViewById(R.id.et_setTime);
        et_setShake=findViewById(R.id.et_setShake);
        btn_save=findViewById(R.id.btn_save);
        rb_timeMode=findViewById(R.id.rb_timeMode);
        rb_shakeMode=findViewById(R.id.rb_shakeMode);
        rb_zenMode=findViewById(R.id.rb_zenMode);
        rb_alarmShake=findViewById(R.id.rb_alarmNo);
        rb_alarmNo=findViewById(R.id.rb_alarmShake);
        rg_lockMode=findViewById(R.id.rg_lockMode);
        rg_alarmMode=findViewById(R.id.rg_alarmMode);
        tv_aboutMode=findViewById(R.id.tv_aboutMode);
        rb_timeMode.setChecked(true);
        rb_alarmShake.setChecked(true);

        //SetClickListener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout_selectShake.setOnClickListener(onClickListener);
        btn_save.setOnClickListener(onClickListener);
        tv_aboutMode.setOnClickListener(onClickListener);
        rg_lockMode.setOnCheckedChangeListener(onCheckedChangeListener);
        rg_alarmMode.setOnCheckedChangeListener(onCheckedChangeListener);





    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.layout_selectShake:

                    break;
                case R.id.btn_save:
                    if(et_setTime.getText().length()!=0&&et_setTime.getText().toString().trim().indexOf('0')!=0){
                        if(lockModeValue!=1)
                        {
                            shakeValue=Integer.valueOf(et_setShake.getEditableText().toString().trim());
                        }
                        mAddTaskPresenter.saveTask(mContext,et_setName.getText().toString(),et_setTime.getText().toString(),lockModeValue,shakeValue,alarmValue);

                        Intent intent=new Intent(AddTaskActivity.this,TaskActivity.class);
                        startActivity(intent);

                    }else {
                        showMessage();
                    }
                    break;
                case R.id.tv_aboutMode:
                    showAboutMode();
                    break;
            }

        }
    };


    RadioGroup.OnCheckedChangeListener onCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_timeMode:
                    layout_selectShake.setVisibility(View.GONE);
                    lockModeValue=1;
                    break;
                case R.id.rb_shakeMode:
                    layout_selectShake.setVisibility(View.VISIBLE);
                    lockModeValue=2;
                    break;
                case R.id.rb_zenMode:
                    layout_selectShake.setVisibility(View.GONE);
                    lockModeValue=3;
                    break;
                case R.id.rb_alarmNo:
                    alarmValue=1;
                    break;
                case R.id.rb_alarmShake:
                    alarmValue=2;
                    break;

            }
        }
    };

    @Override
    public void showMessage() {
        Toast.makeText(AddTaskActivity.this,"专注时间不能为空或者为0",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBack() {
        this.finish();
    }

    @Override
    public void showAboutMode() {
        AlertDialog dialog=new AlertDialog.Builder(mContext)
                .setTitle("关于模式")
                .setMessage("\n番茄：简单番茄钟,普通锁屏，可随时退出\n\n专注：强制锁屏，可使用应用白名单,设置摇动紧急退出\n\n禅定：强制锁屏，仅提供电话接听拨打\n")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }




}
