package com.gerenvip.banner.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.cy.mm.facade.trade.game.dto.RecommendGameDTO;
import com.cy.mm.facade.trade.homepage.dto.HomeBannerDTO;
import com.gerenvip.banner.utils.LogHelper;
import com.gerenvip.banner.utils.RecommendJsonUtils;

import java.util.List;

/**
 * 保存系统设置信息
 * 
 * @author wangwei_cs
 */
public class SettingsMgr {

    private static final String PREFS_FILE = Constants.PREFS_NAME_SETTING_CONFIG;

    private static final String PREFS_KEY_NEW_MSG_NOTIFY = "msg_notify";// 新消息通知开关
    private static final String PREFS_KEY_SOUND_SWITCH = "sound_switch";// 声音是否开启开关
    private static final String PREFS_KEY_VIBRATION_SWITCH = "vibration_switch";// 震动是否开启开关
    private static final String PREFS_KEY_LASTSEARCH_GAME = "last_search_game";// 上次搜索的游戏
    private static final String PREFS_KEY_WIZARD_VERSION = "wizard_version";
    private static final String KEY_HAS_CLICKED_START_EXPERIENCE = "has_clicked_start_experience";// 点击过开始体验按钮
    private static final String KEY_HAS_CHECK_USER_EXP = "has_check_user_exp";// 勾选用户体验
    private static final String KEY_LAST_AD_IMG_HASH = "last_version_adimg_hash";// 首页广告图片链接组合hash值
    private static final String KEY_LAST_RECOMMEND_GAME_IMG_HASH = "last_version_game_hash";// 首页推荐游戏图片url组合hash值
    private static final String KEY_LAST_AD_URLS_SET = "last_ad_urls";// 上一次广告的url
    private static final String KEY_LAST_RECOMMEND_GAMES_INFO = "last_recommend_games_info";// 上一次更新的推荐游戏列表
    private static final String KEY_LAST_RECOMMEND_AD_INFO = "last_recommend_ad_info";//上一次更新的广告列表
    private static final String KEY_SWITCH_AUTO_CHECK_UPDATE = "auto_check";//自动检测更新开关

    private static final String TAG = "SettingsMgr";

    private static SharedPreferences sPrefs = null;

    private static SharedPreferences initSharedPreferences(Context cxt) {
        if (sPrefs == null) {
            sPrefs = cxt.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        }
        return sPrefs;
    }

    /**
     * 设置新消息是否通知
     * 
     * @param cxt
     * @param enabled
     */
    public static void setNewMsgNotify(Context cxt, boolean enabled) {
        SharedPreferences sp = initSharedPreferences(cxt);
        //SharedPreferencesUtils.apply(sp.edit().putBoolean(PREFS_KEY_NEW_MSG_NOTIFY, enabled));
        sp.edit().putBoolean(PREFS_KEY_NEW_MSG_NOTIFY, enabled);
        sp.edit().commit();
    }

    /**
     * 默认新消息开启提醒
     * 
     * @param cxt
     * @return
     */
    public static boolean getNewMsgnotify(Context cxt) {
        SharedPreferences sp = initSharedPreferences(cxt);
        return sp.getBoolean(PREFS_KEY_NEW_MSG_NOTIFY, true);
    }

    /**
     * 设置声音状态
     * 
     * @param cxt
     * @param enabled
     */
    public static void setSoundStatus(Context cxt, boolean enabled) {
        SharedPreferences sp = initSharedPreferences(cxt);
        //SharedPreferencesUtils.apply(sp.edit().clear().putBoolean(PREFS_KEY_SOUND_SWITCH, enabled));
        sp.edit().clear().putBoolean(PREFS_KEY_SOUND_SWITCH, enabled);
        sp.edit().commit();
    }

    public static boolean getSoundStatus(Context cxt) {
        SharedPreferences sp = initSharedPreferences(cxt);
        return sp.getBoolean(PREFS_KEY_SOUND_SWITCH, true);
    }

