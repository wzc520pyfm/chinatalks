package com.baidu.duer.dcs.bean;

import com.baidu.duer.dcs.R;

import java.util.ArrayList;

public class Game {
    public int q_id;
    public String img_src;//图片路径--此参数实际不适用,从请求中获得img_src后,转换为pic编号,存入pic变量
    public String question;
    public String answer;
    public String tip; //new
    public int pic;//图片资源编号

    public Game(){
        q_id=0;
        img_src="";
        question="";
        answer="";
        tip="";

    }

    public static int[] qIdArray={
            1,
            2,
            3
    };
    public static String[] questionArray={
            "请问: 图中的食物叫什么名字?",
            "请问: 包裹粽子的叶片是什么植物的叶子?",
            "请问: 图中展示的画作属于水墨画吗?",
            "请问: 图中交谈中打招呼用语是哪句?"
    };
    public static String[] answerArray={
            "饺子",
            "荷叶",
            "不是",
            "没有"
    };
    public static int[] mPicArray={
            R.drawable.game_example,
            R.drawable.game_example,
            R.drawable.game_example,
            R.drawable.game_example
    };
    public static String[] mtipArray={
            "这是饺子的提示",
            "这是荷叶的提示",
            "这道题当然是不是啊",
            "这道题当然是没有啊"
    };

    public static ArrayList<Game> getDefaultList(){
        ArrayList<Game> gameList = new ArrayList<Game>();
        for(int i = 0; i < questionArray.length; i++){
            Game info = new Game();
            info.q_id=qIdArray[i];
            info.question = questionArray[i];
            info.answer=answerArray[i];
            info.pic=mPicArray[i];
            info.tip=mtipArray[i];

            gameList.add(info);
        }
        return gameList;
    }
}
