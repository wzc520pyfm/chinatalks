package com.baidu.duer.dcs.chinatalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.task.RePsdTask;
import com.baidu.duer.dcs.task.RegTask;
import com.baidu.duer.dcs.task.VarCodeTask;
import com.baidu.duer.dcs.util.SharedUtil;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
/*********************************************************************************************
 * 页面:          更改密码页面                                                                *
 * 进入方式:      点击登录页面的改密按钮进入
 * 需小心的点:   1.
 * 其他说明:     1.使用bmob后端云提供的手机验证码服务
 * ===========================================================================================*/
public class ReChangePsdActivity extends AppCompatActivity implements RePsdTask.OnRePsdListener, View.OnClickListener {

    private String TAG="RePsdActivity";

    protected Context mContext;//声明一个上下文对象

    private ImageView go_back;//返回键
    private EditText username;//用户名输入框
    private EditText userphone;//手机号输入框
    private EditText var_code;//验证码输入框
    private Button var_btn;//发送验证码按钮
    private EditText user_psd;//密码输入框
    private EditText con_psd;//确认输入框
    private Button confirm_btn;//提交按钮

    private String varCode="";//验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_rechange_psd);

        mContext=this;//获取获得页面的上下文

        go_back=(ImageView)findViewById(R.id.imageView7);
        username=(EditText)findViewById(R.id.editTextUsername);
        userphone=(EditText)findViewById(R.id.editTextPhone);
        var_code=(EditText)findViewById(R.id.editTextNumber);
        var_btn=(Button)findViewById(R.id.button7);
        user_psd=(EditText)findViewById(R.id.editTextTextPassword);
        con_psd=(EditText)findViewById(R.id.editTextTextPassword2);
        confirm_btn=(Button)findViewById(R.id.button9);


        go_back.setOnClickListener(this);
        var_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imageView7:
                Intent intent =new Intent();//创建一个新意图
                Bundle bundle=new Bundle();//创建一个新包裹
                bundle.putString("state","success");//往包裹里存入一个字符串
                intent.putExtras(bundle);//把快递包裹塞给意图
                setResult(Activity.RESULT_OK,intent);//携带意图返回前一个页面
                finish();//关闭当前页面
                break;
            case R.id.button7:
                if(userphone.getText().toString().equals(""))
                    Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
                //新开线程--请求验证码
                /**
                 * TODO template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板。
                 */
                BmobSMS.requestSMSCode(userphone.getText().toString(), "登录验证码", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {
                            Log.d("RePsdActivity","发送验证码成功，短信ID：" + smsId + "\n");
                        } else {
                            Log.d("RePsdActivity","发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                        }
                    }
                });
                break;
            case R.id.button9:
                if(userphone.getText().toString().equals(""))
                    Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
                else if(username.getText().toString().equals(""))
                    Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
                else if(var_code.getText().toString().equals(""))
                    Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
                else if(user_psd.getText().toString().equals(""))
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
                else if(con_psd.getText().toString().equals(""))
                    Toast.makeText(this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                else{
                    //验证验证码
                    BmobSMS.verifySmsCode(userphone.getText().toString(), var_code.getText().toString(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.d("RePsdActivity","验证码验证成功，您可以在此时进行绑定操作！\n");
                                //新开线程---改密---------应该将用户名和手机号一起发送到服务端,检查该手机号下的用户名是否是传来的用户名,一致则改密成功,否则不可改密
                                startRegTask(mContext.getString(R.string.url)+"/updatePsd"+"?"+"Userphone="+userphone.getText()+"&"+"Userpsd="+user_psd.getText());
                            } else {
                                Log.d("RegActivity","验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                            }
                        }
                    });
                }
                break;
        }
    }

    //*****************************改密BEGIN**********************
//启动注册加载线程---需要在onCreate中启动(或其他地方启动)
    private void startRegTask(String url){

        RePsdTask asyncTask=new RePsdTask();
        //设置注册加载监听器
        asyncTask.setOnRePsdListener(this);
        asyncTask.execute(url);
    }
    //在线程处理结束时触发
    public void onFindRePsdInfo(Integer reg_fag,String result){
        String desc=String.format("%s 改密成功",result);

        Integer reg=reg_fag;
        Log.d("ReChangePsdActivity","调用改密网络接口构建成功");
        if(reg==0){

            //在此处跳转到首页
            Intent intentHome =new Intent(this, LoginActivity.class);
            startActivityForResult(intentHome,0);
        }else{
            Log.d(TAG,"返回的信息码是: "+ reg_fag.toString());
        }

        Toast.makeText(this,desc,Toast.LENGTH_SHORT).show();
    }

    //在线程处理取消时触发
    public void onRePsdCancel(String result){
    }
    //在线程处理过程中更新进度时触发
    public void onRePsdUpdate(String request,int progress,int sub_progress){
    }

    //在进度处理开始时触发
    public void onRePsdBegin(String request){
    }
}
