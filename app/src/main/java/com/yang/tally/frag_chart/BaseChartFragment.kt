package com.yang.tally.frag_chart

import com.yang.tally.db.DBManager.getChastListFromAccounttb
import com.yang.tally.db.ChartItemBean
import com.yang.tally.adapter.ChartItemAdapter
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.yang.tally.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.AxisBase
import com.yang.tally.db.DBManager
import java.util.ArrayList

abstract class BaseChartFragment : Fragment() {
    var chartLv: ListView? = null
    @JvmField
    var year = 0
    @JvmField
    var month = 0
    var mDatas: MutableList<ChartItemBean>? = null
    private var itemAdapter: ChartItemAdapter? = null
    @JvmField
    var barChart //柱状图控件
            : BarChart? = null
    @JvmField
    var chartTv //如果没有收支情况，显示的文本控件
            : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_income_chart, container, false)
        chartLv = view.findViewById(R.id.frag_chart_lv)
        //获取activity传递的数据
        val bundle = arguments
        year = bundle!!.getInt("year")
        month = bundle.getInt("month")
        //设置数据源
        mDatas = ArrayList()
        //设置适配器
        itemAdapter = ChartItemAdapter(context!!, mDatas)
        chartLv?.setAdapter(itemAdapter)
        //添加头布局
        addLVHeaderView()
        return view
    }

    protected fun addLVHeaderView() {
        //将布局转化成View对象
        val headerView = layoutInflater.inflate(R.layout.item_chartfrag_top, null)
        //将View添加到ListView的头布局上
        chartLv!!.addHeaderView(headerView)
        //查找头布局当中包含的控件
        barChart = headerView.findViewById(R.id.item_chartfrag_chart)
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv)
        //设定柱状图不显示描述
        barChart?.getDescription()?.isEnabled = false
        //设置柱状图的内边距
        barChart?.setExtraOffsets(20f, 20f, 20f, 20f)
        setAxis(year, month) //设置坐标轴
        //设置坐标轴显示的数据
        setAxisData(year, month)
    }

    /**
     * 设置坐标轴显示的数据
     * @param year
     * @param month
     */
    protected abstract fun setAxisData(year: Int, month: Int)

    /**
     * 用来设置柱状图坐标轴的显示：方法必须重写
     * @param year
     * @param month
     */
    protected fun setAxis(year: Int, month: Int) {

        //设置X轴
        val xAxis = barChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM //设置X轴显示在下方
        xAxis.setDrawGridLines(true) //设置绘制该轴的网格线
        //设置X轴标签的个数
        xAxis.labelCount = 31
        xAxis.textSize = 12f //X轴标签大小
        //设置X轴显示的值的格式
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            val `val` = value.toInt()
            if (`val` == 0) {
                return@IAxisValueFormatter "$month-1"
            }
            if (`val` == 14) {
                return@IAxisValueFormatter "$month-15"
            }

            //根据不同的月份显示最后一天的位置
            if (month == 2) {
                if (`val` == 27) {
                    return@IAxisValueFormatter "$month-28"
                }
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (`val` == 30) {
                    return@IAxisValueFormatter "$month-31"
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (`val` == 29) {
                    return@IAxisValueFormatter "$month-30"
                }
            }
            ""
        }
        xAxis.yOffset = 10f //设置标签对X轴的偏移量，垂直方向
        //Y轴在子类的设置
        setYAxis(year, month)
    }

    /**
     * 设置Y轴，因为最高的坐标不确定，所以在子类中设置
     */
    protected abstract fun setYAxis(year: Int, month: Int)
    open fun setDate(year: Int, month: Int) {
        this.year = year
        this.month = month
    }

    fun loadData(year: Int, month: Int, kind: Int) {
        val list = getChastListFromAccounttb(year, month, kind)
        mDatas!!.clear()
        mDatas!!.addAll(list)
        itemAdapter!!.notifyDataSetChanged()
    }
}