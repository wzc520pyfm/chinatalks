package com.baidu.duer.dcs.bean;

import java.util.ArrayList;
/*********************************************************************************
 * 类:                bean类, AnnInfo数据类型
 * 用途:              自定义AnnInfo数据类型
 * 逻辑:              除本身数据初始化外,还提供了一个获取默认值的函数,方便测试
 * 注意:
 *=============================================================================== */
public class AnnInfo {
    public String title;
    public String release_time;
    public String main_text;

    public AnnInfo(){
        title="";
        release_time="";
        main_text="";
    }

    //声明一个公告标题的数组
    private static String[] mTitleArray={
            "This is First important title",
            "This is Second important title",
            "This is Third important title",
            "This is Fourth important title"
    };
    //声明一个公告发布时间的数组
    private static String[] mTimeArray={
            "2019-02-27",
            "2020-09-18",
            "2021-03-20",
            "2021-07-15"
    };
    //声明一个公告内容的数组
    private static String[] mTextArray={
            "This is the First important text,if you see,you will become beautiful",
            "This is the Second important text,if you see,you will become beautiful",
            "This is the Third important text,if you see,you will become beautiful",
            "This is the Fourth important text,if you see,you will become beautiful"
    };

    //获取默认的手机信息列表
    public static ArrayList<AnnInfo> getDefaultList(){
        ArrayList<AnnInfo> annList=new ArrayList<AnnInfo>();
        for(int i=0;i<mTimeArray.length;i++){
            AnnInfo info=new AnnInfo();
            info.title=mTitleArray[i];
            info.release_time=mTimeArray[i];
            info.main_text=mTextArray[i];

            annList.add(info);
        }
        return annList;
    }
}
