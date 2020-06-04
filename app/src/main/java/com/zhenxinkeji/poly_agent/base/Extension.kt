package com.zhenxinkeji.poly_agent.base

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.zhenxinkeji.poly_agent.App
import com.zhenxinkeji.poly_agent.R
import com.zhenxinkeji.poly_agent.utils.DeviceUtils
import com.zhenxinkeji.poly_agent.widgets.LoadingLayout
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.highstreet.taobaocang.net.ExceptionFunction
import com.highstreet.taobaocang.net.HttpObserver
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.xl.aggregationlive.utils.EmptyUtils
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

//字符串扩展函数
fun String.lastChar(): Char = this.get(this.length - 1)

fun String.isNotEmpty(): Boolean = !this.isEmpty()
//copy text到粘贴板
fun String.copyText() = DeviceUtils.copyToClipboard(App.mInstance, this)

//toHtml
@Suppress("DEPRECATION")
fun String.toHtml(): Spanned =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    else
        Html.fromHtml(this)

//集合扩展函数
fun <T> Collection<T>.joinToString(
    separator: String = ",",
    prefix: String = "",
    postfix: String = ""
): String {
    var result = StringBuffer(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//所有类型函数扩展
fun Any.log() = Log.d("flx", this.toString())

fun Any.logh5() = Log.d("h5info", this.toString())

fun Any.toast() {
    val mToast = Toast.makeText(App.mInstance, "", Toast.LENGTH_SHORT)
    mToast.setText(this.toString())
    mToast.show()
}

fun Any.toastCenter() {
    val mToast = Toast.makeText(App.mInstance, "", Toast.LENGTH_SHORT)
    mToast.setText(this.toString())
    mToast.setGravity(Gravity.CENTER, 0, 0)
    mToast.show()
}

//显示小图片加文字
fun Any.toastImg(resId: Int) {
    val mToast = Toast.makeText(App.mInstance, "", Toast.LENGTH_SHORT)
    val inflate = View.inflate(App.mInstance, R.layout.toast_small_img, null)
    inflate.findViewById<ImageView>(R.id.iv_toast_img).setImageResource(resId)
    mToast.view = inflate
    mToast.setGravity(Gravity.CENTER, 0, 0)
    mToast.setText(this.toString())
    mToast.show()
}

//fun Any.testtoast() = Log.d("THC", this.toString())


//RxJava 扩展函数 快速指定线程
fun <T> Observable<T>.sMain(): Observable<T> = this.subscribeOn(Schedulers.io())
    .unsubscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .onErrorResumeNext(ExceptionFunction())

fun <T> Observable<T>.sMain(obj: RxAppCompatActivity): Observable<T> = this.sMain().compose(obj.bindToLifecycle())
fun <T> Observable<T>.sMain(obj: RxFragment): Observable<T> = this.sMain().compose(obj.bindToLifecycle())

/**
 * send  直接返回请求Bean 其余错误情况 Toast提示
 * send2 在send 的基础上扩展了 Disposable 防止多次请求响应
 * send3 在send 的基础上扩展了 错误处理
 * send4 在send 的基础上扩展了 错误处理 Disposable 防止多次请求响应
 */
fun <T> Observable<T>.send(block: (result: T) -> Unit) {
    try {
        subscribe(object : HttpObserver<T>() {
            override fun onSuccess(result: T) {
                block(result)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun <T> Observable<T>.send2(block: (result: T) -> Unit, sendDisposable: (dis: Disposable) -> Unit) {
    try {
        subscribe(object : HttpObserver<T>() {
            override fun onSuccess(result: T) {
                block(result)
            }

            override fun onSubscribe(d: Disposable) {
                sendDisposable(d)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun <T> Observable<T>.send3(block: (result: T?, code: Int, msg: String?) -> Unit) {
    try {
        subscribe(object : HttpObserver<T>() {
            override fun onSuccess(result: T) {
                block(result, Constant.HTTP_SUCCESS_CODE, "")
            }

            override fun onError(code: Int, msg: String?) {
                super.onError(code, msg)
                block(null, code, msg)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun <T> Observable<T>.send4(
    block: (result: T?, code: Int, msg: String?) -> Unit,
    sendDisposable: (dis: Disposable) -> Unit
) {
    try {
        subscribe(object : HttpObserver<T>() {
            override fun onSuccess(result: T) {
                block(result, Constant.HTTP_SUCCESS_CODE, "")
            }

            override fun onError(code: Int, msg: String?) {
                super.onError(code, msg)
                block(null, code, msg)
            }

            override fun onSubscribe(d: Disposable) {
                sendDisposable(d)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun <T> Observable<T>.send5(block: (result: T) -> Unit, failed: () -> Unit) {
    try {
        subscribe(object : HttpObserver<T>() {
            override fun onSuccess(result: T) {
                block(result)
            }

            override fun onError(code: Int, msg: String?) {
                super.onError(code, msg)
                failed()
            }
        })
    } catch (e: Exception) {
        failed()
    }
}


fun <T> Flowable<T>.sMain(): Flowable<T> = this.subscribeOn(Schedulers.io())
    .unsubscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.sMain(obj: RxAppCompatActivity): Flowable<T> = this.sMain().compose(obj.bindToLifecycle())
fun <T> Flowable<T>.sMain(obj: RxFragment): Flowable<T> = this.sMain().compose(obj.bindToLifecycle())


/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        block(it as T)
    }
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 400, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) block(it as T)
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

//edittext 扩展
fun EditText.toText() = this.text.trim().toString()

//Textview 相关
fun TextView.toText() = this.text.trim().toString()

//SwipeRefreshLayout  显示刷新圈
fun SwipeRefreshLayout.show() {
    this.isRefreshing = true
}

//SwipeRefreshLayout 隐藏刷新圈
fun SwipeRefreshLayout.hide() {
    this.isRefreshing = false
}

fun SwipeRefreshLayout.init(end: Int = 150) {
    setProgressViewEndTarget(true, end)
    setColorSchemeResources(R.color.theme_end, R.color.theme_start)
}

fun <VH : RecyclerView.ViewHolder, A : RecyclerView.Adapter<VH>> RecyclerView.init(
    adapter: A,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
        context
    )
) {
    this.layoutManager = layoutManager
    this.adapter = adapter
}

//fun <VH : RecyclerView.ViewHolder, A : RecyclerView.Adapter<VH>> RecyclerView.init(adapter: A, column: Int) {
//    this.layoutManager = GridLayoutManager(context, column)
//    this.adapter = adapter
//}

//LoadingLayout 各种异常视图 TODO 异常图片
fun LoadingLayout.error(code: Int) {
    when (code) {
        1 -> {
            this.showError(R.mipmap.ic_launcher, "发生错误了")
        }
        else -> {
            this.showError(R.mipmap.ic_launcher, "发生错误了")
        }
    }
}

//数据是空的时候显示
fun LoadingLayout.checkEmpty(obj: Any?) {
    if (EmptyUtils.isEmpty(obj)) {
        this.showError(R.mipmap.ic_launcher, "数据为空~")
    } else {
        this.hide()
    }
}

fun ImageView.whitGlide(url: String?): ImageView {
    Glide.with(this)
        .load(url)
        .into(this)
    return this
}

//ImageView
//七牛图片加载 圆角
fun ImageView.whitGlideR(url: String?, mContext: Context, width: Int, hight: Int, roundInt: Int) {
//    /设置图片圆角角度
    val roundedCorners = RoundedCorners(roundInt)
    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
    val options: RequestOptions =
        RequestOptions.bitmapTransform(roundedCorners)
    Glide.with(mContext)
        .load(url)
        .apply(options)
        .into(this)
}

//渐变动画
fun ImageView.whitGlideAnimal1(url: String?) {
    Glide.with(this)
        .load(url)
        .transition(
            DrawableTransitionOptions.with(
                DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(
                    true
                ).build()
            )
        )
        .into(this)
}

fun CommonTabLayout.setTitles(onSelect: (selectPosition: Int) -> Unit, vararg titles: String) {
    val mTabEntities = ArrayList<CustomTabEntity>()
    for (title in titles) {
//        mTabEntities.add(TabEntity(title))
    }
    this.setTabData(mTabEntities)
    this.setOnTabSelectListener(object : OnTabSelectListener {
        override fun onTabSelect(position: Int) {
            onSelect(position)
        }

        override fun onTabReselect(position: Int) {
        }
    })
}

//View可见性
fun View.visibility(visble: Boolean) {
    this.visibility = if (visble) View.VISIBLE else View.GONE
}

fun View.visible() = this.visibility(true)
fun View.gone() = this.visibility(false)
fun View.invisible() = run { this.visibility = View.INVISIBLE }

//View 查找Id
fun <T : View> View.f(id: Int): T = this.findViewById(id)

//数据是空的时候显示
fun View.checkEmptyVisibility(obj: Any?) {
    this.visibility = if (EmptyUtils.isEmpty(obj)) View.VISIBLE else View.GONE
}

//fragment 相关
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}


fun BaseActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        val clazzName = fragment.javaClass.toString()
        val findFragment = supportFragmentManager.findFragmentByTag(clazzName)
        if (findFragment != null) {
            show(findFragment)
        } else {
            add(frameId, fragment, fragment.javaClass.toString())
        }
    }
}

fun BaseActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun BaseActivity.hideFragment(fragment: Fragment) {
    supportFragmentManager.inTransaction {
        hide(fragment)
    }
}


//adapter

fun <T, K : BaseViewHolder> BaseQuickAdapter<T, K>.setLoadView() {
    this.setLoadMoreView(object : LoadMoreView() {
        override fun getLayoutId(): Int {
            return R.layout.load_more_view
        }

        override fun getLoadingViewId(): Int {
            return R.id.load_more_loading_view
        }

        override fun getLoadFailViewId(): Int {
            return R.id.load_more_load_fail_view
        }

        override fun getLoadEndViewId(): Int {
            return R.id.load_more_load_end_view
        }
    })
}


fun BaseFragment.replaceFragment(fragment: Fragment, frameId: Int) {
    mActivity.supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun BaseFragment.hideFragment(fragment: Fragment, frameId: Int) {
    mActivity.supportFragmentManager.inTransaction {
        hide(fragment)
    }
}

fun BaseFragment.addFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction {
        val clazzName = fragment.javaClass.toString()
        val findFragment = childFragmentManager.findFragmentByTag(clazzName)
        if (findFragment != null) {
            show(findFragment)
        } else {
            add(frameId, fragment, fragment.javaClass.toString())
        }
    }
}



