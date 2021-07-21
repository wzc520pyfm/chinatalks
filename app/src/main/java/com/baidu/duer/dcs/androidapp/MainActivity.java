package com.baidu.duer.dcs.androidapp;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baidu.duer.dcs.Adapter.FragmentAdapter;
import com.baidu.duer.dcs.Fragment.FunTestsFragment;
import com.baidu.duer.dcs.Fragment.HiChinaFragment;
import com.baidu.duer.dcs.Fragment.HomeFragment;
import com.baidu.duer.dcs.Fragment.ShareFragment;
import com.baidu.duer.dcs.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView item_home, item_share, item_hichina, item_funtests;
    private ViewPager vp;
    private HomeFragment homeFragment;
    private ShareFragment shareFragment;
    private HiChinaFragment hiChinaFragment;
    private FunTestsFragment funTestsFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        initFragment();
    }

    private void initFragment() {
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_home.setBackgroundResource(R.mipmap.ic_tabbar_home_h);

        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/

                changeBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    private void changeBackground(int position) {
        if (position == 0) {
            item_home.setBackgroundResource(R.mipmap.ic_tabbar_home_h);
            item_share.setBackgroundResource(R.mipmap.ic_tabbar_share);
            item_hichina.setBackgroundResource(R.mipmap.ic_tabbar_hichina);
            item_funtests.setBackgroundResource(R.mipmap.ic_tabbar_funtests);
        } else if (position == 1) {
            item_home.setBackgroundResource(R.mipmap.ic_tabbar_home);
            item_share.setBackgroundResource(R.mipmap.ic_tabbar_share_h);
            item_hichina.setBackgroundResource(R.mipmap.ic_tabbar_hichina);
            item_funtests.setBackgroundResource(R.mipmap.ic_tabbar_funtests);
        } else if (position == 2) {
            item_home.setBackgroundResource(R.mipmap.ic_tabbar_home);
            item_share.setBackgroundResource(R.mipmap.ic_tabbar_share);
            item_hichina.setBackgroundResource(R.mipmap.ic_tabbar_hichina_h);
            item_funtests.setBackgroundResource(R.mipmap.ic_tabbar_funtests);
        } else if (position == 3) {
            item_home.setBackgroundResource(R.mipmap.ic_tabbar_home);
            item_share.setBackgroundResource(R.mipmap.ic_tabbar_share);
            item_hichina.setBackgroundResource(R.mipmap.ic_tabbar_hichina);
            item_funtests.setBackgroundResource(R.mipmap.ic_tabbar_funtests_h);
        }
    }

    private void initView() {

        item_home = (ImageView) findViewById(R.id.home);
        item_share = (ImageView) findViewById(R.id.share);
        item_hichina = (ImageView) findViewById(R.id.hichina);
        item_funtests = (ImageView) findViewById(R.id.funtests);

        item_home.setOnClickListener(this);
        item_share.setOnClickListener(this);
        item_hichina.setOnClickListener(this);
        item_funtests.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.main_viewpager);
        homeFragment = new HomeFragment();
        shareFragment = new ShareFragment();
hiChinaFragment=new HiChinaFragment();
funTestsFragment=new FunTestsFragment();
        //给FragmentList添加数据
        mFragmentList.add(homeFragment);
        mFragmentList.add(shareFragment);
        mFragmentList.add(hiChinaFragment);
        mFragmentList.add(funTestsFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                vp.setCurrentItem(0, true);
                break;
            case R.id.share:
                vp.setCurrentItem(1, true);
                break;
            case R.id.hichina:
                vp.setCurrentItem(2, true);
                break;
            case R.id.funtests:
                vp.setCurrentItem(3, true);
                break;
            default:
                break;
        }
    }
}
