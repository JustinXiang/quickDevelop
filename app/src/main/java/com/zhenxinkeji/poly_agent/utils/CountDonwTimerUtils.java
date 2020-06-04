package com.zhenxinkeji.poly_agent.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zhenxinkeji.poly_agent.R;


/**
 * Created by Administrator on 2018/7/27.
 */

public class CountDonwTimerUtils {
    public static void startCount(long millisInFuture, long countDownInterval, final TextView tv) {
        new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long l) {
                tv.setClickable(false);
                tv.setText("("+l / 1000 + "s)后重新获取");
            }

            @Override
            public void onFinish() {
                tv.setClickable(true);
                tv.setText("重新获取");
            }
        }.start();
    }

    public static void startCount(long millisInFuture, long countDownInterval, final CheckBox tv) {
        new CountDownTimer(millisInFuture, countDownInterval) {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTick(long l) {
                tv.setTextColor(R.color.defter_gray);
                tv.setClickable(false);
                tv.setChecked(true);
                tv.setText("("+l / 1000 + "s)后重新获取");
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                tv.setTextColor(R.color.deftColor);
                tv.setChecked(false);
                tv.setClickable(true);
                tv.setText("重新获取");
            }
        }.start();
    }
}