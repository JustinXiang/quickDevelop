package com.zhenxinkeji.poly_agent.base

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable


/**
 * @describe describe
 */
open class BaseToActivity {
    fun to(java: Class<Any?>) {
        val context = AppManager.getAppManager().currentActivity()
        val intent = Intent(context, java)
        context.startActivity(intent)
    }

    fun <T> to(java: Class<T>, vararg pairs: Pair<String, Any>) {
        val context = AppManager.getAppManager().currentActivity()
        val intent = Intent(context, java)
        for (pair in pairs) {
            val second = pair.second
            when (second) {
                is String -> intent.putExtra(pair.first, second)
                is Int -> intent.putExtra(pair.first, second)
                is Boolean -> intent.putExtra(pair.first, second)
                is Float -> intent.putExtra(pair.first, second)
                is Double -> intent.putExtra(pair.first, second)
                is Long -> intent.putExtra(pair.first, second)
                is Byte -> intent.putExtra(pair.first, second)
                is Short -> intent.putExtra(pair.first, second)
                is Char -> intent.putExtra(pair.first, second)
                is Bundle -> intent.putExtra(pair.first, second)
                is CharSequence -> intent.putExtra(pair.first, second)
                is Parcelable -> intent.putExtra(pair.first, second)
                is Serializable -> intent.putExtra(pair.first, second)
                else -> {
                    if (second == null) {
                        intent.putExtra(pair.first, "")
                    } else {
                        throw IllegalArgumentException("UnKonwBundleData")
                    }
                }
            }
        }
        context.startActivity(intent)
    }

    fun <T> to(java: Class<T>, pairs: Bundle) {
        val context = AppManager.getAppManager().currentActivity()
        val intent = Intent(context, java)
        intent.putExtras(pairs)
        context.startActivity(intent)
    }

    //开启携带返回数据
    fun <T> toForResult(java: Class<T>, bundle: Bundle, requstCode: Int) {
        val context = AppManager.getAppManager().currentActivity()
        val intent = Intent(context, java)
        intent.putExtras(bundle)
        context.startActivityForResult(intent, requstCode)
    }

    fun toDesktop() {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN// "android.intent.action.MAIN"
        intent.addCategory(Intent.CATEGORY_HOME) //"android.intent.category.HOME"
        AppManager.getAppManager().currentActivity().startActivity(intent)
        AppManager.getAppManager().currentActivity().moveTaskToBack(false) // 关键的一行代码
    }

}