package com.baidu.duer.dcs.bean;

import android.content.IntentFilter;
/*********************************************************************************
 * 类:                bean类, Result数据类型
 * 用途:              自定义Result数据类型
 * 逻辑:              数据初始化
 * 注意:
 *=============================================================================== */
public class Result {
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

    public Result(){
        wno=0;
        word="";
        wgra="";
        wmeans="";
        wexplain="";
        wexample="";
        wpinyin="";
        item1="";
        item2="";
        item3="";
        item4="";
    }
}
