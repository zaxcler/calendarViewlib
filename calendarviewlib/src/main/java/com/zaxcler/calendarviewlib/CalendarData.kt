package com.zaxcler.calendarviewlib


/**
 * 类描述：
 * 作者: Created by zaxcler on 2018/12/6.
 * 代码版本: 1.0
 * 邮箱: 610529094@qq.com
 */
/**
 *@param date 当前日期
 *@param day 显示日子
 *@param inMonth 是否属于当前月份
 *@param isChoose 是否选择
 *@param markState
 */
data class CalendarData(
    var date: String = "",
    var day: String = "",
    var inMonth: Boolean = true,
    var isChoose: Boolean = false,
    var markState:Int = 0
)