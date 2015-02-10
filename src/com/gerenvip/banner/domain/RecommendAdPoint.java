package com.gerenvip.banner.domain;

public class RecommendAdPoint {

    private int flag;
    private int platformId;
    private int gameId;
    private String gameName;
    private String url;

    public RecommendAdPoint(int flag, int platformId, int gameId, String gameName, String url) {
        this.flag = flag;
        this.platformId = platformId;
        this.gameId = gameId;
        this.gameName = gameName;
        this.url = url;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "[flag=" + flag + ";platformId=" + platformId + ";gameId=" + gameId + ";gameName=" + gameName
                + ";url=" + url + "]";
    }
}
