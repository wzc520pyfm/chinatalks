package com.baidu.duer.dcs.chinatalk;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.duer.dcs.Adapter.WrongFragmentAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.Wrong;

import java.util.ArrayList;
/*********************************************************************************************
 * 页面:          错题册页面                                                              *
 * 进入方式:      点击测试页面的WrongQues按钮进入
 * 关联到的文件:  /Adapter/WrongFragmentAdapter   :  碎片适配器
 *               /bean/Result                    :  自定义的Result数据类型
 *               /Fragment/ChinaTalkWrongBookFragment :  碎片
 *               以及一些工具类
 *               /layout/chinatalk_activity_wrong_ques_book.xml
 * 碎片:        通过代码动态添加, 主要利用ViewPager
 * 页面主要逻辑: 1.获取Wrong的默认值来构建碎片
 * 已知Bug:     1.
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class WrongQuesBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chiantalk_activity_wrong_ques_book);

        ArrayList<Wrong> wrongsList=Wrong.getDefaultList();//获得Wrong的默认值

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