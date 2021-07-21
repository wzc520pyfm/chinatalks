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
import com.baidu.duer.dcs.androidapp.CultureActivity;
import com.baidu.duer.dcs.util.Culture;

public class HiChinaCultureFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView mQingming, mYuanxiao, mLaba, mDuanwu;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hichina_culture, container, false);
        initView();

        return view;
    }

    private void initView() {
        mQingming = (ImageView) view.findViewById(R.id.hichina_culture_qingming);
        mYuanxiao = (ImageView) view.findViewById(R.id.hichina_culture_yuanxiao);
        mLaba = (ImageView) view.findViewById(R.id.hichina_culture_laba);
        mDuanwu = (ImageView) view.findViewById(R.id.hichina_culture_duanwu);

        mQingming.setOnClickListener(this);
        mYuanxiao.setOnClickListener(this);
        mLaba.setOnClickListener(this);
        mDuanwu.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.hichina_culture_yuanxiao:
                break;

            case R.id.hichina_culture_qingming:
                Culture qingming=new Culture("Qingming Festival",R.mipmap.ic_qingming_image,getString(R.string.qingming_content));

                Intent intent=new Intent(getActivity(), CultureActivity.class);
                intent.putExtra("title",qingming.getTitle());
                intent.putExtra("content",qingming.getContent());
                intent.putExtra("image",qingming.getImg());
                startActivity(intent);


                break;
            case R.id.hichina_culture_duanwu:

                break;
            case R.id.hichina_culture_laba:

                break;

            default:
                break;

        }
    }
}
