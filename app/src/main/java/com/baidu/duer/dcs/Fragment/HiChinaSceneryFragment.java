package com.baidu.duer.dcs.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.XihuActivity;
import com.baidu.duer.dcs.androidapp.XihuTakePhotoActivity;

public class HiChinaSceneryFragment extends Fragment {
    private ImageView mXihu;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hichina_scenery, container, false);

        mXihu= (ImageView) view.findViewById(R.id.item_xihu);
        mXihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),XihuTakePhotoActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

}
