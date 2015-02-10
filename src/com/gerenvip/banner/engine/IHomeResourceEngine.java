package com.gerenvip.banner.engine;

public interface IHomeResourceEngine<M> {

    /**
     * 从服务器拉取主页资源
     */
    public void getHomeResource(AsyncHttpClientListener<M> asyncHttpClientListener);
}
