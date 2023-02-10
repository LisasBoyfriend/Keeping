package com.yang.tally.db

/**
 * 收入或者支出具体类型的类
 */
class TypeBean(
    var id: Int = 0,
    var typename //类型名称
    : String? = null,
    var imageId: Int//未被选中图片id
    = 0,
    var simageId: Int//被选中图片id
    = 0,
    var kind: Int//收入-1,  支出-0
    = 0
) {

}