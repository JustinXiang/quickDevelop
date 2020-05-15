package com.xl.aggregationlive.utils

import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import com.example.quickdevelop.base.Constant
import java.lang.reflect.Array

/**
 * @describe 空数据校验类
 */
class EmptyUtils private constructor() {
    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        /**
         * 判断对象是否为空
         *
         * @param obj 对象
         * @return `true`: 为空<br></br>`false`: 不为空
         */
        fun isEmpty(obj: Any?): Boolean {
            return (obj == null
                    || obj is String && obj.toString().length == 0
                    || obj.javaClass.isArray && Array.getLength(obj) == 0
                    || obj is Collection<*> && obj.isEmpty()
                    || obj is Map<*, *> && obj.isEmpty()
                    || obj is SparseArray<*> && obj.size() == 0
                    || obj is Collection<*> && obj.size == 0
                    || obj is SparseBooleanArray && obj.size() == 0
                    || obj is SparseIntArray && obj.size() == 0
                    || obj is SparseLongArray && obj.size() == 0)
        }

        /**
         * 判断对象是否非空
         *
         * @param obj 对象
         * @return `true`: 非空<br></br>`false`: 空
         */
        fun isNotEmpty(obj: Any?): Boolean {
            return !isEmpty(obj)
        }

        /**
         * 判断checkHttpResult 对象的值是否为空 为空返回False  正确返回
         */
        fun checkHttpResult(obj: Any?, code: Int? = Constant.HTTP_SUCCESS_CODE): Boolean {
            return isNotEmpty(obj) && code == Constant.HTTP_SUCCESS_CODE
        }
    }
}
