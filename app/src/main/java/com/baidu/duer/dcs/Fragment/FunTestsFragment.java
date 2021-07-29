package com.baidu.duer.dcs.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.dcs.R;
import com.baidu.duer.dcs.androidapp.MainActivity;
import com.baidu.duer.dcs.chinatalk.Dev_code;
import com.baidu.duer.dcs.chinatalk.GameActivity;
import com.baidu.duer.dcs.chinatalk.ResultTestActivity;
import com.baidu.duer.dcs.chinatalk.SelectTestActivity;
import com.baidu.duer.dcs.util.SharedUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.mscv5plusdemo.TtsDemo;
import com.iflytek.speech.setting.TtsSettings;
import com.iflytek.speech.util.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Locale;

//@Nullable  //注解表示可以传入null
public class FunTestsFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private ImageButton speak_btn;
    //_________________________BEGIN____________________________________
private String TAG = "FunTestsFragment";
    private Toast mToast;
    private TextView textView;
    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    // 语音识别对象
    private SpeechRecognizer mAsr;
    // 唤醒结果内容
    private String resultString;
    // 识别结果内容
    private String recoString;
    // 设置门限值 ： 门限值越低越容易被唤醒
    private TextView tvThresh;
    private SeekBar seekbarThresh;
    private final static int MAX = 3000;
    private final static int MIN = 0;
    private int curThresh = 1450;
    private String threshStr = "门限值：";
    // 云端语法文件
    private String mCloudGrammar = null;
    // 云端语法id
    private String mCloudGrammarID;
    // 本地语法id
    private String mLocalGrammarID;
    // 本地语法文件
    private String mLocalGrammar = null;
    // 本地语法构建路径
    private String grmPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/msc/test";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
//__________________________END_____________________________________



//_________________________语音合成BEGIN________________________________________
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
    private SharedPreferences mSharedPreferences;
//___________________语音合成END___________________________
//_________________________语音合成END_________________________________________


    Spinner sp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_funtests,container,false);
        speak_btn=view.findViewById(R.id.speak_btn);
        speak_btn.setOnClickListener(this);
        speak_btn.setOnLongClickListener(this);
        //从布局稳健者获取下拉框
        sp=(Spinner) view.findViewById(R.id.sp_dropdown);
        initSpinner();

//__________________________BEGIN____________________________________________
// 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(getContext(), null);
        // 初始化识别对象---唤醒+识别,用来构建语法
        mAsr = SpeechRecognizer.createRecognizer(getContext(), null);
        // 初始化语法文件
        mCloudGrammar = readFile(getContext(), "wake_grammar_sample.abnf", "utf-8");
        mLocalGrammar = readFile(getContext(), "wake.bnf", "utf-8");
//__________________________END_____________________________________________
        speakInit();//自己写的代码*****************************************

//_____________________________语音合成BEGIN________________________________
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

        mSharedPreferences = getContext().getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
