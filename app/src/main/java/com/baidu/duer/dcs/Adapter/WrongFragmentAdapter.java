package com.baidu.duer.dcs.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baidu.duer.dcs.Fragment.ChinaTalkWrongBookFragment;
import com.baidu.duer.dcs.bean.Wrong;

import java.util.ArrayList;
/****************************************************************************************************
 * 类:               错题册页的碎片适配器
 * 主要逻辑:          实现适配器基本函数和逻辑
 *
 * ==================================================================================================*/
public class WrongFragmentAdapter extends FragmentStatePagerAdapter {
    //声明题目队列
    private ArrayList<Wrong> mWrongList = new ArrayList<Wrong>();

    //碎片页适配器的构造函数,传入碎片管理器与题目信息队列
    public WrongFragmentAdapter(FragmentManager fm, ArrayList<Wrong> wrongsList){
        super(fm);
        mWrongList=wrongsList;
    }

    //获取碎片Fragment的个数
    public int getCount(){
        return mWrongList.size();
    }

    //获取指定位置的碎片Fragment
    public Fragment getItem(int position){
        return ChinaTalkWrongBookFragment.newInstance(position,mWrongList.get(position).question,mWrongList.get(position).item,mWrongList.get(position).answer,mWrongList.get(position).wrong_from);
    }


}
