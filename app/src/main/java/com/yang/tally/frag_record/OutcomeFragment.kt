package com.yang.tally.frag_record

import com.yang.tally.db.DBManager.getTypeList
import com.yang.tally.db.DBManager.insertItemToAccounttb
import com.yang.tally.R

/**
 *
 */
class OutcomeFragment : BaseRecordFragment() {
    //重写
    override fun loadDataToGv() {
        super.loadDataToGv()
        //获取数据库当中的数据源
        val outList = getTypeList(0)
        typeList!!.addAll(outList)
        adapter!!.notifyDataSetChanged()
        typeTv!!.text = "其他"
        typeIv!!.setImageResource(R.mipmap.ic_qita_fs)
    }

    override fun saveAccountToDB() {
        accountBean!!.kind = 0
        insertItemToAccounttb(accountBean!!)
    }
}