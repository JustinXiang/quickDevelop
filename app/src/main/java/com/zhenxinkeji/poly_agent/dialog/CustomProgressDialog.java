
package com.zhenxinkeji.poly_agent.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;

import com.zhenxinkeji.poly_agent.R;


public class CustomProgressDialog extends Dialog {
    private static CustomProgressDialog customProgressDialog = null;
    private AnimationDrawable animationDrawable;

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog createDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomDialog);
        customProgressDialog.setContentView(R.layout.customprogressdialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return customProgressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
//        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
////        final Animatable animatable = ((Animatable) imageView.getDrawable());
////        animatable.start();
//        imageView.setImageResource(R.drawable.sobot_loading_dialog_anim);
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
//        animationDrawable.start();
    }

    @Override
    public void dismiss() {
        if (null != animationDrawable) {
            animationDrawable.stop();
        }
        super.dismiss();
    }
}
