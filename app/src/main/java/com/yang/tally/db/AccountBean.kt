package com.yang.tally.db

/**
 * 描述记录一条数据的相关内容类
 */
class AccountBean(
    var id: Int = 0,
    var typename //类型
    : String? = null,
    var sImageId: Int//被选中类型图片
    = 0,
    var beizhu //备注
    : String? = null,
    var money: Float//价格
    = 0f,
    var time //保存时间字符串
    : String? = null,
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var kind: Int //类型  收入---1   支出---0
    = 0
) {


}