package com.baidu.duer.dcs.Fragment;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;

import com.baidu.duer.dcs.Adapter.HomeExamFragmentAdapter;
import com.baidu.duer.dcs.Adapter.TestCenterAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.TestCenter;
import com.baidu.duer.dcs.chinatalk.AnnouncementActivity;
import com.baidu.duer.dcs.chinatalk.Dev_code;
import com.baidu.duer.dcs.chinatalk.EditInformationActivity;
import com.baidu.duer.dcs.database.TestCenterDBHelper;
import com.baidu.duer.dcs.util.Image;
import com.baidu.duer.dcs.util.SharedUtil;

import java.util.ArrayList;

/*********************************************************************************************
 * 页面:          My页面, 即 我的 页面                                                                    *
 * 进入方式:      点击底部导航栏的My按钮进入
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class ChinaTalkMyFragment extends Fragment implements View.OnClickListener {

    protected View mView;
    protected Context mContext;

    private ListView lv_exam;//声明一个列表视图对象
    private ScrollView scrollview;
    private TestCenterDBHelper mTestCenterHelper;//声明一个测试卷数据库的帮助器对象


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chinatalk_activity_home);
        mContext=getActivity();//获取获得页面的上下文



        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_activity_home,container,false);

        ImageButton user_info = mView.findViewById(R.id.imageButton7);
        user_info.setOnClickListener(this);
        Button ann_btn = mView.findViewById(R.id.button34);
        ann_btn.setOnClickListener(this);


        //获取滚动视图
        scrollview = mView.findViewById(R.id.scrollview);

        //从布局文件中获取列表视图
        lv_exam=mView.findViewById(R.id.lv_exam);

//        当ListView完全显示在ScrollView底部时,才激活ListView的滑动
        lv_exam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //scrollView.getScrollY()超出屏幕的高度
                //scrollView.getHeight()屏幕显示的高度
                //scrollView.getPaddingTop()，scrollView.getPaddingBottom()//上下Padding
                //scrollView.getChildAt(0).getHeight()//scrollView唯一子View的高度
                if(scrollview.getScrollY() + scrollview.getHeight() - scrollview.getPaddingTop() - scrollview.getPaddingBottom() == scrollview.getChildAt(0).getHeight()){
                    scrollview.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
//      当ListView滑动到顶部时, 继续滑动则切入ScrollView滑动
        lv_exam.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //判断ScrollView是否不滑动了，判断ListView是否已显示第一个Item了
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && view.getFirstVisiblePosition() == 0){
                    //允许即使在ListView滑动，ScrollView也可以滑动
                    scrollview.requestDisallowInterceptTouchEvent(false);
                    //scrollView滑动，滑动距离为ListView滑动距离
                    scrollview.smoothScrollBy(lv_exam.getScrollX(), lv_exam.getScrollY());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //empty
            }
        });



        return mView;//返回该碎片的视图对象
    }

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
        HomeExamFragmentAdapter adapter=new HomeExamFragmentAdapter(mContext,mTestArray);
        //lv_exam
        lv_exam.setAdapter(adapter);

    }

    private String eFirst="true";//是否首次打开
    public void downLoad(){
        //获取共享参数保存的是否首次打开参数
        eFirst= SharedUtil.getInstance(mContext).readShared("efirst","true");
        if(eFirst.equals("true")){
            ArrayList<TestCenter> testList=TestCenter.getDefaultList();
            for(int i=0;i<testList.size();i++){
                TestCenter info=testList.get(i);
                //往试卷数据库插入一条记录
                long rowid=mTestCenterHelper.insert(info);
            }
        }
        //把是否首次打开写入共享参数
        SharedUtil.getInstance(mContext).writeShared("efirst","false");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButton7:
                Intent intent1 =new Intent(this.getContext(), EditInformationActivity.class);
                startActivityForResult(intent1,0);
                break;
            case R.id.button34:
                Intent intent2 =new Intent(this.getContext(), AnnouncementActivity.class);
                startActivityForResult(intent2,0);
                break;
        }
    }
}