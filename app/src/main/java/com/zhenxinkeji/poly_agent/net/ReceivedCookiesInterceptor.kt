package com.highstreet.taobaocang.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @describe describe
 */
class ReceivedCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (!originalResponse.headers("Set-Cookie").isEmpty() && originalResponse.isSuccessful) {
            var stringBuilder: StringBuilder = StringBuilder()
            originalResponse.headers("Set-Cookie").forEach {
                stringBuilder.append(it)
            }
//            val urlStr = originalResponse?.request()?.url().toString()
//            if (urlStr.contains(Constant.AUTO_LOGIN)
//                || urlStr.contains(Constant.LOGIN)
//                || urlStr.contains(Constant.RESET_PWD)
//                || urlStr.contains(Constant.REGISTER)
//            ) { //如果是keepLogin 保存登录状态
////                SPUtils.put(App.mInstance, Constant.SP_JSESSIONID, cookies.get(0))
//            }
//            "url ${originalResponse.request().url()}ReceivedCookiesInterceptor:  $stringBuilder".log()
        }
//        "intercept: ".log()

        return originalResponse
    }
}
