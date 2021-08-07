package com.baidu.duer.dcs.chinatalk;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.duer.dcs.Fragment.ChinaTalkHomeFragment;
import com.baidu.duer.dcs.Fragment.ChinaTalkTestCenterFragment;
import com.baidu.duer.dcs.Fragment.FunTestsFragment;
import com.baidu.duer.dcs.R;

public class TabFragmentActivity extends AppCompatActivity {

    private static final String TAG = "TabFragmentActivity";
    private FragmentTabHost tabHost;//声明一个碎片标签栏对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_tab_fragment);
        Bundle bundle = new Bundle();//创建一个包裹对象
        bundle.putString("tag",TAG);//往包裹中存入名叫tag的标记
        //从布局文件中获取名叫tabhost的碎片标签栏
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        //把实际的内容框架安装到碎片标签栏
        tabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        //往标签栏添加第一个标签, 其中内容视图展示 ChinaTalkHomeFragment
        tabHost.addTab(getTabView(R.string.menu_home,R.drawable.chinatalk_bar_home),
                ChinaTalkHomeFragment.class,bundle);
        //往标签栏添加第二个标签, 其中内容视图展示ChinaTalkTestCenterFragment
        tabHost.addTab(getTabView(R.string.menu_exam,R.drawable.chinatalk_bar_exam),
                ChinaTalkTestCenterFragment.class,bundle);
        //往标签栏添加第三个标签, 其中内容视图展示FunTestsFragment
        tabHost.addTab(getTabView(R.string.menu_game,R.drawable.chinatalk_bar_game),
                FunTestsFragment.class,bundle);
    }

    //根据字符串和图表的资源编号, 获取对应的标签规格
    private TabHost.TabSpec getTabView(int textId, int imgId) {
        //根据资源编号获取字符串对象
        String text = getResources().getString(textId);
        //根据资源编号获取图形对象
        Drawable drawable =getResources().getDrawable(imgId);
        //设置图形的四周边界. 这里必须设置图片大小, 否则无法显示图标
        drawable.setBounds(0,0,drawable.getMinimumWidth()/2,drawable.getMinimumHeight()/2);
        //根据布局文件chinatalk_item_tabber.xml生成标签按钮对象
        View item_tabber = getLayoutInflater().inflate(R.layout.chinatalk_item_tabbar,null);
        TextView tv_item = item_tabber.findViewById(R.id.tv_item_tabbar);
        tv_item.setText(text);
        //在文字上方显示标签的图标
        tv_item.setCompoundDrawables(null,drawable,null,null);
        //生成并返回该标签按钮对应的标签规格
        return tabHost.newTabSpec(text).setIndicator(item_tabber);
    }
}