package com.baidu.duer.dcs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.duer.dcs.R;

public class HiChinaCombineBeforeFragment extends Fragment {

    private View view;
    private ImageView mBg,mDolls;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_combine_before, container, false);

        Bundle bundle=getArguments();
        int bg=bundle.getInt("Bg");
        int dolls=bundle.getInt("Dolls");

        mBg= (ImageView) view.findViewById(R.id.combine_bg);
        mDolls= (ImageView) view.findViewById(R.id.combine_dolls);

        mBg.setImageResource(bg);
        mDolls.setImageResource(dolls);


        return view;
    }

    public void setBackground(int path){
        mBg.setImageResource(path);

    }

    public void setDolls(int path){
        mDolls.setImageResource(path);
    }

}
