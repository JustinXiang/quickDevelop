package com.example.quickdevelop.bean

/**
 * @version 1.0
 * @describe describe
 */
class BaseResponse<T> {
    var code: Int = 0
    var msg: String? = null
    var errorMsg: String? = null
    var data: T? = null
}