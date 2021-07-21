package com.baidu.duer.dcs.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.DcsSampleApplication;
import com.baidu.duer.dcs.bean.TestCenter;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class TestCenterAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;//声明一个上下文对象
    private ArrayList<TestCenter> mTestCenterArray;//声明一个试卷信息队列

    //购物车适配器的构造函数,传入上下文、信息队列
    public TestCenterAdapter(Context context,ArrayList<TestCenter> test_list){
        mContext=context;
        mTestCenterArray=test_list;
    }

    //获取列表项的个数
    public int getCount(){
        return mTestCenterArray.size();
    }
    //获取列表项的数据
    public Object getItem(int arg0){
        return mTestCenterArray.get(arg0);
    }
    //获取列表项的编号
    public long getItemId(int arg0){
        return arg0;
    }

    //获取指定位置的列表项视图
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        TestCenter info=mTestCenterArray.get(position);
        //这里视图重用有bug,如果启用视图重用,第二次的视图会重用第一次的视图,而第二次需要使用的后三个对象没有在第一次完成初始化,会导致空指针保错,最简单的解决办法就是放弃视图重用
        if(info.finshed_num == -1){//如果是真题测试,那么此值储存为-1
            //if(convertView == null){//转换视图为空
                holder = new ViewHolder();//创建一个新的视图持有者
                //根据布局文件生成转换视图对象
                convertView= LayoutInflater.from(mContext).inflate(R.layout.chinatalk_item_test_center_start,null);
                holder.iv_icon=convertView.findViewById(R.id.imageView11);
                holder.iv_title=convertView.findViewById(R.id.textView26);
                holder.iv_desc=convertView.findViewById(R.id.textView34);
                holder.iv_score=convertView.findViewById(R.id.textView36);
                holder.iv_test_time=convertView.findViewById(R.id.textView38);
                holder.iv_start=convertView.findViewById(R.id.button16);
                //将视图持有者保存到转换视图当中
                convertView.setTag(holder);
            //}else{//转换视图非空
                //从转换视图中获取之前保存的视图持有者
             //   holder=(ViewHolder) convertView.getTag();
            //}
            holder.iv_icon.setImageResource(R.drawable.ic_home_exam);
            holder.iv_title.setText(info.title);
            holder.iv_desc.setText(info.desc);
            holder.iv_score.setText(""+info.score);
            holder.iv_test_time.setText(info.test_time);
            holder.iv_start.setOnClickListener(this);

        }else{//随机测试
            //if(convertView == null){//转换视图为空
                holder = new ViewHolder();//创建一个新的视图持有者
                //根据布局文件生成转换视图对象
                convertView= LayoutInflater.from(mContext).inflate(R.layout.chinatalk_item_test_center_continue,null);
                holder.iv_icon=convertView.findViewById(R.id.imageView11);
                holder.iv_title=convertView.findViewById(R.id.textView26);
                holder.iv_desc=convertView.findViewById(R.id.textView34);
                holder.iv_finshed_num=convertView.findViewById(R.id.textView36);
                holder.iv_restart=convertView.findViewById(R.id.button15);
                holder.iv_continue=convertView.findViewById(R.id.button17);
                //将视图持有者保存到转换视图当中
                convertView.setTag(holder);
            //}else{//转换视图非空
                //从转换视图中获取之前保存的视图持有者
            //    holder=(ViewHolder) convertView.getTag();
            //}
            holder.iv_icon.setImageResource(R.drawable.ic_exam_random);
            holder.iv_title.setText(info.title);
            holder.iv_desc.setText(info.desc);
            holder.iv_finshed_num.setText(""+info.finshed_num);
            holder.iv_restart.setOnClickListener(this);
            holder.iv_continue.setOnClickListener(this);
        }

        return convertView;
    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.button16){//开始

        }else if(v.getId() == R.id.button15){//重新开始

        }else if(v.getId()==R.id.button17){//继续测试

        }
    }

    //定义一个视图持有者,以便重用列表项的视图资源
    public final class ViewHolder{
        //public View v_shape;//试卷布局
        public ImageView iv_icon;//图标
        public TextView iv_title;//标题
        public TextView iv_desc;//描述
        public TextView iv_finshed_num;//! 已完成题目数
        public TextView iv_score;//~分数
        public TextView iv_test_time;//时限
        public Button iv_restart;//! 重新开始
        public Button iv_continue;//! 继续
        public Button iv_start;//~ 开始
    }
}
