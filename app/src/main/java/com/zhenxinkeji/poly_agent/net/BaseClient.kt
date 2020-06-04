package com.highstreet.taobaocang.net

import androidx.multidex.BuildConfig
import com.zhenxinkeji.poly_agent.base.Constant
import com.socks.library.KLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * @version 1.0
 * @describe describe
 */
object BaseClient {
    val CONNECT_TIME_OUT = 30//连接超时时长x秒
    val READ_TIME_OUT = 30//读数据超时时长x秒
    val WRITE_TIME_OUT = 30//写数据接超时时长x秒

    var retrofit: Retrofit? = null

    fun getInstance(): Retrofit? {
        if (null != retrofit) {
            return retrofit!!
        } else {
            retrofit = retrofit()
            return retrofit
        }
    }

    /**
     * 重新创建Retorfit对象
     */
    fun restClient(): Retrofit? {
        retrofit = retrofit()
        return retrofit
    }


    private fun retrofit(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)  //base url
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
    }


    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor {
                KLog.json("json__", it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
        } else {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
        }
//        // 设置请求头
        builder.addInterceptor { chain ->
            val time = (System.currentTimeMillis() / 1000).toString() + ""
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("time", time)
//            val user = UserMannager.getUser()
//            if (EmptyUtils.isNotEmpty(user?.userId)) {
//                requestBuilder.addHeader("userId", user?.userId)
//            }
            chain.proceed(requestBuilder.build())
        }
//        val persistentCookieStore = PersistentCookieStore(App.mInstance)
////        builder.addInterceptor(ReceivedCookiesInterceptor())
//        builder
//            .cookieJar(object : CookieJar {
//                override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
//                    val urlStr = url.toString()
//                    for (cookie in cookies) {
//                        if (urlStr.contains(Constant.AUTO_LOGIN)
//                            || urlStr.contains(Constant.LOGIN)
//                            || urlStr.contains(Constant.REGISTER)
//                            || urlStr.contains(Constant.BINDWECHAT)
//                            || urlStr.contains(Constant.BIND_WECHAT)
//                        ) { //如果是keepLogin 保存登录状态
//                            SPUtils.put(App.mInstance, Constant.SP_JSESSIONID, cookie.value())
//                        }
//                        persistentCookieStore.add(url, cookie)
//                    }
//                }
//
//                override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
//                    return persistentCookieStore.get(url)
//                }
//
//
//            })


        val trustManager = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        }
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        builder.sslSocketFactory(sslSocketFactory, trustManager)
        builder.hostnameVerifier { _, _ -> true }
        return builder.build()
    }


}