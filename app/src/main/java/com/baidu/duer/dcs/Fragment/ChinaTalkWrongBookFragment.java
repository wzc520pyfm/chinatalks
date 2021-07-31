package com.baidu.duer.dcs.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
/****************************************************************************************************
 * 类:                碎片, Select页面的碎片, 存在于Select页的ViewPager中
 *
 * 页面主要逻辑:       1.利用newInstance方法创建碎片单例对象,在页面onCreateView函数中首先从包
 *                    裹中获取数据,将内容显示在碎片上,最后onCreateView返回该碎片视图的对象.
 *                    2.页面监听一个单选组的单选事件
 * 注意:               1.
 *
 * ==================================================================================================*/
public class ChinaTalkWrongBookFragment extends Fragment {
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象

    private int mPosition;//位置序号
    private String mQuestion;
    private String mAnswer;
    private String[] mItems;
    private String mWrongFrom;

    //获取该碎片的一个实例
    public static ChinaTalkWrongBookFragment newInstance(int position, String question,String[] items,String answer,String wrong_from){
        ChinaTalkWrongBookFragment fragment = new ChinaTalkWrongBookFragment();//创建该碎片的一个实例
        Bundle bundle = new Bundle();//创建一个新包裹
        bundle.putInt("position",position);
        bundle.putString("answer",answer);
        bundle.putString("question",question);
        bundle.putStringArray("item",items);
        bundle.putString("wrong_from",wrong_from);

        fragment.setArguments(bundle);
        return fragment;
    }

    //创建碎片视图
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContext=getActivity();//获取获得页面的上下文

        if(getArguments()!=null){//如果碎片携带有包裹,则打开包裹获取参数信息
            mPosition=getArguments().getInt("position",0);
            mQuestion=getArguments().getString("question");
            mAnswer=getArguments().getString("answer");
            mItems=getArguments().getStringArray("item");
            mWrongFrom=getArguments().getString("wrong_from");
        }

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_wrong_book,container,false);

        TextView wrong_question=mView.findViewById(R.id.wrong_question);
        Button rb_A=mView.findViewById(R.id.rb_A);
        Button rb_B=mView.findViewById(R.id.rb_B);
        Button rb_C=mView.findViewById(R.id.rb_C);
        Button rb_D=mView.findViewById(R.id.rb_D);
        TextView wrong_from=mView.findViewById(R.id.wrong_from);

        wrong_question.setText(mQuestion);
        rb_A.setText(mItems[0]);
        rb_B.setText(mItems[1]);
        rb_C.setText(mItems[2]);
        rb_D.setText(mItems[3]);
        wrong_from.setText(mWrongFrom);

        //从布局文件获得名叫rg_select的单选组
        RadioGroup rg_wrong = mView.findViewById(R.id.rg_wrong);
        //设置单选监听器
        rg_wrong.setOnCheckedChangeListener(new RadioListener());

        return mView;//返回该碎片的视图对象
    }

    class RadioListener implements RadioGroup.OnCheckedChangeListener{
        //用户点击组内的单选按钮时触发
        public void onCheckedChanged(RadioGroup group,int checkedId){

            RadioButton r_btn=(RadioButton) group.findViewById(group.getCheckedRadioButtonId());
            Toast.makeText(mContext,"您选中了控件"+r_btn.getText().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
