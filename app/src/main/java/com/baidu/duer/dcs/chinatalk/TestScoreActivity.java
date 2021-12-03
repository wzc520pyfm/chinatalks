package com.baidu.duer.dcs.chinatalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.MainActivity;

import org.w3c.dom.Text;
/*********************************************************************************
 * 页面:                成绩页面, 显示答题成绩
 * 逻辑:                接收上一页面传来的包裹,构造页面
 *=============================================================================== */
public class TestScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_Score;//成绩
    private TextView tv_test_title;//试卷名
    private TextView tv_assess;//答题情况总结
    private Button back_home;//返回按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_test_score);
        tv_Score=(TextView) findViewById(R.id.textView31);
        tv_test_title=(TextView) findViewById(R.id.textView29);
        tv_assess=(TextView)findViewById(R.id.textView30);
        back_home=(Button)findViewById(R.id.button11);


        back_home.setOnClickListener(this);

        //接收上一个页面传来的包裹
        Intent intent = getIntent();//获取前一个页面传来的意图
        Bundle bundle=intent.getExtras();//卸下意图里的快递包裹
        int mScore=bundle.getInt("Score",0);//从包裹中取出整型数
        String mTestTitle=bundle.getString("test_title","");
        int quesNum=bundle.getInt("ques_num",0);//总题数
        tv_Score.setText(""+mScore);
        tv_test_title.setText(mTestTitle);
        tv_assess.setText(String.format("总题数: %s 错题数: %s",""+quesNum,""+(quesNum-mScore)));


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button11://返回首页
                Intent intent =new Intent(this, TabFragmentActivity.class);
                startActivityForResult(intent,0);
                break;
        }
    }
}