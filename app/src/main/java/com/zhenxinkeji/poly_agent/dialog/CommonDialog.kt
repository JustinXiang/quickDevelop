package com.zhenxinkeji.poly_agent.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.zhenxinkeji.poly_agent.R
import com.zhenxinkeji.poly_agent.base.clickWithTrigger
import com.zhenxinkeji.poly_agent.base.gone
import com.zhenxinkeji.poly_agent.base.visible
import kotlinx.android.synthetic.main.dialog_custom_layout.*


open class CommonDialog(context: Context) : Dialog(context, R.style.CustomDialog2) {
    /**
     * 显示的图片
     */
    private var imageIv: ImageView? = null

    /**
     * 显示的标题
     */
    private var titleTv: TextView? = null

    /**
     * 显示的消息
     */
    private var messageTv: TextView? = null

    private var typeInEt: EditText? = null

    /**
     * 确认和取消按钮
     */
    private var negtiveBn: Button? = null
    private var positiveBn: Button? = null

    /**
     * 按钮之间的分割线
     */
    private var columnLineView: View? = null

    /**
     * 都是内容数据
     */
    var message: String? = null
        private set
    var title: String? = null
        private set
    var positive: String? = null
        private set
    var negtive: String? = null
        private set
    var imageResId = -1
        private set

    /**
     * 底部是否只有一个按钮
     */
    var isSingle = false
        private set

    var isSingleBlue = false
        private set

    var showEditText = false
        private set

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom_layout)
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false)
        //初始化界面控件
        initView()
        //初始化界面数据
        refreshView()
        //初始化界面控件的事件
        initEvent()
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private fun initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        positiveBn!!.clickWithTrigger {
            if (onClickBottomListener != null) {
                onClickBottomListener!!.onPositiveClick(if (showEditText) typeInEt!!.text.toString().trim() else messageTv!!.text.toString())
            }
        }
        tv_next.clickWithTrigger {
            if (onClickBottomListener != null) {
                onClickBottomListener!!.onPositiveClick(if (showEditText) typeInEt!!.text.toString().trim() else messageTv!!.text.toString())
            }
        }
        //设置取消按钮被点击后，向外界提供监听
        negtiveBn!!.clickWithTrigger {
            if (onClickBottomListener != null) {
                onClickBottomListener!!.onNegtiveClick()
            }
        }
    }

    /**
     * 初始化界面控件的显示数据
     */
    private fun refreshView() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(title)) {
            titleTv!!.text = title
            titleTv!!.visibility = View.VISIBLE
        } else {
            titleTv!!.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(message)) {
            messageTv!!.text = message
        }
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(positive)) {
            positiveBn!!.text = positive
            tv_next.text = positive
        } else {
            positiveBn!!.text = "确定"
            tv_next.text = "确定"
        }
        if (!TextUtils.isEmpty(negtive)) {
            negtiveBn!!.text = negtive
        } else {
            negtiveBn!!.text = "取消"
        }
        if (imageResId != -1) {
            imageIv!!.setImageResource(imageResId)
            imageIv!!.visibility = View.VISIBLE
        } else {
            imageIv!!.visibility = View.GONE
        }
        /**
         * 只显示一个按钮的时候隐藏取消按钮，回掉只执行确定的事件
         */
        if (isSingle) {
            columnLineView!!.visibility = View.GONE
            negtiveBn!!.visibility = View.GONE
        } else {
            negtiveBn!!.visibility = View.VISIBLE
            columnLineView!!.visibility = View.VISIBLE
        }

        /**
         * 只显示一个蓝色确定按钮
         */
        if(isSingleBlue){
            ll_tw_next.gone()
            tv_next.visible()
        }else {
            tv_next.gone()
            ll_tw_next.visible()
        }

        if (showEditText) {
            typeInEt!!.visibility = View.VISIBLE
        } else {
            typeInEt!!.visibility = View.GONE
        }
    }

    override fun show() {
        super.show()
        refreshView()
    }

    /**
     * 初始化界面控件
     */
    private fun initView() {
        negtiveBn = findViewById<Button>(R.id.negtive)
        positiveBn = findViewById<Button>(R.id.positive)
        titleTv = findViewById<TextView>(R.id.title)
        messageTv = findViewById<TextView>(R.id.message)
        imageIv = findViewById<ImageView>(R.id.image)
        columnLineView = findViewById(R.id.column_line)
        typeInEt = findViewById(R.id.et_type_in)
    }

    /**
     * 设置确定取消按钮的回调
     */
    var onClickBottomListener: OnClickBottomListener? = null
    fun setOnClickBottomListener(onClickBottomListener: OnClickBottomListener?): CommonDialog {
        this.onClickBottomListener = onClickBottomListener
        return this
    }

    interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        fun onPositiveClick(string: String)

        /**
         * 点击取消按钮事件
         */
        fun onNegtiveClick()
    }

    fun setMessage(message: String?): CommonDialog {
        this.message = message
        return this
    }

    fun setTitle(title: String?): CommonDialog {
        this.title = title
        return this
    }

    fun setPositive(positive: String?): CommonDialog {
        this.positive = positive
        return this
    }

    fun setNegtive(negtive: String?): CommonDialog {
        this.negtive = negtive
        return this
    }

    fun setSingle(single: Boolean): CommonDialog {
        isSingle = single
        return this
    }

    fun setSingleBlue(single: Boolean): CommonDialog{
        isSingleBlue =single
        return this
    }

    fun setImageResId(imageResId: Int): CommonDialog {
        this.imageResId = imageResId
        return this
    }

    fun setShowEditText(showEditText: Boolean): CommonDialog {
        this.showEditText = showEditText
        return this
    }
}