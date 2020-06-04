package com.zhenxinkeji.poly_agent.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import com.zhenxinkeji.poly_agent.R;
import com.zhenxinkeji.poly_agent.dialog.CustomProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;


public abstract class BaseActivity extends RxAppCompatActivity {
    public BaseActivity mActivity;

    //是否需要注册EventBus
    private boolean hasEvnet = false;
    private boolean systemUiBlack = true;
    public boolean isPush = true;


    public void setSystemUiBlack(boolean systemUiBlack) {
        this.systemUiBlack = systemUiBlack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        onPrepare();
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示 系统 title
//        if (mAllowFullScreen) {
        fullScreen();
        AppManager.getAppManager().addActivity(this);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
//        //设置状态栏的颜色
//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white), true);
        mActivity = this;
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                initParms(bundle);
            }
            //设置状态栏文字颜色及图标为深色
            if (systemUiBlack) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            setContentView(getResId());
            initViewAndEvent();
            if (hasEvnet) {
                EventBus.getDefault().register(this);
            }
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public <T extends View> T findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null)
            return (T) v.findViewById(id);
        return (T) v;
    }


    /**
     * 初始化界面之前
     *
     * @param
     */
    public void onPrepare() {

    }

    /**
     * [初始化Bundle参数]
     *
     * @param parms
     */
    public void initParms(Bundle parms) {
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    public abstract int getResId();

    /**
     * 初始化控件
     */
    public abstract void initViewAndEvent();

    /**
     * 获取数据
     */
    public abstract void initData();


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 重写getResources()方法，让APP的字体不受系统  设置字体大小影响
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    CustomProgressDialog progressDialog;

    // 显示默认 dialog
    public void showLaodingDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = CustomProgressDialog.createDialog(this);
//                progressDialog.setMessage(text);
            }
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 隐藏dialog
    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            if (null != progressDialog && progressDialog.getWindow() != null) {
                progressDialog.dismiss();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
//        hideLoadingDialog();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将Activity实例从AppManager的堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        if (hasEvnet) {
            EventBus.getDefault().unregister(this);
        }

    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public int getStatusHeight(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return 0;
        }
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 开启EventBus
     */
    public void registerEventBus() {
        hasEvnet = true;
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    private void fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    protected <T extends View> T f(int id) {
        return findViewById(id);
    }


    public void startActivity(Class<?> cls) {
        startActivity(cls, null, true);
    }

    public void startActivity(Class<?> cls, boolean falsh) {
        startActivity(cls, null, falsh);
    }

    public void startActivity(Class<?> cls, Object obj) {
        startActivity(cls, obj, true);
    }

    public void startActivity(Class<?> cls, Object obj, boolean falsh) {
        Intent intent = new Intent(this, cls);
        if (obj != null)
            intent.putExtra("data", (Serializable) obj);
        startActivity(intent, falsh);
    }

    public void startActivity(Intent intent, boolean falsh) {
        // 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        if (isPush) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        if (falsh)
            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        else
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_in);
    }


    public void startActivityForResult(Class<?> cls, Object obj, int requestCode) {
        startActivityForResult(cls, obj, requestCode, true);
    }

    public void startActivityForResult(Class<?> cls, Object obj, int requestCode, boolean falsh) {
        Intent intent = new Intent(this, cls);
        // 设置跳转标志为如此Activity存在则把其从任务堆栈中取出放到最上方
        if (isPush) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (obj != null)
            intent.putExtra("data", (Serializable) obj);
        startActivityForResult(intent, requestCode);
        if (falsh)
            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        else
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_in);
    }

}
