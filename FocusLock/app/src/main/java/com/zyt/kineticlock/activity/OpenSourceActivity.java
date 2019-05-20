package com.zyt.kineticlock.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.zyt.kineticlock.R;

public class OpenSourceActivity extends AppCompatActivity {

    private LinearLayout ll_one,ll_two,ll_three;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);
        init();
    }


    private void init(){
        ll_one=findViewById(R.id.ll_one);
        ll_two=findViewById(R.id.ll_two);
        ll_three=findViewById(R.id.ll_three);

        ll_one.setOnClickListener(onClickListener);
        ll_two.setOnClickListener(onClickListener);
        ll_three.setOnClickListener(onClickListener);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                case R.id.ll_one:
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/yanzhenjie/SwipeRecyclerView");
                    intent.setData(content_url);
                    startActivity(intent);
                    break;
                case R.id.ll_two:
                    Intent intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    Uri content_url2 = Uri.parse("https://github.com/gyf-dev/ImmersionBar");
                    intent2.setData(content_url2);
                    startActivity(intent2);
                    break;
                case R.id.ll_three:
                    Intent intent3 = new Intent();
                    intent3.setAction("android.intent.action.VIEW");
                    Uri content_url3 = Uri.parse("https://github.com/bumptech/glide");
                    intent3.setData(content_url3);
                    startActivity(intent3);
                    break;
            }
        }
    };

}
