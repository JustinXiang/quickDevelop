package com.zhenxinkeji.zhimahuan.utils

import kotlin.math.floor

object  CreatDataUtils {
    fun creatPhoneNumber():String{
        var firstThree = arrayOf(151, 152, 199, 138, 150, 182, 168, 135, 136, 158, 159, 177, 153, 154, 155, 157, 131, 132, 137, 188)
        var firstPhone = floor(Math.random()*20).toInt()
        var lastPhone = ""
        for( index in 0..3 ){
            var element = floor(Math.random() * 10).toInt()
            lastPhone += element
        }
        var phone = firstThree[firstPhone].toString() + "****" + lastPhone
        var amount = floor(Math.random() * 14000 + 1000).toInt()
        var txt = ""
        if (amount % 2 == 0) {
            // 偶数，收款记录
            txt = "${phone}刚刚成功收款${amount}元"
        } else {
            // 奇数，还款记录
            txt = "${phone}刚制定${amount}元还款计划"
        }
        return txt
    }
}