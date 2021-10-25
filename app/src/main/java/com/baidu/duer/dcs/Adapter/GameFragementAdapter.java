package com.baidu.duer.dcs.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.duer.dcs.Fragment.ChinaTalkGameFragment;
import com.baidu.duer.dcs.bean.Game;

import java.util.ArrayList;
/*******************************************************************************************
* 类:                Game页面的碎片适配器
* 页面主要逻辑:       实现适配器基本函数
*
*
*
*
*
*
*
*
*
*
*
*========================================================================================= */
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
    public Fragment getItem(int position){//返回一个碎片,所以调用Fragment
        return ChinaTalkGameFragment.newInstance(position,mGameList.get(position).img_src,mGameList.get(position).question,mGameList.get(position).answer,mGameList.get(position).tip,getCount());
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View)object);
//    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

}
