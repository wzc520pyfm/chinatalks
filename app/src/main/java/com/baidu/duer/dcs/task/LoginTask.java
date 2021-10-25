package com.baidu.duer.dcs.task;

import android.os.AsyncTask;
import android.util.Log;

import com.baidu.duer.dcs.bean.Game;
import com.baidu.duer.dcs.bean.User;
import com.baidu.duer.dcs.http.chinatalk.HttpRequestUtil;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpReqData;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpRespData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginTask extends AsyncTask<String,Integer, ArrayList<User>> {
    private final static String TAG="LoginTask";//测试标识
    //登录
    private String login="登录";

    public LoginTask(){
        super();
    }

    //线程正在后台处理-----params对应异步任务参数的第一个参数即传入参数,可以是自定义的类型---这里是请求url
    protected ArrayList<User> doInBackground(String... params){

        //由params直接传入请求地址
        String url=params[0];
        //创建一个HTTP请求对象
        HttpReqData req_data=new HttpReqData(url);
        //发送HTTP请求信息,并获得HTTP应答对象
//        req_data.params.append("\"{\"Username\":\"mimimi\",\"Userpsd\":\"166\"}\"");
//        req_data.params.append(params[0]);
        HttpRespData resp_data = HttpRequestUtil.postUrl(req_data);

        Log.d(TAG,"这是收到的数据:  "+resp_data.content);

        ArrayList<User> userInfo=new ArrayList<User>();
        //下面从json串中解析字段
        if(resp_data.err_msg.length()<=0){
            Log.d(TAG,"通过err_msg检查");
            try{
                //解析
                JSONObject F_obj=new JSONObject(resp_data.content);
                JSONObject obj=F_obj.getJSONObject("obj");
                JSONArray listArray=obj.getJSONArray("list");
                if(listArray.length()<1){
                    Log.d(TAG,"密码错误");
                    User info=new User();
                    info.Uno=-1;
                    userInfo.add(info);
                }
                for(int i=0;i<listArray.length();i++){
                    Log.d(TAG,"获取数据");
                    JSONObject list_item=listArray.getJSONObject(i);
                    User info=new User();
                    info.Uno=list_item.getInt("Uno");
                    info.Username=list_item.getString("Username");
                    info.Userpsd=list_item.getString("Userpsd");
                    userInfo.add(info);
                    Log.d(TAG,info.Username);
                }

            }catch (JSONException e){
                Log.d(TAG,"抛出异常");
                e.printStackTrace();
            }
        }

        return userInfo;//返回Http应答内容
    }

    //准备启动线程
    protected void onPreExecute(){
        //触发监听器的开始事件
        mListener.onBegin(login);
    }
    //线程在通报处理进展
    protected void onProgressUpdate(Integer... values){
        //触发监听器的进度更新事件
        mListener.onUpdate(login,values[0],0);
    }

    //线程已经完成处理
    protected void onPostExecute(ArrayList<User> userInfo){
        //HTTP调用完毕,触发监听器的得到信息事件
        mListener.onFindLoginInfo(userInfo,login);
    }
    //线程已经取消
    protected void onCancelled(String result){
        //触发监听器的取消事件
        mListener.onCancel(result);
    }

    //声明一个请求信息的监听器对象
    private LoginTask.OnLoginListener mListener;

    //设置查询详细地址的监听器
    public void setOnLoginListener(LoginTask.OnLoginListener listener){
        mListener=listener;
    }

    //定义一个请求信息的监听器接口
    public interface OnLoginListener{//此接口在主activity中实现
        //在线程结束时触发
        void onFindLoginInfo(ArrayList<User> userInfo,String result);
        //在线程处理取消时触发
        void onCancel(String result);
        //在线程处理过程中更新进度时触发
        void onUpdate(String request,int progress,int sub_progress);
        //在线程处理开始时触发
        void onBegin(String request);
    }
}
