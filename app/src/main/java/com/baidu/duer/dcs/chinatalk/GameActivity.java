package com.baidu.duer.dcs.chinatalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.duer.dcs.Adapter.GameFragementAdapter;
import com.baidu.duer.dcs.Fragment.ChinaTalkGameFragment;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.MainActivity;
import com.baidu.duer.dcs.bean.Game;
import com.baidu.duer.dcs.bean.TestCenter;
import com.baidu.duer.dcs.database.AnnouncementDBHelper;
import com.baidu.duer.dcs.database.GameTestDBhelper;
import com.baidu.duer.dcs.task.GetGameTask;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.speech.util.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class GameActivity extends AppCompatActivity implements GetGameTask.OnGameListener, View.OnClickListener {//实现GetGameTask中定义的接口

    private String TAG="GameActivity";
    public final static String EVENT="com.baidu.duer.dcs.chinatalk.GameActivity";

    private int Score=0;//成绩
    boolean hasScore=false;
//_________________BEGIN_____________________
    // 语音听写对象
//    private SpeechRecognizer mIat;
//    // 语音听写UI
//    private RecognizerDialog mIatDialog;
//    // 听写结果内容
//    private EditText mResultText;
//    // 用HashMap存储听写结果
//    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
//
//    private Toast mToast;
//
//    private SharedPreferences mSharedPreferences;
//    private String mEngineType = "cloud";
//___________________END_________________________________
    private ViewPager vp_content;
    private GameTestDBhelper mGameHelper;//声明一个数据库帮助器对象
    private ProgressBar pb_async;//声明一个进度条对象
    private ProgressDialog mDialog;//声明一个进度对话框对象
    public int mShowStyle;//显示风格
    public int BAR_HORIZONTAL=1;//水平条
    public int DIALOG_CIRCLE=2;//圆圈对话框
    public int DIALOG_HORIZONTAL=3;//水平对话框


    private int mCount;


    @Override
//    @SuppressLint("ShowToast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chiantalk_activity_game);
//_____________________BEGIN_______________________________
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
//        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
//
//        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
//        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
//        mIatDialog = new RecognizerDialog(this,mInitListener);
//
//        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
//        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
//        mResultText = ((EditText)findViewById(R.id.title3));//内容文本显示区域
//        findViewById(R.id.imageButton2).setOnClickListener(this);//说话启动按钮
//        //初始化云端
//        mEngineType = SpeechConstant.TYPE_CLOUD;
//____________________END____________________________

        //从布局文件中获取名叫pb_async的进度条
        pb_async=(ProgressBar) findViewById(R.id.pb_async);

        //获取公告数据库的帮助器对象
        mGameHelper= GameTestDBhelper.getInstance(this,1);
        //打开公告数据库的写连接
        mGameHelper.openWriteLink();

        //查询game的本地数据库中所有记录
        ArrayList<Game> infoArray=new ArrayList<Game>();
        infoArray=mGameHelper.query("1=1");
        if(infoArray!=null&&infoArray.size()>0){//如果本地数据库已有缓存即查询结果不为空则直接构建碎片
            //利用查询到的数据构建碎片
            ArrayList<Game> gameList=infoArray;
            //构建一个题目的碎片翻页适配器
            GameFragementAdapter adapter=new GameFragementAdapter(
                    getSupportFragmentManager(),gameList
            );
            mCount=adapter.getCount();
            //从布局中获取名叫 vp_content 的翻页视图
            vp_content= (ViewPager) findViewById(R.id.vp_content);
            //给vp_content设置题目碎片适配器
            vp_content.setAdapter(adapter);
            //设置vp_content默认显示第一个页面
            vp_content.setCurrentItem(0);
            Log.d(TAG,"查询本地数据库碎片构建成功");
        }else{//本地数据库没有缓存,启动game信息请求线程
            startTask(DIALOG_HORIZONTAL,this.getString(R.string.url)+"/findGame");
        }

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    public void onStart(){
        super.onStart();
        //创建一个判断答题是否正确的广播接收器
        isAnswer=new IsAnswer();
        //创建一个意图过滤器,只处理指定事件来源的广播
        IntentFilter filter=new IntentFilter(ChinaTalkGameFragment.EVENT);
        //注册广播接收器,注册之后才能正常接收广播
        LocalBroadcastManager.getInstance(this).registerReceiver(isAnswer,filter);
    }

    //在页面暂停时调用
    protected void onPause(){
        super.onPause();
        //关闭公告数据库的数据库连接
        mGameHelper.closeLink();

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

    //在线程处理结束时触发
    public void onFindGameInfo(ArrayList<Game> gameInfo,String result){
        String desc=String.format("%s  已经加载完毕",result);
        //以下初始化部分应该在异步任务完成处执行
        //这里是直接将gameinfo传给了gameList用于构建页面
        //,但还少了一步---将gameInfo存入本地数据库以加快下次打开速度
        for(int i=0;i<gameInfo.size();i++){
            Game info=gameInfo.get(i);
            //往game数据库插入一条记录
            long rowid=mGameHelper.insert(info);
        }
        ArrayList<Game> gameList=gameInfo;
        //构建一个题目的碎片翻页适配器
        GameFragementAdapter adapter=new GameFragementAdapter(
                getSupportFragmentManager(),gameList
        );
        //从布局中获取名叫 vp_content 的翻页视图
        ViewPager vp_content= (ViewPager) findViewById(R.id.vp_content);
        //给vp_content设置题目碎片适配器
        vp_content.setAdapter(adapter);
        //设置vp_content默认显示第一个页面
        vp_content.setCurrentItem(0);
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

    //声明一个广播接收器
    private IsAnswer isAnswer;

    //定义一个广播接收器,用于接收答题是否正确
    private class IsAnswer extends BroadcastReceiver{
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
                    hasScore=true;
                    //创建一个广播事件的意图---向碎片发送成绩
                    Intent score_intent=new Intent(GameActivity.EVENT);
                    score_intent.putExtra("Score",Score);
                    score_intent.putExtra("hasScore",hasScore);
                    //通过本地的广播管理器来发送广播---将成绩发送出去
                    LocalBroadcastManager.getInstance(context).sendBroadcast(score_intent);
                }
            }
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button:
                Intent intent =new Intent(this, MainActivity.class);
                startActivityForResult(intent,0);
                break;

//                //该方法用来获取viewpager的当前页面的索引--此方法对动态生成的碎片不支持
//                int currentPage=vp_content.getCurrentItem();//设置当前页面
//                int nextPage=currentPage+1==mCount?0:currentPage+1;
//                //给viewpager设置当前的page  为多少
//                vp_content.setCurrentItem(nextPage);
//                if (nextPage +1==mCount) {
//                    Toast.makeText(this, "您已经滑倒了最后一页了", Toast.LENGTH_SHORT).show();
//                    return;
//                }
        }
    }

