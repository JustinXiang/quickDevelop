package com.example.quickdevelop.dialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.example.quickdevelop.R
import com.example.quickdevelop.base.clickWithTrigger
import com.example.quickdevelop.myInterface.PhotoChooseListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.xl.aggregationlive.utils.EmptyUtils
import kotlinx.android.synthetic.main.bottom_dialog_choose_photo.*
import java.util.*

/**
 * @version 1.0
 * @describe 选择图片
 */
class ChooseImageDialog(val con: Activity, var listener: PhotoChooseListener) : BottomDialog(con, true, true) {
    var chooseNum = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_dialog_choose_photo)

        photo.clickWithTrigger {
            openAlbum()
            dismiss()
        }
        camera.clickWithTrigger {
            openClame()
            dismiss()
        }
//        cancel.clickWithTrigger {
//            dismiss()
//        }
    }


    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.CENTER
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }


    private fun openClame() {
        // 单独拍照
        PictureSelector.create(con)
            .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
            .theme(R.style.picture_my_style)// 主题样式设置 具体参考 values/styles
            .maxSelectNum(chooseNum)// 最大图片选择数量
            .minSelectNum(1)// 最小选择数量
            .imageSpanCount(4)// 每行显示个数
            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选  这个多选的可以预览
            .previewImage(true)// 是否可预览图片
            .compress(false)
            .isCamera(true)// 是否显示拍照按钮
            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
            .openClickSound(false)// 是否开启点击声音
            .forResult(PictureConfig.REQUEST_CAMERA)//结果回调onActivityResult code
    }

    /**
     * 打开PictureSelector 之前的相关设置
     */
    private fun openAlbum() {
        PictureSelector.create(con)
            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .theme(R.style.picture_my_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
            .maxSelectNum(chooseNum)// 最大图片选择数量
            .minSelectNum(1)// 最小选择数量
            .imageSpanCount(4)// 每行显示个数
            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选  这个多选的可以预览
            .previewImage(true)// 是否可预览图片
            .compress(false)
            .isCamera(true)// 是否显示拍照按钮
            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
            .openClickSound(false)// 是否开启点击声音
            .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
    }

    fun showSetMaxNum(num: Int) {
        chooseNum = num
        show()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    val pathsList = ArrayList<String>()
                    for (media in PictureSelector.obtainMultipleResult(data)) {
//                        Log.i("图片-----》", media.getPath())
                        pathsList.add(media.getPath())
                    }
                    if (EmptyUtils.isNotEmpty(pathsList)) {
                        listener.clickAlbum(pathsList)
                    }
                }
                PictureConfig.REQUEST_CAMERA -> {
                    val obtainMultipleResult = PictureSelector.obtainMultipleResult(data)
                    if (EmptyUtils.isNotEmpty(obtainMultipleResult)) {
                        if (EmptyUtils.isNotEmpty(obtainMultipleResult.get(0).path)) {
                            listener.clickCamera(obtainMultipleResult.get(0).path)
                        }
                    }
                }
            }
        }
    }

}