package com.example.todoapp

import android.util.Log
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object Util {
    private val TAG: String = getClassName(object : Any() {}.javaClass.enclosingClass.name)

    /**
     * getClassName
     * @return class name tag to string
     */
    fun getClassName(tag: String): String {
        val ret = tag.split(".")
        return ret[ret.size - 1]
    }

    /**
     * getCurrentLocalDateTime
     * @return LocalDateTime
     */
    fun getCurrentLocalDateTime(): LocalDateTime {
        Log.d(TAG, "getCurrentLocalDateTime <start>")
        val localDateTime = LocalDateTime.now()
        Log.d(TAG, "getCurrentLocalDateTime <end>")
        return localDateTime
    }

    /**
     * toLocalDateTime
     * @param date date
     * @return LocalDateTime
     */
    fun toLocalDateTime(date: Date): LocalDateTime {
        Log.d(TAG, "toLocalDateTime <start>")
        val instant = date.toInstant()
        val ret = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        Log.d(TAG, "toLocalDateTime <end>")
        return ret
    }

    /**
     * toLocalDateTime
     * @param date string date data
     * @param formatStr format
     * @return LocalDateTime
     */
    fun toLocalDateTime(date: String, formatStr: String): LocalDateTime {
        Log.d(TAG, "toLocalDateTime <start>")
        val formatter = DateTimeFormatter.ofPattern(formatStr)
        val ret = LocalDateTime.parse(date, formatter)
        Log.d(TAG, "toLocalDateTime <end>")
        return ret
    }

    /**
     * toString
     * @param localDateTime LocalDateTime
     * @param formatStr format
     * @return LocalDateTime String
     */
    fun toString(localDateTime: LocalDateTime, formatStr: String): String {
        Log.d(TAG, "toString <start>")
        val formatter = DateTimeFormatter.ofPattern(formatStr)
        val ret = localDateTime.format(formatter)
        Log.d(TAG, "toString <end>")
        return ret
    }

    /**
     * toString
     * @param date data
     * @param formatStr format
     * @return Date String
     */
    fun toString(date: Date, formatStr: String): String {
        Log.d(TAG, "toString <start>")
        val instant: Instant = date.toInstant()
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val ret = toString(localDateTime, formatStr)
        Log.d(TAG, "toString <end>")
        return ret
    }
}