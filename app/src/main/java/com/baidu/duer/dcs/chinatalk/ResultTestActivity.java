package com.baidu.duer.dcs.chinatalk;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.duer.dcs.Adapter.GameFragementAdapter;
import com.baidu.duer.dcs.Adapter.ResultFragementAdapter;
import com.baidu.duer.dcs.Fragment.ChinaTalkGameFragment;
import com.baidu.duer.dcs.Fragment.ChinaTalkResultFragment;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.MainActivity;
import com.baidu.duer.dcs.bean.Game;
import com.baidu.duer.dcs.bean.Result;
import com.baidu.duer.dcs.database.GameTestDBhelper;
import com.baidu.duer.dcs.database.ResultTestDBhelper;
import com.baidu.duer.dcs.task.GetGameTask;
import com.baidu.duer.dcs.task.GetResultTask;

import java.util.ArrayList;
/*********************************************************************************************
 * 页面:          真题测试页面                                                              *
 * 进入方式:      支持在FunTests页面通过语音唤醒, 亦可点击测试页面的ResultTest按钮进入
 * 关联到的文件:  /Adapter/ResultFragmentAdapter   :  碎片适配器
 *               /bean/Result                    :  自定义的Result数据类型
 *               /database/ResultTestDBhelper    :  存储Result的数据库帮助文件
 *               /Fragment/ChinaTalkResultFragment :  碎片
 *               /task/GetResultTask               :  网络请求线程(请求任务)
 *               以及一些工具类
 *               /layout/chinatalk_activity_result.xml
 * 碎片:        通过代码动态添加, 主要利用ViewPager
 * 页面主要逻辑: 1.在本地数据库中查询Result题目数据, 如果查询到结果, 则开始构建碎片, 如果查询不到结
 *               果,则表明是头一次打开页面, 此时执行GetResultTask线程进行网络请求Result题目数据,
 *               当线程结束时, 先将查询到的数据存入本地Result数据库以便下次取用, 接着利用请求到的
 *               数据构建碎片.
 *              2.页面声明了广播接收器, 并在onStart中进行了注册, 用于与碎片进行通信.
 *              3.与碎片通信的主要逻辑: 接收到碎片发来的广播时,取出内含的数据, 如果收到的答题正
 *                确则分数+1,错误则不加分, 如果收到的信息表示碎片需要成绩值,则发送广播并携带成绩
 *                值.
 *              4.页面还声明了一个点击监听器, 用于监听是否点击了返回按钮, 监听到点击动作时前往页面
 *                MainActivity.class
 * 已知Bug:     1.每一次执行时, 都查询不到本地数据库中数据, 所以每一次都在从网络请求Result题目的数据
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/

public class ResultTestActivity extends AppCompatActivity implements GetResultTask.OnResultListener, View.OnClickListener {

    private String TAG="ResultTestActivity";//测试标识
    public final static String EVENT="com.baidu.duer.dcs.chinatalk.ResultTestActivity";//标识广播事件原

    private String TestName;//试卷名
    private int Score=0;//成绩

    private ViewPager vp_content;//声明用于构建碎片的翻页视图对象

    private Button next_ques;//下一题按钮
    private Button front_ques;//上一题按钮

    private int mCount;//记录碎片数量

    private ResultTestDBhelper mResultHelper;//声明一个数据库帮助器对象
    private ProgressBar pb_async;//声明一个进度条对象
    private ProgressDialog mDialog;//声明一个进度对话框对象
    public int mShowStyle;//显示风格
    public int BAR_HORIZONTAL=1;//水平条
    public int DIALOG_CIRCLE=2;//圆圈对话框
    public int DIALOG_HORIZONTAL=3;//水平对话框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_result_test);

        next_ques=(Button)findViewById(R.id.button10);
        front_ques=(Button)findViewById(R.id.button8);
        next_ques.setOnClickListener(this);
        front_ques.setOnClickListener(this);
        findViewById(R.id.imageView8).setOnClickListener(this);
        TestName="真题测试卷(1)";

        //从布局文件中获取名叫pb_async的进度条
        pb_async=(ProgressBar) findViewById(R.id.pb_async);

        //获取公告数据库的帮助器对象
        mResultHelper= ResultTestDBhelper.getInstance(this,1);
        //打开公告数据库的写连接
        mResultHelper.openWriteLink();

        //查询game的本地数据库中所有记录
        ArrayList<Result> infoArray=new ArrayList<Result>();
        infoArray=mResultHelper.query("1=1");
        if(infoArray!=null&&infoArray.size()>0){//如果本地数据库已有缓存即查询结果不为空则直接构建碎片
            //利用查询到的数据构建碎片
            ArrayList<Result> resultList=infoArray;
            //构建一个题目的碎片翻页适配器
            ResultFragementAdapter adapter=new ResultFragementAdapter(
                    getSupportFragmentManager(),resultList
            );
            //从布局中获取名叫 vp_content 的翻页视图
            vp_content= (ViewPager) findViewById(R.id.vp_content);
            //给vp_content设置题目碎片适配器
            vp_content.setAdapter(adapter);
            //设置vp_content默认显示第一个页面
            vp_content.setCurrentItem(0);
            mCount=adapter.getCount();
            Log.d(TAG,"查询本地数据库碎片构建成功");
        }else{//本地数据库没有缓存,启动game信息请求线程
            startTask(DIALOG_HORIZONTAL,this.getString(R.string.url)+"/findAllwords");
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button10:
                //点击了下一题
                if(vp_content.getCurrentItem()+1>=mCount){
                    Toast.makeText(this,"已经是最后一题了!",Toast.LENGTH_SHORT).show();
                }
                vp_content.setCurrentItem(vp_content.getCurrentItem()+1);
                break;
            case R.id.button8:
                //点击了上一题
                if(vp_content.getCurrentItem()-1<=0){
                    Toast.makeText(this,"这是第一题了!",Toast.LENGTH_SHORT).show();
                }
                vp_content.setCurrentItem(vp_content.getCurrentItem()-1);
                break;
            case R.id.imageView8:
                //点击了返回
//                Intent intent =new Intent(this, MainActivity.class);
//                startActivityForResult(intent,0);
                finish();
        }
    }

    public void onStart(){
        super.onStart();
        //创建一个判断答题是否正确的广播接收器
        isAnswer=new ResultTestActivity.IsAnswer();
        //创建一个意图过滤器,只处理指定事件来源的广播
        IntentFilter filter=new IntentFilter(ChinaTalkResultFragment.EVENT);
        //注册广播接收器,注册之后才能正常接收广播
        LocalBroadcastManager.getInstance(this).registerReceiver(isAnswer,filter);
    }


    //在页面暂停时调用
    protected void onPause(){
        super.onPause();
        //关闭公告数据库的数据库连接
        mResultHelper.closeLink();

    }
    protected void onStop(){
        super.onStop();
        //注销广播接收器,注销之后则不再接收广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(isAnswer);
    }

    //启动game信息加载线程---需要在onCreate中启动(或其他地方启动)
    //启动前先进性判断,先查询本地数据库,若查询到则直接构建碎片,没查询到再启动线程
    private void startTask(int style,String url){
        mShowStyle=style;
        //创建game信息加载线程
        GetResultTask asyncTask=new GetResultTask();
        //设置game加载监听器
        asyncTask.setOnResultListener(this);
        //把game加载线程加入到处理队列
        asyncTask.execute(url);
    }
    //关闭对话框
    private void closeDialog(){
        if(mDialog!=null&&mDialog.isShowing()){
            //对话框仍在显示
            mDialog.dismiss();//关闭对话框
        }
    }

//***************************************实现GetResultTask中接口中的四个函数BEGIN*****************************
    //在线程处理结束时触发
    public void onFindResultInfo(ArrayList<Result> resultInfo,String result){
        String desc=String.format("%s  已经加载完毕",result);
        //以下初始化部分应该在异步任务完成处执行
        //这里是直接将gameinfo传给了gameList用于构建页面
        //,但还少了一步---将gameInfo存入本地数据库以加快下次打开速度
        for(int i=0;i<resultInfo.size();i++){
            Result info=resultInfo.get(i);
            //往game数据库插入一条记录
            long rowid=mResultHelper.insert(info);
        }
        ArrayList<Result> resultList=resultInfo;
        //构建一个题目的碎片翻页适配器
        ResultFragementAdapter adapter=new ResultFragementAdapter(
                getSupportFragmentManager(),resultList
        );
        //从布局中获取名叫 vp_content 的翻页视图
        vp_content= (ViewPager) findViewById(R.id.vp_content);
        //给vp_content设置题目碎片适配器
        vp_content.setAdapter(adapter);
        //设置vp_content默认显示第一个页面
        vp_content.setCurrentItem(0);
        mCount=adapter.getCount();
        Log.d("ResultTestActivity","调用网络接口碎片构建成功");
        closeDialog();//关闭对话框
        Toast.makeText(this,desc,Toast.LENGTH_SHORT).show();
    }

    //在线程处理取消时触发
    public void onCancel(String result){
        String desc=String.format("%s  已经取消加载",result);
        closeDialog();//关闭对话框
        Toast.makeText(this,desc,Toast.LENGTH_SHORT).show();
    }
    //在线程处理过程中更新进度时触发
    public void onUpdate(String request,int progress,int sub_progress){
        String desc=String.format("%s 当前加载进度为%d%%",request,progress);

        if(mShowStyle==BAR_HORIZONTAL){//水平条
            pb_async.setProgress(progress);//设置水平进度条的当前进度
            pb_async.setSecondaryProgress(sub_progress);//设置水平进度的次要进度
        }else if(mShowStyle==DIALOG_HORIZONTAL){//水平对话框
            mDialog.setProgress(progress);//设置水平进度对话框的当前进度
            mDialog.setSecondaryProgress(sub_progress);//设置水平进度对话框的次要进度
        }
    }

    //在进度处理开始时触发
    public void onBegin(String request){
        Toast.makeText(this,request+" 开始加载",Toast.LENGTH_SHORT).show();
        if(mDialog==null||!mDialog.isShowing()){//进度框未弹出
            if(mShowStyle==DIALOG_CIRCLE){//圆圈对话框
                //弹出带有提示文字的圆圈进度对话框
                mDialog=ProgressDialog.show(this,"稍等",request+"页面加载中...");
            }else if(mShowStyle==DIALOG_HORIZONTAL){//水平对话框
                mDialog=new ProgressDialog(this);//创建一个进度对话框
                mDialog.setTitle("稍等");//设置进度对话框的标题文本
                mDialog.setMessage(request+"页面加载中...");//设置进度对话框的内容文本
                mDialog.setIcon(R.drawable.ic_home_search);//设置进度对话框的图标
                mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度对话框的样式
                mDialog.show();//显示进度对话框
            }
        }
    }
//******************************实现GetResultTask中接口END*****************************************
    //声明一个广播接收器
    private ResultTestActivity.IsAnswer isAnswer;

    //定义一个广播接收器,用于接收答题是否正确
    private class IsAnswer extends BroadcastReceiver {
        //一旦接收到答题是否正确的广播,马上触发接收器的onReceive方法
        public void onReceive(Context context, Intent intent){
            if (intent!=null){
                //从广播消息中取出最新结果
                boolean isTrue=intent.getBooleanExtra("isTrue",true);
                boolean isGetScore=intent.getBooleanExtra("isGetScore",true);
                //如果是true,则分值+1,否则不加分
                if(isTrue){
                    Score++;
                }
                //如果isGetScore是ture,则告知碎片成绩
                if(isGetScore){
                    //创建一个广播事件的意图---向碎片发送成绩
                    Intent score_intent=new Intent(ResultTestActivity.EVENT);
                    score_intent.putExtra("Score",Score);
                    score_intent.putExtra("TestName",TestName);
                    //通过本地的广播管理器来发送广播---将成绩发送出去
                    LocalBroadcastManager.getInstance(context).sendBroadcast(score_intent);
                }
            }
        }
    }
}