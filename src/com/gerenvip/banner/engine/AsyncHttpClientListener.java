package com.gerenvip.banner.engine;

/**
 * 异步网络请求监听
 *
 * @param <M>
 * @author zhangyan_yh
 */
public interface AsyncHttpClientListener<M> {

    /**
     * 进行AsyncHttpClient请求之前调用
     */
    public void onAsyncHttpClientStart();

    /**
     * AsyncHttpClient请求成功
     *
     * @param content
     */
    public void onAsyncHttpClientSuccess(M DTOData);

    /**
     * AsyncHttpClient请求失败
     *
     * @param content
     */
    public void onAsyncHttpClientFailure(String content);

    /**
     * AsyncHttpClient请求完成
     */
    public void onAsyncHttpClientFinish();

}
