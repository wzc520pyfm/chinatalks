package com.baidu.duer.dcs.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.Adapter.GameFragementAdapter;
import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.chinatalk.GameActivity;
import com.baidu.duer.dcs.chinatalk.TestScoreActivity;
import com.baidu.duer.dcs.devicemodule.alerts.message.Alert;
import com.baidu.duer.dcs.util.CPResourceUtil;
import com.baidu.duer.dcs.util.Image;
import com.baidu.duer.dcs.util.StringUtils;
import com.baidu.duer.dcs.widget.TextProgressCircle;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.mscv5plusdemo.TtsDemo;
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.speech.setting.TtsSettings;
import com.iflytek.speech.util.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChinaTalkGameFragment extends Fragment implements View.OnClickListener {
    protected View mView;//声明一个视图对象
    protected Context mContext;//声明一个上下文对象

    private ImageButton speak_btn;
    private TextView main_title;

    private  int mCount;
    private int mPosition;//位置序号
    private String mImgSrc;//图片名
    private int mPic;//图片的资源编号
    private String mQuestion;//题目
    private String mAnswer;//答案
    private String tip;//提示
    private String TAG="GameActivity";

    private boolean isFirstAnswer=true;//是否第一次回答
    private boolean isGetScore=false;
    private int Score;

    //声明一个广播事件的标识串----广播BEGIN
    public final static String EVENT="com.baidu.duer.dcs.Fragment.ChinaTalkGameFragment";
    //声明一个判断答题是否正确的boolean
    boolean isTrue=false;//----广播END


    //_________________BEGIN_____________________
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 听写结果内容
    private EditText mResultText;//speak窗口的编辑框
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Toast mToast;

    private SharedPreferences mSharedPreferences;
    private String mEngineType = "cloud";
//___________________END_________________________________

//___________________语音合成BEGIN_________________________
// 语音合成对象
private SpeechSynthesizer mTts;

    // 默认云端发音人
    public static String voicerCloud="xiaoqi";
    // 默认本地发音人
    public static String voicerLocal="xiaoyan";

    public static String voicerXtts="xiaoyan";
    // 云端发音人列表
    private String[] cloudVoicersEntries;
    private String[] cloudVoicersValue ;

    // 本地发音人列表
    private String[] localVoicersEntries;
    private String[] localVoicersValue ;

    // 增强版发音人列表
    private String[] xttsVoicersEntries;
    private String[] xttsVoicersValue;

    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;

    // 云端/本地选择按钮
    private RadioGroup mRadioGroup;
    // 引擎类型
    private String mEngineType2 = SpeechConstant.TYPE_CLOUD;
//___________________语音合成END___________________________

    private EditText mResultText2;//文本输入时的文本编辑框----其内容应与speak同步
    private TextView tip_title;//答题提示标题
    private TextView tip_desc;//答题提示内容
    private LinearLayout speak_mwindow;//语音输入窗口



    String[] tip_color={"color_wrong","color_right"};//对应答错和答对的颜色

    private ImageView iv_pic;
    private TextProgressCircle tpc_progress; // 定义一个文本进度圈对象
    private String mImagePath; // 图片的本地路径
    private DownloadManager mDownloadManager; // 声明一个下载管理器对象
    private long mDownloadId = 0; // 当前任务的下载编号
    private static HashMap<Integer, String> mStatusMap = new HashMap<Integer, String>(); // 下载状态映射


    //获取该碎片的一个实例
    public static ChinaTalkGameFragment newInstance(int position, String img_src, String ques, String ans,String tip,int Count){//需补充tip等参数
        ChinaTalkGameFragment fragment = new ChinaTalkGameFragment();//创建该碎片的一个实例
        Bundle bundle = new Bundle();//创建一个新包裹
        bundle.putInt("position",position);//存入位置编号
        bundle.putString("img_src",img_src);
        //bundle.putInt("pic",pic_id);//存入图片资源id
        bundle.putString("question",ques);//存入题目
        bundle.putString("answer",ans);//存入答案
        bundle.putString("tip",tip);//存入题目提示
        bundle.putInt("count",Count);

        fragment.setArguments(bundle);//把包裹塞给碎片
        return fragment;//返回碎片实例


    }

    //创建碎片视图
    @Override
    @SuppressLint("ShowToast")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContext=getActivity();//获取获得页面的上下文

        //如果碎片携带有包裹,则打开包裹获取参数信息
        if(getArguments()!=null){
            mPosition = getArguments().getInt("position",0);
            mImgSrc=getArguments().getString("img_src");
            //mPic = getArguments().getInt("pic",0);
            mQuestion = getArguments().getString("question");
            mAnswer = getArguments().getString("answer");
            tip=getArguments().getString("tip");
            mCount=getArguments().getInt("count");
        }

        //根据布局文件chinatalk_fragment_game.xml生成视图对象
        mView=inflater.inflate(R.layout.chinatalk_fragment_game,container,false);

        mView.findViewById(R.id.imageView5).setOnClickListener(this);
        iv_pic = mView.findViewById(R.id.imageView4);
        TextView tv_ques = mView.findViewById(R.id.title);
        TextView tv_ques2=mView.findViewById(R.id.textView7);
        //iv_pic.setImageResource(mPic);//现在这里是直接设置图片资源, 改为: 在此处根据img_src发起图片请求,完成后再设置图片

        // 从布局文件中获取名叫tpc_progress的文本进度圈
        tpc_progress = mView.findViewById(R.id.tpc_progress);
        // 从系统服务中获取下载管理器
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        // 下面几行初始化下载状态映射
        mStatusMap.put(DownloadManager.STATUS_PENDING, "挂起");
        mStatusMap.put(DownloadManager.STATUS_RUNNING, "运行中");
        mStatusMap.put(DownloadManager.STATUS_PAUSED, "暂停");
        mStatusMap.put(DownloadManager.STATUS_SUCCESSFUL, "成功");
        mStatusMap.put(DownloadManager.STATUS_FAILED, "失败");
        // 清空图像视图
        iv_pic.setImageDrawable(null);
//************************判断指定文件是否已经在存储内,但有bug: for循环没有执行,每一次都会执行文件不存在,然后去网络请求******************************
        String path=mContext.getFilesDir().getAbsolutePath();
        boolean pdtemp=false;

        ArrayList<String> ss = getFileName(path, ".jpg");
        for (String s : ss) {
            Log.d(TAG, "result:" + s);
            if (s.equals(mImgSrc)) pdtemp=true;
        }

        File file = new File(mImgSrc);
//**************************************************************************
        if(!pdtemp){// 文件不存在

            // 设置文本进度圈的当前进度为0，最大进度为100
            tpc_progress.setProgress(0, 100);
            // 显示文本进度圈
            tpc_progress.setVisibility(View.VISIBLE);
            // 根据图片的下载地址构建一个Uri对象
            Uri uri = Uri.parse(mContext.getString(R.string.url)+"/static/image/"+mImgSrc+".jpg");
            // 创建一个下载请求对象，指定从哪个网络地址下载文件
            Request down = new Request(uri);
            // 设置允许下载的网络类型
            down.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
            // 设置不在通知栏显示
            down.setNotificationVisibility(Request.VISIBILITY_HIDDEN);
            // 设置不在系统下载页面显示。该方法其实不管用，因为国产手机不提供下载app
            //down.setVisibleInDownloadsUi(false);
            // 设置下载文件在本地的保存路径
            down.setDestinationInExternalFilesDir(
                    mContext, Environment.DIRECTORY_DCIM, mImgSrc + ".jpg");
            // 把下载请求对象加入到下载管理器的下载队列中
            mDownloadId = mDownloadManager.enqueue(down);
            // 延迟10毫秒后启动下载进度的刷新任务
            mHandler.postDelayed(mRefresh, 10);

        }else{//文件存在

            //Toast.makeText(mContext,"从sd卡读取",Toast.LENGTH_SHORT).show();
            String fileUri = path+mImgSrc+".jpg";
            mImagePath = Uri.parse(fileUri).getPath();

            // 隐藏文本进度圈
            tpc_progress.setVisibility(View.GONE);
            // 把指定路径的图片显示在图像视图上面
            iv_pic.setImageURI(Uri.parse(mImagePath));
        }








        tv_ques.setText(mQuestion);
        tv_ques2.setText(mQuestion);


        //_____________________BEGIN_______________________________
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(mContext,mInitListener);

        mSharedPreferences = mContext.getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);

        mResultText = (EditText)mView.findViewById(R.id.ch_frg_game_input_answer);//内容文本显示区域

        speak_btn=mView.findViewById(R.id.imageButton2);//说话启动按钮
        speak_btn.setOnClickListener(this);
        //初始化云端
        mEngineType = SpeechConstant.TYPE_CLOUD;

        //初始化控件
        mView.findViewById(R.id.imageButton).setOnClickListener(this);
        mView.findViewById(R.id.button3).setOnClickListener(this);
        tip_title=mView.findViewById(R.id.textView9);//答题提示标题
        tip_desc=mView.findViewById(R.id.textView10);//答题提示内容
        speak_mwindow=mView.findViewById(R.id.speak_mwindow);//语音输入窗口
        mResultText2=mView.findViewById(R.id.editText2);
        main_title=mView.findViewById(R.id.textView6);

        //文本自适应处理: 无论多少文字都在一行上显示,但是必须在xml中明确规定文本框高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            main_title.setAutoSizeTextTypeUniformWithConfiguration(4,24,2, TypedValue.COMPLEX_UNIT_SP);
        }

        //下面是两种获取颜色int资源的方式,均需要验证是否可用---第二种已验证可用--自行封装工具类
        //tip_desc.setTextColor(mView.getResources().getIdentifier(tip_color[0],"color",""));
        //tip_desc.setTextColor(mView.getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[0])));

