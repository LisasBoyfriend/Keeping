package com.yang.tally

import com.yang.tally.db.DBManager.deleteItemFromAccounttbById
import com.yang.tally.db.DBManager.getSumMoneyOneDay
import com.yang.tally.db.DBManager.getSumMoneyOneMonth
import com.yang.tally.db.DBManager.getAccountListOneDayFromAccounttb
import androidx.appcompat.app.AppCompatActivity
import com.yang.tally.db.AccountBean
import com.yang.tally.adapter.AccountAdapter
import android.content.SharedPreferences
import android.os.Bundle
import com.yang.tally.R
import android.widget.AdapterView.OnItemLongClickListener
import com.yang.tally.db.DBManager
import android.content.Intent
import com.yang.tally.SearchActivity
import com.yang.tally.RecordActivity
import com.yang.tally.utils.MoreDialog
import com.yang.tally.MonthChartActivity
import com.yang.tally.utils.BudgetDialog
import android.content.SharedPreferences.Editor
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var todayLv //展示今日收支情况的ListView;
            : ListView? = null
    var searchIv: ImageView? = null
    var editBtn: Button? = null
    var moreBtn: ImageButton? = null

    //声明数据源
    var mDates: MutableList<AccountBean>? = null
    var adapter: AccountAdapter? = null
    var year = 0
    var month = 0
    var day = 0

    //头布局相关控件
    var headerView: View? = null
    var topOutTv: TextView? = null
    var topInTv: TextView? = null
    var topBudgetTv: TextView? = null
    var topConTv: TextView? = null
    var topShowIv: ImageView? = null
    var preferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initTime()
        preferences = getSharedPreferences("budget", MODE_PRIVATE)
        todayLv = findViewById(R.id.main_lv)
        //添加ListView的头布局
        addLvHeaderView()
        mDates = ArrayList()
        //设置适配器，加载每一行数据到列表中
        adapter = AccountAdapter(this, mDates as ArrayList<AccountBean>)
        todayLv?.setAdapter(adapter)
    }

    //给ListView添加头布局的方法
    private fun addLvHeaderView() {
        //将布局转化为View对象
        headerView = layoutInflater.inflate(R.layout.item_mainlv_top, null)
        todayLv!!.addHeaderView(headerView)
        //查找头布局可用控件
        topOutTv = headerView?.findViewById(R.id.item_mainlv_top_tv_out)
        topInTv = headerView?.findViewById(R.id.item_mainlv_top_tv_in)
        topBudgetTv = headerView?.findViewById(R.id.item_mainlv_top_tv_budget)
        topConTv = headerView?.findViewById(R.id.item_mainlv_top_tv_day)
        topShowIv = headerView?.findViewById(R.id.item_mainlv_iv_hide)
        topBudgetTv?.setOnClickListener(this)
        headerView?.setOnClickListener(this)
        topShowIv?.setOnClickListener(this)
    }

    /*初始化自带的View方法*/
    private fun initView() {
        todayLv = findViewById(R.id.main_lv)
        editBtn = findViewById(R.id.main_btn_edit)
        moreBtn = findViewById(R.id.main_btn_more)
        searchIv = findViewById(R.id.main_iv_search)
        editBtn?.setOnClickListener(this)
        moreBtn?.setOnClickListener(this)
        searchIv?.setOnClickListener(this)
        setLVLongClickListener()
    }

    /**
     * 设置ListView的长按事件
     */
    private fun setLVLongClickListener() {
        todayLv!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            if (position == 0) { //点击了头布局
                return@OnItemLongClickListener false
            }
            val pos = position - 1
            val clickBean = mDates!![pos] //获取正在被电击的信息

            //弹出提示用户是否删除的对话框
            showDeleteItemDialog(clickBean)
            false
        }
    }

    /**
     * 弹出是否删除某一条记录的对话框
     */
    private fun showDeleteItemDialog(clickBean: AccountBean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗？")
            .setNegativeButton("取消", null)
            .setPositiveButton("确定") { dialog, which ->
                val click_id = clickBean.id
                //执行删除的操作
                deleteItemFromAccounttbById(click_id)
                mDates!!.remove(clickBean) //实时刷新，移除集合当中的对象
                adapter!!.notifyDataSetChanged() //提示适配器更新数据
                setTopTvShow() //改变头布局TextView显示的内容
            }
        builder.create().show() //显示对话框
    }

    /**
     * 获取今日的具体时间
     */
    private fun initTime() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1
        day = calendar[Calendar.DAY_OF_MONTH]
    }

    //表示当activity获取焦点时会调用的方法
    override fun onResume() {
        super.onResume()
        loadDBData()
        setTopTvShow()
    }

    /**
     * 设置头布局当中文本内容的显示
     */
    private fun setTopTvShow() {
        //获取近日支出和收入总金额，显示在view当中
        val incomeOneDay = getSumMoneyOneDay(year, month, day, 1)
        val outcomeOneDay = getSumMoneyOneDay(year, month, day, 0)
        val infoOneDay = "今日支出 ¥$outcomeOneDay     收入 ¥$incomeOneDay"
        topConTv!!.text = infoOneDay
        //获取本月的收入和支出金额
        val incomeOneMonth = getSumMoneyOneMonth(year, month, 1)
        val outcomeOneMonth = getSumMoneyOneMonth(year, month, 0)
        topInTv!!.text = "¥$incomeOneMonth"
        topOutTv!!.text = "¥$outcomeOneMonth"

        //设置显示预算剩余
        val bmoney = preferences!!.getFloat("bmoney", 0f) //预算
        if (bmoney == 0f) {
            topBudgetTv!!.text = "¥ 0"
        } else {
            val syMoney = bmoney - outcomeOneMonth
            topBudgetTv!!.text = "¥$syMoney"
        }
    }

    private fun loadDBData() {
        val list = getAccountListOneDayFromAccounttb(year, month, day)
        mDates!!.clear()
        mDates!!.addAll(list)
        adapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.main_iv_search -> {
                val it = Intent(this, SearchActivity::class.java) //跳转界面
                startActivity(it)
            }
            R.id.main_btn_edit -> {
                val it1 = Intent(this, RecordActivity::class.java) //跳转界面
                startActivity(it1)
            }
            R.id.main_btn_more -> {
                val moreDialog = MoreDialog(this)
                moreDialog.show()
                moreDialog.setDialogSize()
            }
            R.id.item_mainlv_top_tv_budget -> showBudgetDialog()
            R.id.item_mainlv_iv_hide ->                 //切换TextView明文和密文
                taggleShow()
        }
        if (v === headerView) {
            //头布局被点击
            val intent = Intent()
            intent.setClass(this, MonthChartActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 显示预算设置对话框
     */
    private fun showBudgetDialog() {
        val dialog = BudgetDialog(this)
        dialog.show()
        dialog.setDialogSize()
        dialog.onEnsureListener = object : BudgetDialog.OnEnsureListener {
            override fun onEnsure(money: Float) {
                //将预算金额写入到共享参数中，进行存储
                val editor = preferences!!.edit()
                editor.putFloat("bmoney", money)
                editor.commit()

                //计算剩余金额
                val outcomeOneMonth = getSumMoneyOneMonth(year, month, 0)
                val syMoney = money - outcomeOneMonth //预算剩余=预算-支出
                topBudgetTv!!.text = "¥$syMoney"
            }
        }
    }

    var isShow = true

    /**
     * 点击头布局眼睛时，如果原来是铭文，就加密，否则就显示
     */
    private fun taggleShow() {
        if (isShow) { //明文-->密文
            val passwordMethod = PasswordTransformationMethod.getInstance()
            topInTv!!.transformationMethod = passwordMethod //设置隐藏
            topOutTv!!.transformationMethod = passwordMethod //设置隐藏
            topBudgetTv!!.transformationMethod = passwordMethod //设置隐藏
            topShowIv!!.setImageResource(R.mipmap.ih_hide)
            isShow = false //设置标志位为隐藏
        } else { //密文-->明文
            val hideMethod = HideReturnsTransformationMethod.getInstance()
            topInTv!!.transformationMethod = hideMethod //设置隐藏
            topOutTv!!.transformationMethod = hideMethod //设置隐藏
            topBudgetTv!!.transformationMethod = hideMethod //设置隐藏
            topShowIv!!.setImageResource(R.mipmap.ih_show)
            isShow = true //设置标志位为隐藏
        }
    }
}