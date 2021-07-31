package com.baidu.duer.dcs.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.duer.dcs.Adapter.MyAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.UserInfoBottom;

import java.util.ArrayList;
/*****************************************************************************************
* 类:                用户信息页面的下半部分的列表适配器
* 用途:              展示用户信息中格式基本一致的内容,使用listView
*
*======================================================================================= */
public class UserInfoBottomAdapter extends BaseAdapter {
    private Context mContext;//声明一个上下文对象
    private ArrayList<UserInfoBottom> mInfoList;//声明一个列表信息队列

    //适配器的构造函数,传入上下文与列表信息队列
    public UserInfoBottomAdapter(Context context,ArrayList<UserInfoBottom> info_list){
        mContext=context;
        mInfoList=info_list;

        //验证4条数据是否都正常接收----结果显示成功
        for(int i=0;i<mInfoList.size();i++){
            UserInfoBottom temp=mInfoList.get(i);
            Log.i("Adapterssss",temp.mdesc);
        }
    }

    //获取列表项个数
    public int getCount(){
        return mInfoList.size();
    }

    //获取列表项的数据
    public Object getItem(int arg0){
        return mInfoList.get(arg0);
    }

    //获取列表项的编号
    public long getItemId(int arg0){
        return arg0;
    }

    //获取指定位置的列表项视图
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView==null){//转换视图为空
            holder=new ViewHolder();//创建一个新的视图持有者
            //根据布局文件生成转换视图对象
            convertView= LayoutInflater.from(mContext).inflate(R.layout.chinatalk_activity_user_info_bottom,null);
            holder.tv_title=convertView.findViewById(R.id.item_title);
            holder.et_desc=convertView.findViewById(R.id.item_text);

            //将视图持有者保存到转换视图当中
            convertView.setTag(holder);
        }else{//转换视图非空
            //从转换视图中获取之前保存的视图持有者
            holder=(ViewHolder) convertView.getTag();
        }

        UserInfoBottom my_info=mInfoList.get(position);
        Log.i("Adapter",my_info.mdesc);
        holder.tv_title.setText(my_info.mtitle);//显示标题
        holder.et_desc.setHint(my_info.mdesc);//显示提示语
        return convertView;
    }

    //定义一个视图持有者,以便重用列表项的视图资源
    public final class ViewHolder{
        public TextView tv_title;//声明标题
        public EditText et_desc;//声明编辑框
    }
}
