package com.yang.tally.utils

import java.math.BigDecimal

object FloatUtils {
    /**
     * 进行除法运算，保留4位小数
     */
    fun div(v1: Float, v2: Float): Float {
        val v3 = v1 / v2
        val b1: BigDecimal = BigDecimal(v3.toDouble())
        return b1.setScale(3, 4).toFloat()
    }

    //将浮点数类型转化成百分比显示形式
    fun ratioToPercent(`val`: Float): String {
        val v = `val` * 100
        val b1: BigDecimal = BigDecimal(v.toDouble())
        val v1 = b1.setScale(2, 4).toFloat()
        return "$v1%"
    }
}