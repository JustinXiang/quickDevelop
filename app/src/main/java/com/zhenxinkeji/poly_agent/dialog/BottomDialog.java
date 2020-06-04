package com.zhenxinkeji.poly_agent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.zhenxinkeji.poly_agent.R;

/**
 * Created by xuyun on 2018/6/14.
 */

public class BottomDialog extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private Context context;

    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public BottomDialog(Context context, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.BottomDialog);
        this.context = context;
        this.iscancelable = true;//isCancelable;
        this.isBackCancelable = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }
}
