package com.yang.tally

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.yang.tally.adapter.RecordPaperAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yang.tally.R
import com.yang.tally.frag_record.OutcomeFragment
import com.yang.tally.frag_record.IncomeFragment
import java.util.ArrayList

class RecordActivity : AppCompatActivity() {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private val paperAdapter: RecordPaperAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        //1.查找控件
        tabLayout = findViewById(R.id.record_tabs)
        viewPager = findViewById(R.id.record_vp)

        //设置viewpaper加载页面
        initPaper()
    }

    private fun initPaper() {
        //初始化ViewPaper页面的集合
        val fragmentList: MutableList<Fragment> = ArrayList()
        //创建收入和支出页面，放置在fragment当中
        val outFrag = OutcomeFragment()
        val inFrag = IncomeFragment()
        fragmentList.add(outFrag)
        fragmentList.add(inFrag)
        //创建适配器
        val paperAdapter = RecordPaperAdapter(supportFragmentManager, fragmentList)
        //设置适配器
        viewPager!!.adapter = paperAdapter
        //将tablayout和viewpager关联
        tabLayout!!.setupWithViewPager(viewPager)
    }

    /*点击事件*/
    fun onClick(view: View) {
        when (view.id) {
            R.id.record_iv_back -> finish()
        }
    }
}