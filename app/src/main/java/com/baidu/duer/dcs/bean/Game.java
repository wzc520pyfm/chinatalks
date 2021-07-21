package com.baidu.duer.dcs.bean;

import com.baidu.duer.dcs.R;

import java.util.ArrayList;

public class Game {
    public String img_src;//图片路径
    public String question;
    public String answer;
    public int pic;//图片资源编号

    public Game(){
        img_src="";
        question="";
        answer="";

    }

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

    public static ArrayList<Game> getDefaultList(){
        ArrayList<Game> gameList = new ArrayList<Game>();
        for(int i = 0; i < questionArray.length; i++){
            Game info = new Game();
            info.question = questionArray[i];
            info.answer=answerArray[i];
            info.pic=mPicArray[i];

            gameList.add(info);
        }
        return gameList;
    }
}