//____________________END____________________________

        //如果你不细心,在上面获取文本显示区域的时候一定会到空指针问题,哈哈,菜!!!
        //你要学会我这样:
//        View view = View.inflate(mActivity, R.layout.fragment_listview, null);
//        ListView lv = (ListView) view.findViewById(R.id.lv_listView);

//____________________语音合成BEGIN__________________________________
// 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(getContext(), mTtsInitListener);

        // 云端发音人名称列表
        cloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        cloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);

        // 本地发音人名称列表
        localVoicersEntries = getResources().getStringArray(R.array.voicer_local_entries);
        localVoicersValue = getResources().getStringArray(R.array.voicer_local_values);

        // 增强版发音人名称列表
        xttsVoicersEntries = getResources().getStringArray(R.array.voicer_xtts_entries);
        xttsVoicersValue = getResources().getStringArray(R.array.voicer_xtts_values);

        mSharedPreferences = mContext.getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
//____________________语音合成END___________________________________

        return mView;//返回该碎片的视图对象
    }



    //______________________________BEGIN_____________________________
    int ret = 0;// 函数调用返回值
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.imageButton2:
                speak_btn.setImageResource(R.drawable.btn_voice_ongoing);
                mResultText.setText(null);// 清空显示内容
                mResultText2.setText(null);//自己编辑的代码**********************************
                mIatResults.clear();
                // 设置参数
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    showTip(getString(R.string.text_begin));
                } else {
                    // 不显示听写对话框
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        showTip("听写失败,错误码：" + ret+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                    } else {
                        showTip(getString(R.string.text_begin));
                    }
                }
                break;
            case R.id.imageButton:
                //点击语音输入按钮
                //将语音输入窗口显示出来
                speak_mwindow.setVisibility(View.VISIBLE);//显示


                break;

            case R.id.button3:
                //点击了确认按钮
                //隐藏语音输入窗口,并检查答案是否正确,在textView9,textView10显示不同内容,并关闭文本和语音输入
                speak_mwindow.setVisibility(View.INVISIBLE);//隐藏
                if(mAnswer.equals(StringUtils.GetDeleteShort(mResultText2.getText().toString()))){
                    //与答案一样---正确
                    isTrue=true;//设置答题为正确

                    tip_title.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[1])));
                    tip_title.setText(R.string.answer_right);
                    tip_desc.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[1])));
                    String tip_desc_string="答对了!继续加油吧! 左滑进入下一题";
                    tip_desc.setText(String.format("%s\n%s",tip_desc_string,tip));//需替换---其他文件已经接受到此参数,此文件里偷懒了