//____________________语音合成END___________________________________
//_____________________________语音合成END__________________________________
        return view;
    }

    public void speakInit(){
//        ____________________________BEGIN__________________________________
        int ret = 0;
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            // 设置参数
            mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
            mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            // 开始构建语法
            ret = mAsr.buildGrammar("abnf", mCloudGrammar, grammarListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("语法构建失败,错误码：" + ret+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        } else {
            mAsr.setParameter(SpeechConstant.PARAMS, null);
            mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            // 设置引擎类型
            mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
            // 设置语法构建路径
            mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
            // 设置资源路径
            mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
            ret = mAsr.buildGrammar("bnf", mLocalGrammar, grammarListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("语法构建失败,错误码：" + ret+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
//        _______________________________END_______________________________________
    }
//____________________________________BEGIN______________________________
GrammarListener grammarListener = new GrammarListener() {
    @Override
    public void onBuildFinish(String grammarId, SpeechError error) {
        if (error == null) {
            if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
                mCloudGrammarID = grammarId;
            } else {
                mLocalGrammarID = grammarId;
            }
            showTip("语法构建成功：" + grammarId);
        } else {
            showTip("语法构建失败,错误码：" + error.getErrorCode()+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }
};
//____________________________________END_______________________________



    @Override
    public void onClick(View v){

        switch (v.getId()){//触发唤醒开始事件
            case R.id.speak_btn:
                // 非空判断，防止因空指针使程序崩溃
                mIvw = VoiceWakeuper.getWakeuper();
                speak_btn.setImageResource(R.drawable.btn_voice_ongoing);
                if (mIvw != null) {
                    Toast.makeText(getContext(),"请说话",Toast.LENGTH_SHORT).show();
                    resultString = "";
                    recoString = "";

//                    textView.setText(resultString);

                    final String resPath = ResourceUtil.generateResourcePath(getContext(), ResourceUtil.RESOURCE_TYPE.assets, "ivw/"+getString(R.string.app_id)+".jet");
                    // 清空参数
                    mIvw.setParameter(SpeechConstant.PARAMS, null);
                    // 设置识别引擎
                    mIvw.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
                    // 设置唤醒资源路径
                    mIvw.setParameter(ResourceUtil.IVW_RES_PATH, resPath);
                    /**
                     * 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
                     * 示例demo默认设置第一个唤醒词，建议开发者根据定制资源中唤醒词个数进行设置
                     */
                    mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:"
                            + curThresh);
                    // 设置唤醒+识别模式
                    mIvw.setParameter(SpeechConstant.IVW_SST, "oneshot");
                    // 设置返回结果格式
                    mIvw.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//				mIvw.setParameter(SpeechConstant.IVW_SHOT_WORD, "0");

                    // 设置唤醒录音保存路径，保存最近一分钟的音频
                    mIvw.setParameter( SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath()+"/msc/ivw.wav" );
                    mIvw.setParameter( SpeechConstant.AUDIO_FORMAT, "wav" );
                    mIvw.setParameter( SpeechConstant.KEEP_ALIVE, "0");
                    // 设置语音后端点:静音超时时间
                    mAsr.setParameter(SpeechConstant.VAD_EOS,  "4000");

                    if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
                        if (!TextUtils.isEmpty(mCloudGrammarID)) {
                            // 设置云端识别使用的语法id
                            mIvw.setParameter(SpeechConstant.CLOUD_GRAMMAR,
                                    mCloudGrammarID);
                            mIvw.startListening(mWakeuperListener);//开始录音
                        } else {
                            showTip("请先构建语法");
                        }
                    } else {
                        if (!TextUtils.isEmpty(mLocalGrammarID)) {
                            // 设置本地识别资源
                            mIvw.setParameter(ResourceUtil.ASR_RES_PATH,
                                    getResourcePath());
                            // 设置语法构建路径
                            mIvw.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
                            // 设置本地识别使用语法id
                            mIvw.setParameter(SpeechConstant.LOCAL_GRAMMAR,
                                    mLocalGrammarID);
                            mIvw.startListening(mWakeuperListener);//开始录音
                            //mIvw.stopListening();
                        } else {
                            showTip("请先构建语法");
                        }
                    }

                } else {
                    showTip("唤醒未初始化");
                }
                break;
        }

    }

    @Override
    public boolean onLongClick(View v){
        if(v.getId()==R.id.speak_btn){
            //        Intent intent =new Intent(this.getContext(),AnnouncementActivity.class);
//        Intent intent =new Intent(this.getContext(), EditInformationActivity.class);
            //Intent intent =new Intent(this.getContext(), GameActivity.class);
//        Intent intent =new Intent(this.getContext(), SelectTestActivity.class);
            //Intent intent =new Intent(this.getContext(), WrongQuesBookActivity.class);
//        Intent intent =new Intent(this.getContext(), TestCenterActivity.class);
            Intent intent =new Intent(this.getContext(), Dev_code.class);
//        Intent intent =new Intent(this.getContext(), DcsSampleOAuthActivity.class);
//        Intent intent =new Intent(this.getContext(), OneShotDemo.class); //唤醒+识别
//        Intent intent =new Intent(this.getContext(), TtsDemo.class);  //语音合成
//        Intent intent =new Intent(this.getContext(), IatDemo.class);  //语音听写
//        Intent intent =new Intent(this.getContext(), AsrDemo.class);  //语音评测
//        Intent intent =new Intent(this.getContext(), com.iflytek.mscv5plusdemo.MainActivity.class);//综合语音测试,//一定要确保MainActivity是讯飞包里的,而不是百度包里的(md,蛋疼,这两包里有同名文件,c)
//        Intent intent =new Intent(this.getContext(), Head.class);
            startActivityForResult(intent,0);
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    //初始化下拉框
    private void initSpinner(){
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> languageAdapter=new ArrayAdapter<String>(super.getContext(),R.layout.chinatalk_select_sex,languageArray);
        //设置数组适配器的布局样式
        languageAdapter.setDropDownViewResource(R.layout.chinatalk_item_sex);
        //设置下拉框的数组适配器
        sp.setAdapter(languageAdapter);
        //设置默认项
        sp.setSelection(3);
        //设置下拉框的选择监听器
        sp.setOnItemSelectedListener(new FunTestsFragment.MySelectedListener());
    }
    //定义下拉列表的文本数组
    private String[] languageArray={"Romania","Serbia","English","设置语言"};
    public int count=0;

    //定义一个选择监听器
    class MySelectedListener implements AdapterView.OnItemSelectedListener {
        //选择事件的处理方法,arg2代表选择项的序号
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2,long arg3){
            Log.d("HeadSpinner","您点击了Spinner!");
            //Toast.makeText(FunTestsFragment.this,"您点击了Spinner",Toast.LENGTH_SHORT).show();
            if(count==0){
                count++;
            }else{
                switch (arg2){
                    case 0:
                        changeAppLanguages("ro");
                        break;
                    case 1:
                        changeAppLanguages("sr");
                        break;
                    case 2:
                        changeAppLanguages("en");
                    default:
                        break;
                }
            }

        }
        @Override
        //未选中时的处理方法,一般不必关注
        public void onNothingSelected(AdapterView<?> arg0){}
    }

    //    更改应用语言的函数
    public void changeAppLanguages(String locale){
        DisplayMetrics metrics =getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        //if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
        configuration.locale=new Locale(locale);

        //}else{
        //configuration.locale=locale;
        //}
        getResources().updateConfiguration(configuration,metrics);
        SharedUtil.getInstance(super.getActivity()).writeShared("COUNTRY",""+locale);//将地区存入共享参数
        //重新启动Activity

        Intent intent = new Intent(super.getContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


//______________________________________BEGIN_________________________________________
private WakeuperListener mWakeuperListener = new WakeuperListener() {

    @Override
    public void onResult(WakeuperResult result) {//唤醒结果反馈事件

        try {
            String text = result.getResultString();
            JSONObject object;
            object = new JSONObject(text);
            StringBuffer buffer = new StringBuffer();
            buffer.append("【RAW】 "+text);
            buffer.append("\n");
            buffer.append("【操作类型】"+ object.optString("sst"));
            buffer.append("\n");
            buffer.append("【唤醒词id】"+ object.optString("id"));
            buffer.append("\n");
            buffer.append("【得分】" + object.optString("score"));
            buffer.append("\n");
            buffer.append("【前端点】" + object.optString("bos"));
            buffer.append("\n");
            buffer.append("【尾端点】" + object.optString("eos"));
            resultString =buffer.toString();
        } catch (JSONException e) {
            resultString = "结果解析出错";
            e.printStackTrace();
        }

        String tip_desc_string="您好";
        Toast.makeText(getContext(),tip_desc_string,Toast.LENGTH_SHORT).show();
        //if(mAsr.isListening()){}else{speak_btn.setImageResource(R.drawable.btn_voice_start);}
//______________________________在语音唤醒代码中间插入了语音合成BEGIN___________________________________
//______________________________语音合成BEGIN______________________
        // 开始合成
        // 收到onCompleted 回调时，合成结束、生成合成音频
        // 合成的音频格式：只支持pcm格式
        // 设置参数
        //setParam2();
        //int code = mTts.startSpeaking(tip_desc_string, mTtsListener);
        //			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);
        //if (code != ErrorCode.SUCCESS) {
         //   showTip("语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
       // }
//_______________________________语音合成END__________________________________________
//______________________________在语音唤醒代码中间插入了语音合成END___________________________________

        //resultString即回馈结果----待使用
//        textView.setText(resultString);
    }

    @Override
    public void onError(SpeechError error) {
        showTip(error.getPlainDescription(true));
    }

    @Override
    public void onBeginOfSpeech() {
        showTip("开始说话");
    }

    @Override
    public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
        Log.d(TAG, "eventType:"+eventType+ "arg1:"+isLast + "arg2:" + arg2);
        // 识别结果
        if (SpeechEvent.EVENT_IVW_RESULT == eventType) {
            RecognizerResult reslut = ((RecognizerResult)obj.get(SpeechEvent.KEY_EVENT_IVW_RESULT));
            recoString += JsonParser.parseGrammarResult(reslut.getResultString());
//            textView.setText(recoString);//recoString 即识别结果
            speak_btn.setImageResource(R.drawable.btn_voice_start);
            Toast.makeText(getContext(),recoString,Toast.LENGTH_SHORT).show();
            if(recoString.contains("汉语闯关")){
                //语音合成
                setParam2();
                int code = mTts.startSpeaking("正在为您打开汉语闯关", mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                }//语音合成end

                Intent intent =new Intent(getContext(), GameActivity.class);
                startActivityForResult(intent,0);
            }else if(recoString.contains("真题测试一")){
                //语音合成
                setParam2();
                int code = mTts.startSpeaking("正在为您打开真题测试卷一", mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                }//语音合成end

                Intent intent =new Intent(getContext(), ResultTestActivity.class);
                startActivityForResult(intent,0);
            }else if(recoString.contains("随机测试")){
                //语音合成
                setParam2();
                int code = mTts.startSpeaking("正在为您打开随机测试", mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                }//语音合成end

                Intent intent =new Intent(getContext(), SelectTestActivity.class);
                startActivityForResult(intent,0);
            }
        }
    }

    @Override
    public void onVolumeChanged(int volume) {
        // TODO Auto-generated method stub

    }

};

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy OneShotDemo");
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        } else {
            showTip("唤醒未初始化");
        }

//_____________语音合成Begin_________________
        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
//_____________语音合成END________________
    }

    /**
     * 读取asset目录下文件。
     *
     * @return content
     */
    public static String readFile(Context mContext, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            InputStream in = mContext.getAssets().open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取识别资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        // 识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(getContext(),
                ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        return tempBuffer.toString();
    }

    private void showTip(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mToast.setText(str);
//                mToast.show();
            }
        });
    }


//______________________________________END__________________________________________

//_____________________________________语音合成BEGIN_________________________________
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
                                            ((EditText) getView().findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);
                                        }else {
                                            ((EditText) getView().findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
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
                                            ((EditText) getView().findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);
                                        }else {
                                            ((EditText) getView().findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
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
                                            ((EditText) getView().findViewById(R.id.tts_text)).setText(R.string.text_tts_source_en);
                                        }else {
                                            ((EditText) getView().findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
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
            tempBuffer.append(ResourceUtil.generateResourcePath(getContext(), ResourceUtil.RESOURCE_TYPE.assets, type+"/"+ TtsDemo.voicerXtts+".jet"));
        }else {
            tempBuffer.append(ResourceUtil.generateResourcePath(getContext(), ResourceUtil.RESOURCE_TYPE.assets, type + "/" + TtsDemo.voicerLocal + ".jet"));
        }

        return tempBuffer.toString();
    }
//________________________________语音合成END_______________________________
//____________________________________语音合成END_____________________________________
}
