package com.gerenvip.banner.net;

import com.gerenvip.banner.config.FeatureConfig;

/**
 * @author dupengtao@cyou-inc.com 2014-3-18
 */
public class UriHelper {

    // public static final Uri BASE_URI = Uri
    // .parse("http://m.weather.com.cn/");
    /**
     * 主机地址
     */
    public static final String SERVER_HOST;
    //public static final String SERVER_FUNDS_HOST;

    static {
        if (FeatureConfig.ONLINE_SERVER) {// 线上
            SERVER_HOST = "http://iuu.changyou.com/m/";
        } else {// 线下
        }
    }

    /**
     * 主页连接
     * /home
     *
     * @return
     */
    public static String getHomeUrl() {
        StringBuilder sb = new StringBuilder(SERVER_HOST);
        sb.append("home");
        return sb.toString();
    }
}
