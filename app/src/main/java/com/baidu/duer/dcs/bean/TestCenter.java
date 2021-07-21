package com.baidu.duer.dcs.bean;

import com.baidu.duer.dcs.R;

import java.util.ArrayList;

public class TestCenter {
    public long rowid;//行号
    public long test_id;//试卷编号
    public String title;
    public String desc;
    public int finshed_num;
    public int score;
    public String test_time;

    public TestCenter(){
        rowid=0L;
        test_id=0L;
        title="";
        desc="";
        finshed_num=0;
        score=0;
        test_time="";
    }
    public static long[] testIdArray={
            0,
            1,
            2,
            3
    };
    public static String[] titleArray={
            "随机测试",
            "汉语四级真题测试卷(1)",
            "随机测试",
            "汉语四级真题测试卷(2)"
    };
    public static String[] descArray={
            "总题数: 4000题",
            "总题数: 30题",
            "总题数: 3000题",
            "总题数: 40题"
    };
    public static int[] finshedNumArray={
            79,
            -1,
            80,
            -1
    };
    public static int[] scoreArray={
            0,
            88,
            0,
            89
    };
    public static String[] testTimeArray={
            " ",
            "测试总时长: 120分钟",
            " ",
            "测试总时长: 110分钟"
    };

    public static ArrayList<TestCenter> getDefaultList(){
        ArrayList<TestCenter> testList = new ArrayList<TestCenter>();
        for(int i = 0; i < testIdArray.length; i++){
            TestCenter info = new TestCenter();
            info.test_id=testIdArray[i];
            info.title=titleArray[i];
            info.desc=descArray[i];
            info.finshed_num=finshedNumArray[i];
            info.score=scoreArray[i];
            info.test_time=testTimeArray[i];

            testList.add(info);
        }
        return testList;
    }
}
