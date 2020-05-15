package com.example.quickdevelop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.quickdevelop.R;
import com.example.quickdevelop.myInterface.CompleteListener;
import com.example.quickdevelop.myInterface.DialogLRClickListener;



/**
 * 提示的对话框
 */
public class HintDialog extends Dialog implements View.OnClickListener {


    private String hint;
    private String hint_cancel;
    private String hint_determine;
    private String title;
    private TextView tv_hint;
    private TextView tv_determine;
    private TextView tv_cancel;
    private DialogLRClickListener listener;
    private CompleteListener completeListener;
    private TextView tv_title;
    private View iv_close;


//    public TextView getTv_hint() {
//        return tv_hint;
//    }

    public TextView getTv_determine() {
        return tv_determine;
    }

    public TextView getTv_cancel() {
        return tv_cancel;
    }

    /**
     * 改变两个button文字的构造
     */
    public HintDialog(Context context, String hint, String hintCancel, String hintDetermine, DialogLRClickListener listener) {
        super(context, R.style.common_dialog);
        this.hint = hint;
        this.listener = listener;
        this.hint_cancel = hintCancel;
        this.hint_determine = hintDetermine;
    }

    /**
     * Title 带提示的
     */
    public HintDialog(Context context, String title, String hint, String hintDetermine, CompleteListener completeListener) {
        super(context, R.style.common_dialog);
        this.hint = hint;
        this.title = title;
        this.hint_determine = hintDetermine;
        this.completeListener = completeListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);
        tv_hint = findViewById(R.id.tv_hint);
        tv_cancel = findViewById(R.id.dialog_leftbutton);
        tv_determine = findViewById(R.id.dialog_rightbutton);
        tv_title = findViewById(R.id.tv_title);
        iv_close = findViewById(R.id.iv_close);
        if (!TextUtils.isEmpty(hint_cancel)) {
            tv_cancel.setText(hint_cancel);
        } else {
            tv_cancel.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(hint_determine)) {
            tv_determine.setText(hint_determine);
        }

        if (!TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
        tv_hint.setText(hint);
        tv_cancel.setOnClickListener(this);
        tv_determine.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }


    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_leftbutton:
                if (listener != null) {
                    listener.onLeftClick();
                }
                break;

            case R.id.dialog_rightbutton:
                if (listener != null) {
                    listener.onRightClick();
                }
                if (completeListener != null) {
                    completeListener.complete();
                }
                break;
            default:
                break;
        }
        //点击之后都要取消
        dismiss();
    }


//    public void setCloseVisible(boolean b) {
//        iv_close.setVisibility(View.VISIBLE);
//    }
//
//    public void setCenterTitle(String text) {
//        tv_title.setVisibility(View.VISIBLE);
//        tv_title.setText(text);
//    }
}
