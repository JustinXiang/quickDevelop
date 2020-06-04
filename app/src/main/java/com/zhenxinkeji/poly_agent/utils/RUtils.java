package com.zhenxinkeji.poly_agent.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.zhenxinkeji.poly_agent.App;

/**
 * @describe 资源相关工具类
 */
public class RUtils {

    public static Resources getResources() {
        return App.mInstance.getResources();
    }

    /**
     * 获取字符串
     */
    public static String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取颜色
     *
     * @param resId 资源ID
     */
    public static int getColor(@ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getResources().getColor(resId, App.mInstance.getTheme());
        }
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色
     *
     * @param context 上下文
     * @param resId   资源ID
     */
    public static ColorStateList getColorStateList(Context context, @ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getResources().getColorStateList(resId, context.getTheme());
        }
        return getResources().getColorStateList(resId);
    }

    /**
     * 获取Drawable
     *
     * @param resId 资源ID
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getResources().getDrawable(resId, App.mInstance.getTheme());
        }
        return getResources().getDrawable(resId);
    }

    /**
     * 获取尺寸资源
     *
     * @param resId 资源ID
     */
    public static float getDimen(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }
}
