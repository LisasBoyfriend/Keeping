package com.yang.tally.utils

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.yang.tally.R
import android.text.TextUtils
import android.widget.Toast
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView

class BudgetDialog(context: Context) : Dialog(context), View.OnClickListener {
    var cancelIv: ImageView? = null
    var ensureBtn: Button? = null
    var moneyEt: EditText? = null

    interface OnEnsureListener {
        fun onEnsure(money: Float)
    }

    var onEnsureListener: OnEnsureListener? = null
    @JvmName("setOnEnsureListener1")
    fun setOnEnsureListener(onEnsureListener: OnEnsureListener?) {
        this.onEnsureListener = onEnsureListener
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_budget)
        cancelIv = findViewById(R.id.dialog_budget_iv_error)
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure)
        moneyEt = findViewById(R.id.dialog_budget_et)
        cancelIv?.setOnClickListener(this)
        ensureBtn?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dialog_budget_iv_error -> cancel() //取消对话框
            R.id.dialog_budget_btn_ensure -> {
                //获取输入数据数值
                val data = moneyEt!!.text.toString()
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(context, "输入数据不能为空!", Toast.LENGTH_SHORT).show()
                    return
                }
                val money = data.toFloat()
                if (money <= 0) {
                    Toast.makeText(context, "预算金额必须大于0！!", Toast.LENGTH_SHORT).show()
                    return
                }
                if (onEnsureListener != null) {
                    onEnsureListener!!.onEnsure(money)
                }
                cancel()
            }
        }
    }

    /*设置dialog的尺寸和屏幕尺寸一致*/
    fun setDialogSize() {
        //获取当前窗口对象
        val window = window
        //获取窗口对象的参数
        val wlp = window!!.attributes
        //获取屏幕宽度
        val d = window.windowManager.defaultDisplay
        wlp.width = d.width //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.attributes = wlp
        handler.sendEmptyMessageDelayed(1, 100)
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            //自动弹出软键盘方法
            val inputMethodManager =
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}