package com.baidu.duer.dcs.chinatalk;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.duer.dcs.Adapter.MyAdapter;
import com.baidu.duer.dcs.Adapter.TestCenterAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.DcsSampleApplication;
import com.baidu.duer.dcs.bean.AnnInfo;
import com.baidu.duer.dcs.bean.TestCenter;
import com.baidu.duer.dcs.database.TestCenterDBHelper;
import com.baidu.duer.dcs.util.SharedUtil;

import java.util.ArrayList;

public class TestCenterActivity extends AppCompatActivity {

    private final static String TAG="TestCenterActivity";
    private LinearLayout ll_content;//声明布局对象

    private ListView lv_test_center;//声明一个列表视图对象
    private TestCenterDBHelper mTestCenterHelper;//声明一个测试卷数据库的帮助器对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_test_center);

//        ll_content=(LinearLayout) findViewById(R.id.ll_content);
        //从布局文件中获取列表视图
        lv_test_center=(ListView) findViewById(R.id.lv_test_center);


    }

    //声明当前的试卷对象
    private TestCenter mTestCenter;
    private View mCurrentView;//声明一个当前视图的对象

    //声明一个处理器对象
    private Handler mHandler=new Handler();
    //定义一个上下文菜单的弹出任务
//    private Runnable mPopupMenu = new Runnable() {
//        @Override
//        public void run() {
//            //取消lv_test_center的点击监听器
//            lv_test_center.setOnItemClickListener(null);
//            //取消lv_test_center的长按监听器
//            lv_test_center.setOnItemLongClickListener(null);
//            //注册列表项视图的上下文菜单
//            registerForContextMenu(mCurrentView);
//            //为该列表项视图弹出上下文菜单
//            openContextMenu(mCurrentView);
//            //注销列表项视图的上下文菜单
//            unregisterForContextMenu(mCurrentView);
//            //构建购物车商品列表的适配器对象
//            TestCenterAdapter adapter=new TestCenterAdapter(TestCenterActivity.this,mTestArray);
//            //给列表项设置列表适配器
//            lv_test_center.setAdapter(adapter);
//            //如有必要,可以在此重新设置列表项点击和长按监听器
//        }
//    };

    //声明一个试卷信息队列
    private ArrayList<TestCenter> mTestArray = new ArrayList<TestCenter>();


    @Override
    protected void onResume(){
        super.onResume();
        //获取试卷是巨亏的帮助器对象
        mTestCenterHelper=TestCenterDBHelper.getInstance(this,1);
        //打开数据的写连接
        mTestCenterHelper.openWriteLink();
        //模拟网络请求
        downLoad();



        showTestCenter();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //关闭数据库连接
        mTestCenterHelper.closeLink();
    }

    //展示试卷信息列表
    protected void showTestCenter(){
        //查询数据库中所有的试卷记录
        mTestArray=mTestCenterHelper.query("1=1");
        if(mTestArray==null||mTestArray.size()<=0){
            return;
        }
        for(int i=0;i<mTestArray.size();i++){
            TestCenter info=mTestArray.get(i);
            //根据试卷编号查询数据库中的记录
            mTestArray.set(i,info);
        }
        //构建试卷类别的适配器对象
        TestCenterAdapter adapter=new TestCenterAdapter(this,mTestArray);
        //为lv_test_center设置列表适配器
        lv_test_center.setAdapter(adapter);

    }

    private String tFirst="true";//是否首次打开
    public void downLoad(){
        //获取共享参数保存的是否首次打开参数
        tFirst= SharedUtil.getInstance(this).readShared("tfirst","true");
        if(tFirst.equals("true")){
            ArrayList<TestCenter> testList=TestCenter.getDefaultList();
            for(int i=0;i<testList.size();i++){
                TestCenter info=testList.get(i);
                //往试卷数据库插入一条记录
                long rowid=mTestCenterHelper.insert(info);
            }
        }
        //把是否首次打开写入共享参数
        SharedUtil.getInstance(this).writeShared("tfirst","false");
    }

}