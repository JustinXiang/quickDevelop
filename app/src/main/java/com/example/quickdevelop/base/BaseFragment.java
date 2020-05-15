package com.example.quickdevelop.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.example.quickdevelop.dialog.CustomProgressDialog;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseFragment extends RxFragment {


    /**
     * 获取页面的默认名称
     */
    public final String FRAGMEN_SIMPLE_NAME = this.getClass().getSimpleName();

    public BaseActivity mActivity;

    public View contentView;

    protected boolean isUIVisible;
    protected boolean isViewCreated;

    private boolean hasEvnet;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
            if (mActivity == null) {
                mActivity = (BaseActivity) getContext();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        onPrepare();
        super.onCreate(savedInstanceState);

//        http = HttpApi.getInstance();
        Bundle bundle = getArguments();
        initParms(bundle);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(getResId(), container, false);
        return contentView;
    }

    //    public void onViewCreated(View view, Bundle savedInstanceState) {
//        init();
//    }
    /*
     * 当Activity初始化之后可以在这里进行一些数据的初始化操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareFetchData();
        initViewAndEvent();
        initData();
        if (hasEvnet) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 初始化界面之前
     *
     * @param
     */
    public void onPrepare() {

    }

    /**
     * 通过bundle传递参数
     *
     * @param bundle
     */
    protected void initParms(Bundle bundle) {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        prepareFetchData();
    }

    //查看这个fragment的可见状态
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUIVisible = isVisibleToUser;
        if (isUIVisible) {
            prepareFetchData();
        }
    }

    //懒加载 的方法
    protected void delayload() {
    }

    private void prepareFetchData() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            delayload();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

        }
    }

    CustomProgressDialog progressDialog;

    // 显示默认 dialog
    public void showDialog() {
        try {
            if (progressDialog == null && mActivity != null) {
                progressDialog = CustomProgressDialog.createDialog(mActivity);
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
    public void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            if (null != progressDialog && progressDialog.getWindow() != null) {
                progressDialog.dismiss();
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasEvnet) {
            EventBus.getDefault().unregister(this);
        }
        mActivity = null;
    }


    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    protected void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 开启EventBus
     */
    public void registerEventBus() {
        hasEvnet = true;
    }


    protected <T extends View> T f(View view, int id) {
        return view.findViewById(id);
    }
}
