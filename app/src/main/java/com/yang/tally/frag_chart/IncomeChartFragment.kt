package com.yang.tally.frag_chart

import android.graphics.Color
import android.view.View
import com.yang.tally.db.DBManager.getSumMoneyOneDayInMonth

import com.yang.tally.db.DBManager.getMaxMoneyOneDayInMonth

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.data.BarData
import java.util.ArrayList

class IncomeChartFragment : BaseChartFragment() {
    var kind = 1
    override fun onResume() {
        super.onResume()
        loadData(year, month, kind)
    }

    override fun setAxisData(year: Int, month: Int) {
        val sets: MutableList<IBarDataSet> = ArrayList()
        //获取这个月每天的支出总金额
        val list = getSumMoneyOneDayInMonth(year, month, kind)
        if (list.size == 0) {
            barChart!!.visibility = View.GONE
            chartTv!!.visibility = View.VISIBLE
        } else {
            barChart!!.visibility = View.VISIBLE
            chartTv!!.visibility = View.GONE
            //设置有多少根柱子
            val barEntries1: MutableList<BarEntry> = ArrayList()
            for (i in 0..30) {
                //初始化每一根柱子，添加到柱状图当中
                val entry = BarEntry(i.toFloat(), 0.0f)
                barEntries1.add(entry)
            }
            for (i in list.indices) {
                val itemBean = list[i]
                val day = itemBean.day //获取日期
                //根据天数获取X轴的位置
                val xIndex = day - 1
                val barEntry = barEntries1[xIndex]
                barEntry.y = itemBean.summoney
            }
            val barDataSet1 = BarDataSet(barEntries1, "")
            barDataSet1.valueTextColor = Color.BLACK // 值的颜色
            barDataSet1.valueTextSize = 8f // 值的大小
            barDataSet1.color = Color.parseColor("#006400") // 柱子的颜色

            // 设置柱子上数据显示的格式
            barDataSet1.valueFormatter =
                IValueFormatter { value, entry, dataSetIndex, viewPortHandler -> // 此处的value默认保存一位小数
                    if (value == 0f) {
                        ""
                    } else value.toString() + ""
                }
            sets.add(barDataSet1)
            val barData = BarData(sets)
            barData.barWidth = 0.2f // 设置柱子的宽度
            barChart!!.data = barData
        }
    }

    override fun setYAxis(year: Int, month: Int) {

        //获取本月收入最高的一天为多少，将它设定为Y轴最大值
        val maxMoney = getMaxMoneyOneDayInMonth(year, month, kind)
        val max = Math.ceil(maxMoney.toDouble()).toFloat() //将最大金额向上取整
        //设置Y轴
        val yAxis_right = barChart!!.axisRight
        yAxis_right.axisMaximum = max // 设置y轴的最大值
        yAxis_right.axisMinimum = 0f // 设置y轴的最小值
        yAxis_right.isEnabled = false // 不显示右边的y轴
        val yAxis_left = barChart!!.axisLeft
        yAxis_left.axisMaximum = max
        yAxis_left.axisMinimum = 0f
        yAxis_left.isEnabled = false
        //设置不显示图例
        val legend = barChart!!.legend
        legend.isEnabled = false
    }

    override fun setDate(year: Int, month: Int) {
        super.setDate(year, month)
        loadData(year, month, kind)
        //清空柱状图中的数据
        barChart!!.clear()
        barChart!!.invalidate() //重新绘制柱状图
        setAxis(year, month)
        setAxisData(year, month)
    }
}