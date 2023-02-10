package com.yang.tally.adapter

import android.content.Context
import com.yang.tally.db.ChartItemBean
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.yang.tally.R
import com.yang.tally.utils.FloatUtils
import android.widget.TextView

class ChartItemAdapter(
    /**
     * 账单详情页面ListView的适配器
     * @return
     */
    var context: Context, var mDatas: List<ChartItemBean>?
) : BaseAdapter() {
    var inflater: LayoutInflater
    override fun getCount(): Int {
        return mDatas?.size!!
    }

    override fun getItem(position: Int): ChartItemBean? {
        return mDatas?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chartfrag_lv, parent, false)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        //获取显示内容
        val bean = mDatas?.get(position)
        bean?.let { holder!!.iv.setImageResource(it.sImageId) }
        holder.typeTv.text = bean?.type
        if (bean != null) {
            holder.totalTv.text = "￥ " + bean.totalMoney
        }
        val radio = bean?.radio
        val pert = radio?.let { FloatUtils.ratioToPercent(it) }
        holder.ratioTv.text = pert
        return convertView
    }

    internal inner class ViewHolder(view: View) {
        var typeTv: TextView
        var ratioTv: TextView
        var totalTv: TextView
        var iv: ImageView

        init {
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type)
            ratioTv = view.findViewById(R.id.item_chartfrag_tv_pert)
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum)
            iv = view.findViewById(R.id.item_chartfrag_iv)
        }
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}