package com.baidu.duer.dcs.chinatalk;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.duer.dcs.Adapter.SelectFragmentAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.Select;

import java.util.ArrayList;

public class SelectTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_select_test);

        ArrayList<Select> selectsList = Select.getDefaultList();
        //构建一个题目的碎片翻页适配器
        SelectFragmentAdapter adapter = new SelectFragmentAdapter(
                getSupportFragmentManager(),selectsList
        );
        //从布局文件中获取名叫vp_content的翻页视图
        ViewPager question_vp_content = (ViewPager) findViewById(R.id.question_vp_content);
        //给vp_content设置题目的碎片适配器
        question_vp_content.setAdapter(adapter);
        //设置vp_content默认显示第一个页面
        question_vp_content.setCurrentItem(0);
    }
}