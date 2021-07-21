package com.baidu.duer.dcs.androidapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.duer.dcs.R;

public class XihuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xihu);



    }

    public void back(View view) {
        this.finish();
    }

    public void toNext(View view) {
        Intent intent=new Intent(XihuActivity.this,XihuTakePhotoActivity.class);
        startActivity(intent);
    }
}
