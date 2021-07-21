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

public class ShareFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView item_feeling, item_photo;
    private ViewPager vp;
    private PhotoFragment photoFragment;
    private FeelingFragment feelingFragment;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share, container, false);
        initView();

        initFragment();
        return view;
    }

    private void initView() {
        item_feeling = (TextView) view.findViewById(R.id.item_feeling);
        item_photo = (TextView) view.findViewById(R.id.item_photo);

        item_feeling.setOnClickListener(this);
        item_photo.setOnClickListener(this);

        vp = (ViewPager) view.findViewById(R.id.fragment_share_viewpager);
        feelingFragment = new FeelingFragment();
        photoFragment = new PhotoFragment();
        //给FragmentList添加数据
        mFragmentList.add(feelingFragment);
        mFragmentList.add(photoFragment);
    }

    private void initFragment() {
        mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(3);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        TextPaint textPaint = item_feeling.getPaint();
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
            item_feeling.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            item_feeling.getPaint().setFakeBoldText(true);
            item_photo.getPaint().setFakeBoldText(false);
            item_photo.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else if (position == 1) {
            item_feeling.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            item_feeling.getPaint().setFakeBoldText(false);
            item_photo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            item_photo.getPaint().setFakeBoldText(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_feeling:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_photo:
                vp.setCurrentItem(1, true);
                break;
           default:
               break;
        }

    }
}
