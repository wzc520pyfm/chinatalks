package com.baidu.duer.dcs.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baidu.duer.dcs.Fragment.ChinaTalkGameFragment;
import com.baidu.duer.dcs.Fragment.ChinaTalkResultFragment;
import com.baidu.duer.dcs.bean.Game;
import com.baidu.duer.dcs.bean.Result;

import java.util.ArrayList;

public class ResultFragementAdapter extends FragmentStatePagerAdapter {
    //声明一个题目信息队列
    private ArrayList<Result> mResultList = new ArrayList<Result>();

    //碎片也适配器的构造函数,传入碎片管理器与题目信息队列
    public ResultFragementAdapter(FragmentManager fm, ArrayList<Result> resultsList){
        super(fm);
        mResultList=resultsList;


    }

    //获取碎片的Fragment的个数
    public int getCount(){
        return mResultList.size();
    }

    //获取指定位置的碎片Fragment
    public Fragment getItem(int position){
        return ChinaTalkResultFragment.newInstance(position,mResultList.get(position).wno,mResultList.get(position).word,mResultList.get(position).wgra,mResultList.get(position).wmeans,mResultList.get(position).wexplain,mResultList.get(position).wexample,mResultList.get(position).wpinyin,mResultList.get(position).item1,mResultList.get(position).item2,mResultList.get(position).item3,mResultList.get(position).item4,getCount());
    }
}
