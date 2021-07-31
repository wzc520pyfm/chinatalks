package com.baidu.duer.dcs.task;

import android.os.AsyncTask;
import android.util.Log;


import com.baidu.duer.dcs.bean.Result;
import com.baidu.duer.dcs.http.chinatalk.HttpRequestUtil;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpReqData;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpRespData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/****************************************************************************************************
 * 类:            网络请求线程, 用于请求Result题目数据
 * 进入方式:       由ResultActivity唤起
 * 页面主要逻辑:    1.线程中发起Http请求,收到数据后进行json解析,最后返回网络应答数据.
 *                 2.完成了线程状态的监听函数,以及一个请求信息的监听接口,留在主Activity中实现.
 *
 * ==================================================================================================*/
public class GetResultTask extends AsyncTask<String,Integer, ArrayList<Result>> {
    private final static String TAG="GetResultTask";
    //请求地址--留作备用
    private String mResultUrl = "";
    //真题测试
    private String result="真题测试";

    public GetResultTask(){
        super();
    }

    //线程正在后台处理
    protected ArrayList<Result> doInBackground(String... params){

        //由params直接传入请求地址
        String url=params[0];
        //创建一个HTTP请求对象
        HttpReqData req_data=new HttpReqData(url);
        //发送HTTP请求信息,并获得HTTP应答对象
        HttpRespData resp_data= HttpRequestUtil.postData(req_data);
        Log.d(TAG,"return json= "+resp_data.content);
        ArrayList<Result> resultInfo=new ArrayList<Result>();
        //下面从json串中解析字段
        if(resp_data.err_msg.length()<=0){
            try{
                //解析
                JSONObject F_obj=new JSONObject(resp_data.content);
                JSONObject obj=F_obj.getJSONObject("obj");
//                JSONObject result=obj.getJSONObject("result");
                JSONArray listArray=obj.getJSONArray("list");
                //读取内容---应利用for循环
                for(int i=0;i<listArray.length();i++){
                    JSONObject list_item=listArray.getJSONObject(i);
                    Result info=new Result();
                    info.wno=list_item.getInt("Wno");
                    info.word=list_item.getString("Word");
                    info.wgra=list_item.getString("Wgra");
                    info.wmeans=list_item.getString("Wmeans");
                    info.wexplain=list_item.getString("Wexplain");
                    info.wexample=list_item.getString("Wexample");
                    info.wpinyin=list_item.getString("Wpinyin");
                    info.item1=list_item.getString("item1");
                    info.item2=list_item.getString("item2");
                    info.item3=list_item.getString("item3");
                    info.item4=list_item.getString("item4");

                    resultInfo.add(info);
                    Log.d(TAG,"wno="+info.wno+",word="+info.word+",wgra="+info.wgra+",wmeans="+info.wmeans+"wexplain="+info.wexplain+",wexample="+info.wexample+",wpinyin="+info.wpinyin+",item1="+info.item1+",item2="+info.item2+",item3="+info.item3+",item4="+info.item4);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return resultInfo;//返回Http应答内容
    }

    //准备启动线程
    protected void onPreExecute(){
        //触发监听器的开始事件
        mListener.onBegin(result);
    }
    //线程在通报处理进展
    protected void onProgressUpdate(Integer... values){
        //触发监听器的进度更新事件
        mListener.onUpdate(result,values[0],0);
    }

    //线程已经完成处理
    protected void onPostExecute(ArrayList<Result> resultInfo){
        //HTTP调用完毕,触发监听器的得到信息事件
        mListener.onFindResultInfo(resultInfo,result);
    }
    //线程已经取消
    protected void onCancelled(String result){
        //触发监听器的取消事件
        mListener.onCancel(result);
    }

    //声明一个请求信息的监听器对象
    private OnResultListener mListener;

    //设置查询详细地址的监听器
    public void setOnResultListener(OnResultListener listener){
        mListener=listener;
    }

    //定义一个请求信息的监听器接口
    public interface OnResultListener{//此接口在主activity中实现
        //在线程结束时触发
        void onFindResultInfo(ArrayList<Result> resultInfo,String result);
        //在线程处理取消时触发
        void onCancel(String result);
        //在线程处理过程中更新进度时触发
        void onUpdate(String request,int progress,int sub_progress);
        //在线程处理开始时触发
        void onBegin(String request);
    }
}
