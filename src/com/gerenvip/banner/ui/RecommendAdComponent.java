package com.gerenvip.banner.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gerenvip.banner.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 展示广告轮转的组件
 * 
 * @author wangwei_cs
 */
public class RecommendAdComponent {

    /**
     * 无效的图片切换时间，如果为0 表示不自动切换
     */
    public static final int SWITCH_TIME_INVALID = 0;

    private Context mCxt;

    // 图片url集合
    private List<String> mImgUrls;
    // 图片切换时间
    private int mSwitchTime;
    //自动滚动的定时器 
    private Timer mTimer;
    // 显示圆点指示器的布局
    private LinearLayout mIndicatorLayout;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private int currentIndex; // 当前页面,在0和getSize()直接
    private int pagerCurrent;//在viewpager中，的当前页面，取值在0和Integer.MAX_VALUE之间
    private boolean timeRunning;

    /**
     * @param context
     * @param viewpager viewPager组件
     * @param pagerAdapter
     * @param adUrls 装有图片url的集合
     * @param switchTime 图片切换时间(ms) {@link RecommendAdComponent#SWITCH_TIME_INVALID}：不自动切换
     * @param indicatorLayout 显示圆点指示器的布局
     */
    public RecommendAdComponent(Context context, ViewPager viewpager, PagerAdapter pagerAdapter, List<String> adUrls, int switchTime,
                                LinearLayout indicatorLayout) {
        this.mCxt = context;
        this.mViewPager = viewpager;
        this.mPagerAdapter = pagerAdapter;
        this.mImgUrls = adUrls;
        this.mSwitchTime = switchTime;
        this.mIndicatorLayout = indicatorLayout;
        initIndicatorLayout();
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
//        setViewpagerAnimator();
    }

    /**
     * 初始化指示器
     */
    private void initIndicatorLayout() {
        ImageView iv = null;
        if (mIndicatorLayout != null && getSize() < 2) {
            // 如果只有一第图时不显示圆点容器
            mIndicatorLayout.setVisibility(View.INVISIBLE);
        } else if (mIndicatorLayout != null) {
            mIndicatorLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < getSize(); i++) {
                iv = new ImageView(mCxt);
                iv.setTag(i);
                int padding = mCxt.getResources().getDimensionPixelSize(R.dimen.indicator_padding);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                params.setMargins(padding, 0, padding, 0);
                mIndicatorLayout.addView(iv, params);
            }
        }
    }

    private void resetIndicatorLayout() {
        mIndicatorLayout.removeAllViews();
        initIndicatorLayout();
    }

    /**
     * 给viewPager设置动画
     */
    private void setViewpagerAnimator(){
        if (mViewPager !=null) {
            PageTransformer pageTransformer=new PageTransformer() {
                
                @Override
                public void transformPage(View view, float arg1) {
//                    view.setAnimation(AnimationUtils.loadAnimation(mCxt, android.R.anim.slide_out_right));
                    view.setAnimation(AnimationUtils.loadAnimation(mCxt, R.anim.right_in));
                }
            };
            mViewPager.setPageTransformer(true, pageTransformer);
        }
    }

    /**
     * 获取图片集合的size
     * 
     * @return
     */
    private int getSize() {
        return (mImgUrls == null ? 0 : mImgUrls.size());
    }

    public boolean isEmpty() {
        return (mImgUrls == null);
    }

    /**
     * 开启组件
     */
    public void startUpAdComponent() {
        currentIndex = 0;
        pagerCurrent = 0;
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentIndex);
        updateIndicator(currentIndex);
        startTimer();
    }

    /**
     * 更新组件中adapter数据
     */
    public void updateAdComponent() {
        stopTimer();
        resetIndicatorLayout();
        mPagerAdapter.notifyDataSetChanged();
        startUpAdComponent();
    }

    /**
     * 在页面销毁的时候调用该方法
     */
    public void stopAdComponent() {
        stopTimer();
    }

    /**
     * 停止自动滚动的任务
     */
    public void stopTimer() {
        timeRunning = false;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 开始自动滚动的任务，注意，只有图片个数大于1的时候才会自动滚动
     */
    public void startTimer() {
        timeRunning = true;
        if (mTimer == null && getSize() > 1 && mSwitchTime > 0) {
            mTimer = new Timer();
            mTimer.schedule(new PagerTimerTask(), mSwitchTime, mSwitchTime);
        }
    }

    private class PagerTimerTask extends TimerTask {

        @Override
        public void run() {
            currentIndex++;
            pagerCurrent++;
            mHandler.sendEmptyMessage(0);
        }
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            pagerCurrent = position;
            currentIndex = position % getSize();// 更新当前页面
            updateIndicator(currentIndex);
        }
    }

    /**
     * 更新圆点指示器
     */
    private void updateIndicator(int position) {
        if (!isEmpty() && position < getSize()) {
            if (getSize() > 1) {
                resetAllIndicator(getSize());// 重置所有的指示器为为选择状态
                View v = mIndicatorLayout.findViewWithTag(position);
                if (v != null) {
                    v.setBackgroundResource(R.drawable.circle_indicator_selected);// 点亮
                }
            }
        }
    }

    /**
     * 重置所有的指示器
     */
    private void resetAllIndicator(int size) {
        if (mIndicatorLayout != null) {
            for (int i = 0; i < size; i++) {
                View v = mIndicatorLayout.findViewWithTag(i);
                if (v != null) {
                    v.setBackgroundResource(R.drawable.circle_indicator_normal);
                }
            }
        }
    }
    
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mViewPager.setCurrentItem(pagerCurrent);
        };
    };
}
