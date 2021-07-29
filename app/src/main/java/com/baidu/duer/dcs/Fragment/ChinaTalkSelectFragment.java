package com.baidu.duer.dcs.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.chinatalk.ResultTestActivity;
import com.baidu.duer.dcs.chinatalk.SelectTestActivity;
import com.baidu.duer.dcs.chinatalk.TestScoreActivity;
import com.baidu.duer.dcs.framework.message.Event;
import com.baidu.duer.dcs.util.CPResourceUtil;

public class ChinaTalkSelectFragment extends Fragment {
    public static String EVENT="com.baidu.duer.dcs.Fragment.ChinaTalkSelectFragment";

    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象

    private TextView tipTitle;
    private TextView tipDesc;

    private String TestName;

    private int mPosition;//位置序号
    private int mQno;
    private String mQuestion;
    private String mAnswer;
    private String mItem1;
    private String mItem2;
    private String mItem3;
    private String mItem4;
    private String mDesc;
    private String mTip;
    private String mKeyWord;

    private boolean isFirstAnswer=true;//是否第一次回答
    private boolean isGetScore=false;//是否要获取成绩
    private int Score;//将接收到的成绩存储起来
    //声明一个判断答题是否正确的boolean
    boolean isTrue=false;//----广播END

    private int mCount;//存储题目总数

    String[] tip_color={"color_wrong","color_right"};//对应答错和答对的颜色

    //获取该碎片的一个实例
    public static ChinaTalkSelectFragment newInstance(int position,int Qno, String question, String answer,String item1,String item2,String item3,String item4,String desc,String tip,String key_word,int count){
        ChinaTalkSelectFragment fragment = new ChinaTalkSelectFragment();//创建该碎片的一个实例
        Bundle bundle = new Bundle();//创建一个新包裹
        bundle.putInt("position",position);
        bundle.putInt("Qno",Qno);
        bundle.putString("question",question);
        bundle.putString("answer",answer);
        bundle.putString("item1",item1);
        bundle.putString("item2",item2);
        bundle.putString("item3",item3);
        bundle.putString("item4",item4);
        bundle.putString("desc",desc);
        bundle.putString("tip",tip);
        bundle.putString("key_word",key_word);
        bundle.putInt("count",count);

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
            mItem1=getArguments().getString("item1");
            mItem2=getArguments().getString("item2");
            mItem3=getArguments().getString("item3");
            mItem4=getArguments().getString("item4");
            mDesc=getArguments().getString("desc");
            mTip=getArguments().getString("tip");
            mKeyWord=getArguments().getString("key_word");
            mCount=getArguments().getInt("count");
        }

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_select,container,false);

        TextView select_question=mView.findViewById(R.id.select_question);
        Button rb_A=mView.findViewById(R.id.rb_A);
        Button rb_B=mView.findViewById(R.id.rb_B);
        Button rb_C=mView.findViewById(R.id.rb_C);
        Button rb_D=mView.findViewById(R.id.rb_D);

        select_question.setText((mPosition+1)+".  "+mQuestion);
        rb_A.setText(mItem1);
        rb_B.setText(mItem2);
        rb_C.setText(mItem3);
        rb_D.setText(mItem4);

        tipTitle=mView.findViewById(R.id.textView9);
        tipDesc=mView.findViewById(R.id.textView10);

        //从布局文件获得名叫rg_select的单选组
        RadioGroup rg_select = mView.findViewById(R.id.rg_select);
        //设置单选监听器
        rg_select.setOnCheckedChangeListener(new RadioListener());

        return mView;//返回该碎片的视图对象
    }

    //声明一个广播接收器
    private ChinaTalkSelectFragment.MyScore myScore;

    class RadioListener implements RadioGroup.OnCheckedChangeListener{
        //用户点击组内的单选按钮时触发
        public void onCheckedChanged(RadioGroup group,int checkedId){

            RadioButton r_btn=(RadioButton) group.findViewById(group.getCheckedRadioButtonId());

            if(r_btn.getText().toString().contains(mAnswer)){//答对了
                //与答案一样---正确
                isTrue=true;//设置答题为正确
                r_btn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_selector),null,null,null);

                String title="答对了";
                //设置一些提示语(恭喜语, 解析)
                String desc=String.format("解析:\n%s",mDesc);
                tipTitle.setText(title);
                tipTitle.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[1])));
                tipDesc.setText(desc);
                tipDesc.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[1])));

                //发出 "叮"  的声音,2s后自动前往下一个碎片



            }else{//答错了
                isTrue=false;
                r_btn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_selecter_wrong),null,null,null);

                String tip_desc="答错了!";
                //显示题目提示
                String desc=String.format("答错了! 我可以提供一点提示:\n%s",mTip);
                tipTitle.setText(tip_desc);
                tipTitle.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[0])));
                tipDesc.setText(desc);
                tipDesc.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[0])));
            }

            //如果是第一次回答,将回答结果广播出去,否则不广播
            if(isFirstAnswer){
                isGetScore=false;
                //创建一个广播事件的意图
                Intent intent=new Intent(ChinaTalkSelectFragment.EVENT);
                intent.putExtra("isTrue",isTrue);
                intent.putExtra("isGetScore",isGetScore);
                //通过本地的广播管理器来发送广播---将是否答对发送出去
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                isFirstAnswer=false;

            }

            if(mPosition==mCount-1){//如果是最后一题--向主activity请求成绩,并注册广播接收器--在广播接收器中构建可跳转成绩页面的对话框
                isGetScore=true;
                //创建一个广播事件的意图---向主页面请求成绩
                Intent intent=new Intent(ChinaTalkSelectFragment.EVENT);
                intent.putExtra("isTrue",false);
                intent.putExtra("isGetScore",isGetScore);
                //通过本地的广播管理器来发送广播---将是否答对发送出去
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                //创建一个接收成绩的广播接收器
                myScore=new ChinaTalkSelectFragment.MyScore();
                //创建一个意图过滤器,只处理指定事件来源的广播
                IntentFilter filter=new IntentFilter(SelectTestActivity.EVENT);
                //注册广播接收器,注册之后才能正常接收广播
                LocalBroadcastManager.getInstance(mContext).registerReceiver(myScore,filter);


            }

            isFirstAnswer=false;//已答过一次了,
