/* Project: KomectSport
 *
 * File Created at 2016/11/14
 *
 * Copyright 2016 CMCC Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZYHY Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license.
 */
package com.zhenxinkeji.poly_agent.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.zhenxinkeji.poly_agent.App;
import java.lang.reflect.Method;

public class DensityUtils {
    private DensityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * Dp 2 px int.
     *
     * @param dpVal the dp val
     * @return the int
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(1, dpVal,
                App.mInstance.getResources().getDisplayMetrics());
    }

    /**
     * Sp 2 px int.
     *
     * @param spVal the sp val
     * @return the int
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(2, spVal,
                App.mInstance.getResources().getDisplayMetrics());
    }

    /**
     * Px 2 dp float.
     *
     * @param pxVal the px val
     * @return the float
     */
    public static float px2dp(float pxVal) {
        float scale = App.mInstance.getResources().getDisplayMetrics().density;
        return pxVal / scale;
    }

    /**
     * Px 2 sp float.
     *
     * @param pxVal the px val
     * @return the float
     */
    public static float px2sp(float pxVal) {
        return pxVal / App.mInstance.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * dp 转sp
     *
     * @param dpVal
     * @return
     */
    public static int dp2sp(float dpVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                App.mInstance.getResources().getDisplayMetrics()));
    }

    /**
     * sp 转dp
     *
     * @param spVal
     * @return
     */
    public static int sp2dp(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                App.mInstance.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕宽度
     *
     * @return screen width
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getScreenWidth() {
        DisplayMetrics dm = App.mInstance.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return screen width
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getScreenHeight() {
        DisplayMetrics dm = App.mInstance.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取状态栏高度——方法
     */
    public static int getStatusBarHeight() {

        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = App.mInstance.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = App.mInstance.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    /**
     * 获取View 弹出pop布局高度  适配全面屏
     */
    public static int getPopLayoutHeight(View view) {
        Rect visibleFrame = new Rect();
        view.getGlobalVisibleRect(visibleFrame);
        int height = view.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
//        if (hasNavigationBar(App.mInstance)) { //如果虚拟键存在适配
//            height += getNavigationBarHeight();
//        }
        return height;
    }

    //获取虚拟按键的高度
    public static int getNavigationBarHeight() {
        int vh = 0;
        WindowManager windowManager = (WindowManager) App.mInstance.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    //判断是否存在NavigationBar
    public static boolean hasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            //反射获取SystemProperties类，并调用它的get方法
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }
}
