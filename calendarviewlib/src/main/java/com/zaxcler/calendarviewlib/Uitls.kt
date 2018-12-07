package com.zaxcler.calendarviewlib

import android.content.res.Resources

/**
 * 类描述：
 * 作者: Created by zaxcler on 2018/12/6.
 * 代码版本: 1.0
 * 邮箱: 610529094@qq.com
 */
object Uitls{
    fun dip2px(dp:Int):Int{
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
    fun px2dip(dp:Int):Int{
        val scale = Resources.getSystem().displayMetrics.density
        return (dp / scale + 0.5f).toInt()
    }

    fun dip2px(dp:Float):Int{
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun px2dip(dp:Float):Int{
        val scale = Resources.getSystem().displayMetrics.density
        return (dp / scale + 0.5f).toInt()
    }
}