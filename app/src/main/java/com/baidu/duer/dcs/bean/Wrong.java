package com.baidu.duer.dcs.bean;

import java.util.ArrayList;
/*********************************************************************************
 * 类:                bean类, Wrong数据类型
 * 用途:              自定义Wrong数据类型
 * 逻辑:              除本身数据初始化外,还提供了一个获取默认值的函数,方便测试
 * 注意:
 *=============================================================================== */
public class Wrong {
    public String question;
    public String answer;
    public String[] item;
    public String wrong_from;

    public Wrong(){
        question="";
        answer="";
        item=new String[4];
        wrong_from="" ;
    }
    public static String[] questionArray={
            "90.下雨天地滑,小明SDASDADDADADADASDADASDASDASDASDASDASDASDASDASD?",
            "92.ADADADADADQWDRSDUBFKSJBFEUFBFUQDBJUWBDJBQWUBQWJBQUWE",
            "93. KNJHVYGCFTDXZEAWESZDRTFVGYUHJIKJMKMLKFDDFDSFS"
    };
    public static String[] answerArray={
            "A. 名词",
            "B. bala",
            "C. xixixi"
    };
    public static String[][] itemArray={
            {
                    "A. 名词",
                    "B. 动词",
                    "C. 结构助词",
                    "D. 形容词"
            },
            {
                    "A. lala",
                    "B. bala",
                    "C. lili",
                    "D. gugu"
            },
            {
                    "A. popo",
                    "B. vivi",
                    "C. xixixi",
                    "D. lplp"
            }
    };
    public static String[] wrongFromArray={
            "错题来源: 《汉语四级真题测试卷(1)》",
            "错题来源: 《汉语四级真题测试卷(2)》",
            "错题来源: 《汉语四级真题测试卷(3)》"
    };
    public static ArrayList<Wrong> getDefaultList(){
        ArrayList<Wrong> wrongsList=new ArrayList<Wrong>();
        for(int i=0;i<questionArray.length;i++){
            Wrong info=new Wrong();
            info.question=questionArray[i];
            info.answer=answerArray[i];
            info.item=itemArray[i];
            info.wrong_from=wrongFromArray[i];

            wrongsList.add(info);
        }
        return wrongsList;
    }
}
