package com.baidu.duer.dcs.bean;

import java.util.ArrayList;

public class Select {
    public String question;
    public String answer;
    public String[] item;

    public Select(){
        question="";
        answer="";
        item=new String[4];
    }

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
    public static ArrayList<Select> getDefaultList(){
        ArrayList<Select> selectList=new ArrayList<Select>();
        for(int i=0;i<questionArray.length;i++){
            Select info=new Select();
            info.question=questionArray[i];
            info.answer=answerArray[i];
            info.item=itemArray[i];

            selectList.add(info);
        }
        return selectList;
    }
}
