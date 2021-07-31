package com.baidu.duer.dcs.chinatalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.DcsSampleApplication;
import com.baidu.duer.dcs.androidapp.DcsSampleMainActivity;
import com.iflytek.mscv5plusdemo.MainActivity;
/*********************************************************************************************
 * 页面:          chinatalk测试页面                                                              *
 * 进入方式:      单击FunTests页面的语音按钮
 * 关联到的文件:  软件用到的所有页面
 * 页面主要逻辑: 1.通过点击事件进入不同的测试页面
 * 已知Bug:     1.
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class Dev_code extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_dev_code);
        //从布局文件中获取名叫btn_back的图形按钮
        ImageButton btn_back= (ImageButton) findViewById(R.id.back);
        //为btn_back绑定点击监听器
        btn_back.setOnClickListener(new Dev_code.ButtonOnClickListener());

        Button ann=(Button) findViewById(R.id.button18);
        ann.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button edit_info=(Button) findViewById(R.id.button19);
        edit_info.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button game=(Button)findViewById(R.id.button20);
        game.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button home= (Button)findViewById(R.id.button21);
        home.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button login=(Button) findViewById(R.id.button22);
        login.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button reg=(Button) findViewById(R.id.button23);
        reg.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button result=(Button) findViewById(R.id.button24);
        result.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button select=(Button)findViewById(R.id.button25);
        select.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button test_center=(Button) findViewById(R.id.button26);
        test_center.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button test_score=(Button) findViewById(R.id.button27);
        test_score.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button user_info=(Button) findViewById(R.id.button28);
        user_info.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button wrong_ques=(Button) findViewById(R.id.button29);
        wrong_ques.setOnClickListener(new Dev_code.ButtonOnClickListener());
        Button goto_xun_fei=(Button) findViewById(R.id.button30);
        goto_xun_fei.setOnClickListener(new Dev_code.ButtonOnClickListener());

    }

    //定义一个点击监听器
    class ButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){//点击事件的处理方法
            switch (v.getId()){
                case R.id.back:
                    Intent intent =new Intent();//创建一个新意图
                    Bundle bundle=new Bundle();//创建一个新包裹
                    bundle.putString("state","success");//往包裹里存入一个字符串
                    intent.putExtras(bundle);//把快递包裹塞给意图
                    setResult(Activity.RESULT_OK,intent);//携带意图返回前一个页面
                    finish();//关闭当前页面
                    break;
                case R.id.button18:
                    Intent intent18 =new Intent(Dev_code.this, AnnouncementActivity.class);
                    startActivityForResult(intent18,0);
                    break;
                case R.id.button19:
                    Intent intent19 =new Intent(Dev_code.this, EditInformationActivity.class);
                    startActivityForResult(intent19,0);
                    break;
                case R.id.button20:
                    Intent intent20 =new Intent(Dev_code.this, GameActivity.class);
                    startActivityForResult(intent20,0);
                    break;
                case R.id.button21:
                    Intent intent21 =new Intent(Dev_code.this, HomeActivity.class);
                    startActivityForResult(intent21,0);
                    break;
                case R.id.button22:
                    Intent intent22 =new Intent(Dev_code.this, LoginActivity.class);
                    startActivityForResult(intent22,0);
                    break;
                case R.id.button23:
                    Intent intent23 =new Intent(Dev_code.this, RegActivity.class);
                    startActivityForResult(intent23,0);
                    break;
                case R.id.button24:
                    Intent intent24 =new Intent(Dev_code.this, ResultTestActivity.class);
                    startActivityForResult(intent24,0);
                    break;
                case R.id.button25:
                    Intent intent25 =new Intent(Dev_code.this, SelectTestActivity.class);
                    startActivityForResult(intent25,0);
                    break;
                case R.id.button26:
                    Intent intent26 =new Intent(Dev_code.this, TestCenterActivity.class);
                    startActivityForResult(intent26,0);
                    break;
                case R.id.button27:
                    Intent intent27 =new Intent(Dev_code.this, TestScoreActivity.class);
                    startActivityForResult(intent27,0);
                    break;
                case R.id.button28:
                    Intent intent28 =new Intent(Dev_code.this, UserInformationActivity.class);
                    startActivityForResult(intent28,0);
                    break;
                case R.id.button29:
                    Intent intent29 =new Intent(Dev_code.this, WrongQuesBookActivity.class);
                    startActivityForResult(intent29,0);
                    break;
                case R.id.button30:
                    //一定要确保MainActivity是讯飞包里的,而不是百度包里的(md,蛋疼,这两包里有同名文件,c)
                    Intent intent30 =new Intent(Dev_code.this, MainActivity.class);
                    startActivityForResult(intent30,0);
                    break;
            }
        }
    }
}
