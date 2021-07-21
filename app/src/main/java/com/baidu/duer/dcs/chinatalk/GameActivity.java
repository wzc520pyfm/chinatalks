package com.baidu.duer.dcs.chinatalk;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.duer.dcs.Adapter.GameFragementAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.Game;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chiantalk_activity_game);

        ArrayList<Game> gameList=Game.getDefaultList();
        //构建一个题目的碎片翻页适配器
        GameFragementAdapter adapter=new GameFragementAdapter(
                getSupportFragmentManager(),gameList
        );
        //从布局中获取名叫 vp_content 的翻页视图
        ViewPager vp_content= (ViewPager) findViewById(R.id.vp_content);
        //给vp_content设置题目碎片适配器
        vp_content.setAdapter(adapter);
        //设置vp_content默认显示第一个页面
        vp_content.setCurrentItem(0);
    }
}