//______________________________语音合成BEGIN______________________
                    // 开始合成
                    // 收到onCompleted 回调时，合成结束、生成合成音频
                    // 合成的音频格式：只支持pcm格式
                    // 设置参数
                    setParam2();
                    int code = mTts.startSpeaking(tip_desc_string, mTtsListener);
                    //			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);
                    if (code != ErrorCode.SUCCESS) {
                        showTip("语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                    }
//_______________________________语音合成END__________________________________________
                }else{//回答错误
                    isTrue=false;

                    tip_title.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[0])));
                    tip_title.setText(R.string.answer_wrong);
                    tip_desc.setTextColor(getContext().getResources().getColor(CPResourceUtil.getColorId(getContext(),tip_color[0])));
                    String tip_desc_string="答错了! 再想一下吧";
                    tip_desc.setText(String.format("%s\n%s",tip_desc_string,tip));//需替换---其他文件已经接受到此参数,此文件里偷懒了

                    setParam2();
                    int code = mTts.startSpeaking(tip_desc_string, mTtsListener);
                    if (code != ErrorCode.SUCCESS) {
                        showTip("语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                    }
                }

                if(isFirstAnswer){//如果是第一次回答题目,则将是否答对通过广播发送出去
                    isGetScore=false;
                    //创建一个广播事件的意图
                    Intent intent=new Intent(ChinaTalkGameFragment.EVENT);
                    intent.putExtra("isTrue",isTrue);
                    intent.putExtra("isGetScore",isGetScore);
                    //通过本地的广播管理器来发送广播---将是否答对发送出去
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    isFirstAnswer=false;
                }
                if(mPosition==mCount-1){
                    isGetScore=true;
                    //创建一个广播事件的意图---向主页面请求成绩
                    Intent intent=new Intent(ChinaTalkGameFragment.EVENT);
                    intent.putExtra("isTrue",false);
                    intent.putExtra("isGetScore",isGetScore);
                    //通过本地的广播管理器来发送广播---将是否答对发送出去
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                    //创建一个接收成绩的广播接收器
                    myScore=new MyScore();
                    //创建一个意图过滤器,只处理指定事件来源的广播
                    IntentFilter filter=new IntentFilter(GameActivity.EVENT);
                    //注册广播接收器,注册之后才能正常接收广播
                    LocalBroadcastManager.getInstance(mContext).registerReceiver(myScore,filter);


                }

                break;
            case R.id.imageView5:
                speak_mwindow.setVisibility(View.GONE);
            default:
                break;
        }
    }
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。

            showTip(error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {

            String text = JsonParser.parseIatResult(results.getResultString());
            mResultText.append(text);
            mResultText.setSelection(mResultText.length());


            if(isLast) {
                //TODO 最后的结果

            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
                Log.d(TAG, "session id =" + sid);
            }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, "recognizer result：" + results.getResultString());

            String text = JsonParser.parseIatResult(results.getResultString());
            mResultText.append(text);
            mResultText2.append(text); //自己编辑的代码***************************************
            mResultText.setSelection(mResultText.length());
            speak_btn.setImageResource(R.drawable.btn_voice_end);

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));

        }

    };

    private void showTip(final String str)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }

    /**
     * 参数设置
     * @return
     */
    public void setParam(){
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
        // 设置引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        //mIat.setParameter(MscKeys.REQUEST_AUDIO_URL,"true");

        //	this.mTranslateEnable = mSharedPreferences.getBoolean( this.getString(R.string.pref_key_translate), false );
        if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        }
        // 在线听写支持多种小语种，若想了解请下载在线听写能力，参看其speechDemo
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT,lag);

        }


        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }
