package com.highstreet.taobaocang.base

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author hasee
 * @date 2019-05-23
 */
abstract class BaseAdapter<T>(layoutResId: Int, data: List<T>) :

    BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data)
