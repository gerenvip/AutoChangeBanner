package com.gerenvip.banner.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * Created by wangwei_cs on 2014/5/23.
 */
public abstract class NoLeakHandler<T> extends Handler {

    private static Class<?> sFragmentClass;
    private static Method sGetActivity;
    private final WeakReference<T> mContext;

    static {
        try {
            sFragmentClass = Class.forName("android.support.v4.app.Fragment", false,
                    Thread.currentThread().getContextClassLoader());
            sGetActivity = sFragmentClass.getDeclaredMethod("getActivity", null);
        } catch (Exception e) {
            e.printStackTrace();
            sFragmentClass = null;
            sGetActivity = null;
        }
    }

    public NoLeakHandler(T context) {
        this.mContext = new WeakReference(context);
    }

    public void handleMessage(Message msg) {
        T context = this.mContext.get();
        if (context != null) {
            Activity activity = null;
            if ((sFragmentClass != null) && (sFragmentClass.isInstance(context))) {
                try {
                    activity = (Activity) sGetActivity.invoke(context, null);
                } catch (Exception localException) {
                }
            } else if ((context instanceof Activity)) {
                activity = (Activity) context;
            }
            if ((activity == null) || ((activity != null) && (activity.isFinishing()))) {
                return;
            }
            processMessage(context, msg);
        }
    }

    protected abstract void processMessage(T paramT, Message paramMessage);
}
