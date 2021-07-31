/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.duer.dcs.androidapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.baidu.duer.dcs.util.SharedUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.HashMap;
import java.util.Locale;

/**
 * DcsSample application
 * <p>
 * Created by zhangyan42@baidu.com on 2017/5/11.
 */
//本App的注释规范:
/*********************************************************************************************
 * 页面:          页面/类的描述                                                                    *
 * 进入方式:      如何进入此页面/逻辑
 * 关联到的文件:  /上一路径/类名   :  用途
 *
 * 碎片:
 * 页面主要逻辑: 1.
 * 已知Bug:     1.
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
/*********************************************************************************************
 * 页面:          主Application                                                                  *
 * 进入方式:      *
 * 关联到的文件:
 * 页面主要逻辑: 1.实现获取该单例的方法
 *              2.从共享参数中读取语言种类
 *              3.对科大讯飞SDK初始化
 * 已知Bug:     1.语言种类读取和设置不生效
 * 需小心的点:   1.
 * 其他说明:     1.
 * ===========================================================================================*/
public class DcsSampleApplication extends Application {
    //声明一个当前应用的静态实例
    private static volatile DcsSampleApplication instance = null;

    //声明一个公共的信息映射,可当作全局变量使用
    public HashMap<String,String> mInfoMap=new HashMap<String,String>();

    @Override
    public void onCreate() {

        super.onCreate();
        //在打开应用时对静态的应用实例赋值
        instance = this;
        // LeakCanary.install(this);


        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
// 请勿在“=”与appid之间添加任何空字符或者转义符

        SpeechUtility.createUtility(DcsSampleApplication.this, SpeechConstant.APPID +"=d213d266");

        //初始化语言--好像还有bug
        String location = SharedUtil.getInstance(this).readShared("COUNTRY","");//(可用Sharedpreferences代替，获取选中的语言种类)
        if (!TextUtils.isEmpty(location)) {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            Locale myLocale = new Locale(location);
            config.locale = myLocale;
            resources.updateConfiguration(config, dm);
        }

    }
//利用单例模式获取当前应用的唯一实例
    public static DcsSampleApplication getInstance() {
        return instance;
    }
}