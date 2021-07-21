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

public class ChinaTalkSelectFragment extends Fragment {
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象

    private int mPosition;//位置序号
    private String mQuestion;
    private String mAnswer;
    private String[] mItems;

    //获取该碎片的一个实例
    public static ChinaTalkSelectFragment newInstance(int position, String question,String[] items,String answer){
        ChinaTalkSelectFragment fragment = new ChinaTalkSelectFragment();//创建该碎片的一个实例
        Bundle bundle = new Bundle();//创建一个新包裹
        bundle.putInt("position",position);
        bundle.putString("answer",answer);
        bundle.putString("question",question);
        bundle.putStringArray("item",items);

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
        }

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_select,container,false);

        TextView select_question=mView.findViewById(R.id.select_question);
        Button rb_A=mView.findViewById(R.id.rb_A);
        Button rb_B=mView.findViewById(R.id.rb_B);
        Button rb_C=mView.findViewById(R.id.rb_C);
        Button rb_D=mView.findViewById(R.id.rb_D);

        select_question.setText(mQuestion);
        rb_A.setText(mItems[0]);
        rb_B.setText(mItems[1]);
        rb_C.setText(mItems[2]);
        rb_D.setText(mItems[3]);


        //从布局文件获得名叫rg_select的单选组
        RadioGroup rg_select = mView.findViewById(R.id.rg_select);
        //设置单选监听器
        rg_select.setOnCheckedChangeListener(new RadioListener());

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
