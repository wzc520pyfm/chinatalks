package com.baidu.duer.dcs.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.util.Image;

public class ChinaTalkGameFragment extends Fragment {
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象

    private int mPosition;//位置序号
    private int mPic;//图片的资源编号
    private String mQuestion;//题目
    private String mAnswer;//答案

    //获取该碎片的一个实例
    public static ChinaTalkGameFragment newInstance(int position, int pic_id, String ques, String ans){
        ChinaTalkGameFragment fragment = new ChinaTalkGameFragment();//创建该碎片的一个实例
        Bundle bundle = new Bundle();//创建一个新包裹
        bundle.putInt("position",position);//存入位置编号
        bundle.putInt("pic",pic_id);//存入图片资源id
        bundle.putString("question",ques);//存入题目
        bundle.putString("answer",ans);//存入答案

        fragment.setArguments(bundle);//把包裹塞给碎片
        return fragment;//返回碎片实例


    }

    //创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContext=getActivity();//获取获得页面的上下文
        //如果碎片携带有包裹,则打开包裹获取参数信息
        if(getArguments()!=null){
            mPosition = getArguments().getInt("position",0);
            mPic = getArguments().getInt("pic",0);
            mQuestion = getArguments().getString("question");
            mAnswer = getArguments().getString("answer");
        }

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_game,container,false);

        ImageView iv_pic = mView.findViewById(R.id.imageView4);
        TextView tv_ques = mView.findViewById(R.id.title);
        iv_pic.setImageResource(mPic);
        tv_ques.setText(mQuestion);
        return mView;//返回该碎片的视图对象
    }
}
