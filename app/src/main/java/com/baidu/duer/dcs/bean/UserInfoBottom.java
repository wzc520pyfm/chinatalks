package com.baidu.duer.dcs.bean;

import java.util.ArrayList;
/*********************************************************************************
 * 类:                bean类, UserInfoBottom数据类型
 * 用途:              自定义UserInfoBottom数据类型,用户信息中格式相同部分的存储
 * 逻辑:              除本身数据初始化外,还提供了一个获取默认值的函数,方便测试
 * 注意:
 *=============================================================================== */
public class UserInfoBottom {
    public String mtitle;
    public String mdesc;

    public UserInfoBottom(String mtitle,String mdesc){
        this.mdesc=mdesc;
        this.mtitle=mtitle;
    }

    private static String[] titleArray={"Age","School","Major","Phone"};
    private static String[] descArray={"please enter age","please enter school","please enter major","please enter phone"};

    public static ArrayList<UserInfoBottom> getInfoList(){
        ArrayList<UserInfoBottom> InfoList=new ArrayList<UserInfoBottom>();
        for(int i=0;i<4;i++){
            InfoList.add(new UserInfoBottom(titleArray[i],descArray[i]));
        }
        return InfoList;
    }
}
