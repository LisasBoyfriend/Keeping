package com.yang.tally.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.yang.tally.R
import android.content.Intent
import com.yang.tally.AboutActivity
import com.yang.tally.SettingActivity
import com.yang.tally.HistoryActivity
import com.yang.tally.MonthChartActivity
import android.view.WindowManager
import android.view.Display
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView

class MoreDialog(context: Context) : Dialog(context), View.OnClickListener {
    var aboutBtn: Button? = null
    var settingBtn: Button? = null
    var historyBtn: Button? = null
    var infoBtn: Button? = null
    var errorIv: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_more)
        aboutBtn = findViewById(R.id.dialog_more_btn_about)
        settingBtn = findViewById(R.id.dialog_more_btn_setting)
        historyBtn = findViewById(R.id.dialog_more_btn_record)
        infoBtn = findViewById(R.id.dialog_more_btn_info)
        errorIv = findViewById(R.id.dialog_more_iv)
        aboutBtn?.setOnClickListener(this)
        settingBtn?.setOnClickListener(this)
        historyBtn?.setOnClickListener(this)
        infoBtn?.setOnClickListener(this)
        errorIv?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val intent = Intent()
        when (v.id) {
            R.id.dialog_more_btn_about -> {
                intent.setClass(context, AboutActivity::class.java)
                context.startActivity(intent)
            }
            R.id.dialog_more_btn_setting -> {
                intent.setClass(context, SettingActivity::class.java)
                context.startActivity(intent)
            }
            R.id.dialog_more_btn_record -> {
                intent.setClass(context, HistoryActivity::class.java)
                context.startActivity(intent)
            }
            R.id.dialog_more_btn_info -> {
                intent.setClass(context, MonthChartActivity::class.java)
                context.startActivity(intent)
            }
            R.id.dialog_more_iv -> cancel()
        }
        cancel()
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
    }
}