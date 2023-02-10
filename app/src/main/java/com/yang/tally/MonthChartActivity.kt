package com.yang.tally

import android.graphics.Color
import com.yang.tally.db.DBManager.getSumMoneyOneMonth
import com.yang.tally.db.DBManager.getCountItemOneMonth
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.yang.tally.frag_chart.IncomeChartFragment
import com.yang.tally.frag_chart.OutcomeChartFragment
import com.yang.tally.adapter.ChartVPAdapter
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.yang.tally.R
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.yang.tally.db.DBManager
import com.yang.tally.utils.CalendarDialog
import java.util.*

class MonthChartActivity : AppCompatActivity() {
    var inBtn: Button? = null
    var outBtn: Button? = null
    var dateTv: TextView? = null
    var inTv: TextView? = null
    var outTv: TextView? = null
    var chartVp: ViewPager? = null
    private var year = 0
    private var month = 0
    var selectPos = -1
    var selectMonth = -1
    var chartFragList: MutableList<Fragment>? = null
    private var incomeChartFragment: IncomeChartFragment? = null
    private var outcomeChartFragment: OutcomeChartFragment? = null
    private var chartVPAdapter: ChartVPAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_chart)
        initView()
        initTime()
        initStatistics(year, month)
        initFrag()
        setVPSelectListener()
    }

    private fun setVPSelectListener() {
        chartVp!!.addOnPageChangeListener(object : SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                setButtonStyle(position)
            }
        })
    }

    private fun initFrag() {
        chartFragList = ArrayList()
        //添加fragment对象
        incomeChartFragment = IncomeChartFragment()
        outcomeChartFragment = OutcomeChartFragment()
        //添加数据到fragment当中
        val bundle = Bundle()
        bundle.putInt("year", year)
        bundle.putInt("month", month)
        incomeChartFragment!!.arguments = bundle
        outcomeChartFragment!!.arguments = bundle
        //将fragment添加到数据源当中
        chartFragList?.add(outcomeChartFragment!!)
        chartFragList?.add(incomeChartFragment!!)
        //使用适配器
        chartVPAdapter = ChartVPAdapter(supportFragmentManager,
            chartFragList as ArrayList<Fragment>
        )
        chartVp!!.adapter = chartVPAdapter
        //将fragment加载到activity 当中
    }

    /**统计某年某月的收支情况数据
     * @param year
     * @param month
     */
    private fun initStatistics(year: Int, month: Int) {
        val inMoneyOneMonth = getSumMoneyOneMonth(year, month, 1) //收入总钱数
        val outMoneyOneMonth = getSumMoneyOneMonth(year, month, 0) //支出总钱数
        val incountItemOneMonth = getCountItemOneMonth(year, month, 1) //收入多少笔
        val outcountItemOneMonth = getCountItemOneMonth(year, month, 0) //支出多少笔
        dateTv!!.text = year.toString() + "年" + month + "月账单"
        inTv!!.text = "共" + incountItemOneMonth + "笔收入, ￥ " + inMoneyOneMonth
        outTv!!.text = "共" + outcountItemOneMonth + "笔支出, ￥ " + outMoneyOneMonth
    }

    /**
     * 初始化时间的方法
     */
    private fun initTime() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1
    }

    private fun initView() {
        inBtn = findViewById(R.id.chart_btn_in)
        outBtn = findViewById(R.id.chart_btn_out)
        dateTv = findViewById(R.id.chart_tv_date)
        inTv = findViewById(R.id.chart_tv_in)
        outTv = findViewById(R.id.chart_tv_out)
        chartVp = findViewById(R.id.chart_vp)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.chart_iv_back -> finish()
            R.id.chart_iv_rili -> showCalendarDialog()
            R.id.chart_btn_in -> {
                setButtonStyle(1)
                chartVp!!.currentItem = 1
            }
            R.id.chart_btn_out -> {
                setButtonStyle(0)
                chartVp!!.currentItem = 0
            }
        }
    }

    /**
     * 显示日历的对话框
     */
    private fun showCalendarDialog() {
        val dialog = CalendarDialog(this, selectPos, selectMonth)
        dialog.show()
        dialog.setDialogSize()
        dialog.onRefreshListener = object : CalendarDialog.OnRefreshListener {
            override fun onRefresh(selPos: Int, year: Int, month: Int) {
                selectPos = selPos
                selectMonth = month
                initStatistics(year, month)
                incomeChartFragment!!.setDate(year, month)
                outcomeChartFragment!!.setDate(year, month)
            }
        }
    }

    /**
     * 设置按钮样式的改变 支出-0，收入-1
     */
    private fun setButtonStyle(kind: Int) {
        if (kind == 0) {
            outBtn!!.setBackgroundResource(R.drawable.main_recordbtn_bg)
            outBtn!!.setTextColor(Color.WHITE)
            inBtn!!.setBackgroundResource(R.drawable.dialog_btn_bg)
            inBtn!!.setTextColor(Color.BLACK)
        } else if (kind == 1) {
            inBtn!!.setBackgroundResource(R.drawable.main_recordbtn_bg)
            inBtn!!.setTextColor(Color.WHITE)
            outBtn!!.setBackgroundResource(R.drawable.dialog_btn_bg)
            outBtn!!.setTextColor(Color.BLACK)
        }
    }
}