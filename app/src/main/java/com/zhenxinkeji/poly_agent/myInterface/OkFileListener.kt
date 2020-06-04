package com.zhenxinkeji.poly_agent.myInterface

import java.io.File

/**
 * 文件成功传输 压缩 等回调接口
 */
interface OkFileListener {
    fun complete(file: File?)
}