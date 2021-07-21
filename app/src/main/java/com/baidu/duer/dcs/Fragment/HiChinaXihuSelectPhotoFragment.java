package com.baidu.duer.dcs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.duer.dcs.Adapter.MyAdapter;
import com.baidu.duer.dcs.R;
import com.ryan.rv_gallery.AnimManager;
import com.ryan.rv_gallery.GalleryRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HiChinaXihuSelectPhotoFragment extends Fragment {

    private List<Integer> mDatas;
    private   GalleryRecyclerView mRecyclerView;
    private int currentIndex;
    private   LinearLayoutManager layoutManager;
    private boolean isFirst=true;

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hichina_xihu_selectphoto, container, false);

        mDatas=new ArrayList<>();
        mDatas.add(R.drawable.yishuzhao);
        mDatas.add(R.drawable.yishuzhao2);


        mRecyclerView = (GalleryRecyclerView) view.findViewById(R.id.rv_list);
        MyAdapter adapter = new MyAdapter(mDatas);
        layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setAdapter(adapter);



        mRecyclerView
                // 设置滑动速度（像素/s）
                .initFlingSpeed(9000)
                // 设置页边距和左右图片的可见宽度，单位dp
              //  .initPageParams(0, 40)
                // 设置切换动画的参数因子
                .setAnimFactor(0.1f)
                // 设置切换动画类型，目前有AnimManager.ANIM_BOTTOM_TO_TOP和目前有AnimManager.ANIM_TOP_TO_BOTTOM
                .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)
                // 设置点击事件
//                .setOnItemClickListener(this)
                // 设置自动播放
                .autoPlay(false)
                // 设置自动播放间隔时间 ms
//                .intervalTime(2000)
                // 设置初始化的位置
                .initPosition(0)
                // 在设置完成之后，必须调用setUp()方法
                .setUp();



        return view;
    }

    public int getCurrentIndex(){

            currentIndex=layoutManager.findFirstCompletelyVisibleItemPosition();

        return currentIndex;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mRecyclerView != null) {
            // 释放资源
            mRecyclerView.release();
        }
    }


}
