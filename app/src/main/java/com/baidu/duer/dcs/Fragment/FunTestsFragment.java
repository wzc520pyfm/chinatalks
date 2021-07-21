package com.baidu.duer.dcs.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.DcsSampleApplication;
import com.baidu.duer.dcs.androidapp.DcsSampleOAuthActivity;
import com.baidu.duer.dcs.chinatalk.AnnouncementActivity;
import com.baidu.duer.dcs.chinatalk.Dev_code;
import com.baidu.duer.dcs.chinatalk.EditInformationActivity;
import com.baidu.duer.dcs.chinatalk.GameActivity;
import com.baidu.duer.dcs.chinatalk.SelectTestActivity;
import com.baidu.duer.dcs.chinatalk.TestCenterActivity;
import com.baidu.duer.dcs.chinatalk.WrongQuesBookActivity;

//@Nullable  注解表示可以传入null
public class FunTestsFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_funtests,container,false);
        ImageButton speak_btn=view.findViewById(R.id.speak_btn);
        speak_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v){
//        Intent intent =new Intent(this.getContext(),AnnouncementActivity.class);
//        Intent intent =new Intent(this.getContext(), EditInformationActivity.class);
        //Intent intent =new Intent(this.getContext(), GameActivity.class);
//        Intent intent =new Intent(this.getContext(), SelectTestActivity.class);
        //Intent intent =new Intent(this.getContext(), WrongQuesBookActivity.class);
//        Intent intent =new Intent(this.getContext(), TestCenterActivity.class);
        Intent intent =new Intent(this.getContext(), Dev_code.class);
//        Intent intent =new Intent(this.getContext(), DcsSampleOAuthActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
