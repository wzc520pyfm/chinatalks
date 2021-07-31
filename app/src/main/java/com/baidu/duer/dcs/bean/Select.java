package com.baidu.duer.dcs.bean;

import java.util.ArrayList;
/****************************************************************************************************
 * 类:                bean类, Select数据类型
 * 用途:              自定义Select数据类型
 * 逻辑:              数据初始化
 * 注意:
 * ==================================================================================================*/
public class Select {//各属性与数据库中的属性名对应
    public int Qno;
    public String question;
    public String answer;
    public String item1;
    public String item2;
    public String item3;
    public String item4;
    public String desc;
    public String tip;
    public String key_word;

    public Select(){
        Qno=0;
        question="";
        answer="";
        item1="";
        item2="";
        item3="";
        item4="";
        desc="";
        tip="";
        key_word="";
    }
    public static int[] QnoArray={
            0,
            1,
            2
    };

    public static String[] questionArray={
        "79.下雨天地滑,小明SDASDADDADADADASDADASDASDASDASDASDASDASDASDASD?",
            "80.ADADADADADQWDRSDUBFKSJBFEUFBFUQDBJUWBDJBQWUBQWJBQUWE",
            "81. KNJHVYGCFTDXZEAWESZDRTFVGYUHJIKJMKMLKFDDFDSFS"
    };
    public static String[] answerArray={
        "A. 名词",
            "B. bala",
            "C. xixixi"
    };
    public static String[] item1Array={
                "A. 名词",
                "A. 动词",
                "A. 结构助词",

            };

    public static String[] item2Array={
                    "B. lala",
                    "B. bala",
                    "B. lili",

            };
    public static String[] item3Array={
                    "C. popo",
                    "C. vivi",
                    "C. xixixi",

            };
    public static String[] item4Array={
            "D. lplp",
            "D. 形容词",
            "D. lplp",
    };
    public static String[] descArray={
            "这是第一题答对之后的试题解析",
            "这是第二题答对之后的试题解析",
            "这是第三题答对之后的试题解析"
    };
    public static String[] tipArray={
            "这是第一题答对之后的试题提示",
            "这是第二题答对之后的试题提示",
            "这是第三题答对之后的试题提示"
    };
    public static String[] keyWordArray={
            "第一个 把  ",
            "的",
            "拉"
    };
    public static ArrayList<Select> getDefaultList(){
        ArrayList<Select> selectList=new ArrayList<Select>();
        for(int i=0;i<questionArray.length;i++){
            Select info=new Select();
            info.Qno=QnoArray[i];
            info.question=questionArray[i];
            info.answer=answerArray[i];
            info.item1=item1Array[i];
            info.item2=item2Array[i];
            info.item3=item3Array[i];
            info.item4=item4Array[i];
            info.desc=descArray[i];
            info.tip=tipArray[i];
            info.key_word=keyWordArray[i];

            selectList.add(info);
        }
        return selectList;
    }
}
