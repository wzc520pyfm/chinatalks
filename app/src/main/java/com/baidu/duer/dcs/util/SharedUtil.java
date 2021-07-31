package com.baidu.duer.dcs.util;


import android.content.Context;
import android.content.SharedPreferences;
/************************************************************************************
 * 类:                工具类
 * 用途:               共享参数管理
 *
 *================================================================================= */
//这是共享参数的工具类,统一对共享参数的读写操作
public class SharedUtil {
    private static SharedUtil mUtil;//声明一个共享参数工具类的实例
    private static SharedPreferences mShared;//声明一个共享参数的实例

    //通过单例模式获取共享参数工具类的唯一实例
    public static SharedUtil getInstance(Context ctx){
        if(mUtil==null){
            mUtil=new SharedUtil();
        }
        //从share.xml中获取共享参数对象
        mShared=ctx.getSharedPreferences("share",Context.MODE_PRIVATE);
        return mUtil;
    }


    //把配对信息写入共享参数
    public void writeShared(String key,String value){
        SharedPreferences.Editor editor=mShared.edit();//获取编辑器的对象
        editor.putString(key,value);//添加一个指定键名的字符串参数
        editor.commit();//提交编辑器中的修改
    }

    //根据键名到共享参数中查找对应的值对象
    public String readShared(String key,String defaultValue){
        return mShared.getString(key,defaultValue);
    }

}


//  完成ShareUtil编写---共享参数
//  完成AnnouncementActicity里模拟网络的操作--用到了共享参数first
//  完成AnnouncementActicity里页面活动和暂停代码的编写,活动时执行模拟网络的操作
//这样就可以为公共赋予初始值,并且随时从本地数据库查询新数据
//  完成back按钮的操作