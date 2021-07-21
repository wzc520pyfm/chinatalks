package com.baidu.duer.dcs.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baidu.duer.dcs.Fragment.ChinaTalkSelectFragment;
import com.baidu.duer.dcs.bean.Select;

import java.util.ArrayList;

public class SelectFragmentAdapter extends FragmentStatePagerAdapter {
    //声明题目队列
    private ArrayList<Select> mSelectList = new ArrayList<Select>();

    //碎片页适配器的构造函数,传入碎片管理器与题目信息队列
    public SelectFragmentAdapter(FragmentManager fm,ArrayList<Select> selectsList){
        super(fm);
        mSelectList=selectsList;
    }

    //获取碎片Fragment的个数
    public int getCount(){
        return mSelectList.size();
    }

    //获取指定位置的碎片Fragment
    public Fragment getItem(int position){
        return ChinaTalkSelectFragment.newInstance(position,mSelectList.get(position).question,mSelectList.get(position).item,mSelectList.get(position).answer);
    }


}