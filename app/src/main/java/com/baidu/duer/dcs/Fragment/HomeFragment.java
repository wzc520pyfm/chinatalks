package com.baidu.duer.dcs.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.duer.dcs.Adapter.FragmentAdapter;
import com.baidu.duer.dcs.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private View view;
    private TextView item_intro, item_news, item_policy;
    private ViewPager vp;
    private IntroductionFragment introductionFragment;
    private NewsFragment newsFragment;
    private PolicyFragment policyFragment;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home,container,false);
        initView();

        initFragment();
        return view;
    }
    private void initView() {
        item_intro= (TextView) view.findViewById(R.id.item_introduction);
        item_news= (TextView) view.findViewById(R.id.item_news);
        item_policy= (TextView)view. findViewById(R.id.item_policy);

        item_intro.setOnClickListener(this);
        item_news.setOnClickListener(this);
        item_policy.setOnClickListener(this);

        vp= (ViewPager) view.findViewById(R.id.fragment_home_viewpager);
        introductionFragment=new IntroductionFragment();
        newsFragment=new NewsFragment();
        policyFragment=new PolicyFragment();
        //给FragmentList添加数据
        mFragmentList.add(introductionFragment);
        mFragmentList.add(newsFragment);
        mFragmentList.add(policyFragment);
    }
    private void initFragment() {
        mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(3);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        TextPaint textPaint=item_intro.getPaint();
        textPaint.setFakeBoldText(true);

        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/

                changeTextColor(position);
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

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
           item_intro.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
           item_intro.getPaint().setFakeBoldText(true);
           item_news.getPaint().setFakeBoldText(false);
            item_news.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_policy.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
           item_policy.getPaint().setFakeBoldText(false);
        } else if (position == 1) {
            item_intro.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_intro.getPaint().setFakeBoldText(false);
            item_news.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            item_news.getPaint().setFakeBoldText(true);
            item_policy.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_policy.getPaint().setFakeBoldText(false);
        } else if (position == 2) {
            item_intro.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_intro.getPaint().setFakeBoldText(false);
            item_policy.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            item_news.getPaint().setFakeBoldText(false);
            item_news.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_policy.getPaint().setFakeBoldText(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_introduction:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_news:
                vp.setCurrentItem(1, true);
                break;
            case R.id.item_policy:
                vp.setCurrentItem(2, true);
                break;
        }

    }
}
