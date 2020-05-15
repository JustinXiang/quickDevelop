package com.highstreet.taobaocang.net

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function


/**
 * @Package com.highstreet.taobaocang.net
 * @author LiHongCheng
 * @E-mail diosamolee2014@gmail.com
 * @time 2019/1/8 10:10
 * @version 1.0
 * @describe describe
 */
class ExceptionFunction <T> : Function<Throwable, Observable<T>> {
    override fun apply(@NonNull throwable: Throwable): Observable<T> {
        return Observable.error(ExceptionEngine().handleException(throwable))
    }
}