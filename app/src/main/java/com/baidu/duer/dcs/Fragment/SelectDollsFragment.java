package com.baidu.duer.dcs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.duer.dcs.R;

public class SelectDollsFragment extends Fragment implements View.OnClickListener {

    public int getmDollsNumber() {
        return mDollsNumber;
    }

    private int mDollsNumber;
    private ImageView mBnz, mFh, mXq, mXx;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hichina_scenery_selectdolls, container, false);
        initView();

        return view;
    }



    private void initView() {
        mBnz = (ImageView) view.findViewById(R.id.selecdolls_bainiangzi);
        mFh = (ImageView) view.findViewById(R.id.selecdolls_fahai);
        mXq = (ImageView) view.findViewById(R.id.selecdolls_xiaoqing);
        mXx = (ImageView) view.findViewById(R.id.selecdolls_xuxian);

        mBnz.setOnClickListener(this);
        mFh.setOnClickListener(this);
        mXq.setOnClickListener(this);
        mXx.setOnClickListener(this);
    }


    public void hideAll(){
        switch (mDollsNumber){
            case 1:
                mXx.setImageResource(R.drawable.xuxianjpg);
                break;
            case 2:
                mBnz.setImageResource(R.drawable.bainiangzijpg);
                break;
            case 3:
                mFh.setImageResource(R.drawable.fahaijpg);
                break;
            case 4:
                mXq.setImageResource(R.drawable.xiaoqingjpg);
                break;
        }
            }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.selecdolls_xuxian:
                hideAll();
                mXx.setImageResource(R.drawable.xuxian_focused);
                mDollsNumber = 1;

                break;

            case R.id.selecdolls_bainiangzi:
                hideAll();
                mBnz.setImageResource(R.drawable.bainiangzi_focused);
                mDollsNumber = 2;

                break;


            case R.id.selecdolls_fahai:
                hideAll();
                mFh.setImageResource(R.drawable.fahai_focused);
                mDollsNumber = 3;

                break;
            case R.id.selecdolls_xiaoqing:
                hideAll();
                mXq.setImageResource(R.drawable.xiaoqing_focused);
                mDollsNumber = 4;

                break;


        }
    }
}
