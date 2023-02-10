package com.yang.tally

import com.yang.tally.db.DBManager.deleteAllAccount
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yang.tally.R
import com.yang.tally.db.DBManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.setting_iv_back -> finish()
            R.id.setting_tv_clear -> showDeleteDialog()
        }
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("删除提示")
            .setMessage("您确定要删除所有记录吗？\n注意：删除后无法回复，请慎重选择！")
            .setNegativeButton("取消", null)
            .setPositiveButton("确定") { dialog, which ->
                deleteAllAccount()
                Toast.makeText(this@SettingActivity, "删除成功", Toast.LENGTH_SHORT).show()
            }
        builder.create().show()
    }
}