//            Toast.makeText(mContext,"您选中了"+r_btn.getText().toString(),
//                    Toast.LENGTH_LONG).show();


            Toast.makeText(mContext,"您选中了控件"+r_btn.getText().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }


    //定义一个广播接收器,用于接收成绩
    private class MyScore extends BroadcastReceiver {
        //一旦接收到成绩的广播,马上触发接收器的onReceive方法
        public void onReceive(Context context, final Intent intent){
            if (intent!=null){
                //从广播消息中取出最新结果
                int m_Score=intent.getIntExtra("Score",0);
                TestName=intent.getStringExtra("TestName");
                Score=m_Score;//将成绩保存在这里


                //创建答题结束对话框的建造器
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                //给建造器设置对话框的标题文本
                builder.setTitle("答题结束");
                //给建造器设置对话框的信息文本
                builder.setMessage("快来看看您的成绩吧!");
                //给建造器设置对话框的肯定按钮文本及其点击监听器
                builder.setPositiveButton("看成绩", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转页面--并将Score一起带过去
                        Intent mIntent=new Intent(getActivity(), TestScoreActivity.class);//创建意图
                        Bundle bundle = new Bundle();//创建新包裹
                        bundle.putString("test_title",TestName);
                        bundle.putInt("Score",Score);//将Score存入包裹
                        bundle.putInt("ques_num",mCount);//总题数
                        mIntent.putExtras(bundle);//把包塞给意图
                        startActivity(mIntent);
                    }
                });
                //给建造器设置对话框的否定按钮文本及其点击监听器
                builder.setNegativeButton("再看看题目", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"再次点击确认按钮可前往成绩页面哦",Toast.LENGTH_SHORT).show();
                    }
                });
                //根据建造器完成对话框对象的构建
                AlertDialog alert=builder.create();
                //在界面上显示提醒对话框
                alert.show();
            }
        }
    }

}
