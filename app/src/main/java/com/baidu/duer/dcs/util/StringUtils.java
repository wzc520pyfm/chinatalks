package com.baidu.duer.dcs.util;

/**
 * qq986945193 Project
 * ============================================================================
 * Copyright (c) 2015-2016 http://www.qq986945193.com All rights reserved.
 * ----------------------------------------------------------------------------
 * 类名：字符串的一个工具类
 * ----------------------------------------------------------------------------
 * 功能描述：
 * ----------------------------------------------------------------------------
 */
public class StringUtils {
    /**
     * 去掉String字符串中的.
     * @param str
     * @return
     */
    public static String GetDeleteShort(String str){
        String s = str.replace(".", "");
        s=s.replace("。","");
        return s;
    }
}