//_________________________________END______________________________________

//_________________________________语音合成BEGIN___________________________

    private static int selectedNumCloud=0;
    private static int selectedNumLocal=0;
    /**
     * 发音人选择。
     */
    private void showPresonSelectDialog() {
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            // 选择在线合成
            case R.id.tts_radioCloud:
                new AlertDialog.Builder(getContext()).setTitle("在线合成发音人选项")
                        .setSingleChoiceItems(cloudVoicersEntries, // 单选框有几项,各是什么名字
                                selectedNumCloud, // 默认的选项
                                new DialogInterface.OnClickListener() { // 点击单选框后的处理
                                    public void onClick(DialogInterface dialog,
                                                        int which) { // 点击了哪一项
                                        voicerCloud = cloudVoicersValue[which];

                                        if ("catherine".equals(voicerCloud) || "henry".equals(voicerCloud) || "vimary".equals(voicerCloud)) {
                                            ((EditText) mView.findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);
                                        }else {
                                            ((EditText) mView.findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
                                        }
                                        selectedNumCloud = which;
                                        dialog.dismiss();
                                    }
                                }).show();
                break;

            // 选择本地合成
            case R.id.tts_radioLocal:
                new AlertDialog.Builder(getContext()).setTitle("本地合成发音人选项")
                        .setSingleChoiceItems(localVoicersEntries, // 单选框有几项,各是什么名字
                                selectedNumLocal, // 默认的选项
                                new DialogInterface.OnClickListener() { // 点击单选框后的处理
                                    public void onClick(DialogInterface dialog,
                                                        int which) { // 点击了哪一项
                                        voicerLocal = localVoicersValue[which];
                                        if ("catherine".equals(voicerLocal) || "henry".equals(voicerLocal) || "vimary".equals(voicerLocal)) {
                                            ((EditText) mView.findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);
                                        }else {
                                            ((EditText) mView.findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
                                        }
                                        selectedNumLocal = which;
                                        dialog.dismiss();
                                    }
                                }).show();
                break;
            case R.id.tts_radioXtts:
                new AlertDialog.Builder(getContext()).setTitle("增强版合成发音人选项")
                        .setSingleChoiceItems(xttsVoicersEntries, // 单选框有几项,各是什么名字
                                selectedNumLocal, // 默认的选项
                                new DialogInterface.OnClickListener() { // 点击单选框后的处理
                                    public void onClick(DialogInterface dialog,
                                                        int which) { // 点击了哪一项
                                        voicerXtts = xttsVoicersValue[which];
                                        //Toast.makeText(this,voicerXtts,Toast.LENGTH_LONG);
                                        System.out.println("sssssss:"+voicerXtts);
                                        if ("catherine".equals(voicerXtts) || "henry".equals(voicerXtts) || "vimary".equals(voicerXtts)) {
                                            ((EditText) mView.findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);
                                        }else {
                                            ((EditText) mView.findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
                                        }
                                        selectedNumLocal = which;
                                        dialog.dismiss();
                                    }
                                }).show();
                break;

            default:
                break;
        }
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");

            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
            Log.d(TAG,"开始播放："+ System.currentTimeMillis());
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
//                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
                Log.d(TAG, "session id =" + sid);
            }

            //实时音频流输出参考
			/*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
				byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
				Log.e("MscSpeechLog", "buf is =" + buf);
			}*/
        }
    };

