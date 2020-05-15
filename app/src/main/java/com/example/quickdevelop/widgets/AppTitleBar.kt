package com.example.quickdevelop.widgets

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.quickdevelop.R


/**
 * @Author : 张
 * @Email : manitozhang@foxmail.com
 * @Date : 2018/9/19
 *
 * 一个简单的自定义标题栏
 */
class AppTitleBar(
    context: Context,
    attrs: AttributeSet
) :
    RelativeLayout(context, attrs) {
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvMore: TextView? = null
    private var ivMore: ImageView? = null

    //初始化视图
    private fun initView(
        context: Context,
        attributeSet: AttributeSet
    ) {
        val inflate: View =
            LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this)
        ivBack = inflate.findViewById(R.id.iv_back)
        tvTitle = inflate.findViewById(R.id.tv_title)
        tvMore = inflate.findViewById(R.id.tv_more)
        ivMore = inflate.findViewById(R.id.iv_more)
        init(context, attributeSet)
    }

    //初始化资源文件
    private fun init(
        context: Context,
        attributeSet: AttributeSet?
    ) {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.AppTitleBar)
        val title = typedArray.getString(R.styleable.AppTitleBar_title) //标题
        val leftIcon = typedArray.getResourceId(
            R.styleable.AppTitleBar_left_icon,
            R.drawable.ic_title_bar_back
        )
        //左边图片
        val rightIcon = typedArray.getResourceId(
            R.styleable.AppTitleBar_right_icon,
            R.drawable.ic_title_bar_back
        )
        val isShowLeftIcon = typedArray.getBoolean(
            R.styleable.AppTitleBar_show_left_icon,
            true
        )
        ivBack!!.visibility = if (isShowLeftIcon){
            View.VISIBLE
        }else{
            View.GONE
        }

        //右边图片
        val rightText =
            typedArray.getString(R.styleable.AppTitleBar_right_text) //右边文字
        val titleBarType =
            typedArray.getInt(R.styleable.AppTitleBar_titlebar_type, 10) //标题栏类型,默认为10

        //赋值进去我们的标题栏
        tvTitle!!.text = title
        ivBack!!.setImageResource(leftIcon)
        tvMore!!.text = rightText
        ivMore!!.setImageResource(rightIcon)

        //可以传入type值,可自定义判断值
        if (titleBarType == 10) { //不传入,默认为10,显示更多 文字,隐藏更多图标按钮
            ivMore!!.visibility = View.GONE
            tvMore!!.visibility = View.VISIBLE
        } else if (titleBarType == 11) { //传入11,显示更多图标按钮,隐藏更多 文字
            tvMore!!.visibility = View.GONE
            ivMore!!.visibility = View.VISIBLE
        }
    }

    //左边图片点击事件
    fun setLeftIconOnClickListener(l: OnClickListener?) {
            ivBack!!.setOnClickListener(l)

    }

    //右边图片点击事件
    fun setRightIconOnClickListener(l: OnClickListener?) {
            ivMore!!.setOnClickListener(l)
    }

    //右边文字点击事件
    fun setRightTextOnClickListener(l: OnClickListener?) {
            tvMore!!.setOnClickListener(l)
    }

    fun setRightTextVisibility(visibility: Boolean){
        if (visibility){
            tvMore!!.visibility =  View.VISIBLE
        } else {
            tvMore!!.visibility =  View.GONE
        }

    }

    fun setRightEnable(enable : Boolean){
        tvMore!!.isEnabled = enable
    }


    init {
        initView(context, attrs)
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}