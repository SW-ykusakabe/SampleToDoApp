package com.example.todoapp

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object Util {
    private val TAG: String = getClassName(object : Any() {}.javaClass.enclosingClass.name)

    fun getClassName(str: String): String {
        val ret = str.split(".")
        return ret[ret.size - 1]
    }

    /**
     * getCurrentLocalDateTime
     * @return LocalDateTime
     */
    fun getCurrentLocalDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    /**
     * toLocalDateTime
     * @param date string date data
     * @param formatStr format
     * @return LocalDateTime
     */
    fun toLocalDateTime(date: String, formatStr: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(formatStr)
        return LocalDateTime.parse(date, formatter)
    }

    /**
     * toString
     * @param localDateTime LocalDateTime
     * @param formatStr format
     * @return LocalDateTime String
     */
    fun toString(localDateTime: LocalDateTime, formatStr: String): String {
        val formatter = DateTimeFormatter.ofPattern(formatStr)
        return localDateTime.format(formatter)
    }

    /**
     * toString
     * @param date data
     * @param formatStr format
     * @return Date String
     */
    fun toString(date: Date, formatStr: String): String {
        val instant: Instant = date.toInstant()
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return this.toString(localDateTime, formatStr)
    }

    fun getWeekAsInt(year: Int, month: Int, day: Int): Int {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, day)
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    fun getWeekAsString(year: Int, month: Int, day: Int): String {
        val ret: String
        when (getWeekAsInt(year, month, day)) {
            Calendar.SUNDAY -> {
                ret = "Sun"
            }
            Calendar.MONDAY -> {
                ret = "Mon"
            }
            Calendar.TUESDAY -> {
                ret = "Tue"
            }
            Calendar.WEDNESDAY -> {
                ret = "Wed"
            }
            Calendar.THURSDAY -> {
                ret = "The"
            }
            Calendar.FRIDAY -> {
                ret = "Fri"
            }
            Calendar.SATURDAY -> {
                ret = "Sat"
            }
            else -> {
                ret = "n"
            }
        }
        return ret
    }

    fun extractToYear(str: String): String {
        return str.substring(0, 4)
    }

    fun extractToMonth(str: String): String {
        return str.substring(5, 7)
    }

    fun extractToDay(str: String): String {
        return str.substring(8, 10)
    }

    fun extractToWeek(str: String): String {
        return str.substring(11, 12)
    }

    fun extractToHour(str: String): String {
        return str.substring(14, 16)
    }

    fun extractToMinuit(str: String): String {
        return str.substring(17)
    }

    fun extractToTime(str: String): String {
        return str.substring(14)
    }

    fun insertWhitespace(str: String): String {
        val ret = StringBuilder(str)
        ret.insert(3, " ")
        ret.insert(2, " ")
        return ret.toString()
    }


    fun paddingLeftToString(str: String, count: Int): String {
        val loopCount = count - 1
        var ret: String = str
        for (i in ret.length..loopCount) {
            ret = "0$ret"
        }
        return ret
    }
}