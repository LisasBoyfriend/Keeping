package com.yang.tally.db


import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import com.yang.tally.utils.FloatUtils
import java.util.ArrayList

/**
 * 负责管理数据库的类
 * 主要对于表当中的内容进行操作
 */
object DBManager {
    private var db: SQLiteDatabase? = null

    /*初始化数据库对象*/
    fun initDB(context: Context?) {
        val helper = DBOpenHelper(context) //得到帮助类对象
        db = helper.writableDatabase //得到数据库对象
    }

    /**
     * 读取数据库当中的数据，写入内存集合内
     * kind：表示收入或者支出
     */
    @JvmStatic
    fun getTypeList(kind: Int): List<TypeBean> {
        val list: MutableList<TypeBean> = ArrayList()
        //读取typetb表当中的数据
        val sql = "select * from typetb where kind = $kind"
        val cursor = db!!.rawQuery(sql, null)
        //循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            val typename = cursor.getString(cursor.getColumnIndex("typename"))
            val imageId = cursor.getInt(cursor.getColumnIndex("imageId"))
            val sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"))
            val kind1 = cursor.getInt(cursor.getColumnIndex("kind"))
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val typeBean = TypeBean(id, typename, imageId, sImageId, kind)
            list.add(typeBean)
        }
        return list
    }

    /**
     * 想记账表中插入一条元素
     */
    @JvmStatic
    fun insertItemToAccounttb(bean: AccountBean) {
        val values = ContentValues()
        values.put("typename", bean.typename)
        values.put("sImageId", bean.sImageId)
        values.put("beizhu", bean.beizhu)
        values.put("money", bean.money)
        values.put("time", bean.time)
        values.put("year", bean.year)
        values.put("month", bean.month)
        values.put("day", bean.day)
        values.put("kind", bean.kind)
        db!!.insert("accounttb", null, values)
    }

    /*
     * 获取记账表当中某一月的所有支出或者收入情况
     * */
    /*
     * 获取记账表当中某一天的所有支出或者收入情况
     * */
    @JvmStatic
    fun getAccountListOneDayFromAccounttb(year: Int, month: Int, day: Int): List<AccountBean> {
        val list: MutableList<AccountBean> = ArrayList()
        val sql = "select * from accounttb where year=? and month=? and day=? order by id desc"
        val cursor = db!!.rawQuery(
            sql,
            arrayOf(year.toString() + "", month.toString() + "", day.toString() + "")
        )
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val typename = cursor.getString(cursor.getColumnIndex("typename"))
            val beizhu = cursor.getString(cursor.getColumnIndex("beizhu"))
            val time = cursor.getString(cursor.getColumnIndex("time"))
            val sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"))
            val kind = cursor.getInt(cursor.getColumnIndex("kind"))
            val money = cursor.getFloat(cursor.getColumnIndex("money"))
            val accountBean =
                AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind)
            list.add(accountBean)
        }
        return list
    }

    /**
     * 获取某一天的支出或者收入的总金额 kind:支出==0，收入==1
     */
    @JvmStatic
    fun getSumMoneyOneDay(year: Int, month: Int, day: Int, kind: Int): Float {
        var total = 0.0f
        val sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?"
        val cursor = db!!.rawQuery(
            sql,
            arrayOf(
                year.toString() + "",
                month.toString() + "",
                day.toString() + "",
                kind.toString() + ""
            )
        )
        //遍历
        if (cursor.moveToFirst()) {
            val money = cursor.getFloat(cursor.getColumnIndex("sum(money)"))
            total = money
        }
        return total
    }

    /**
     * 获取某一月的支出或者收入的总金额 kind:支出==0，收入==1
     */
    @JvmStatic
    fun getSumMoneyOneMonth(year: Int, month: Int, kind: Int): Float {
        var total = 0.0f
        val sql = "select sum(money) from accounttb where year=? and month=? and kind=?"
        val cursor = db!!.rawQuery(
            sql,
            arrayOf(year.toString() + "", month.toString() + "", kind.toString() + "")
        )
        //遍历
        if (cursor.moveToFirst()) {
            val money = cursor.getFloat(cursor.getColumnIndex("sum(money)"))
            total = money
        }
        return total
    }

    /**
     * 统计某月份支出或者收入情况有多少条  收入-1，支出-0
     */
    @JvmStatic
    fun getCountItemOneMonth(year: Int, month: Int, kind: Int): Int {
        var total = 0
        val sql = "select count(money) from accounttb where year = ? and month = ? and kind = ?"
        val cursor = db!!.rawQuery(
            sql,
            arrayOf(year.toString() + "", month.toString() + "", kind.toString() + "")
        )
        if (cursor.moveToFirst()) {
            val count = cursor.getInt(cursor.getColumnIndex("count(money)"))
            total = count
        }
        return total
    }

    /**
     * 获取某一年的支出或者收入的总金额 kind:支出==0，收入==1
     */
    fun getSumMoneyOneYear(year: Int, kind: Int): Float {
        var total = 0.0f
        val sql = "select sum(money) from accounttb where year=? and kind=?"
        val cursor = db!!.rawQuery(sql, arrayOf(year.toString() + "", kind.toString() + ""))
        //遍历
        if (cursor.moveToFirst()) {
            val money = cursor.getFloat(cursor.getColumnIndex("sum(money)"))
            total = money
        }
        return total
    }

    /**
     * 根据传入的id，删除accounttb中的一条数据
     */
    @JvmStatic
    fun deleteItemFromAccounttbById(id: Int): Int {
        return db!!.delete("accounttb", "id=?", arrayOf(id.toString() + ""))
    }

    /**
     * 根据备注搜索收入或者支出的情况列表
     */
    @JvmStatic
    fun getAccountListByRemarkFromAccounttb(beizhu: String): List<AccountBean> {
        val list: MutableList<AccountBean> = ArrayList()
        val sql = "select * from accounttb where beizhu like '%$beizhu%'"
        val cursor = db!!.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val typename = cursor.getString(cursor.getColumnIndex("typename"))
            val bz = cursor.getString(cursor.getColumnIndex("beizhu"))
            val time = cursor.getString(cursor.getColumnIndex("time"))
            val sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"))
            val kind = cursor.getInt(cursor.getColumnIndex("kind"))
            val money = cursor.getFloat(cursor.getColumnIndex("money"))
            val year = cursor.getInt(cursor.getColumnIndex("year"))
            val month = cursor.getInt(cursor.getColumnIndex("month"))
            val day = cursor.getInt(cursor.getColumnIndex("day"))
            val accountBean =
                AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind)
            list.add(accountBean)
        }
        return list
    }

    /*
     * 获取记账表当中某一月的所有支出或者收入情况
     * */
    @JvmStatic
    fun getAccountListOneMonthFromAccounttb(year: Int, month: Int): List<AccountBean> {
        val list: MutableList<AccountBean> = ArrayList()
        val sql = "select * from accounttb where year=? and month=? order by id desc"
        val cursor = db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + ""))
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val typename = cursor.getString(cursor.getColumnIndex("typename"))
            val beizhu = cursor.getString(cursor.getColumnIndex("beizhu"))
            val time = cursor.getString(cursor.getColumnIndex("time"))
            val sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"))
            val kind = cursor.getInt(cursor.getColumnIndex("kind"))
            val money = cursor.getFloat(cursor.getColumnIndex("money"))
            val day = cursor.getInt(cursor.getColumnIndex("day"))
            val accountBean =
                AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind)
            list.add(accountBean)
        }
        return list
    }

    /**
     * 查询记账的表当中有几个年份信息
     */
    @JvmStatic
    val yearListFromAccounttb: MutableList<Int>
        get() {
            val list: MutableList<Int> = ArrayList()
            val sql = "select distinct(year) from accounttb order by year asc"
            val cursor = db!!.rawQuery(sql, null)
            while (cursor.moveToNext()) {
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                list.add(year)
            }
            return list
        }

    /**
     * 删除accounttb表格当中的所有数据
     */
    @JvmStatic
    fun deleteAllAccount() {
        val sql = "delete from accounttb"
        db!!.execSQL(sql)
    }

    /**
     * 查询指定年份和月份的收入或者支出每一种类型的总钱数
     *
     */
    @JvmStatic
    fun getChastListFromAccounttb(year: Int, month: Int, kind: Int): List<ChartItemBean> {
        val list: MutableList<ChartItemBean> = ArrayList()
        val sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind) //求出支出或收入总钱数
        val sql =
            "select typename,sImageId,sum(money) as total from accounttb where year = ? and month = ? and kind = ? group by typename " +
                    "order by total desc"
        val cursor = db!!.rawQuery(
            sql,
            arrayOf(year.toString() + "", month.toString() + "", kind.toString() + "")
        )
        while (cursor.moveToNext()) {
            val sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"))
            val typename = cursor.getString(cursor.getColumnIndex("typename"))
            val total = cursor.getFloat(cursor.getColumnIndex("total"))
            //计算所占百分比 total/sumMonth
            val ratio = FloatUtils.div(total, sumMoneyOneMonth)
            val bean = ChartItemBean(sImageId, typename, ratio, total)
            list.add(bean)
        }
        return list
    }

    /**
     * 获取这个月当中某一天收入支出最大的金额，金额是多少
     *
     */
    @JvmStatic
    fun getMaxMoneyOneDayInMonth(year: Int, month: Int, kind: Int): Float {
        val sql =
            "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc"
        val cursor = db!!.rawQuery(
            sql,
            arrayOf(year.toString() + "", month.toString() + "", kind.toString() + "")
        )
        return if (cursor.moveToFirst()) {
            cursor.getFloat(cursor.getColumnIndex("sum(money)"))
        } else 0f
    }

    /**
     * 根据指定月份获取每以日收入或者支出的总钱数的集合
     */
    @JvmStatic
    fun getSumMoneyOneDayInMonth(year: Int, month: Int, kind: Int): List<BarChartItemBean> {
        val sql =
            "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day"
        val cursor = db!!.rawQuery(
            sql,
            arrayOf(year.toString() + "", month.toString() + "", kind.toString() + "")
        )
        val list: MutableList<BarChartItemBean> = ArrayList()
        while (cursor.moveToNext()) {
            val day = cursor.getInt(cursor.getColumnIndex("day"))
            val sMoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"))
            val itemBean = BarChartItemBean(year, month, day, sMoney)
            list.add(itemBean)
        }
        return list
    }
}