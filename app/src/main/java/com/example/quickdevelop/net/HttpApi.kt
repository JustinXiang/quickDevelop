package com.highstreet.taobaocang.net

import com.example.quickdevelop.base.Constant

/**
 * @version 1.0
 * @describe 网络请求参数封装
 */
object HttpApi {
    private var apiservice: Apiservice? = null

    fun start(): Apiservice {
        if (null != apiservice) {
            return apiservice!!
        } else {
            apiservice = BaseClient.getInstance()?.create(Apiservice::class.java)
            return apiservice!!
        }
    }

    /**
     * 更改Base url
     */
    fun setBaseUrl(url: String) {
        Constant.BASE_URL = url
        apiservice = BaseClient.restClient()?.create(Apiservice::class.java)
    }

    //上传文件
//    fun upFile(it: File): Observable<BaseResponse<UpFileBean>> {
//        val textType = MediaType.parse("text/plain")
//        val name = RequestBody.create(textType, it.name)
//        val fileType = RequestBody.create(textType, it.path.substringAfter("."))
//        //上传类型为form表单
//        val requestFile = RequestBody.create(MultipartBody.FORM, it)
//        //cmt_pic为服务器定义的字段
//        val body = MultipartBody.Part.createFormData("file", it.getName(), requestFile)
//        return HttpApi.start().uploadFile(body, name, fileType)
//    }


}

