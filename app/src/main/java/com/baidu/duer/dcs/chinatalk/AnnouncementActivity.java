package com.baidu.duer.dcs.chinatalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.DcsSampleApplication;
import com.baidu.duer.dcs.bean.AnnInfo;
import com.baidu.duer.dcs.database.AnnouncementDBHelper;
import com.baidu.duer.dcs.util.SharedUtil;

import java.util.ArrayList;
/*********************************************************************************************
 * 页面:          试卷中心页面                                                              *
 * 进入方式:      点击测试页面的Ann按钮进入
 * 关联到的文件:  /database/AnnouncementDBHelper   :  数据库帮助器
 *               /bean/AnnInfo                    :  自定义的AnnInfo数据类型
 *               以及一些工具类
 *               /layout/chinatalk_activity_announcement.xml
 * 页面主要逻辑: 1.模拟网络请求来获取数据,将数据展示在页面上
 *              2.页面监听了一个点击事件,用于返回首页
 * 已知Bug:     1.
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class AnnouncementActivity extends AppCompatActivity {

    private AnnouncementDBHelper mAnnHelper;//声明一个公告数据库的帮助器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_announcement);
        //从布局文件中获取名叫btn_back的图形按钮
        ImageButton btn_back= (ImageButton) findViewById(R.id.btn_back);
        //为btn_back绑定点击监听器
        btn_back.setOnClickListener(new BackOnClickListener());



    }

    //在页面恢复时调用
    protected void onResume(){
        super.onResume();
        //获取公告数据库的帮助器对象
        mAnnHelper=AnnouncementDBHelper.getInstance(this,1);
        //打开公告数据库的写连接
        mAnnHelper.openWriteLink();

        //模拟网络接口---获取公告信息
        downloadGoods();
        super.onResume();

    }

    //在页面暂停时调用
    protected void onPause(){
        super.onPause();
        //关闭公告数据库的数据库连接
        mAnnHelper.closeLink();

    }

    //暂未完成后端服务,这里进行模拟操作:
    //如果是首次打开则初始化数据,如果不是,则从数据库查询数据
    //在后端完成后,将初始化部分改为网络请求数据
    private String mFirst="true";//是否首次打开

    //模拟网络数据,初始化数据库找那个的商品信息
    private void downloadGoods(){
        //获取共享参数保存的是否首次打开参数
        mFirst= SharedUtil.getInstance(this).readShared("first","true");
        //获取当前App的私有存储路径
        String path= DcsSampleApplication.getInstance().getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS
        ).toString()+"/";
        //从布局文件获取响应的文本编辑组件
        TextView title=(TextView) findViewById(R.id.title);
        TextView release_time=(TextView) findViewById(R.id.release_time);
        TextView text=(TextView) findViewById(R.id.text);
        if(mFirst.equals("true")){//如果是首次打开
            ArrayList<AnnInfo> annsList=AnnInfo.getDefaultList();
            //通过上条语句获得了所有公告信息,但显示往往只显示最新的公告
            AnnInfo info=annsList.get(annsList.size()-1);
            //往公告库中插入一条记录
            long rowid=mAnnHelper.insert(info);
            //设置布局内公告内容

            title.setText(info.title);
            release_time.setText(info.release_time);
            text.setText(info.main_text);
        }else{//不是首次打开
            //查询公告数据库中所有公告记录
            ArrayList<AnnInfo> annArray=mAnnHelper.query("1=1");
            AnnInfo info=annArray.get(annArray.size()-1);
            title.setText(info.title);
            release_time.setText(info.release_time);
            text.setText(info.main_text);
        }

        //把是否首次打开写入共享参数
        SharedUtil.getInstance(this).writeShared("first","false");
    }

    //定义一个点击监听器
    class BackOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){//点击事件的处理方法
            Intent intent =new Intent();//创建一个新意图
            Bundle bundle=new Bundle();//创建一个新包裹
            bundle.putString("state","success");//往包裹里存入一个字符串
            intent.putExtras(bundle);//把快递包裹塞给意图
            setResult(Activity.RESULT_OK,intent);//携带意图返回前一个页面
            finish();//关闭当前页面
        }
    }
}