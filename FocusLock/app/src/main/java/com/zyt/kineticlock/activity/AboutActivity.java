package com.zyt.kineticlock.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.kineticlock.R;

public class AboutActivity extends AppCompatActivity {

    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";
    private TextView hongBao;
    private AppCompatAutoCompleteTextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hongBao=findViewById(R.id.tv_HongBao);
        setSupportActionBar(toolbar);



        //设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager cm = (ClipboardManager) getSystemService(AboutActivity.this.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                ClipData clipData = ClipData.newPlainText("支付宝赏金", "8046029\n");
                cm.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),"已经复制到剪切板",Toast.LENGTH_SHORT).show();

                try {
                    Uri uri = Uri.parse("alipayqr://platformapi/startapp");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
