package com.gerenvip.banner.engine;

import android.content.Context;
import android.util.Log;
import com.gerenvip.banner.net.UriHelper;
import com.gerenvip.banner.utils.JacksonUtil;
import com.gerenvip.banner.utils.LogHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HomeResourceEngineImpl<M> implements IHomeResourceEngine<M> {

    private static final String TAG = "HomeResourceEngineImpl";
    protected Context mContext;
    protected Class<M> mClazz;
    private static AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient();

    public HomeResourceEngineImpl(Context context, Class<M> clazz) {
        this.mContext = context;
        this.mClazz = clazz;
    }

    @Override
    public void getHomeResource(AsyncHttpClientListener<M> asyncHttpClientListener) {
        String url = UriHelper.getHomeUrl();
        LogHelper.d(TAG, "home url=" + url);
        doGet(url, 5000, asyncHttpClientListener);
    }

    public void doGet(String url, int timeout,
                      final AsyncHttpClientListener<M> asyncHttpClientListener) {
        mAsyncHttpClient = addCommonHeader(mAsyncHttpClient);
        mAsyncHttpClient.setTimeout(timeout);
        mAsyncHttpClient.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                LogHelper.d(TAG, "开始请求");
                asyncHttpClientListener.onAsyncHttpClientStart();
                super.onStart();
            }

            @Override
            public void onSuccess(String content) {
                //boolean outOfData = handleLoginOutOfData(content);
                //if (outOfData) {
                //    LogHelper.d(TAG, "登录信息已经过时，请重新登录；返回的json串：" + content);
                //    LoginUtils.emptyLoginParams(mContext);
                //    goToLogin();
                //} else {
                LogHelper.d(TAG, "请求成功；返回的json串：" + content);
                asyncHttpClientListener
                        .onAsyncHttpClientSuccess(getDTOData(content));
                //}

                super.onSuccess(content);
            }

            @Override
            public void onFinish() {
                LogHelper.d(TAG, "请求完成");
                asyncHttpClientListener.onAsyncHttpClientFinish();
                super.onFinish();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                LogHelper.d(TAG, "请求失败");
                asyncHttpClientListener.onAsyncHttpClientFailure(content);
                super.onFailure(error, content);
            }
        });
    }

    private M getDTOData(String content) {
        M result = null;
        try {
            LogHelper.d(TAG, "需要转化的字节码" + mClazz);
            result = JacksonUtil.getInstance().readValue(content, mClazz);
            LogHelper.d(TAG, "转化的结果" + result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "解析异常：" + e.toString());
        }
        return result;
    }

    /**
     * 添加公共头信息
     *
     * @param client
     * @return
     */
    private AsyncHttpClient addCommonHeader(AsyncHttpClient client) {
        client.addHeader("JSESSIONID", "");// 登录标识
        client.addHeader("TOKEN", "");// token
        client.addHeader("IMEI", "");
        client.addHeader("TICKET", "");
        client.addHeader("CUCUMBER", "");
        LogHelper.e(TAG, "JSESSIONID=" + "");
        return client;
    }
}
