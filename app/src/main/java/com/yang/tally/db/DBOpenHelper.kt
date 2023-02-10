package com.yang.tally.db

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import com.yang.tally.R

class DBOpenHelper(context: Context?) : SQLiteOpenHelper(context, "tally.db", null, 1) {
    //创建数据库的方法，只有项目第一次运行时会被调用
    override fun onCreate(db: SQLiteDatabase) {
        //创建表示类型的表
        var sql =
            "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer)"
        db.execSQL(sql)
        insertType(db)

        //记账表
        sql =
            "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money float," +
                    "time varchar(60),year integer,month integer,day integer,kind integer)"
        db.execSQL(sql)
    }

    private fun insertType(db: SQLiteDatabase) {
        //向typedb表当中插入元素
        val sql = "insert into typetb(typename,imageId,sImageId,kind) values (?,?,?,?)"
        db.execSQL(sql, arrayOf<Any>("其他", R.mipmap.ic_qita, R.mipmap.ic_qita_fs, 0))
        db.execSQL(sql, arrayOf<Any>("餐饮", R.mipmap.ic_canyin, R.mipmap.ic_canyin_fs, 0))
        db.execSQL(sql, arrayOf<Any>("交通", R.mipmap.ic_jiaotong, R.mipmap.ic_jiaotong_fs, 0))
        db.execSQL(sql, arrayOf<Any>("购物", R.mipmap.ic_gouwu, R.mipmap.ic_gouwu_fs, 0))
        db.execSQL(sql, arrayOf<Any>("服饰", R.mipmap.ic_fushi, R.mipmap.ic_fushi_fs, 0))
        db.execSQL(sql, arrayOf<Any>("日用品", R.mipmap.ic_riyongpin, R.mipmap.ic_riyongpin_fs, 0))
        db.execSQL(sql, arrayOf<Any>("娱乐", R.mipmap.ic_yule, R.mipmap.ic_yule_fs, 0))
        db.execSQL(sql, arrayOf<Any>("零食", R.mipmap.ic_lingshi, R.mipmap.ic_lingshi_fs, 0))
        db.execSQL(sql, arrayOf<Any>("烟酒茶", R.mipmap.ic_yanjiu, R.mipmap.ic_yanjiu_fs, 0))
        db.execSQL(sql, arrayOf<Any>("学习", R.mipmap.ic_xuexi, R.mipmap.ic_xuexi_fs, 0))
        db.execSQL(sql, arrayOf<Any>("医疗", R.mipmap.ic_yiliao, R.mipmap.ic_yiliao_fs, 0))
        db.execSQL(sql, arrayOf<Any>("住宅", R.mipmap.ic_zhufang, R.mipmap.ic_zhufang_fs, 0))
        db.execSQL(sql, arrayOf<Any>("水电煤", R.mipmap.ic_shuidianfei, R.mipmap.ic_shuidianfei_fs, 0))
        db.execSQL(sql, arrayOf<Any>("通讯", R.mipmap.ic_tongxun, R.mipmap.ic_tongxun_fs, 0))
        db.execSQL(
            sql,
            arrayOf<Any>("人情往来", R.mipmap.ic_renqingwanglai, R.mipmap.ic_renqingwanglai_fs, 0)
        )
        db.execSQL(sql, arrayOf<Any>("其他", R.mipmap.in_qt, R.mipmap.in_qt_fs, 1))
        db.execSQL(sql, arrayOf<Any>("薪资", R.mipmap.in_xinzi, R.mipmap.in_xinzi_fs, 1))
        db.execSQL(sql, arrayOf<Any>("奖金", R.mipmap.in_jiangjin, R.mipmap.in_jiangjin_fs, 1))
        db.execSQL(sql, arrayOf<Any>("借入", R.mipmap.in_jieru, R.mipmap.in_jieru_fs, 1))
        db.execSQL(sql, arrayOf<Any>("收债", R.mipmap.in_shouzhai, R.mipmap.in_shouzhai_fs, 1))
        db.execSQL(sql, arrayOf<Any>("利息收入", R.mipmap.in_lixifuji, R.mipmap.in_lixifuji_fs, 1))
        db.execSQL(sql, arrayOf<Any>("投资回报", R.mipmap.in_touzi, R.mipmap.in_touzi_fs, 1))
        db.execSQL(
            sql,
            arrayOf<Any>("二手交易", R.mipmap.in_ershoushebei, R.mipmap.in_ershoushebei_fs, 1)
        )
        db.execSQL(sql, arrayOf<Any>("意外所得", R.mipmap.in_yiwai, R.mipmap.in_yiwai_fs, 1))
    }

    //数据库版本在更新时发生改变，会调用此方法
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}