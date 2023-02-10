package com.yang.tally.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ChartVPAdapter(fm: FragmentManager, var fragmentList: List<Fragment>) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}