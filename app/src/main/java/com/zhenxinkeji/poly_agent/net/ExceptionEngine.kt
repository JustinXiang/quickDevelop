package com.highstreet.taobaocang.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * @version 1.0
 * @describe describe
 */
class ExceptionEngine {
    val UN_KNOWN_ERROR = 1000//未知错误
    val ANALYTIC_SERVER_DATA_ERROR = 1001//解析(服务器)数据错误
    val CONNECT_ERROR = 1002//网络连接错误
    val TIME_OUT_ERROR = 1003//网络连接超时
    val UN_KNOWN_HOST = 1004//主机连接失败

    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        e.printStackTrace()

        if (e is ApiException) {  //服务器返回的错误
            return e
        } else if (e is HttpException) {       //HTTP错误
            ex = ApiException(e, e.code())
            ex.msg = "网络错误:" + ex.code
            return ex
        } else if (e is UnknownHostException) {
            ex = ApiException(e, UN_KNOWN_HOST)
            ex.msg = "连接失败:" + ex.code
            return ex
        } else if (e is JsonParseException || e is JsonSyntaxException
            || e is JSONException || e is IllegalStateException
            || e is ParseException || e is MalformedJsonException
        ) { //解析数据错误
            ex = ApiException(e, ANALYTIC_SERVER_DATA_ERROR)
//            ex.msg = "数据异常"
            return ex
        } else if (e is ConnectException) {//连接网络错误
            ex = ApiException(e, CONNECT_ERROR)
            ex.msg = "连接失败"
            return ex
        } else if (e is SocketTimeoutException || e is TimeoutException) {//网络超时
            ex = ApiException(e, TIME_OUT_ERROR)
            ex.msg = "网络超时"
            return ex
        } else { //未知错误
            ex = ApiException(e, UN_KNOWN_ERROR)
            ex.msg = e.message
            return ex
        }
    }
}