package com.example.quickdevelop.myInterface

/**
 * @describe 图片选择方式回调
 */
interface PhotoChooseListener {
    //确定的点击
    fun clickAlbum(paths: MutableList<String>)

    //取消的点击
    fun clickCamera(path: String)
}
