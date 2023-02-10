package com.yang.tally.utils

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.yang.tally.R
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button

class BeiZhuDialog(context: Context) : Dialog(context), View.OnClickListener {
    var et: EditText? = null
    var cancelBtn: Button? = null
    var ensureBtn: Button? = null
    var onEnsureListener: OnEnsureListener? = null

    //设定回调接口的方法
    @JvmName("setOnEnsureListener1")
    fun setOnEnsureListener(onEnsureListener: OnEnsureListener?) {
        this.onEnsureListener = onEnsureListener
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_beizhu) //设置对话框显示布局
        et = findViewById(R.id.dialog_beizhu_et)
        cancelBtn = findViewById(R.id.dialog_beizhu_btn_cancel)
        ensureBtn = findViewById(R.id.dialog_beizhu_btn_ensure)
        cancelBtn?.setOnClickListener(this)
        ensureBtn?.setOnClickListener(this)
    }

    interface OnEnsureListener {
        fun onEnsure()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dialog_beizhu_btn_cancel -> cancel()
            R.id.dialog_beizhu_btn_ensure -> if (onEnsureListener != null) {
                onEnsureListener!!.onEnsure()
            }
        }
    }

    //获取输入数据的方法
    val editText: String
        get() = et!!.text.toString().trim { it <= ' ' }

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