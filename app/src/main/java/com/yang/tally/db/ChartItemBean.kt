package com.yang.tally.db

class ChartItemBean(
    var sImageId: Int = 0,
    var type: String? = null,
    var radio: Float//所占比例
    = 0f,
    var totalMoney: Float = 0f
) {

}