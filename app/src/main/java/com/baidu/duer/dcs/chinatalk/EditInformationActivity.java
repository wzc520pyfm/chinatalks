package com.baidu.duer.dcs.chinatalk;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.bean.UserInfoBottom;
import com.baidu.duer.dcs.Adapter.UserInfoBottomAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class EditInformationActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener {

    private ArrayList<UserInfoBottom> infoBList;//声明列表信息队列


    private LinearLayout user_birth;//获取用户出生日期点击框
    private TextView user_birth_date;//获取用户出生日期显示栏
    private ImageButton date_btn;//获取出生日期提示按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinatalk_activity_edit_information);
        initSpinner();
        initList();

        date_btn=(ImageButton) findViewById(R.id.date_btn);
        user_birth_date=(TextView) findViewById(R.id.user_birth_date);
        user_birth=(LinearLayout) findViewById(R.id.user_birth);
        user_birth.setOnClickListener(this);
        date_btn.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.user_birth || v.getId()==R.id.date_btn){
            //获取日历的一个实例,里面包含了当前的年月日
            Calendar calendar=Calendar.getInstance();
            //构建一个日期对话框,该对话框已集成日期选择器
            //DatePickerDialog的第二个构造参数指定了日期监听器
            DatePickerDialog dialog=new DatePickerDialog(this,this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            //把日期对话框显示在界面上
            dialog.show();
        }else if(v.getId()==R.id.btn_back){
            Intent intent =new Intent();//创建一个新意图
            Bundle bundle=new Bundle();//创建一个新包裹
            bundle.putString("state","success");//往包裹里存入一个字符串
            intent.putExtras(bundle);//把快递包裹塞给意图
            setResult(Activity.RESULT_OK,intent);//携带意图返回前一个页面
            finish();//关闭当前页面
        }
    }

    //一旦点击日期对话框上的确定按钮,就会触发监听器的onDateSet方法
    public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth){
        //获取日期对话框设定的年月份
        String desc=String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth);
        user_birth_date.setText(desc);
    }

    //初始化列表
    private void initList(){
        //获取列表信息队列
        infoBList=UserInfoBottom.getInfoList();
        //验证4条数据是否都正常接收----结果显示成功
        for(int i=0;i<infoBList.size();i++){
            UserInfoBottom temp=infoBList.get(i);
            Log.i("EditInformation",temp.mdesc);
        }
        //构建一个列表适配器
        UserInfoBottomAdapter adapter=new UserInfoBottomAdapter(this,infoBList);

        //从布局文件获取ListView
        ListView ls=(ListView) findViewById(R.id.user_info_bottom);
        //设置列表的列表适配器
        ls.setAdapter(adapter);
        //动态计算ListView高度
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(ls);
    }

    //动态计算ListView高度,因为Scroll布局无法自行计算listView高度
    //会导致listview只显示一项的尴尬bug
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); if (listAdapter == null) { // pre-condition
            return;
        }
        int totalHeight = 0; for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView); // listItem.measure(0, 0);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    //初始化下拉框
    private void initSpinner(){
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter=new ArrayAdapter<String>(this,R.layout.chinatalk_select_sex,starArray);
        //设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.chinatalk_item_sex);
        //从布局文件中获取名叫sp_sex的下拉框
        Spinner sp= (Spinner) findViewById(R.id.sp_sex);
        //设置下拉框的数组适配器
        sp.setAdapter(starAdapter);
        //设置下拉框默认显示第一项
        sp.setSelection(0);
        //给下拉框设置选择监听器,一旦用户选中某一项,就出发监听器的onItemSelected方法
        sp.setOnItemSelectedListener(new MySelectedListener());
    }

    //定义下拉列表需要显示的文本数组
    private String[] starArray={"Female","Man"};
    //定义一个选择监听器,它实现了接口OnItemSelectedListener
    class MySelectedListener implements AdapterView.OnItemSelectedListener{
        //选择事件的处理方法,其中arg2代表选择项的序号
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
            Toast.makeText(EditInformationActivity.this,"您选择的是"+starArray[arg2],Toast.LENGTH_LONG).show();
        }

        //未选择时的处理方法,通常无需关注
        public void onNothingSelected(AdapterView<?> arg0){}
    }
}