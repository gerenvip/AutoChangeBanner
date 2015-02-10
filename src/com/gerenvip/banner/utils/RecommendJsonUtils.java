package com.gerenvip.banner.utils;

import com.cy.mm.facade.trade.game.dto.RecommendGameDTO;
import com.cy.mm.facade.trade.homepage.dto.HomeBannerDTO;
import com.gerenvip.banner.domain.RecommendAdPoint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendJsonUtils {
    private static final String TAG = "RecommendJsonParseUtils";

    /**
     * 解析首页广告urls的json格式数据
     * 
     * @param jsonStr
     * @return
     */
    public static List<String> parseAdRecommendUrls(String jsonStr) {
        if (jsonStr == null) {
            LogHelper.d(TAG, "parseAdRecommendUrls... but json is null");
            return null;
        }
        LogHelper.d(TAG, "json=" + jsonStr);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("oldurl");
            if (jsonArray == null) {
                return null;
            }
            List<String> urls = getJsonArrayToUrlList(jsonArray);
            return urls;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建json字符串
     * 
     * @param urls
     * @return
     */
    public static String buildAdUrlsJson(List<String> urls) {
        if (urls == null) {
            LogHelper.e(TAG, "buildAdUrlsJson ... but urls is null");
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < urls.size(); i++) {
                jsonArray.put(i, urls.get(i));
            }
            jsonObject.put("oldurl", jsonArray);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getJsonArrayToUrlList(JSONArray jsonArray) {
        if (jsonArray == null) {
            LogHelper.d(TAG, "jsonArray is null");
            return null;
        }
        List<String> list = new ArrayList<String>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            String url = jsonArray.optString(i);
            LogHelper.d(TAG, "old url [" + i + "]:" + url);
            list.add(url);
        }
        return list;
    }

    /**
     * 将推荐游戏列表以json的样式存储
     * 
     * @param gameList
     * @return
     */
    public static String buildRecommendGameJson(List<RecommendGameDTO> gameList) {
        if (gameList == null) {
            LogHelper.e(TAG, "buildRecommendGameJson failure because gameList is null");
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (RecommendGameDTO dto : gameList) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("gameIconUrl", dto.getBigIcon());
                jsonObject.put("gameId", dto.getGameId());
                jsonObject.put("gameName", dto.getGameName());
                jsonObject.put("platformId", dto.getPlatformId());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                LogHelper.e(TAG, "buildRecommendGameJson exception");
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    /**
     * 解析推荐游戏列表json数据
     * 
     * @param jsonStr
     * @return
     */
    public static List<RecommendGameDTO> getRecommendGameList(String jsonStr) {
        if (jsonStr == null) {
            LogHelper.e(TAG, "getRecommendGameList failure, because jsonStr is null");
            return null;
        }
        LogHelper.d(TAG, "recommend game json str = " + jsonStr);
        List<RecommendGameDTO> gameList = new ArrayList<RecommendGameDTO>();
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            int length = jsonArray.length();
            RecommendGameDTO game;
            JSONObject jsonObject;
            for (int i = 0; i < length; i++) {
                game = new RecommendGameDTO();
                jsonObject = jsonArray.getJSONObject(i);
                game.setBigIcon(jsonObject.optString("gameIconUrl"));
                game.setGameId(jsonObject.optInt("gameId"));
                game.setGameName(jsonObject.optString("gameName"));
                game.setPlatformId(jsonObject.optInt("platformId"));
                gameList.add(game);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gameList;
    }

    /**
     * 将推荐广告列表存储为json
     * 
     * @param bannerList
     * @return
     */
    public static String buildRecommendAdJson(List<HomeBannerDTO> bannerList) {
        if (bannerList == null) {
            LogHelper.e(TAG, "buildRecommendAdJson failure because bannerList is null");
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (HomeBannerDTO dto : bannerList) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("point", dto.getPoint());
                jsonObject.put("adUrl", dto.getUrl());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                LogHelper.e(TAG, "buildRecommendAdJson exception");
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    /**
     * 将json格式的推荐广告转成list
     * 
     * @param jsonStr
     * @return
     */
    public static List<HomeBannerDTO> getRecommendAdList(String jsonStr) {
        if (jsonStr == null) {
            LogHelper.e(TAG, "getRecommendAdList failure, because jsonStr is null");
            return null;
        }
        LogHelper.d(TAG, "recommend ad json str = " + jsonStr);
        List<HomeBannerDTO> bannerList = new ArrayList<HomeBannerDTO>();
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            int length = jsonArray.length();
            HomeBannerDTO banner;
            JSONObject jsonObject;
            for (int i = 0; i < length; i++) {
                banner = new HomeBannerDTO();
                jsonObject = jsonArray.getJSONObject(i);
                banner.setPoint(jsonObject.optString("point"));
                banner.setUrl(jsonObject.optString("adUrl"));
                bannerList.add(banner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bannerList;
    }

    /**
     * 转换推荐广告point的json为bean对象
     * 
     * @param json
     * @return
     */
    public static RecommendAdPoint convertJsonToRecommendAdPoint(String json) {
        if (json == null) {
            LogHelper.e(TAG, "convertJsonToRecommendAdPoint failure, because json is null");
            return null;
        }
        LogHelper.d(TAG, "Recommend ad json = " + json);
        RecommendAdPoint point = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int flag = jsonObject.optInt("flag");// 标识调整网页还是应用内部
            int platformId = jsonObject.optInt("platformId");
            int gameId = jsonObject.optInt("gameId");
            String url = jsonObject.optString("url");
            String gameName = jsonObject.optString("gameName");
            point = new RecommendAdPoint(flag, platformId, gameId, gameName, url);
            LogHelper.d(TAG, "RecommendAdPoint=" + point.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return point;
    }
}
