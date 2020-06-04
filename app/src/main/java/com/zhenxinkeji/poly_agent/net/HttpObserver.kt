package com.highstreet.taobaocang.net

import com.zhenxinkeji.poly_agent.base.Constant
import com.zhenxinkeji.poly_agent.base.toast
import com.zhenxinkeji.poly_agent.bean.BaseResponse
import com.zhenxinkeji.zhimahuan.utils.EmptyUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @version 1.0
 * @describe describe
 */
abstract class HttpObserver<T> : Observer<T> {

    /**
     * 标记是否为特殊情况
     */
    private var resultNull: Boolean = true

    override fun onComplete() {
        // 特殊情况：当请求成功，但T == null时会跳过onNext，仍需当成功处理
//        if (resultNull)
//            onSuccess(null)
    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        if (e is ApiException && !interceptToast(e.code, e.msg ?: "")) {
            if (EmptyUtils.isNotEmpty(e.msg)) {
                e.msg?.toast()
            }
            onError(e.code, e.msg)
        } else {
            onError(0, e.message)
        }
    }

    //拦截异常情况code 和 提示
    open fun interceptToast(code: Int, msg: String): Boolean = false


    override fun onNext(t: T) {
        try {
            resultNull = false
            if (t is BaseResponse<*>) {
                when (t.code) {
                    Constant.HTTP_SUCCESS_CODE -> {
                        onSuccess(t)
                    }
//                    1002 -> {
//                        UserMannager.keepLogin()  //keep 登录
//                        onError(t.code, t.msg)
//                    }
//                    1003 -> {
//                        UserMannager.logout()  //清空token ， 弹出登录
//                        onError(t.code, t.msg)
//                    }
                    else -> {
                        if (EmptyUtils.isNotEmpty(t.msg) && !interceptToast(t.code, t.msg ?: "")) {
                            t.msg?.toast()
                        }
                        onError(t.code, t.msg)
                    }
                }
            } else {
                onSuccess(t)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    abstract fun onSuccess(result: T)

    /**
     * 统一处理失败，比如登录失效等
     *
     * @param code
     * @param msg
     */
    open fun onError(code: Int, msg: String?) {

    }

}