package com.example.quickdevelop.utils;

import android.app.Activity;

import com.example.quickdevelop.base.ExtensionKt;
import com.example.quickdevelop.myInterface.OkFileListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

import java.io.File;
import java.util.List;

/**
 * @author LiHongCheng
 * @version 1.0
 * @E-mail diosamolee2014@gmail.com
 * @time 2019/7/12 11:53
 * @describe 图片相关工具类
 */
public class ImageUtils {

    public static void flowablePic(Activity activity, File outputFile, OkFileListener okFileListener) {
        try {
            //使用鲁班压缩压缩上传图片
            Flowable.just(outputFile)
                    .observeOn(Schedulers.io())
                    .map(file -> {
                        List<File> files = Luban.with(activity).load(file).get();
                        return files.get(0);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            file -> okFileListener.complete(file)
                    );
        } catch (Exception e) {
            ExtensionKt.toast("压缩图片异常，请重试");
            e.printStackTrace();
        }
    }
}
