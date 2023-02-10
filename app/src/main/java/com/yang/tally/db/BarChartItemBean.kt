package com.yang.tally.db

/**
 * 用于描述绘制柱状图时每一个柱子表示的对象
 */
class BarChartItemBean(
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var summoney: Float = 0f
) {

}