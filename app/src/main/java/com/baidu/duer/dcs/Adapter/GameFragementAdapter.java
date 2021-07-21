package com.baidu.duer.dcs.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baidu.duer.dcs.Fragment.ChinaTalkGameFragment;
import com.baidu.duer.dcs.bean.Game;

import java.util.ArrayList;

public class GameFragementAdapter extends FragmentStatePagerAdapter {
    //声明一个题目信息队列
    private ArrayList<Game> mGameList = new ArrayList<Game>();

    //碎片也适配器的构造函数,传入碎片管理器与题目信息队列
    public GameFragementAdapter(FragmentManager fm, ArrayList<Game> gamesList){
        super(fm);
        mGameList=gamesList;
    }

    //获取碎片的Fragment的个数
    public int getCount(){
        return mGameList.size();
    }

    //获取指定位置的碎片Fragment
    public Fragment getItem(int position){
        return ChinaTalkGameFragment.newInstance(position,mGameList.get(position).pic,mGameList.get(position).question,mGameList.get(position).answer);
    }

    //
}
