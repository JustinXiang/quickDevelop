package com.example.quickdevelop.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    /**
     * 获取系统时间戳
     * @return
     */
    fun getCurTimeLong(): Long {
        return System.currentTimeMillis()
    }

    fun getCurDate(): String {
        var simpleDateFormat: SimpleDateFormat? = null
        var date: Date? = null
        try {
            simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            date = Date(System.currentTimeMillis())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return simpleDateFormat!!.format(date)
    }

    fun getCurYear(): String {
        var simpleDateFormat: SimpleDateFormat? = null
        var date: Date? = null
        try {
            simpleDateFormat = SimpleDateFormat("yyyy")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            date = Date(System.currentTimeMillis())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return simpleDateFormat!!.format(date)
    }

    fun getCurMonth(): String {
        var simpleDateFormat: SimpleDateFormat? = null
        var date: Date? = null
        try {
            simpleDateFormat = SimpleDateFormat("MM")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            date = Date(System.currentTimeMillis())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return simpleDateFormat!!.format(date)
    }

    fun getCurYearMonth(): String {
        var simpleDateFormat: SimpleDateFormat? = null
        var date: Date? = null
        try {
            simpleDateFormat = SimpleDateFormat("yyyy-MM")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            date = Date(System.currentTimeMillis())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return simpleDateFormat!!.format(date)
    }

    fun getCurDay(): String {
        var simpleDateFormat: SimpleDateFormat? = null
        var date: Date? = null
        try {
            simpleDateFormat = SimpleDateFormat("dd")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            date = Date(System.currentTimeMillis())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return simpleDateFormat!!.format(date)
    }

    /**
     * 获得指定日期的前一天
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedDayBefore(specifiedDay: String?): String {

        val c: Calendar = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day: Int = c.get(Calendar.DATE)
        c.set(Calendar.DATE, day - 1)
        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得指定日期的后一天
     * @param specifiedDay
     * @return
     */
    fun getSpecifiedDayAfter(specifiedDay: String?): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day = c[Calendar.DATE]
        c[Calendar.DATE] = day + 1
        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得指定日期的前一周
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedWeekBefore(specifiedDay: String?): String {

        val c: Calendar = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day: Int = c.get(Calendar.DATE)
        c.set(Calendar.DATE, day - 7)
        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得指定日期的后一天
     * @param specifiedDay
     * @return
     */
    fun getSpecifiedWeekAfter(specifiedDay: String?): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day = c[Calendar.DATE]
        c[Calendar.DATE] = day + 7
        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得指定日期的前一月
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedMonthBefore(specifiedDay: String?): String {

        val c: Calendar = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day: Int = c.get(Calendar.DATE)
        c.set(Calendar.DATE, day - 30)
        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedNowYearMonthBefore(): String {
        val specifiedDay = getCurYearMonth()
        val c: Calendar = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day: Int = c.get(Calendar.DATE)
        c.set(Calendar.DATE, day - 365)
        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得指定日期的后一月
     * @param specifiedDay
     * @return
     */
    fun getSpecifiedMonthAfter(specifiedDay: String?): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM-dd").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day = c[Calendar.DATE]
        c[Calendar.DATE] = day + 30
        return SimpleDateFormat("yyyy-MM-dd").format(c.time)
    }

    /**
     * 获得指定日期的前一月
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedYearMonthBefore(specifiedDay: String?): String {

        val c: Calendar = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day: Int = c.get(Calendar.DATE)
        c[Calendar.DATE] = if (SimpleDateFormat("yyyy-MM").format(date).split("-")[1] == "02") {
            day - 28
        } else {
            day - 30
        }
        return SimpleDateFormat("yyyy-MM").format(c.time)
    }

    /**
     * 获得指定日期的后一月
     * @param specifiedDay
     * @return
     */
    fun getSpecifiedYearMonthAfter(specifiedDay: String?): String {
        val c = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yy-MM").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day = c[Calendar.DATE]
        c[Calendar.DATE] = if (SimpleDateFormat("yyyy-MM").format(date).split("-")[1] == "02") {
            day + 29
        } else {
            day + 31
        }

        return SimpleDateFormat("yyyy-MM").format(c.time)
    }

    /**
     * 获得指定日期的前一年
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    fun getSpecifiedYearBefore(specifiedDay: String?): String {

        val c: Calendar = Calendar.getInstance()
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy").parse(specifiedDay)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.time = date
        val day: Int = c.get(Calendar.DATE)
        c.set(Calendar.DATE, day - 365)
        return SimpleDateFormat("yyyy").format(c.time)
    }

    fun getSupportEndDayofMonth(year: Int, monthOfYear: Int): String {
        val cal = Calendar.getInstance()
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = monthOfYear
        cal[Calendar.DAY_OF_MONTH] = 1
        cal[Calendar.HOUR_OF_DAY] = 23
        cal[Calendar.MINUTE] = 59
        cal[Calendar.SECOND] = 59
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val lastDate = cal.time
        cal[Calendar.DAY_OF_MONTH] = 1
        val firstDate = cal.time
        return SimpleDateFormat("yyyy-MM-dd").format(lastDate)
    }


    fun isDateOneBigger(str1: String, str2: String): Boolean {
        var isBigger = false
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var dt1: Date? = null
        var dt2: Date? = null
        try {
            dt1 = sdf.parse(str1)
            dt2 = sdf.parse(str2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (dt1!!.time > dt2!!.time) {
            isBigger = true
        } else if (dt1.time < dt2.time) {
            isBigger = false
        }
        return isBigger
    }

    fun isValidDate(str: String): Boolean {
        var convertSuccess = true
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            format.isLenient = false
            format.parse(str)
        } catch (e: ParseException) {
            convertSuccess = false
        }
        return convertSuccess
    }
}