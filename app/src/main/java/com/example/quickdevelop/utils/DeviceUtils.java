package com.example.quickdevelop.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quickdevelop.base.Constant;
import com.xl.aggregationlive.utils.EmptyUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;

/**
 * The type Device utils.
 */
public class DeviceUtils {

    /**
     * 获取屏幕尺寸
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            return new Point(display.getWidth(), display.getHeight());
        } else {
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }

    /**
     * get build model
     *
     * @return
     */
    public static String getBuildModel() {
        String model = Build.MODEL.toLowerCase(Locale.getDefault());
        return model;
    }


    public static String getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
        }
    }

    /**
     * <pre>
     *  获取网络类型
     * </pre>
     *
     * @param context
     * @return
     */
    public static String getAndroidNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return "NETTYPE=" + info.getTypeName();
        } else {
            return "";
        }
    }

    /**
     * 检查网络（提示语 “网络异常,请检查手机网络"）
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        if (!DeviceUtils.checkNetEnv(context)) {
            Toast.makeText(context, "网络异常,请检查手机网络", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 检查网络(提示语 “无网络，不可操作”)
     *
     * @param context
     * @return
     */
    public static boolean checkNetWorkOnClick(Context context) {
        if (!DeviceUtils.checkNetEnv(context)) {
            Toast.makeText(context, "无网络，不可操作", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context mContext) {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    /**
     * 检查网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetEnv(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State mobile = null;
        NetworkInfo.State wifi = null;
        NetworkInfo mobileInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileInfo != null) {
            mobile = mobileInfo.getState();
        }
        NetworkInfo wifiInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null) {
            wifi = wifiInfo.getState();
        }
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            return true;
        }
        return wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING;
    }

    /**
     * 是否连接WiFi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isTopActivity(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断网络环境（wifi，2g，3g，4g）
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String strSubTypeName = networkInfo.getSubtypeName();

//                Log.e("cocos2d-x", "Network getSubtypeName : " + strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        return TYPE_2G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        return TYPE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        return TYPE_4G;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if ("TD-SCDMA".equalsIgnoreCase(strSubTypeName) || "WCDMA".equalsIgnoreCase(strSubTypeName)
                                || "CDMA2000".equalsIgnoreCase(strSubTypeName)) {
                            return TYPE_3G;
                        } else {
                            return TYPE_UNKNOW;
                        }
                }
            }
        }
        return TYPE_UNKNOW;
    }

    /**
     * 判断是WIFI网络还是移动数据网络
     *
     * @param context
     * @return
     */
    public static int getNetType(Context context) {
        int type = getNetworkType(context);
        switch (type) {
            case TYPE_WIFI:
                return TYPE_WIFI;
            default:
                return TYPE_MOBILE;
        }
    }

    public final static int TYPE_WIFI = 0;
    public final static int TYPE_MOBILE = 1;
    public final static int TYPE_2G = 2;
    public final static int TYPE_3G = 3;
    public final static int TYPE_4G = 4;
    public final static int TYPE_UNKNOW = -1;

    public static String getDeviceId(Context context) {
        String deviceID = (String) SPUtils.get(context, Constant.Companion.getSP_DEVICE_ID(), "");
        if (EmptyUtils.Companion.isNotEmpty(deviceID)) {
            return deviceID;
        }
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("d");
        try {
            // wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) {
                    String wifiMac = info.getMacAddress();
                    if (EmptyUtils.Companion.isNotEmpty(wifiMac)) {
                        deviceId.append("wifi");
                        deviceId.append(wifiMac);
                        SPUtils.put(context, Constant.Companion.getSP_DEVICE_ID(),
                                deviceId.toString());
                        return deviceId.toString();
                    }
                }
            } else {
            }

            // IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                @SuppressLint("MissingPermission") String imei = tm.getDeviceId();
                if (EmptyUtils.Companion.isNotEmpty(imei)) {
                    deviceId.append("imei");
                    deviceId.append(imei);
                    SPUtils.put(context, Constant.Companion.getSP_DEVICE_ID(), deviceId.toString());
                    return deviceId.toString();
                }
            }

            // 序列号（sn）
            if (tm != null) {
                @SuppressLint("MissingPermission") String sn = tm.getSimSerialNumber();
                if (EmptyUtils.Companion.isNotEmpty(sn)) {
                    deviceId.append("sn");
                    deviceId.append(sn);
                    SPUtils.put(context, Constant.Companion.getSP_DEVICE_ID(), deviceId.toString());
                    return deviceId.toString();
                }
            }

            // 如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (EmptyUtils.Companion.isNotEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }

        SPUtils.put(context, Constant.Companion.getSP_DEVICE_ID(), deviceId.toString());
        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        String uuid = (String) SPUtils.get(context, Constant.Companion.getSP_DEVICE_ID(), "");
        if (EmptyUtils.Companion.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            SPUtils.put(context, Constant.Companion.getSP_DEVICE_ID(), uuid);
        }
        return uuid;
    }

    public static boolean isInstallWechat(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.tencent.mm", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager systemService = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        systemService.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public static void opeSystemKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(activity.getCurrentFocus(),
                InputMethodManager.SHOW_FORCED);
    }

    //每次都会弹出
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //显示软键盘
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.showSoftInput(editText, 0);

    }

    //只弹出一次
    public static void showSoftInput(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //显示软键盘

    }
}
