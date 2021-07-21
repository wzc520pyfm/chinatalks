package com.baidu.duer.dcs.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.duer.dcs.R;

public class ChinaTalkTestFragment extends Fragment {
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象

    //创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContext=getActivity();//获取获得页面的上下文
        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_test,container,false);

        return mView;//返回该碎片的视图对象
    }
}
