package com.gerenvip.banner.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * 复写该控件是因为在PullToRefreshScrollView中，viewpager无法水平滑动
 *
 * @author wangwei_cs
 */
public class HorizontalViewPager extends ViewPager {

    private GestureDetector mGestureDetector;

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalViewPager(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
        if (dispatchTouchEvent) {
            if (mGestureDetector.onTouchEvent(ev)) {
                //请求父类放弃事件的处理
                requestDisallowInterceptTouchEvent(true);
            }
        }
        return dispatchTouchEvent;
    }

    class YScrollDetector extends SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            // 如果我们滚动更接近水平方向,返回true,自己处理，否则，让出处理权限
            return (Math.abs(distanceX) > Math.abs(distanceY));
        }
    }
}
