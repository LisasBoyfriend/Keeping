package com.yang.tally.adapter

import android.content.Context
import android.graphics.Color
import android.widget.BaseAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.yang.tally.R
import android.widget.TextView
import java.util.ArrayList

/**
 * 历史账单页面，点击日历表，弹出对话框，当中的GridView对应的适配器
 */
class CalendarAdapter(var context: Context, var yearCount: Int) : BaseAdapter() {
    var mDatas: MutableList<String>
    @JvmField
    var selPos = -1
    fun setYear(year: Int) {
        this.yearCount = year
        mDatas.clear()
        loadDatas(year)
        notifyDataSetChanged()
    }

    private fun loadDatas(year: Int) {
        for (i in 1..12) {
            val data = "$year/$i"
            mDatas.add(data)
        }
    }

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItem(position: Int): Any {
        return mDatas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView =
            LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv, parent, false)
        val tv = convertView.findViewById<TextView>(R.id.item_dialogcal_gv_tv)
        tv.text = mDatas[position]
        tv.setBackgroundResource(R.color.grey_f3f3f3)
        tv.setTextColor(Color.BLACK)
        if (position == selPos) {
            tv.setBackgroundResource(R.color.green_006400)
            tv.setTextColor(Color.WHITE)
        }
        return convertView
    }

    init {
        mDatas = ArrayList()
        loadDatas(yearCount)
    }
}