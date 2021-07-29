package com.baidu.duer.dcs.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.chinatalk.GameActivity;
import com.baidu.duer.dcs.chinatalk.ResultTestActivity;
import com.baidu.duer.dcs.chinatalk.TestScoreActivity;
import com.baidu.duer.dcs.util.CPResourceUtil;


public class ChinaTalkResultFragment extends Fragment {
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象

    private String TestName;

    private ImageButton speak_btn;
    private TextView main_title;
    private TextView question;
    private RadioButton rb_item1;
    private RadioButton rb_item2;
    private RadioButton rb_item3;
    private RadioButton rb_item4;
    private TextView tip;

    private  int mCount;//总题数
    private int mPosition;//位置序号
    public int wno;//题目id
    public String word;//答案
    public String wgra;//答案的其他词性
    public String wmeans;//问题
    public String wexplain;//英文版问题
    public String wexample;//例句
    public String wpinyin;//答案的拼音
    public String item1;//选项
    public String item2;//选项
    public String item3;//选项
    public String item4;//选项
    private String TAG="ResultActivity";

    private boolean isFirstAnswer=true;//是否第一次回答
    private boolean isGetScore=false;//是否要获取成绩
    private int Score;//将接收到的成绩存储起来

    //声明一个广播事件的标识串----广播BEGIN
    public final static String EVENT="com.baidu.duer.dcs.Fragment.ChinaTalkResultFragment";
    //声明一个判断答题是否正确的boolean
    boolean isTrue=false;//----广播END





    String[] tip_color={"color_wrong","color_right"};//对应答错和答对的颜色




    //获取该碎片的一个实例
    public static ChinaTalkResultFragment newInstance(int position, int wno, String word, String wgra,String wmeans,String wexplain,String wexample,String wpinyin,String item1,String item2,String item3,String item4,int Count){//需补充tip等参数
        ChinaTalkResultFragment fragment = new ChinaTalkResultFragment();//创建该碎片的一个实例
        Bundle bundle = new Bundle();//创建一个新包裹
        bundle.putInt("position",position);//存入位置编号
        bundle.putInt("wno",wno);//存入图片资源id
        bundle.putString("word",word);//存入题目
        bundle.putString("wgra",wgra);//存入答案
        bundle.putString("wmeans",wmeans);//存入题目提示
        bundle.putString("wexplain",wexplain);
        bundle.putString("wexample",wexample);
        bundle.putString("wpinyin",wpinyin);
        bundle.putString("item1",item1);
        bundle.putString("item2",item2);
        bundle.putString("item3",item3);
        bundle.putString("item4",item4);
        bundle.putInt("count",Count);


        fragment.setArguments(bundle);//把包裹塞给碎片
        return fragment;//返回碎片实例


    }

    //创建碎片视图
    @Override
    @SuppressLint("ShowToast")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContext=getActivity();//获取获得页面的上下文

        //如果碎片携带有包裹,则打开包裹获取参数信息
        if(getArguments()!=null){
            mPosition = getArguments().getInt("position",0);
            wno = getArguments().getInt("wno",0);//题目id
            word = getArguments().getString("word");//答案
            wgra = getArguments().getString("wgra");//答案的其他词性
            wmeans=getArguments().getString("wmeans");//问题
            wexplain=getArguments().getString("wexplain");//英文版问题
            wexample=getArguments().getString("wexample");//例句
            wpinyin=getArguments().getString("wpinyin");//答案的拼音
            item1=getArguments().getString("item1");
            item2=getArguments().getString("item2");
            item3=getArguments().getString("item3");
            item4=getArguments().getString("item4");
            mCount=getArguments().getInt("count");//总题数
        }

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_result,container,false);

        //获取组件
        question=mView.findViewById(R.id.textView25);
        rb_item1=mView.findViewById(R.id.rb_A);
        rb_item2=mView.findViewById(R.id.rb_B);
        rb_item3=mView.findViewById(R.id.rb_C);
        rb_item4=mView.findViewById(R.id.rb_D);
        tip=mView.findViewById(R.id.tip);


        question.setText(""+(mPosition+1)+".  "+wmeans);
        rb_item1.setText(item1);
        rb_item2.setText(item2);
        rb_item3.setText(item3);
        rb_item4.setText(item4);



        //从布局文件获得名叫rg_select的单选组
        RadioGroup rg_select = mView.findViewById(R.id.rg_select);
        //设置单选监听器
        rg_select.setOnCheckedChangeListener(new ChinaTalkResultFragment.RadioListener());



        return mView;//返回该碎片的视图对象
    }




    //声明一个广播接收器
    private ChinaTalkResultFragment.MyScore myScore;

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

    class RadioListener implements RadioGroup.OnCheckedChangeListener{
        //用户点击组内的单选按钮时触发
        public void onCheckedChanged(RadioGroup group,int checkedId){

            RadioButton r_btn=(RadioButton) group.findViewById(group.getCheckedRadioButtonId());

            if(r_btn.getText().toString().contains(word)){//答对了
                //与答案一样---正确
                isTrue=true;//设置答题为正确
                r_btn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_selector),null,null,null);

                //设置一些提示语(恭喜语, 解析)
                String desc=String.format("答对了!\n解析:\n题目:%s\n'%s' 的读音是: %s\n他还有其他词性: %s\n例句: %s",wexplain,word,wpinyin,wgra,wexample);
                tip.setText(desc);
                tip.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[1])));

                //发出 "叮"  的声音,2s后自动前往下一个碎片



            }else{//答错了
                isTrue=false;
                r_btn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_selecter_wrong),null,null,null);

                //显示题目提示
                String desc=String.format("答错了! 我可以提供一点提示:\n他还有其他的词性比如: %s",wgra);
                tip.setText(desc);
                tip.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[0])));
            }

            //如果是第一次回答,将回答结果广播出去,否则不广播
            if(isFirstAnswer){
                isGetScore=false;
                //创建一个广播事件的意图
                Intent intent=new Intent(ChinaTalkResultFragment.EVENT);
                intent.putExtra("isTrue",isTrue);
                intent.putExtra("isGetScore",isGetScore);
                //通过本地的广播管理器来发送广播---将是否答对发送出去
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                isFirstAnswer=false;

            }

            if(mPosition==mCount-1){//如果是最后一题--向主activity请求成绩,并注册广播接收器--在广播接收器中构建可跳转成绩页面的对话框
                isGetScore=true;
                //创建一个广播事件的意图---向主页面请求成绩
                Intent intent=new Intent(ChinaTalkResultFragment.EVENT);
                intent.putExtra("isTrue",false);
                intent.putExtra("isGetScore",isGetScore);
                //通过本地的广播管理器来发送广播---将是否答对发送出去
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                //创建一个接收成绩的广播接收器
                myScore=new ChinaTalkResultFragment.MyScore();
                //创建一个意图过滤器,只处理指定事件来源的广播
                IntentFilter filter=new IntentFilter(ResultTestActivity.EVENT);
                //注册广播接收器,注册之后才能正常接收广播
                LocalBroadcastManager.getInstance(mContext).registerReceiver(myScore,filter);


            }

            isFirstAnswer=false;//已答过一次了,
//            Toast.makeText(mContext,"您选中了"+r_btn.getText().toString(),
//                    Toast.LENGTH_LONG).show();
        }
    }

}
