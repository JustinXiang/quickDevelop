package com.example.quickdevelop

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.webkit.WebView
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex

class App : Application() {

    companion object{
        lateinit var mInstance : App
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        setTextTheme()
        //h5通过浏览器调试开启
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            WebView.setWebContentsDebuggingEnabled(true)
        }


    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //dex预加载
        MultiDex.install(base)
    }

    // 加载系统默认设置，字体不随用户设置变化
    fun setTextTheme(){
        var res = super.getResources()
        var config =Configuration()
        config.setToDefaults()
        resources.updateConfiguration(config,res.displayMetrics)
    }

    override fun getResources(): Resources {
        var res = super.getResources()
        var config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config,res.displayMetrics)
        return res
    }
}