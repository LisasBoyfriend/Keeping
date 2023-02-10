package com.yang.tally.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import com.yang.tally.db.DBManager.yearListFromAccounttb
import com.yang.tally.adapter.CalendarAdapter
import android.os.Bundle
import com.yang.tally.R
import android.widget.AdapterView.OnItemClickListener
import android.view.Gravity
import android.view.View
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarDialog(context: Context, selectPos: Int, selectMonth: Int) : Dialog(context),
    View.OnClickListener {
    var errorIv: ImageView? = null
    var gv: GridView? = null
    var hsvLayout: LinearLayout? = null
    var hsvViewList: MutableList<TextView>? = null
    var yearList: MutableList<Int>? = null
    var selectPos = -1 //表示正在被点击的年份的位置
    private var adapter: CalendarAdapter? = null
    var selectMonth = -1

    interface OnRefreshListener {
        fun onRefresh(selPos: Int, year: Int, month: Int)
    }

    var onRefreshListener: OnRefreshListener? = null
    @JvmName("setOnRefreshListener1")
    fun setOnRefreshListener(onRefreshListener: OnRefreshListener?) {
        this.onRefreshListener = onRefreshListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_calendar)
        gv = findViewById(R.id.dialog_calendar_gv)
        errorIv = findViewById(R.id.dialog_calendar_iv)
        hsvLayout = findViewById(R.id.dialog_calendar_layout)
        errorIv?.setOnClickListener(this)

        //向横向的scrollView当中添加View的方法
        addViewLayout()
        initGridView()
        //设置GridView当中每一个item的点击事件
        setGVListener()
    }

    private fun setGVListener() {
        gv!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            adapter!!.selPos = position
            adapter!!.notifyDataSetChanged()
            //获取到被选中的年份和月份
            val month = position + 1
            val year = adapter!!.yearCount
            onRefreshListener!!.onRefresh(selectPos, year, month)
            cancel()
        }
    }

    private fun initGridView() {
        val selYear = yearList!![selectPos]
        adapter = CalendarAdapter(context, selYear)
        if (selectMonth == -1) {
            val month = Calendar.getInstance()[Calendar.MONTH]
            adapter!!.selPos = month
        } else {
            adapter!!.selPos = selectMonth - 1
        }
        gv!!.adapter = adapter
    }

    private fun addViewLayout() {
        hsvViewList = ArrayList() //将添加进入线性布局当中的TextView进行统一管理的集合
        yearList = yearListFromAccounttb //获取数据库当中存储了多少年分
        //如果数据库当中没有记录，就添加今年的信息
        if (yearList!!.size == 0) {
            val year = Calendar.getInstance()[Calendar.YEAR]
            yearList?.add(year)
        }

        //遍历年份，有几年就向scrollView当中添加几个View
        for (i in yearList!!.indices) {
            val year = yearList!![i]
            val view = layoutInflater.inflate(R.layout.item_dialogcal_hsv, null)
            hsvLayout!!.addView(view)
            val hsvTv = view.findViewById<TextView>(R.id.item_dialogcal_hsv_tv)
            hsvTv.text = year.toString() + ""
            hsvViewList?.add(hsvTv)
        }
        if (selectPos == -1) {
            selectPos = (hsvViewList?.size?.minus(1)!!)//设置当前被选中的是最近的年份
        }
        changeTvbg(selectPos) //将最后一个设置为选中状态
        setHSVClickListener() //设置每一个view的监听事件
    }

    /**
     * 给横向的ScrollView当中每一个TextView设置点击事件
     */
    private fun setHSVClickListener() {
        for (i in hsvViewList!!.indices) {
            val view = hsvViewList!![i]
            view.setOnClickListener {
                changeTvbg(i)
                selectPos = i
                //获取被选中的年份，然后下面的GridView显示数据源会发生变化
                val year = yearList!![selectPos]
                adapter!!.setYear(year)
            }
        }
    }

    /**
     * 传入被选中的位置，改变此位置上的背景和文字颜色
     * @param selectPos
     */
    private fun changeTvbg(selectPos: Int) {
        for (i in hsvViewList!!.indices) {
            val tv = hsvViewList!![i]
            tv.setBackgroundResource(R.drawable.dialog_btn_bg)
            tv.setTextColor(Color.BLACK)
        }
        val selView = hsvViewList!![selectPos]
        selView.setBackgroundResource(R.drawable.main_recordbtn_bg)
        selView.setTextColor(Color.WHITE)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dialog_calendar_iv -> cancel()
        }
    }

    /*设置dialog的尺寸和屏幕尺寸一致*/
    fun setDialogSize() {
        //获取当前窗口对象
        val window = window
        //获取窗口对象的参数
        val wlp = window!!.attributes
        //获取屏幕宽度
        val d = window.windowManager.defaultDisplay
        wlp.width = d.width //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.TOP
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.attributes = wlp
    }

    init {
        this.selectPos = selectPos
        this.selectMonth = selectMonth
    }
}