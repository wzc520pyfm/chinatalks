package com.baidu.duer.dcs.chinatalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.duer.dcs.R;
/*********************************************************************************************
 * 页面:          Home页面, 即软件主页面                                                                    *
 * 进入方式:      点击测试页面的Home按钮进入
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_home);
    }
}