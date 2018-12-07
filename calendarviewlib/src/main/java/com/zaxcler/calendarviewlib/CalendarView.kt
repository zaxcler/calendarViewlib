package com.zaxcler.calendarviewlib

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zaxcler.calendarviewlib.Uitls.dip2px
import java.text.SimpleDateFormat
import java.util.*

/**
 * 类描述：
 * 作者: Created by zaxcler on 2018/12/6.
 * 代码版本: 1.0
 * 邮箱: 610529094@qq.com
 */
class CalendarView : LinearLayout {

    private val dayViews = arrayListOf<ViewGroup>()
    private var dianWidth = dip2px(5) //点宽
    private var textChooseColor = Color.WHITE  //字体选中颜色
    private var textNormalColor = Color.BLACK  //字体正常颜色
    private var textIgnoreColor = Color.GRAY //字体忽略颜色
    private var bgChoose = 0//选中资源
    private var markNormal = 0//标记正常资源
    private var markState = 0//标记状态资源
    private var datas = arrayListOf<CalendarData>()


    constructor(context: Context?) : super(context){
        initView(null)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        orientation = VERTICAL
        val topLayout = LinearLayout(context)
        val topLp =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        topLayout.layoutParams = topLp
        addView(topLayout)
        initTopWeek(topLayout)
        initDayView()
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        textChooseColor = ta.getColor(R.styleable.CalendarView_text_color_choose,Color.WHITE)
        textNormalColor = ta.getColor(R.styleable.CalendarView_text_color_normal,Color.BLACK)
        textIgnoreColor = ta.getColor(R.styleable.CalendarView_text_color_ignore,Color.GRAY)
        bgChoose = ta.getResourceId(R.styleable.CalendarView_choose_bg,0)
        markNormal = ta.getResourceId(R.styleable.CalendarView_mark_normal,0)
        markState = ta.getResourceId(R.styleable.CalendarView_mark_state,0)
//        string = ta.getString(R.styleable.CalendarView_test) ?: ""
        ta.recycle()
    }

    /**
     * 初始化星期头部
     * */
    private fun initTopWeek(topLayout: LinearLayout) {
        for (i in 0 until 7) {
            val topView = TextView(context)
            topView.text = when (i) {
                0 -> "一"
                1 -> "二"
                2 -> "三"
                3 -> "四"
                4 -> "五"
                5 -> "六"
                6 -> "日"
                else -> ""
            }
            val tlp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            topView.textSize = 14f
            topView.gravity = Gravity.CENTER
            tlp.weight = 1f
            topView.layoutParams = tlp
            topLayout.addView(topView)
        }
    }


    /**
     * 初始化视图
     * 利用tablelayout 作为日历
     * */
    private fun initDayView() {
        val dayLayout = TableLayout(context)
        val llp =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        llp.topMargin = dip2px(10)
        dayLayout.layoutParams = llp
        addView(dayLayout)

        /**
         * 日历是5 * 7 的 表格构成
         * */
        var index = 0
        for (i in 0 until 6) {
            val row = TableRow(context)
            for (j in 0 until 7) {

                val dayView = LinearLayout(context)
                val rowLp = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                rowLp.weight = 1f
                dayView.gravity = Gravity.CENTER
                dayView.orientation = VERTICAL
                dayView.layoutParams = rowLp

                val day = TextView(context)
                val dayLp = LinearLayout.LayoutParams(dip2px(22), dip2px(22))
                day.layoutParams = dayLp
                day.text = "1"
                day.gravity = Gravity.CENTER
                dayView.addView(day)

                val dian = ImageView(context)
                val dianLp = LinearLayout.LayoutParams(dianWidth, dianWidth)
                dianLp.setMargins(0, dip2px(2), 0, dip2px(2))
                dian.layoutParams = dianLp
                dian.visibility = View.VISIBLE
                dayView.addView(dian)
                row.addView(dayView)
                dayViews.add(dayView)
                val temp = index
                dayView.setOnClickListener { _ ->
                    if (temp < datas.size) {
                        if (!datas[temp].inMonth){
                            return@setOnClickListener
                        }
                        datas.forEach { it.isChoose = false }
                        datas[temp].isChoose = true
                        notifyDataChange()
                        mOnDateChooseListener?.invoke(datas[temp])
                    }

                }
                index++
            }
            dayLayout.addView(row)
        }
        val calendar = Calendar.getInstance(Locale.CHINA)

        setData(calendar,null)
    }

    fun setData(calendar: Calendar,stateList: List<StateData>?) {
        val clone = calendar.clone() as Calendar
        val currentDay = clone.get(Calendar.DAY_OF_YEAR)//当前日期
        clone.set(Calendar.DAY_OF_MONTH, 1) // 设到当前月份的第一天
        val month = clone.get(Calendar.MONTH)//当前月份
        val weekOfFirstDay = clone.get(Calendar.DAY_OF_WEEK) // 获取本月第一天是星期几

        val day2push = if (weekOfFirstDay == 1) { //美国星期日是开始
            6
        } else {
            weekOfFirstDay - 2
        }
        clone.add(Calendar.DAY_OF_MONTH, -day2push)// 向前推 几天
        datas.clear()

        for (i in 0 until 42) {
            val day = clone.get(Calendar.DAY_OF_MONTH)
            val time = SimpleDateFormat("YYYY-MM-dd").format(clone.time)
            val  state =  stateList?.find { it.date == time }?.state?:0 //标记状态
            val calendarData = CalendarData(
                time,
                "$day",
                month == clone.get(Calendar.MONTH),
                currentDay == clone.get(Calendar.DAY_OF_YEAR),
                state
            )
            datas.add(calendarData)
            setValue(dayViews[i], calendarData)
            clone.add(Calendar.DAY_OF_MONTH, 1)
        }

    }

    private fun setValue(viewGroup: ViewGroup, calendarData: CalendarData) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is TextView) {
                child.text = calendarData.day
                if (calendarData.inMonth) { // 不在本月的显示灰色，本月显示黑色
                    child.setTextColor(textNormalColor)
                } else {
                    child.setTextColor(textIgnoreColor)
                }
                if (calendarData.isChoose) { //选中的有背景
                    child.setBackgroundResource(bgChoose)
                    child.setTextColor(textChooseColor)
                } else {
                    child.setBackgroundColor(0)
                }
            }
            if (child is ImageView) {// 标记是否显示
                if (calendarData.markState!=0){
                    child.visibility = View.VISIBLE
                    if (calendarData.markState == 1){
                        child.setBackgroundResource(markNormal)
                    }else if (calendarData.markState == 2){
                        child.setBackgroundResource(markState)
                    }
                }else{
                    child.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun notifyDataChange() {
        dayViews.forEachIndexed { index, viewGroup ->
            if (index < datas.size) {
                setValue(viewGroup, datas[index])
            }
        }
    }

    private var mOnDateChooseListener: ((data: CalendarData) -> Unit)? = null
    fun addOnDateChooseListener(onDateChooseListener: ((data: CalendarData) -> Unit)?) {
        mOnDateChooseListener = onDateChooseListener
    }

}