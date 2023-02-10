package com.yang.tally.frag_record

import com.yang.tally.db.DBManager.getTypeList
import com.yang.tally.db.DBManager.insertItemToAccounttb
import com.yang.tally.R

class IncomeFragment : BaseRecordFragment() {
    override fun loadDataToGv() {
        super.loadDataToGv()
        //获取数据库当中的数据源
        val inList = getTypeList(1)
        typeList?.addAll(inList)
        adapter!!.notifyDataSetChanged()
        typeTv!!.text = "其他"
        typeIv!!.setImageResource(R.mipmap.in_qt_fs)
    }

    override fun saveAccountToDB() {
        accountBean!!.kind = 1
        insertItemToAccounttb(accountBean!!)
    }
}