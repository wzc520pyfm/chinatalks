package com.baidu.duer.dcs.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.database.TestCenterDBHelper;

public class ChinaTalkHomeFragment extends Fragment implements View.OnClickListener {

    protected View mView;
    protected Context mContext;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();//获取获得页面的上下文

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_home,container,false);

        return mView;//返回该碎片的视图对象
    }

    @Override
    public void onClick(View v) {

    }
}
