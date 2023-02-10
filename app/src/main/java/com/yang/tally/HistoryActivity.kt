package com.yang.tally

import com.yang.tally.db.DBManager.deleteItemFromAccounttbById
import com.yang.tally.db.DBManager.getAccountListOneMonthFromAccounttb
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.yang.tally.db.AccountBean
import com.yang.tally.adapter.AccountAdapter
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.yang.tally.utils.CalendarDialog
import java.util.*

class HistoryActivity : AppCompatActivity() {
    var historyLv: ListView? = null
    var timeTv: TextView? = null
    var year = 0
    var month = 0
    var mDatas: MutableList<AccountBean>? = null
    var adapter: AccountAdapter? = null
    var dialogSelPos = -1
    var dialogSelMonth = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        historyLv = findViewById(R.id.history_lv)
        timeTv = findViewById(R.id.history_tv_time)
        mDatas = ArrayList()
        //设置适配器
        adapter = AccountAdapter(this, mDatas as ArrayList<AccountBean>)
        historyLv?.setAdapter(adapter)
        initTime()
        timeTv?.setText(year.toString() + "年" + month + "月")
        loadData(year, month)
        setLVClickListener()
    }

    /**
     * 设置ListView每一个item的长按事件
     */
    private fun setLVClickListener() {
        historyLv!!.onItemLongClickListener =
            OnItemLongClickListener { parent, view, position, id ->
                val accountBean = mDatas!![position]
                deleteItem(accountBean)
                false
            }
    }

    private fun deleteItem(accountBean: AccountBean) {
        val delId = accountBean.id
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗？")
            .setNegativeButton("取消", null)
            .setPositiveButton("确定") { dialog, which ->
                deleteItemFromAccounttbById(delId)
                mDatas!!.remove(accountBean)
                adapter!!.notifyDataSetChanged()
            }
        builder.create().show()
    }

    /**
     * 获取指定年。月份收支情况列表
     */
    private fun loadData(year: Int, month: Int) {
        val list = getAccountListOneMonthFromAccounttb(year, month)
        mDatas!!.clear()
        mDatas!!.addAll(list)
        adapter!!.notifyDataSetChanged()
    }

    private fun initTime() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.history_iv_back -> finish()
            R.id.history_iv_rili -> {
                val dialog = CalendarDialog(this, dialogSelPos, dialogSelMonth)
                dialog.show()
                dialog.setDialogSize()
                dialog.onRefreshListener = object : CalendarDialog.OnRefreshListener {
                    override fun onRefresh(selPos: Int, year: Int, month: Int) {
                        timeTv!!.text = year.toString() + "年" + month + "月"
                        loadData(year, month)
                        dialogSelPos = selPos
                        dialogSelMonth = month
                    }
                }
            }
        }
    }
}