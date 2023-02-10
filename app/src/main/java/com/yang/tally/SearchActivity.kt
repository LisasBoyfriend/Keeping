package com.yang.tally

import com.yang.tally.db.DBManager.getAccountListByRemarkFromAccounttb
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.yang.tally.db.AccountBean
import com.yang.tally.adapter.AccountAdapter
import android.os.Bundle
import com.yang.tally.R
import android.text.TextUtils
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.yang.tally.db.DBManager
import java.util.ArrayList

class SearchActivity : AppCompatActivity() {
    var searchLv: ListView? = null
    var searchEt: EditText? = null
    var emptyTv: TextView? = null
    var mDatas //数据源
            : MutableList<AccountBean>? = null
    var adapter //适配器对象
            : AccountAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        mDatas = ArrayList()
        adapter = AccountAdapter(this, mDatas as ArrayList<AccountBean>)
        searchLv!!.adapter = adapter
        searchLv!!.emptyView = emptyTv //设置无数据时显示的控件
    }

    private fun initView() {
        searchLv = findViewById(R.id.search_lv)
        searchEt = findViewById(R.id.search_et)
        emptyTv = findViewById(R.id.search_tv_empty)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.search_iv_back -> finish()
            R.id.search_iv_sh -> {
                val msg = searchEt!!.text.toString().trim { it <= ' ' }
                //判断输入内容是否为空,如果是空，就提示不能搜索
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this, "输入内容不能为空！", Toast.LENGTH_SHORT).show()
                    return
                }
                //开始搜索
                val list = getAccountListByRemarkFromAccounttb(msg)
                mDatas!!.clear()
                mDatas!!.addAll(list)
                adapter!!.notifyDataSetChanged()
            }
        }
    }
}