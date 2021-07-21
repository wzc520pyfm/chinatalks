package com.baidu.duer.dcs.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.MainActivity;
import com.baidu.duer.dcs.androidapp.NewsDetailActivity;

public class NewsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_news, container, false);
    }

    private LinearLayout new_1, new_2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new_1 = (LinearLayout) getActivity().findViewById(R.id.new_1);
        new_2 = (LinearLayout) getActivity().findViewById(R.id.new_2);
        new_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),NewsDetailActivity.class));
//                Toast.makeText(getActivity(), "chenggong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