    /**
     * 设置震动状态
     * 
     * @param cxt
     * @param enabled
     */
    public static void setVibrationStatus(Context cxt, boolean enabled) {
        SharedPreferences sp = initSharedPreferences(cxt);
        //SharedPreferencesUtils.apply(sp.edit().putBoolean(PREFS_KEY_VIBRATION_SWITCH, enabled));
        sp.edit().putBoolean(PREFS_KEY_VIBRATION_SWITCH, enabled).commit();
    }

    public static boolean getVibrationStatus(Context cxt) {
        SharedPreferences sp = initSharedPreferences(cxt);
        return sp.getBoolean(PREFS_KEY_VIBRATION_SWITCH, true);
    }

    /**
     * 保存上次搜索的游戏名称
     * 
     * @param lastGame
     */
    public static void setLastSearchGame(Context cxt, String lastGame) {
        SharedPreferences sp = initSharedPreferences(cxt);
        //SharedPreferencesUtils.apply(sp.edit().putString(PREFS_KEY_LASTSEARCH_GAME, lastGame));
        sp.edit().putString(PREFS_KEY_LASTSEARCH_GAME, lastGame).commit();
    }

    public static String getLastSearchGame(Context cxt, String defValue) {
        SharedPreferences sp = initSharedPreferences(cxt);
        return sp.getString(PREFS_KEY_LASTSEARCH_GAME, defValue);
    }

    public static boolean getHasCheckUserExperience(Context context) {
        SharedPreferences sp = initSharedPreferences(context);
        return sp.getBoolean(KEY_HAS_CHECK_USER_EXP, true);
    }

    /**
     * 存储主页广告图片url组合hash值
     * 
     * @param context
     * @param hashCode
     */
    public static void setLastAdImageHash(Context context, int hashCode) {
        SharedPreferences sp = initSharedPreferences(context);
        //SharedPreferencesUtils.apply(sp.edit().putInt(KEY_LAST_AD_IMG_HASH, hashCode));
        sp.edit().putInt(KEY_LAST_AD_IMG_HASH, hashCode).commit();
    }

    /**
     * 获取上一次广告图片url的hash值
     * 
     * @param context
     * @return
     */
    public static int getLastAdImgHash(Context context) {
        SharedPreferences sp = initSharedPreferences(context);
        return sp.getInt(KEY_LAST_AD_IMG_HASH, 0);
    }

    /**
     * 存储主页推荐游戏图标url的组合hash值
     * 
     * @param context
     * @param hashCode
     */
    public static void setLastRecommendGameImgHash(Context context, int hashCode) {
        SharedPreferences sp = initSharedPreferences(context);
        //SharedPreferencesUtils.apply(sp.edit().putInt(KEY_LAST_RECOMMEND_GAME_IMG_HASH, hashCode));
        sp.edit().putInt(KEY_LAST_RECOMMEND_GAME_IMG_HASH, hashCode).commit();
    }

    /**
     * 获取上一次推荐游戏icon的url组合hash值
     * 
     * @param context
     * @return
     */
    public static int getLastRecommendGameImgHash(Context context) {
        SharedPreferences sp = initSharedPreferences(context);
        return sp.getInt(KEY_LAST_RECOMMEND_GAME_IMG_HASH, 0);
    }

    /**
     * 获取上次更新的广告url地址
     * 
     * @param context
     * @return
     */
    public static List<String> getLastAdUrls(Context context) {
        List<String> urls = null;
        SharedPreferences sp = initSharedPreferences(context);
        String urlsJson = sp.getString(KEY_LAST_AD_URLS_SET, null);
        if (urlsJson !=null) {
            urls = RecommendJsonUtils.parseAdRecommendUrls(urlsJson);
        }
        return urls;
    }