//    //______________________________BEGIN_____________________________
//    int ret = 0;// 函数调用返回值
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            // 开始听写
//            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
//            case R.id.imageButton2:
//                mResultText.setText(null);// 清空显示内容
//                mIatResults.clear();
//                // 设置参数
//                setParam();
//                boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show), true);
//                if (isShowDialog) {
//                    // 显示听写对话框
//                    mIatDialog.setListener(mRecognizerDialogListener);
//                    mIatDialog.show();
//                    showTip(getString(R.string.text_begin));
//                } else {
//                    // 不显示听写对话框
//                    ret = mIat.startListening(mRecognizerListener);
//                    if (ret != ErrorCode.SUCCESS) {
//                        showTip("听写失败,错误码：" + ret+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
//                    } else {
//                        showTip(getString(R.string.text_begin));
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//    /**
//     * 初始化监听器。
//     */
//    private InitListener mInitListener = new InitListener() {
//
//        @Override
//        public void onInit(int code) {
//            Log.d(TAG, "SpeechRecognizer init() code = " + code);
//            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
//            }
//        }
//    };
//    /**
//     * 听写监听器。
//     */
//    private RecognizerListener mRecognizerListener = new RecognizerListener() {
//
//        @Override
//        public void onBeginOfSpeech() {
//            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            showTip("开始说话");
//        }
//
//        @Override
//        public void onError(SpeechError error) {
//            // Tips：
//            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
//
//            showTip(error.getPlainDescription(true));
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
//        }
//
//        @Override
//        public void onResult(RecognizerResult results, boolean isLast) {
//
//            String text = JsonParser.parseIatResult(results.getResultString());
//            mResultText.append(text);
//            mResultText.setSelection(mResultText.length());
//
//
//            if(isLast) {
//                //TODO 最后的结果
//
//            }
//        }
//
//        @Override
//        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前正在说话，音量大小：" + volume);
//            Log.d(TAG, "返回音频数据："+data.length);
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//            // 若使用本地能力，会话id为null
//            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//                String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
//                Log.d(TAG, "session id =" + sid);
//            }
//        }
//    };
//
//    /**
//     * 听写UI监听器
//     */
//    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
//        public void onResult(RecognizerResult results, boolean isLast) {
//            Log.d(TAG, "recognizer result：" + results.getResultString());
//
//            String text = JsonParser.parseIatResult(results.getResultString());
//            mResultText.append(text);
//            mResultText.setSelection(mResultText.length());
//
//        }
//
//        /**
//         * 识别回调错误.
//         */
//        public void onError(SpeechError error) {
//            showTip(error.getPlainDescription(true));
//
//        }
//
//    };
//
//    private void showTip(final String str)
//    {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mToast.setText(str);
//                mToast.show();
//            }
//        });
//    }
//
//    /**
//     * 参数设置
//     * @return
//     */
//    public void setParam(){
//        // 清空参数
//        mIat.setParameter(SpeechConstant.PARAMS, null);
//        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
//        // 设置引擎
//        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
//        // 设置返回结果格式
//        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//        //mIat.setParameter(MscKeys.REQUEST_AUDIO_URL,"true");
//
//        //	this.mTranslateEnable = mSharedPreferences.getBoolean( this.getString(R.string.pref_key_translate), false );
//        if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
//            // 设置本地识别资源
//            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
//        }
//        // 在线听写支持多种小语种，若想了解请下载在线听写能力，参看其speechDemo
//        if (lag.equals("en_us")) {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
//            mIat.setParameter(SpeechConstant.ACCENT, null);
//
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT,lag);
//
//        }
//
//
//        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
//
//        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
//
//        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
//
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
//    }
//
//    private String getResourcePath(){
//        StringBuffer tempBuffer = new StringBuffer();
//        //识别通用资源
//        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
//        tempBuffer.append(";");
//        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
//        //识别8k资源-使用8k的时候请解开注释
//        return tempBuffer.toString();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if( null != mIat ){
//            // 退出时释放连接
//            mIat.cancel();
//            mIat.destroy();
//        }
//    }
////_________________________________END______________________________________
}


