package com.example.todoapp

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
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
        return LocalDateTime.now()
    }

    /**
     * toLocalDateTime
     * @param date date
     * @return LocalDateTime
     */
    fun toLocalDateTime(date: Date): LocalDateTime {
        val instant = date.toInstant()
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
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
     * toDate
     * @param localDateTime
     * @return date
     */
    fun toDate(localDateTime: LocalDateTime): Date {
        val zone: ZoneId = ZoneId.systemDefault()
        val zonedDateTime: ZonedDateTime = ZonedDateTime.of(localDateTime, zone)

        val instant: Instant = zonedDateTime.toInstant()
        return Date.from(instant)
    }

    /**
     * getCurrentTimeOfString
     * @param formatStr format
     * @return String
     */
    fun getCurrentTimeOfString(formatStr: String): String {
        return toString(getCurrentLocalDateTime(), formatStr)
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
        return toString(localDateTime, formatStr)
    }
}