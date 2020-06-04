package com.zhenxinkeji.poly_agent.manager

import com.zhenxinkeji.poly_agent.App
import com.zhenxinkeji.poly_agent.base.Constant
import com.zhenxinkeji.poly_agent.bean.UserInfoBean
import com.zhenxinkeji.poly_agent.utils.SPUtils
import com.google.gson.Gson

/**
 * @describe 用户管理界面
 */
object UserMannager {

    private const val KEY_USER = "KEY_USER"

    private var user: UserInfoBean? = null

    fun getUser(): UserInfoBean? {
        if (user == null) {
            try {
                val json = SPUtils.get(App.mInstance, KEY_USER, "") as String
                if (!"".equals(json)) {
                    user = Gson().fromJson(json, UserInfoBean::class.java)
                }
            } catch (e: RuntimeException) {
            }
        }
        return user
    }

    fun setUser(user: UserInfoBean?) {
        if (user != null) {
            try {
                SPUtils.put(App.mInstance, KEY_USER, Gson().toJson(user))
                if (!user.userId.isEmpty()) {
                    SPUtils.put(App.mInstance, Constant.SP_TOKEN, user.userId)
                }
            } catch (e: RuntimeException) {
            }
        }
        this.user = user
    }

    //登出
    fun logOut() {
        SPUtils.put(App.mInstance, Constant.SP_TOKEN, "")
//        ToActivity.to(LoginAcivity::class.java)
    }


}