package com.zyt.kineticlock.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.zyt.kineticlock.R;
import com.zyt.kineticlock.contract.SetTasksContract;
import com.zyt.kineticlock.presenter.SetTasksPresenter;
import com.zyt.kineticlock.utils.PhotoFromPhotoAlbum;
import com.zyt.kineticlock.utils.SharedPreferencesHelper;

public class SetTaskActivity extends AppCompatActivity implements SetTasksContract.View {

    private LinearLayout ll_setBackground,ll_setWhiteApp,ll_supportme,ll_version,ll_starapp,ll_openSource,ll_about;
    private SetTasksContract.Presenter mPresenter;
    private SharedPreferencesHelper spHelper;
    private Context mContext;
    private Toolbar toolbar;
    private Switch sh_setHideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_task);
        new SetTasksPresenter(this);
        mContext=this;
        initView();
    }




    private void  initView() {
        //bindView
        ll_setBackground=findViewById(R.id.ll_setBackground);
        ll_setWhiteApp=findViewById(R.id.ll_setWhiteApp);
        ll_supportme=findViewById(R.id.ll_supportme);
        ll_version=findViewById(R.id.ll_version);
        ll_starapp=findViewById(R.id.ll_starapp);
        ll_about=findViewById(R.id.ll_about);
        ll_openSource=findViewById(R.id.ll_openSource);
        sh_setHideBar=findViewById(R.id.sh_setHideBar);

        ll_setBackground.setOnClickListener(onClickListener);
        ll_setWhiteApp.setOnClickListener(onClickListener);
        ll_supportme.setOnClickListener(onClickListener);
        ll_about.setOnClickListener(onClickListener);
        ll_starapp.setOnClickListener(onClickListener);
        ll_version.setOnClickListener(onClickListener);
        ll_openSource.setOnClickListener(onClickListener);

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


    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_setBackground:
                    showPic();
                    break;
                case R.id.ll_setWhiteApp:
                    Intent intent=new Intent(SetTaskActivity.this,AppActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_supportme:
                    showSupportMe();
                    break;
                case  R.id.ll_version:
                    showVersion();
                    break;
                case R.id.ll_starapp:
                    showStore();
                    break;
                case R.id.ll_openSource:
                    showOpenSource();
                    break;
                case R.id.ll_about:
                    showAbout();
                    break;
            }
        }
    };


    @Override
    public void showPic() {
        mPresenter.setLockPic(mContext);
    }

    @Override
    public void showSupportMe() {
        mPresenter.openSupportMe(mContext);
    }

    @Override
    public void showVersion() {
        mPresenter.openViersion(mContext);
    }

    @Override
    public void showStore() {
        mPresenter.openStore(mContext);
    }

    @Override
    public void showOpenSource() {
        mPresenter.openOpenSource(mContext);
    }

    @Override
    public void showAbout() {
        mPresenter.openAbout(mContext);
    }

    @Override
    public void setPresenter(SetTasksContract.Presenter presenter) {
        mPresenter=presenter;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        String photoPath;

        if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = PhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            spHelper=new SharedPreferencesHelper(mContext,"Task");
            spHelper.putValue("pic",photoPath);
            Toast.makeText(SetTaskActivity.this,"壁纸设置成功",Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