//    private void showTip(final String str){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mToast.setText(str);
//                mToast.show();
//            }
//        });
//    }

    /**
     * 参数设置
     */
    private void setParam2(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD))
        {
            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,voicerCloud);

        }else if(mEngineType.equals(SpeechConstant.TYPE_LOCAL)){
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath2());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,voicerLocal);
        }else{
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_XTTS);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath2());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME,voicerXtts);
        }
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        //	mTts.setParameter(SpeechConstant.STREAM_TYPE, AudioManager.STREAM_MUSIC+"");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");


    }

    //获取发音人资源路径
    private String getResourcePath2(){
        StringBuffer tempBuffer = new StringBuffer();
        String type= "tts";
        if(mEngineType.equals(SpeechConstant.TYPE_XTTS)){
            type="xtts";
        }
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(getContext(), ResourceUtil.RESOURCE_TYPE.assets, type+"/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        if(mEngineType.equals(SpeechConstant.TYPE_XTTS)){
            tempBuffer.append(ResourceUtil.generateResourcePath(getContext(), ResourceUtil.RESOURCE_TYPE.assets, type+"/"+TtsDemo.voicerXtts+".jet"));
        }else {
            tempBuffer.append(ResourceUtil.generateResourcePath(getContext(), ResourceUtil.RESOURCE_TYPE.assets, type + "/" + TtsDemo.voicerLocal + ".jet"));
        }

        return tempBuffer.toString();
    }
