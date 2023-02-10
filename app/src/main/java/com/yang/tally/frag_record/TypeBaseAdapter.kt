package com.yang.tally.frag_record

import android.content.Context
import com.yang.tally.db.TypeBean
import android.widget.BaseAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.yang.tally.R
import android.widget.TextView

class TypeBaseAdapter(var context: Context, var mDatas: List<TypeBean>) : BaseAdapter() {
    var selectPos = 0 //选中位置
    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItem(position: Int): Any {
        return mDatas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //这个适配器不考虑复用问题，因为所有item都显示在页面上，不会因为滑动消失，所以没有剩余的convertView，所以不用复写
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView =
            LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false)
        //查找布局当中的控件
        val iv = convertView.findViewById<ImageView>(R.id.item_recordfrag_iv)
        val tv = convertView.findViewById<TextView>(R.id.item_recordfrag_tv)
        //获取指定位置的数据源
        val typeBean = mDatas[position]
        tv.text = typeBean.typename
        //判断当前位置是否为选中位置，如果是选中的位置就显示成有颜色的图片
        if (selectPos == position) {
            iv.setImageResource(typeBean.simageId)
        } else {
            iv.setImageResource(typeBean.imageId)
        }
        return convertView
    }
}