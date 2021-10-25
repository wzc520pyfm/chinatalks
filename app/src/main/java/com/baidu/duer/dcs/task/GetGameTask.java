package com.baidu.duer.dcs.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.DcsSampleApplication;
import com.baidu.duer.dcs.bean.Game;
import com.baidu.duer.dcs.http.chinatalk.HttpRequestUtil;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpReqData;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpRespData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/****************************************************************************************
* 类:            网络请求线程, 用于请求Game题目数据
* 进入方式:       由GameActivity唤起
* 页面主要逻辑:    1.线程中发起Http请求,收到数据后进行json解析,最后返回网络应答数据.
*                 2.完成了线程状态的监听函数,以及一个请求信息的监听接口,留在主Activity中实现.
*
*
*
*
*
*
*
*
*
*
*========================================================================================= */
public class GetGameTask extends AsyncTask<String,Integer, ArrayList<Game>> {
    private final static String TAG="GetGameTask";//测试标识
    //请求地址--留作备用
    private String mGameUrl = "";
    //趣味测试
    private String game="趣味测试";

    public GetGameTask(){
        super();
    }

    //线程正在后台处理-----params对应异步任务参数的第一个参数即传入参数,可以是自定义的类型---这里是请求url
    protected ArrayList<Game> doInBackground(String... params){

        //由params直接传入请求地址
        String url=params[0];
        //创建一个HTTP请求对象
        HttpReqData req_data=new HttpReqData(url);
        //发送HTTP请求信息,并获得HTTP应答对象
        HttpRespData resp_data= HttpRequestUtil.postData(req_data);
        Log.d(TAG,"return json= "+resp_data.content);
        ArrayList<Game> gameInfo=new ArrayList<Game>();//创建存储响应数据的Game数组
        //下面从json串中解析字段
        if(resp_data.err_msg.length()<=0){
            try{
                //解析
                JSONObject F_obj=new JSONObject(resp_data.content);
                JSONObject obj=F_obj.getJSONObject("obj");
                JSONArray listArray=obj.getJSONArray("list");
                //读取内容---应利用for循环
                for(int i=0;i<listArray.length();i++){
                    JSONObject list_item=listArray.getJSONObject(i);
                    Game info=new Game();
                    info.q_id=list_item.getInt("q_id");
                    info.img_src=list_item.getString("img_src");
//                    try{//根据图片名获取图片id---利用反射机制
//                        info.pic= (Integer) R.drawable.class.getField(list_item.getString("img_src")).get(new R.drawable());
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }//pic被废弃,转而直接存储图片名img_src,在碎片中使用图片时根据图片名向网络请求
                    info.question=list_item.getString("question");
                    info.answer=list_item.getString("answer");
                    info.tip=list_item.getString("tip");
                    gameInfo.add(info);
                    Log.d(TAG,"q_id="+info.q_id+",img_src="+info.img_src+",pic="+info.pic+",question="+info.question+"answer="+info.answer+",tip="+info.tip);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return gameInfo;//返回Http应答内容
    }

    //准备启动线程
    protected void onPreExecute(){
        //触发监听器的开始事件
        mListener.onBegin(game);
    }
    //线程在通报处理进展
    protected void onProgressUpdate(Integer... values){
        //触发监听器的进度更新事件
        mListener.onUpdate(game,values[0],0);
    }

    //线程已经完成处理
    protected void onPostExecute(ArrayList<Game> gameInfo){
        //HTTP调用完毕,触发监听器的得到信息事件
        mListener.onFindGameInfo(gameInfo,game);
    }
    //线程已经取消
    protected void onCancelled(String result){
        //触发监听器的取消事件
        mListener.onCancel(result);
    }

    //声明一个请求信息的监听器对象
    private OnGameListener mListener;

    //设置查询详细地址的监听器
    public void setOnGameListener(OnGameListener listener){
        mListener=listener;
    }

    //定义一个请求信息的监听器接口
    public interface OnGameListener{//此接口在主activity中实现
        //在线程结束时触发
        void onFindGameInfo(ArrayList<Game> gameInfo,String result);
        //在线程处理取消时触发
        void onCancel(String result);
        //在线程处理过程中更新进度时触发
        void onUpdate(String request,int progress,int sub_progress);
        //在线程处理开始时触发
        void onBegin(String request);
    }
}
