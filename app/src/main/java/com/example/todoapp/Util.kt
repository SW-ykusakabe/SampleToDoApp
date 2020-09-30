package com.example.todoapp

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Util {
    companion object {
        val TAG: String = object: Any() {}.javaClass.enclosingClass.name
    }

    fun getDayTime(formatStr: String): String {
        val formatter = DateTimeFormatter.ofPattern(formatStr)
        val current = LocalDateTime.now()
        return current.format(formatter)
    }

    fun getWeek(year: Int, month: Int, day: Int): String {
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
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
                ret = ""
            }
        }
        return ret
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