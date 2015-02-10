package com.gerenvip.banner.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangwei_cs on 2014/8/8.
 */
public class ImageUtils {

    /**
     * 根绝url生成img的文件名
     *
     * @param str
     * @return
     */
    public static String convertUrlToFilename(String str) {
        // 只允许字母和数字
        String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
