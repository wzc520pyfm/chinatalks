package com.baidu.duer.dcs.chinatalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.User;
import com.baidu.duer.dcs.task.LoginTask;
import com.baidu.duer.dcs.task.RegTask;
import com.baidu.duer.dcs.task.VarCodeTask;

import java.util.ArrayList;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/*********************************************************************************************
 * 页面:          注册页面, 即软件注册页面                                                                    *
 * 进入方式:      点击测试页面的Reg按钮进入
 * 需小心的点:   1.
 * 其他说明:     1.使用bmob后端云提供的手机验证码服务
 * ===========================================================================================*/
public class RegActivity extends AppCompatActivity implements VarCodeTask.OnVarCodeListener, RegTask.OnRegListener,View.OnClickListener {

    private String TAG="RegActivity";

    protected Context mContext;//声明一个上下文对象

    private ImageView go_back;//返回按钮
    private EditText user_name;//用户名
    private EditText user_phone;//用户注册手机号
    private EditText user_mail;//用户注册邮箱
    private EditText ver_code;//用户验证码
    private Button send_ver;//发送验证码的按钮
    private EditText user_psd;//用户密码
    private EditText con_user_psd;//确认密码
    private Button register;//注册

    private String varCode="";//验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_reg);

        mContext=this;//获取获得页面的上下文

        go_back=(ImageView)findViewById(R.id.imageView7);
        user_name=(EditText)findViewById(R.id.user_name);
        user_phone=(EditText)findViewById(R.id.editTextPhone);
        user_mail=(EditText)findViewById(R.id.editTextTextEmailAddress);
        ver_code=(EditText)findViewById(R.id.editTextNumber);
        send_ver=(Button)findViewById(R.id.button7);
        user_psd=(EditText)findViewById(R.id.editTextTextPassword);
        con_user_psd=(EditText)findViewById(R.id.editTextTextPassword2);
        register=(Button)findViewById(R.id.button9);

        go_back.setOnClickListener(this);
        send_ver.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imageView7://返回键
                Intent intent =new Intent();//创建一个新意图
                Bundle bundle=new Bundle();//创建一个新包裹
                bundle.putString("state","success");//往包裹里存入一个字符串
                intent.putExtras(bundle);//把快递包裹塞给意图
                setResult(Activity.RESULT_OK,intent);//携带意图返回前一个页面
                finish();//关闭当前页面
                break;
            case R.id.button7://发送验证码
                if(user_phone.getText().toString().equals(""))
                    Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
                //新开线程--请求验证码
//                startTask("请求验证码--模拟");

                /**
                 * TODO template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板。
                 */
                BmobSMS.requestSMSCode(user_phone.getText().toString(), "登录验证码", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {
                            Log.d("RegActivity","发送验证码成功，短信ID：" + smsId + "\n");
                        } else {
                            Log.d("RegActivity","发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                        }
                    }
                });


                break;
            case R.id.button9://注册
                if(user_phone.getText().toString().equals(""))
                    Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
                else if(user_name.getText().toString().equals(""))
                    Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
                else if(user_mail.getText().toString().equals(""))
                    Toast.makeText(this,"请输入邮箱",Toast.LENGTH_SHORT).show();
                else if(ver_code.getText().toString().equals(""))
                    Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
                else if(user_psd.getText().toString().equals(""))
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
                else if(con_user_psd.getText().toString().equals(""))
                    Toast.makeText(this,"请再次输入密码",Toast.LENGTH_SHORT).show();
//                else if(!ver_code.getText().toString().equals(varCode)){
//                    Toast.makeText(this,"验证码错误",Toast.LENGTH_SHORT).show();
//                    Log.d("RegActivity","验证码错误,验证码是 "+varCode);
//                }
                else{
//                    Log.d("RegActivity","验证码正确,验证码是"+varCode);
                    //验证验证码

                    BmobSMS.verifySmsCode(user_phone.getText().toString(), ver_code.getText().toString(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.d("RegActivity","验证码验证成功，您可以在此时进行绑定操作！\n");
                                //新开线程---注册
                                //将请求参数拼接到url上
//                                startRegTask(mContext.getString(R.string.url)+"/newUser"+"?"+"Username="+user_name.getText()+"&"+"Userpsd="+user_psd.getText());
                                startRegTask(mContext.getString(R.string.url)+"/newUser"+"?"+"Username="+user_name.getText()+"&"+"Userpsd="+user_psd.getText()+"&"+"Userphone="+user_phone.getText());
                            } else {
                                Log.d("RegActivity","验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                            }
                        }
                    });

                }
                break;
        }
    }
//************************************获取验证码BEGIN*********************
    //启动验证码加载线程---需要在onCreate中启动(或其他地方启动)
    private void startTask(String url){

        VarCodeTask asyncTask=new VarCodeTask();
        //设置验证码加载监听器
        asyncTask.setOnVarCodeListener(this);
        asyncTask.execute(url);

    }

    //在线程处理结束时触发
    public void onFindVarCodeInfo(String var_code, String result){

        String desc= "验证码已发送";
//        result就是验证码--123456
//        String desc=String.format("%s  验证码已发送",result);

        varCode=var_code;//保存收到的验证码
        Log.d("RegActivity","成功收到验证码"+var_code);

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
//******************************获取验证码END*********************
//*****************************注册BEGIN**********************
//启动注册加载线程---需要在onCreate中启动(或其他地方启动)
    private void startRegTask(String url){

        RegTask asyncTask=new RegTask();
        //设置注册加载监听器
        asyncTask.setOnRegListener(this);
        asyncTask.execute(url);
    }
    //在线程处理结束时触发
    public void onFindRegInfo(Integer reg_fag,String result){
        String desc=String.format("%s 注册成功",result);

        Integer reg=reg_fag;
        Log.d("RegActivity","调用注册网络接口构建成功");
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
    public void onRegCancel(String result){
    }
    //在线程处理过程中更新进度时触发
    public void onRegUpdate(String request,int progress,int sub_progress){
    }

    //在进度处理开始时触发
    public void onRegBegin(String request){
    }
}