package com.baidu.duer.dcs.chinatalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.duer.dcs.Adapter.GameFragementAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.Game;
import com.baidu.duer.dcs.bean.User;
import com.baidu.duer.dcs.http.chinatalk.HttpRequestUtil;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpReqData;
import com.baidu.duer.dcs.http.chinatalk.tool.HttpRespData;
import com.baidu.duer.dcs.task.GetGameTask;
import com.baidu.duer.dcs.task.LoginTask;
import com.baidu.duer.dcs.util.SharedUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*********************************************************************************************
 * 页面:          登录页面, 即软件登录页面                                                                    *
 * 进入方式:      点击测试页面的Login按钮进入
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class LoginActivity extends AppCompatActivity implements LoginTask.OnLoginListener,View.OnClickListener{

    private EditText user_tv;
    private EditText psd_tv;
    private Button sign_btn;
    private Button re_psd_btn;
    private Button reg_btn;
    private ImageButton see_psd;

    private String TAG="Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_login);

        user_tv=(EditText)findViewById(R.id.editTextTextPersonName);
        psd_tv=(EditText)findViewById(R.id.editTextTextPersonName2);
        sign_btn=(Button)findViewById(R.id.button5);
        re_psd_btn=(Button)findViewById(R.id.button36);
        reg_btn=(Button)findViewById(R.id.button4);
        see_psd=(ImageButton)findViewById(R.id.imageButton3);


        sign_btn.setOnClickListener(this);
        re_psd_btn.setOnClickListener(this);
        reg_btn.setOnClickListener(this);
        see_psd.setOnClickListener(this);

    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button5:
//                登录---所有的网络请求都应该分线程!!!!!
                if(user_tv.getText().toString().equals("")){
                    Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }else if(psd_tv.getText().toString().equals("")){
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else{
                    //执行网络请求--启动分线程
                    startTask(this.getString(R.string.url)+"/Login"+"?"+"Username="+user_tv.getText()+"&"+"Userpsd="+psd_tv.getText());
                }
                break;
            case R.id.button36:
//                更改密码
                Intent intentRePsd =new Intent(this, ReChangePsdActivity.class);
                startActivityForResult(intentRePsd,0);
                break;
            case R.id.button4:
//                注册
                Intent intentReg =new Intent(this, RegActivity.class);
                startActivityForResult(intentReg,0);
                break;
            case R.id.imageButton3:
//                显示密码
                if(psd_tv.getInputType() == 128){//如果当前是可见密码,则设为不可见
                    psd_tv.setInputType(129);
                }else{//反之
                    psd_tv.setInputType(128);
                }
                psd_tv.setSelection(psd_tv.getText().length());//设置光标到末尾
                break;
        }
    }
    //启动game信息加载线程---需要在onCreate中启动(或其他地方启动)
    //启动前先进性判断,先查询本地数据库,若查询到则直接构建碎片,没查询到再启动线程
    private void startTask(String url){

//        注释掉的部分在post才使用,现在仅使用路径参数
//        try {
            // 创建一个JSON对象
            //JSONObject obj = new JSONObject();
            // 添加一个名叫Username的参数
            //obj.put("Username", "mimimi");
            //obj.put("Userpsd", "166");
            //创建game信息加载线程
            LoginTask asyncTask=new LoginTask();
            //设置game加载监听器
            asyncTask.setOnLoginListener(this);
            //把game加载线程加入到处理队列
//            asyncTask.execute(obj.toString());
            asyncTask.execute(url);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
    //在线程处理结束时触发
    public void onFindLoginInfo(ArrayList<User> userInfo,String result){
        String desc=String.format("%s  已经加载完毕",result);

        ArrayList<User> userList=userInfo;
        Log.d("LoginActivity","调用网络接口碎片构建成功");
        if(userList.get(0).Uno!=-1){
            //将请求到的信息存储起来
            SharedUtil sharedUtil= SharedUtil.getInstance(this);
            sharedUtil.writeShared("Uno",userList.get(0).Uno);
            sharedUtil.writeShared("Username",userList.get(0).Username);
            sharedUtil.writeShared("Userpsd",userList.get(0).Userpsd);
            //在此处跳转到首页
            Intent intentHome =new Intent(this, TabFragmentActivity.class);
            startActivityForResult(intentHome,0);
        }else{
            Log.d(TAG,userList.get(0).Username);
        }

        Toast.makeText(this,desc,Toast.LENGTH_SHORT).show();
    }

    //在线程处理取消时触发
    public void onCancel(String result){
    }
    //在线程处理过程中更新进度时触发
    public void onUpdate(String request,int progress,int sub_progress){
    }

    //在进度处理开始时触发
    public void onBegin(String request){
    }
}