//________________________________语音合成END_______________________________


    @Override
    public void onDestroy() {
        super.onDestroy();
//_____________BEGIN________________
        if( null != mIat ){
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
//_____________END_________________
//_____________语音合成Begin_________________
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
//_____________语音合成END________________
    }


    //声明一个广播接收器
    private ChinaTalkGameFragment.MyScore myScore;

    //定义一个广播接收器,用于接收成绩
    private class MyScore extends BroadcastReceiver {
        //一旦接收到成绩的广播,马上触发接收器的onReceive方法
        public void onReceive(Context context, final Intent intent){
            if (intent!=null){
                //从广播消息中取出最新结果
                int m_Score=intent.getIntExtra("Score",0);
                    Score=m_Score;//将成绩保存在这里


                    //创建答题结束对话框的建造器
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    //给建造器设置对话框的标题文本
                    builder.setTitle("答题结束");
                    //给建造器设置对话框的信息文本
                    builder.setMessage("快来看看您的成绩吧!");
                    //给建造器设置对话框的肯定按钮文本及其点击监听器
                    builder.setPositiveButton("看成绩", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转页面--并将Score一起带过去
                            Intent mIntent=new Intent(getActivity(), TestScoreActivity.class);//创建意图
                            Bundle bundle = new Bundle();//创建新包裹
                            bundle.putString("test_title","趣味测试");
                            bundle.putInt("Score",Score);//将Score存入包裹
                            bundle.putInt("ques_num",mCount);//总题数
                            mIntent.putExtras(bundle);//把包塞给意图
                            startActivity(mIntent);
                        }
                    });
                    //给建造器设置对话框的否定按钮文本及其点击监听器
                    builder.setNegativeButton("再看看题目", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),"再次点击确认按钮可前往成绩页面哦",Toast.LENGTH_SHORT).show();
                        }
                    });
                    //根据建造器完成对话框对象的构建
                    AlertDialog alert=builder.create();
                    //在界面上显示提醒对话框
                    alert.show();

            }
        }
    }


    private Handler mHandler = new Handler(); // 声明一个处理器对象
    // 定义一个下载进度的刷新任务
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            boolean isFinished = false;
            // 创建一个下载查询对象，按照下载编号过滤
            DownloadManager.Query down_query = new DownloadManager.Query();
            // 设置下载查询对象的编号过滤器
            down_query.setFilterById(mDownloadId);
            // 向下载管理器发起查询操作，并返回查询结果集的游标
            Cursor cursor = mDownloadManager.query(down_query);
            while (cursor.moveToNext()) {
                int nameIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                int uriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                int mediaTypeIdx = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE);
                int totalSizeIdx = cursor.getColumnIndex(
                        DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int nowSizeIdx = cursor.getColumnIndex(
                        DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                // 根据总大小和已下载大小，计算当前的下载进度
                int progress = (int) (100 * cursor.getLong(nowSizeIdx) / cursor.getLong(totalSizeIdx));
                if (cursor.getString(uriIdx) == null) {
                    break;
                }
                // 设置文本进度圈的当前进度
                tpc_progress.setProgress(progress, 100);
                // Android7.0之后提示COLUMN_LOCAL_FILENAME已废弃
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    mImagePath = cursor.getString(nameIdx);
                } else {
                    // 所以7.0之后要先获取文件的Uri，再根据Uri获取文件路径
                    String fileUri = cursor.getString(uriIdx);
                    mImagePath = Uri.parse(fileUri).getPath();
                }
                if (progress == 100) { // 下载完毕
                    isFinished = true;
                }
                // 获得实际的下载状态
                int status = isFinished ? DownloadManager.STATUS_SUCCESSFUL : cursor.getInt(statusIdx);

            }
            cursor.close(); // 关闭数据库游标
            if (!isFinished) { // 未完成，则继续刷新
                // 延迟100毫秒后再次启动下载进度的刷新任务
                mHandler.postDelayed(this, 10);
            } else { // 已完成，则显示图片
                // 隐藏文本进度圈
                tpc_progress.setVisibility(View.GONE);
                // 把指定路径的图片显示在图像视图上面
                iv_pic.setImageURI(Uri.parse(mImagePath));
            }
        }
    };

    //用于判断存储中是否已有指定文件的函数
    public ArrayList<String> getFileName(String fileAbsolutePaht, String type) {
        ArrayList<String>  result = new ArrayList<String>();
        File file = new File(fileAbsolutePaht);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; ++i) {
            if (!files[i].isDirectory()) {
                String fileName = files[i].getName();
                if (fileName.trim().toLowerCase().endsWith(type)) {
                    result.add(fileName);
                }
            }
        }
        return result;
    }

}
