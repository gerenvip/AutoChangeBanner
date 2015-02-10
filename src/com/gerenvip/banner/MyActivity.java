package com.gerenvip.banner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.cy.mm.facade.trade.homepage.dto.HomeBannerDTO;
import com.cy.mm.facade.trade.homepage.dto.HomepageDTO;
import com.gerenvip.banner.config.Constants;
import com.gerenvip.banner.config.SettingsMgr;
import com.gerenvip.banner.domain.RecommendAdPoint;
import com.gerenvip.banner.engine.AsyncHttpClientListener;
import com.gerenvip.banner.engine.HomeResourceEngineImpl;
import com.gerenvip.banner.engine.IHomeResourceEngine;
import com.gerenvip.banner.ui.HorizontalViewPager;
import com.gerenvip.banner.ui.RecommendAdComponent;
import com.gerenvip.banner.utils.*;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {

    private static final String TAG = MyActivity.class.getSimpleName();
    private LinearLayout mIndicatorLayout;
    private int width, height;
    private Context mCxt;
    private List<String> imageUrls = new ArrayList<String>();
    private RecommendAdComponent mAdComponent;
    private List<HomeBannerDTO> mBannerList = new ArrayList<HomeBannerDTO>();
    private static boolean isRequesting = false;
    private static final int MSG_REQUEST_SUCCESS = 1;// 请求成功
    private static final int MSG_REQUEST_FAILURE = 2;// 请求失败
    private static final int MSG_REQUEST_FINISH = 3;// 请求完成
    private IHomeResourceEngine<HomepageDTO> mEngine;

    private static class MyHandler extends NoLeakHandler<MyActivity> {

        public MyHandler(MyActivity context) {
            super(context);
        }

        @Override
        protected void processMessage(MyActivity context, Message msg) {
            context.processMessage(msg);
        }
    }

    private void processMessage(Message msg) {
        if (isFinishing()) return;
        switch (msg.what) {
            case MSG_REQUEST_SUCCESS:
                HomepageDTO data = (HomepageDTO) msg.obj;
                handleDataResoure(data);
                break;
            case MSG_REQUEST_FAILURE:
                LogHelper.d(TAG, "request failure");

                break;
            case MSG_REQUEST_FINISH:
                isRequesting = false;
                break;
        }
    }

    private Handler mHandle = new MyHandler(this);

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mCxt = this;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        initImageLoader(this);
        initView();
    }

    private void initView() {
        initRecommendAd();
        //requestHomeResourcesFromServer();
        createDtoFromLOcal();
    }

    /**
     * 初始化推荐广告专区
     */
    private void initRecommendAd() {
        HorizontalViewPager mViewPager = (HorizontalViewPager) findViewById(R.id.viewpager);
        mIndicatorLayout = (LinearLayout) findViewById(R.id.ll_indicator);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT, height * 24 / 100);
        mViewPager.setLayoutParams(params);
        AdImagePagerAdapter mPagerAdapter = new AdImagePagerAdapter(mCxt, imageUrls, new AdImagePagerAdapter.ViewPagerItemClickListener() {

            @Override
            public void OnViewPagerItemClick(int position) {
                if (imageUrls.size() > 0) {
                    if (imageUrls.size() == 1) {
                        boolean enable = urlEnable(imageUrls.get(0));
                        if (enable) {
                            handleClickEvent(position);
                        }
                    } else {
                        handleClickEvent(position);
                    }
                }
            }
        });
        List<String> lastAdUrls = getLastAdUrls();
        if (lastAdUrls == null || lastAdUrls.size() == 0) {
            // 没有记录上一次广告url信息,添加一个无效url路径
            imageUrls.add(Constants.INVALID_URL);
            LogHelper.e(TAG, "no last ad record");
        } else {
            LogHelper.e(TAG, "show last ad record");
            imageUrls.addAll(lastAdUrls);
        }
        mAdComponent = new RecommendAdComponent(mCxt, mViewPager, mPagerAdapter, imageUrls, 5000,
                mIndicatorLayout);
        mAdComponent.startUpAdComponent();
        mPagerAdapter.notifyDataSetChanged();
    }

    private List<String> getLastAdUrls() {
        List<HomeBannerDTO> lastRecommendAdList = SettingsMgr.getLastRecommendAdList(mCxt);
        if (lastRecommendAdList != null && lastRecommendAdList.size() > 0) {
            mBannerList.clear();
            mBannerList.addAll(lastRecommendAdList);
            List<String> adUrls = new ArrayList<String>();
            for (HomeBannerDTO banner : lastRecommendAdList) {
                adUrls.add(banner.getUrl());
            }
            return adUrls;
        }
        return null;
    }

    private boolean urlEnable(String url) {
        return !Constants.INVALID_URL.equals(url);
    }

    /**
     * 处理广告图的点击事件
     *
     * @param position 点击的条目
     */
    private void handleClickEvent(int position) {
        int size = mBannerList.size();
        if (size == 0 || position >= size) {
            return;
        }
        HomeBannerDTO banner = mBannerList.get(position);
        String point = banner.getPoint();
        //String str = "{\"flag\": 1,\"platformId\": 1,\"gameId\": 1,"gameName":"武侠OL",\"url\": \"http://www.baidu.com\"}";
        RecommendAdPoint adPoint = RecommendJsonUtils.convertJsonToRecommendAdPoint(point);
        if (adPoint == null) {
            return;
        }
        if (adPoint.getFlag() == 1) {
            //跳网页
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adPoint.getUrl()));
            startActivity(intent);
        } else if (adPoint.getFlag() == 0) {
            //跳应用内部
            Toast.makeText(mCxt, "跳转内部页面", Toast.LENGTH_LONG).show();
        } else if (adPoint.getFlag() == 2) {
            //跳帮助页面
            Intent i = new Intent(mCxt, TestActivity.class);
            startActivity(i);
        }
    }

    /**
     * 去服务器拉取主页信息
     */
    private void requestHomeResourcesFromServer() {
        if (!NetWorkUtil.isNetworkAvaialble(mCxt)) {
            mHandle.obtainMessage(MSG_REQUEST_FAILURE).sendToTarget();
        } else {
            realRequestData();
        }
    }

    private void realRequestData() {
        if (mEngine == null) {
            mEngine = new HomeResourceEngineImpl<HomepageDTO>(mCxt, HomepageDTO.class);
        }
        isRequesting = true;
        mEngine.getHomeResource(new AsyncHttpClientListener<HomepageDTO>() {
            @Override
            public void onAsyncHttpClientSuccess(HomepageDTO data) {
                if (data != null && data.getErrorCode() == 200) {
                    Message msg = mHandle.obtainMessage(MSG_REQUEST_SUCCESS);
                    msg.obj = data;
                    mHandle.sendMessage(msg);
                } else {
                    mHandle.obtainMessage(MSG_REQUEST_FAILURE).sendToTarget();
                }
            }

            @Override
            public void onAsyncHttpClientStart() {
            }

            @Override
            public void onAsyncHttpClientFinish() {
                mHandle.obtainMessage(MSG_REQUEST_FINISH).sendToTarget();
            }

            @Override
            public void onAsyncHttpClientFailure(String content) {
                mHandle.obtainMessage(MSG_REQUEST_FAILURE).sendToTarget();
            }
        });
    }

    protected void handleDataResoure(HomepageDTO data) {
        List<HomeBannerDTO> bannerList = data.getHomeBannerDTOs();
        LogHelper.d(TAG,
                "bannerList is null=" + (bannerList == null) + ";;bannerList size=" + bannerList.size());
        //handleBanners(bannerList);

        //TODO 以上是从网络拉起的数据

        //本地生成HomepageDTO


    }

    private void createDtoFromLOcal() {
        List<HomeBannerDTO> list = createData();
        handleBanners(list);
    }

    private List<HomeBannerDTO> createData() {
        ArrayList<HomeBannerDTO> list = new ArrayList<HomeBannerDTO>();
        for (int i = 0; i < 3; i++) {
            HomeBannerDTO dto = new HomeBannerDTO();

            JSONObject obj = new JSONObject();
            String str = null;
            try {

                obj.put("platformId", 1);
                obj.put("gameId", 1);
                obj.put("gameName", "111");
                if (i % 2 == 0) {
                    dto.setUrl(Constants.url1);
                    obj.put("flag", 1);
                    obj.put("url", "http://iuu.changyou.com");
                } else {
                    dto.setUrl(Constants.url2);
                    obj.put("flag", 2);
                    obj.put("url", "http://iuu.changyou.com");
                }

                str = obj.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (str != null) {
                dto.setPoint(str);
            }
            list.add(dto);
        }
        return list;
    }

    /**
     * 处理推荐广告数据
     *
     * @param list
     */
    private void handleBanners(List<HomeBannerDTO> list) {
        if (list != null && list.size() > 0) {
            String str = "";
            List<String> urls = new ArrayList<String>();
            for (HomeBannerDTO banner : list) {
                urls.add(banner.getUrl());

                str += ImageUtils.convertUrlToFilename(banner.getUrl());
            }
            int hashCode = str.hashCode();
            LogHelper.d(TAG, "new url hashcode:" + hashCode);
            int lastHashCode = SettingsMgr.getLastAdImgHash(mCxt);
            LogHelper.d(TAG, "last url hashcode:" + lastHashCode + "; new == old:"
                    + (hashCode == lastHashCode));
            if (hashCode == lastHashCode) {
                // 说明没有更新推荐广告
                LogHelper.d(TAG, "no new ad in server");
            } else {// 服务器有新版本广告推荐
                // 保存hash值
                SettingsMgr.setLastAdImageHash(mCxt, hashCode);
                // 更新viewpager中list集合
                updateAdSources(list);
            }
        }
    }

    private void updateAdSources(List<HomeBannerDTO> list) {
        imageUrls.clear();
        for (HomeBannerDTO dto : list) {
            imageUrls.add(dto.getUrl());
        }
        mBannerList.clear();
        mBannerList.addAll(list);
        //更新banner组件
        mAdComponent.updateAdComponent();
        LogHelper.d(TAG, "updateAdComponent .....");
        SettingsMgr.setLastRecommendAdList(mCxt, mBannerList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogHelper.e("HomeActivity", "HomeActivity ondestory");
        if (mAdComponent != null) {
            mAdComponent.stopAdComponent();
            mAdComponent = null;
        }
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
