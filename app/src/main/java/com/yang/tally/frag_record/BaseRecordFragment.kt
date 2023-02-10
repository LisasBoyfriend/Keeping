package com.yang.tally.frag_record

import android.inputmethodservice.KeyboardView
import com.yang.tally.db.TypeBean
import com.yang.tally.db.AccountBean
import android.os.Bundle
import com.yang.tally.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import com.yang.tally.utils.KeyBoardUtils
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.yang.tally.utils.SelectTimeDialog
import com.yang.tally.utils.BeiZhuDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 记录页面当中的支出模块
 *
 */
abstract class BaseRecordFragment : Fragment(), View.OnClickListener {
    var keyboardView: KeyboardView? = null
    var moneyEt: EditText? = null
    @JvmField
    var typeIv: ImageView? = null
    @JvmField
    var typeTv: TextView? = null
    var beizhuTv: TextView? = null
    var timeTv: TextView? = null
    var typeGv: GridView? = null
    @JvmField
    var typeList: ArrayList<TypeBean>? = null
    @JvmField
    var adapter: TypeBaseAdapter? = null
    @JvmField
    var accountBean //将需要插入到记账本中的数据保存成对象的形式
            : AccountBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountBean = AccountBean() //创建对象
        accountBean!!.typename = "其他"
        accountBean!!.sImageId = R.mipmap.ic_qita_fs
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_outcome, container, false)
        initView(view)
        setInitTime()
        //给GridView填充数据的方法
        loadDataToGv()
        setGVListener() //设置GridView每一项的点击事件
        return view
        // Inflate the layout for this fragment
    }

    //获取当前时间，显示在timeTv上
    private fun setInitTime() {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        val time = sdf.format(date)
        timeTv!!.text = time
        accountBean!!.time = time
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1
        val day = calendar[Calendar.DAY_OF_MONTH]
        accountBean!!.year = year
        accountBean!!.month = month
        accountBean!!.day = day
    }

    //设置GridView每一项的点击事件
    private fun setGVListener() {
        typeGv!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            adapter!!.selectPos = position
            adapter!!.notifyDataSetChanged() //提示绘制发生变化
            val typeBean = typeList!![position]
            val typename = typeBean.typename
            typeTv!!.text = typename
            accountBean!!.typename = typename
            val simageId = typeBean.simageId
            typeIv!!.setImageResource(simageId)
            accountBean!!.sImageId = simageId
        }
    }

    //给gridview填充数据的方法
    open fun loadDataToGv() {
        typeList = ArrayList()
        adapter = context?.let { TypeBaseAdapter(it, typeList!!) }
        typeGv!!.adapter = adapter
    }

    private fun initView(view: View) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard)
        moneyEt = view.findViewById(R.id.frag_record_et_money)
        typeIv = view.findViewById(R.id.frag_record_iv)
        typeGv = view.findViewById(R.id.frag_record_gv)
        typeTv = view.findViewById(R.id.frag_record_tv_type)
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu)
        timeTv = view.findViewById(R.id.frag_record_tv_time)
        beizhuTv?.setOnClickListener(this)
        timeTv?.setOnClickListener(this)
        //让自定义软键盘显示出来
        val boardUtils = KeyBoardUtils(keyboardView, moneyEt)
        boardUtils.showKeyboard()
        //设置接口，监听确定按钮被点击
        boardUtils.setOnEnsureListener(object : KeyBoardUtils.OnEnsureListener{
            override fun onEnsure() {
                val moneyStr = moneyEt?.getText().toString()
                if (TextUtils.isEmpty(moneyStr) || moneyStr == "0") {
                    activity!!.finish()
                }
                val money = moneyStr.toFloat()
                accountBean!!.money = money
                //获取记录的信息，保存在数据库中
                saveAccountToDB()
                //返回上一级页面
                activity!!.finish()
            }

        })
    }

    //让子类一定要重写该方法
    abstract fun saveAccountToDB()
    override fun onClick(v: View) {
        when (v.id) {
            R.id.frag_record_tv_time -> showTimeDialog()
            R.id.frag_record_tv_beizhu -> showBZDialog()
        }
    }

    //弹出显示时间的对话框
    protected fun showTimeDialog() {
        val dialog = SelectTimeDialog(context!!)
        dialog.show()
        //设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener(object : SelectTimeDialog.OnEnsureListener{
            override fun onEnsure(time: String?, year: Int, month: Int, day: Int) {
                timeTv?.text = time
                accountBean?.time = time
                accountBean?.year = year
                accountBean?.month = month
                accountBean?.day = day
            }

        })
    }

    //弹出备注对话框
    protected fun showBZDialog() {
        val dialog = BeiZhuDialog(context!!)
        dialog.show()
        dialog.setDialogSize()
        dialog.setOnEnsureListener(object : BeiZhuDialog.OnEnsureListener{

            override fun onEnsure() {
                val msg = dialog.editText
                msg?.let { beizhuTv?.text = it
                    accountBean?.beizhu = it}
            }

        })
    }
}