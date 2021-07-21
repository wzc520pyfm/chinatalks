package com.baidu.duer.dcs.chinatalk;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.duer.dcs.Adapter.WrongFragmentAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.Wrong;

import java.util.ArrayList;

public class WrongQuesBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chiantalk_activity_wrong_ques_book);

        ArrayList<Wrong> wrongsList=Wrong.getDefaultList();

        //构建一个题目的碎片翻页适配器
        WrongFragmentAdapter adapter = new WrongFragmentAdapter(
                getSupportFragmentManager(),wrongsList
        );
        //从布局文件中获取名叫vp_content的翻页视图
        ViewPager wrong_vp_content = (ViewPager) findViewById(R.id.wrong_vp_content);
        //给vp_content设置题目的碎片适配器
        wrong_vp_content.setAdapter(adapter);
        //设置vp_content默认显示第一个页面
        wrong_vp_content.setCurrentItem(0);

    }
}