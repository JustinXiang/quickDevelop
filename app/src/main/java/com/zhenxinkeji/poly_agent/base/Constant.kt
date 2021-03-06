package com.zhenxinkeji.poly_agent.base

interface Constant {
    companion object {

        var BASE_URL = ""

        //加载状态
        val LOADING_ERROR = 1

        /**
         * device id
         */
        val SP_DEVICE_ID = "device_id"

        val SP_TOKEN ="sp_token"

        val HTTP_SUCCESS_CODE = 100

        val RESULT = "result"
        val PATHS = "paths"
        val TITLE = "title"
        val HINT = "hint"

        val V_PATH = "vedioPath"
        val PIC_PATH = "pucturePath"

        const val FRAGMENT_ID = "fragment_id"
        const val FRAGMENT_INFO = "fragment_info"
        val SP_PHONE = "sp_memberNoMobile"

        val SP_PWD = "sp_pwd"

    }
}