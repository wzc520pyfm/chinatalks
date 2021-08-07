package com.baidu.duer.dcs.Fragment;


import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
/*********************************************************************************************
 * 页面:          试卷中心页面                                                              *
 * 进入方式:      点击测试页面的TestCenter按钮进入
 * 关联到的文件:  /Adapter/TestCenterFragmentAdapter   :  碎片适配器
 *               /bean/TestCenter                    :  自定义的Result数据类型
 *               以及一些工具类
 *               /layout/chinatalk_activity_test_center.xml
 * 页面主要逻辑: 1.模拟网络请求来获取数据,将数据展示在页面上
 * 已知Bug:     1.
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class ChinaTalkTestCenterFragment extends Fragment {

    private final static String TAG="TestCenterActivity";//测试标识
    private LinearLayout ll_content;//声明布局对象

    private ListView lv_test_center;//声明一个列表视图对象
    private TestCenterDBHelper mTestCenterHelper;//声明一个测试卷数据库的帮助器对象


    protected View mView;
    protected Context mContext;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chinatalk_activity_home);
        mContext=getActivity();//获取获得页面的上下文

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_activity_test_center,container,false);


        //从布局文件中获取列表视图
        lv_test_center=mView.findViewById(R.id.lv_test_center);
        return mView;//返回该碎片的视图对象
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
    public void onResume(){
        super.onResume();
        //获取试卷的帮助器对象
        mTestCenterHelper=TestCenterDBHelper.getInstance(mContext,1);
        //打开数据的写连接
        mTestCenterHelper.openWriteLink();
        //模拟网络请求
        downLoad();



        showTestCenter();
    }

    @Override
    public void onPause(){
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
        TestCenterAdapter adapter=new TestCenterAdapter(mContext,mTestArray);
        //为lv_test_center设置列表适配器
        lv_test_center.setAdapter(adapter);

    }

    private String tFirst="true";//是否首次打开
    public void downLoad(){
        //获取共享参数保存的是否首次打开参数
        tFirst= SharedUtil.getInstance(mContext).readShared("tfirst","true");
        if(tFirst.equals("true")){
            ArrayList<TestCenter> testList=TestCenter.getDefaultList();
            for(int i=0;i<testList.size();i++){
                TestCenter info=testList.get(i);
                //往试卷数据库插入一条记录
                long rowid=mTestCenterHelper.insert(info);
            }
        }
        //把是否首次打开写入共享参数
        SharedUtil.getInstance(mContext).writeShared("tfirst","false");
    }

}