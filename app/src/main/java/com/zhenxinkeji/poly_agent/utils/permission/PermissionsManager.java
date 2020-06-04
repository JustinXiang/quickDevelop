package com.zhenxinkeji.poly_agent.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;

import androidx.annotation.NonNull;

/**
 * @Description: 用于请求权限和 放置功能需求权限的管理类  可以在Activity 和 Fragment 中使用
 */
public class PermissionsManager {


    public static final String PERMISSIONS_PROMPT = "使用功能，请您先允许申请的权限";
    public static final String PERMISSIONS_CALL = "拨打电话,需要同意权限";
    public static final String DOWNLOAD_HINT = "下载图片,需要同意权限";
    public static final String START_HINT = "使用APP,请同意申请权限";
    public static final String NEVERASK_PROMPT = "权限已被禁止申请，请前往设置中心手动打开";
    public static final String PERMISSIONS_DENIED = "权限获取失败,使用APP请同意权限"; //获取权限相关

    public static final int SPLASH =1; //欢迎页面权限申请



    //获取手机通讯录
    public static final String[] READ_CONTACTS = {Manifest.permission.READ_CONTACTS};
    //拨打电话
    public static final String[] CALL_PHONE = {Manifest.permission.CALL_PHONE};
    //读写内存
    public static final String[] READ_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};



    /**
     * 传入Activity 和请求的权限数组
     *
     * @param activity
     */
    public static void applyPermissions(@NonNull Activity activity, @NonNull int code) {
        if (code == SPLASH){
            PermissionsUtils.requestPerssions(activity, START_HINT, READ_STORAGE);
        }


    }

    /**
     * 传入Activity 和请求的权限数组 和请求码 可以在回调中获得
     *
     * @param activity
     * @param requestCode
     */
    public static void applyPermissions(@NonNull Activity activity, @NonNull int code, int requestCode) {
        if (code == SPLASH){
            PermissionsUtils.requestPerssions(activity, START_HINT, requestCode,READ_STORAGE);
        }

    }

    /**
     * 传入Activity 和请求的权限数组
     *
     * @param fragment
     * @param permissions
     */
    public static void applyPermissions(@NonNull androidx.fragment.app.Fragment fragment, @NonNull String rationale, @NonNull String... permissions) {
        PermissionsUtils.requestPerssions(fragment, rationale, permissions);
    }

    /**
     * 传入Activity 和请求的权限数组 和请求码 可以在回调中获得
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    public static void applyPermissions(@NonNull androidx.fragment.app.Fragment fragment, @NonNull String rationale, int requestCode, @NonNull String... permissions) {
        PermissionsUtils.requestPerssions(fragment, rationale, requestCode, permissions);
    }

    /**
     * 传入Activity 和请求的权限数组
     *
     * @param fragment
     * @param permissions
     */
    public static void applyPermissions(@NonNull Fragment fragment, @NonNull String rationale, @NonNull String... permissions) {
        PermissionsUtils.requestPerssions(fragment, rationale, permissions);
    }

    /**
     * 传入Activity 和请求的权限数组 和请求码 可以在回调中获得
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    public static void applyPermissions(@NonNull Fragment fragment, @NonNull String rationale, int requestCode, @NonNull String... permissions) {
        PermissionsUtils.requestPerssions(fragment, rationale, requestCode, permissions);
    }

    /**
     * 接口回调权限请求结果并且回调不同函数
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param callBack
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults, @NonNull PermissionsUtils.OnRequestPermissionsResultCallbacks callBack) {
        PermissionsUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, callBack);
    }
}
