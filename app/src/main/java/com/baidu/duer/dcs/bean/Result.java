package com.baidu.duer.dcs.bean;

import android.content.IntentFilter;

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
