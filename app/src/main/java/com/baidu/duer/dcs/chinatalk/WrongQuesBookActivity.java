package com.baidu.duer.dcs.chinatalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.duer.dcs.Adapter.GameFragementAdapter;
import com.baidu.duer.dcs.Adapter.WrongFragmentAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.MainActivity;
import com.baidu.duer.dcs.bean.Game;
import com.baidu.duer.dcs.bean.Wrong;
import com.baidu.duer.dcs.database.GameTestDBhelper;
import com.baidu.duer.dcs.task.GetGameTask;
import com.baidu.duer.dcs.task.RePsdTask;
import com.baidu.duer.dcs.task.WrongQuesTask;
import com.baidu.duer.dcs.util.SharedUtil;

import java.util.ArrayList;
/*********************************************************************************************
 * 页面:          错题册页面                                                              *
 * 进入方式:      点击测试页面的WrongQues按钮进入
 * 关联到的文件:  /Adapter/WrongFragmentAdapter   :  碎片适配器
 *               /bean/Result                    :  自定义的Result数据类型
 *               /Fragment/ChinaTalkWrongBookFragment :  碎片
 *               以及一些工具类
 *               /layout/chinatalk_activity_wrong_ques_book.xml
 * 碎片:        通过代码动态添加, 主要利用ViewPager
 * 页面主要逻辑: 1.获取Wrong的默认值来构建碎片
 * 已知Bug:     1.
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class WrongQuesBookActivity extends AppCompatActivity implements View.OnClickListener,GetGameTask.OnGameListener, WrongQuesTask.OnWQuesListener {

    private ViewPager vp_content;//声明一个翻页视图对象
    private GameTestDBhelper mGameHelper;//声明一个数据库帮助器对象
    private ProgressBar pb_async;//声明一个进度条对象
    private ProgressDialog mDialog;//声明一个进度对话框对象
    public int mShowStyle;//显示风格
    public int BAR_HORIZONTAL=1;//水平条
    public int DIALOG_CIRCLE=2;//圆圈对话框
    public int DIALOG_HORIZONTAL=3;//水平对话框

    private ArrayList<Game> gameList;//碎片数据源
    private GameFragementAdapter my_adapter;//碎片适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chiantalk_activity_wrong_ques_book);

        //从布局中获取名叫 vp_content 的翻页视图
        vp_content= (ViewPager) findViewById(R.id.wrong_vp_content);
        //******************错题册--1.0
//        ArrayList<Wrong> wrongsList=Wrong.getDefaultList();//获得Wrong的默认值
//
//        //构建一个题目的碎片翻页适配器
//        WrongFragmentAdapter adapter = new WrongFragmentAdapter(
//                getSupportFragmentManager(),wrongsList
//        );
//        //从布局文件中获取名叫vp_content的翻页视图
//        ViewPager wrong_vp_content = (ViewPager) findViewById(R.id.wrong_vp_content);
//        //给vp_content设置题目的碎片适配器
//        wrong_vp_content.setAdapter(adapter);
//        //设置vp_content默认显示第一个页面
//        wrong_vp_content.setCurrentItem(0);
//******************************************************************************

        findViewById(R.id.button6).setOnClickListener(this);//为删除按钮绑定点击事件

        //从布局文件中获取名叫pb_async的进度条
        pb_async=(ProgressBar) findViewById(R.id.pb_async);
        //启动请求线程
        SharedUtil sharedUtil=SharedUtil.getInstance(this);
        startTask(DIALOG_HORIZONTAL,this.getString(R.string.url)+"/findUserCollects"+"?Uno="+sharedUtil.readIntShared("Uno",0));
    }

    //启动game信息加载线程---需要在onCreate中启动(或其他地方启动)
    //启动前先进性判断,先查询本地数据库,若查询到则直接构建碎片,没查询到再启动线程
    private void startTask(int style,String url){
        mShowStyle=style;
        //创建game信息加载线程
        GetGameTask asyncTask=new GetGameTask();
        //设置game加载监听器
        asyncTask.setOnGameListener(this);
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

    //********************GetGameTask实现接口中的四个监听函数BEGIN************************
    //在线程处理结束时触发
    public void onFindGameInfo(ArrayList<Game> gameInfo, String result){
        String desc=String.format("%s  已经加载完毕",result);
        //利用请求到的数据构建碎片---碎片数据源
        gameList=gameInfo;
        //构建一个题目的碎片翻页适配器---适配器
        GameFragementAdapter adapter=new GameFragementAdapter(
                getSupportFragmentManager(),gameList
        );

        //给vp_content设置题目碎片适配器
        vp_content.setAdapter(adapter);
        //设置vp_content默认显示第一个页面
        vp_content.setCurrentItem(0);
        my_adapter=adapter;
        Log.d("GameActivity","调用网络接口碎片构建成功");
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
//****************************************GetGameTask接口实现END***************************


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button6:
                //如果删除一个错题
                int position = vp_content.getCurrentItem();
                int q_id=gameList.get(position).q_id;
                gameList.remove(position);//删除一项数据源
                //执行删除的网络请求
                SharedUtil sharedUtil = SharedUtil.getInstance(this);
                startWQTask(this.getString(R.string.url)+"/cancleCollect?"+"Uno="+sharedUtil.readIntShared("Uno",0)+"&q_id="+q_id);

                break;
        }
    }

    //启动注册加载线程---需要在onCreate中启动(或其他地方启动)
    private void startWQTask(String url){

        WrongQuesTask asyncTask=new WrongQuesTask();
        //设置注册加载监听器
        asyncTask.setOnWQuesListener(this);
        asyncTask.execute(url);
    }
    //在线程处理结束时触发
    public void onFindWQuesInfo(Integer reg_fag,String result){
        String desc=String.format("%s 删题成功",result);

        Integer reg=reg_fag;
        Log.d("ReChangePsdActivity","调用改密网络接口构建成功");
        if(reg==0){

            //删题成功
            my_adapter.notifyDataSetChanged();//通知UI更新碎片
        }else{
            Log.d("WrongQuesBookActivity","返回的信息码是: "+ reg_fag.toString());
        }

        Toast.makeText(this,desc,Toast.LENGTH_SHORT).show();
    }

    //在线程处理取消时触发
    public void onWQuesCancel(String result){
    }
    //在线程处理过程中更新进度时触发
    public void onWQuesUpdate(String request,int progress,int sub_progress){
    }

    //在进度处理开始时触发
    public void onWQuesBegin(String request){
    }

}