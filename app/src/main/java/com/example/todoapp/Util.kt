package com.example.todoapp

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Util {
    companion object {
        private val TAG: String = Util().getClassName(object :
            Any() {}.javaClass.enclosingClass.name)
    }

    fun getClassName(str: String): String {
        val ret = str.split(".")
        return ret[ret.size - 1]
    }

    fun getLocalDayTime(localDateTime: LocalDateTime,  formatStr: String): String {
        val formatter = DateTimeFormatter.ofPattern(formatStr)
        return localDateTime.format(formatter)
    }

    fun toLocalDateTime(date: Date): LocalDateTime {
        val instant: Instant = date.toInstant()
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    fun getWeek(year: Int, month: Int, day: Int): String {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, day)
        val ret: String
        when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> {
                ret = "日"
            }
            Calendar.MONDAY -> {
                ret = "月"
            }
            Calendar.TUESDAY -> {
                ret = "火"
            }
            Calendar.WEDNESDAY -> {
                ret = "水"
            }
            Calendar.THURSDAY -> {
                ret = "木"
            }
            Calendar.FRIDAY -> {
                ret = "金"
            }
            Calendar.SATURDAY -> {
                ret = "土"
            }
            else -> {
                ret = "n"
            }
        }
        return ret
    }

    fun extractionToYear(str: String): String {
        return str.substring(0, 4)
    }

    fun extractionToMonth(str: String): String {
        return str.substring(5, 7)
    }

    fun extractionToDay(str: String): String {
        return str.substring(8, 10)
    }

    fun extractionToWeek(str: String): String {
        return str.substring(11, 12)
    }

    fun extractionToHour(str: String): String {
        return str.substring(14, 16)
    }

    fun extractionToMinuit(str: String): String {
        return str.substring(17, 19)
    }

    fun extractionToTime(str: String): String {
        // yyyy/mm/dd(u)-hh:mm
        return str.substring(14, 19)
    }

    fun insertWhitespace(str: String): String {
        val ret = StringBuilder(this.extractionToTime(str))
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