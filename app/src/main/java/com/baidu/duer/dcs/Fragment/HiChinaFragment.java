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
import android.widget.TextView;

import com.baidu.duer.dcs.Adapter.FragmentAdapter;
import com.baidu.duer.dcs.R;

import java.util.ArrayList;
import java.util.List;

public class HiChinaFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView item_intro, item_scenery, item_culture;
    private ViewPager vp;
    private HiChinaCultureFragment cultureFragment;
    private HiChinaIntroductionFragment introductionFragment;
    private HiChinaSceneryFragment sceneryFragment;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hichina, container, false);
        initView();

        initFragment();
        return view;
    }


    private void initView() {
        item_intro = (TextView) view.findViewById(R.id.item_introduction);
        item_scenery = (TextView) view.findViewById(R.id.item_scenery);
        item_culture = (TextView) view.findViewById(R.id.item_culture);

        item_intro.setOnClickListener(this);
        item_scenery.setOnClickListener(this);
        item_culture.setOnClickListener(this);

        vp = (ViewPager) view.findViewById(R.id.fragment_hichina_viewpager);
        cultureFragment = new HiChinaCultureFragment();
        introductionFragment = new HiChinaIntroductionFragment();
        sceneryFragment = new HiChinaSceneryFragment();
        //给FragmentList添加数据
        mFragmentList.add(introductionFragment);
        mFragmentList.add(sceneryFragment);
        mFragmentList.add(cultureFragment);
    }

    private void initFragment() {
        mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(3);//ViewPager的缓存为3帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        TextPaint textPaint = item_intro.getPaint();
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
            item_scenery.getPaint().setFakeBoldText(false);
            item_scenery.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_culture.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_culture.getPaint().setFakeBoldText(false);
        } else if (position == 1) {
            item_intro.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_intro.getPaint().setFakeBoldText(false);
            item_scenery.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            item_scenery.getPaint().setFakeBoldText(true);
            item_culture.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_culture.getPaint().setFakeBoldText(false);
        } else if (position == 2) {
            item_intro.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_intro.getPaint().setFakeBoldText(false);
            item_scenery.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_scenery.getPaint().setFakeBoldText(false);
            item_culture.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            item_culture.getPaint().setFakeBoldText(true);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_introduction:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_scenery:
                vp.setCurrentItem(1, true);
                break;
            case R.id.item_culture:
                vp.setCurrentItem(2, true);
                break;
        }
    }
}
