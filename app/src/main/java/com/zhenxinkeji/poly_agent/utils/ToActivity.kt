package com.xl.aggregationlive.utils

import com.zhenxinkeji.poly_agent.App
import com.zhenxinkeji.poly_agent.base.BaseToActivity
import com.zhenxinkeji.poly_agent.base.Constant
import com.zhenxinkeji.poly_agent.utils.SPUtils


/**
 * @version 1.0
 * @describe describe
 */
object ToActivity : BaseToActivity() {
    //去首页
//    fun toHome() {
//        toHomeNavigation("0")
//    }

    //去登录
    fun toLogin(clean: Boolean){
        if(clean){
//            Hawk.put(Constant.SP_LOGIN,null)
//            Hawk.put(Constant.SP_MEMBERNO,"")
            SPUtils.put(App.mInstance, Constant.SP_TOKEN,"")
        }
//        to(FRAGMENT_LOGIN_IN, LoginActivity::class.java)
    }


    //根据index 跳转首页fragment
//    fun toHomeNavigation(index: String) = to(HomeActivity::class.java, "action" to index) //首页 编辑推荐


    //首页 编辑推荐
//    fun toTaskDetial(taskId: String) = to(TaskDetailAcivity::class.java, "taskId" to taskId)

    //前往图片详情
//    fun toImgDetial(vararg paths: String) =
//        to(PhotoDetailActivity::class.java, Constant.PATHS to paths.toMutableList().toTypedArray())
//
//    fun toVedio(vedioPath: String, pucturePath: String) =
//        to(VedioAcivity::class.java, Constant.V_PATH to vedioPath, Constant.PIC_PATH to pucturePath)
//
//    fun toLiveVedio(vedioPath: String, pucturePath: String) =
//        to(LivePlayerActivity::class.java, Constant.V_PATH to vedioPath, Constant.PIC_PATH to pucturePath)
//    /**
//     * 前往H5 Web页面
//     */
//    @JvmOverloads
//    fun toh5(url: String, title: String = "", isFullScreen: Boolean = false) =
//        to(WebviewActivity::class.java, "url" to url, "title" to title, "isFullScreen" to isFullScreen)
}