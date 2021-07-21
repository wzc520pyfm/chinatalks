package com.baidu.duer.dcs.bean;

import java.util.ArrayList;

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