    /**
     * 保存上一次更新的广告url连接
     * 
     * @param context
     * @param urls
     */
    public static void setLastAdUrls(Context context, List<String> urls) {
        if (urls != null) {
            SharedPreferences sp = initSharedPreferences(context);
            String urlsJson = RecommendJsonUtils.buildAdUrlsJson(urls);
            if (urlsJson != null) {
                //SharedPreferencesUtils.apply(sp.edit().putString(KEY_LAST_AD_URLS_SET, urlsJson));
                sp.edit().putString(KEY_LAST_AD_URLS_SET, urlsJson).commit();
            } else {
                LogHelper.e(TAG, "urls is not null, but create jsonstr exception");
            }
        } else {
            LogHelper.e(TAG, "urls is null");
        }
    }

    /**
     * 保存上次更新的游戏列表
     * 
     * @param context
     * @param gameList
     */
    public static void setLastRecommendGameInfo(Context context, List<RecommendGameDTO> gameList) {
        if (gameList != null) {
            SharedPreferences sp = initSharedPreferences(context);
            String gameJson = RecommendJsonUtils.buildRecommendGameJson(gameList);
            if (gameJson != null) {
                LogHelper.d(TAG, "setLastRecommendGameInfo gameJson:"+gameJson);
                //SharedPreferencesUtils.apply(sp.edit().putString(KEY_LAST_RECOMMEND_GAMES_INFO, gameJson));
                sp.edit().putString(KEY_LAST_RECOMMEND_GAMES_INFO, gameJson).commit();
            } else {
                LogHelper.e(TAG, "gameList is not null, but create gameJson exception");
            }
        }
    }

    /**
     * 获取上次保存的游戏列表
     * @return
     */
    public static List<RecommendGameDTO> getLasetRecommendGameList(Context context) {
        List<RecommendGameDTO> gameList = null;
        SharedPreferences sp = initSharedPreferences(context);
        String gameJson = sp.getString(KEY_LAST_RECOMMEND_GAMES_INFO, null);
        if (gameJson != null) {
            LogHelper.d(TAG, "getLasetRecommendGameList gameJson="+gameJson);
            gameList = RecommendJsonUtils.getRecommendGameList(gameJson);
        }
        return gameList;
    }

    public static void setLastRecommendAdList(Context context, List<HomeBannerDTO> bannerList) {
        if (bannerList != null) {
            SharedPreferences sp = initSharedPreferences(context);
            String recommendAdJson = RecommendJsonUtils.buildRecommendAdJson(bannerList);
            if (recommendAdJson != null) {
                LogHelper.d(TAG, "setLastRecommendAdList recommendAdJson:" + recommendAdJson);
                /*SharedPreferencesUtils
                        .apply(sp.edit().putString(KEY_LAST_RECOMMEND_AD_INFO, recommendAdJson));*/
                sp.edit().putString(KEY_LAST_RECOMMEND_AD_INFO, recommendAdJson).commit();
            } else {
                LogHelper.e(TAG, "bannerList is not null, but create jsonstr exception");
            }
        }
    }

    public static List<HomeBannerDTO> getLastRecommendAdList(Context context) {
        List<HomeBannerDTO> bannerList = null;
        SharedPreferences sp = initSharedPreferences(context);
        String bannerJson = sp.getString(KEY_LAST_RECOMMEND_AD_INFO, null);
        if (bannerJson != null) {
            LogHelper.d(TAG, "getLastRecommendAdList bannerJson=" + bannerJson);
            bannerList = RecommendJsonUtils.getRecommendAdList(bannerJson);
        }
        return bannerList;
    }

    /**
     * 设置自动检测更新开关
     * @param context
     * @param value
     */
    public static void setSwitchOfAutoCheckUpdate(Context context, boolean value) {
        SharedPreferences sp = initSharedPreferences(context);
        //SharedPreferencesUtils.apply(sp.edit().putBoolean(KEY_SWITCH_AUTO_CHECK_UPDATE, value));
        sp.edit().putBoolean(KEY_SWITCH_AUTO_CHECK_UPDATE, value).commit();
    }

    /**
     * 查询自动检测更新状态
     * @param context
     * @return
     */
    public static boolean getStateOfAutoCheckUpdate(Context context) {
        SharedPreferences sp = initSharedPreferences(context);
        return sp.getBoolean(KEY_SWITCH_AUTO_CHECK_UPDATE, true);
    }
}
