package com.example.todoapp.entitys

import android.util.Log
import com.example.todoapp.Util
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateManager(localDateTime: LocalDateTime) {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }

    private var mCalendar: Calendar
    private val mWeeks: Int = 6

    val days: List<Date>
        get() {
            Log.d(TAG, "getDays <start>")
            val startDate: Date = mCalendar.time

            val count = mWeeks * 7

            mCalendar.set(Calendar.DATE, 1)
            val dayOfWeek: Int = mCalendar.get(Calendar.DAY_OF_WEEK) - 1
            mCalendar.add(Calendar.DATE, -dayOfWeek)
            val days: MutableList<Date> = ArrayList()
            for (i in 0 until count) {
                days.add(mCalendar.time)
                mCalendar.add(Calendar.DATE, 1)
            }

            mCalendar.time = startDate
            Log.d(TAG, "getDays <end>")
            return days
        }

    init {
        Log.d(TAG, "init <start>")
        mCalendar = Calendar.getInstance()
        mCalendar.time = Util.toDate(localDateTime)
        Log.d(TAG, "init <end>")
    }

    /**
     * isCurrentMonth
     * @param date
     * @return Boolean
     */
    fun isCurrentMonth(date: Date): Boolean {
        Log.d(TAG, "isCurrentMonth <start>")
        val format = SimpleDateFormat("yyyy.MM", Locale.JAPAN)
        val currentMonth: String = format.format(mCalendar.time)
        Log.d(TAG, "isCurrentMonth <end>")
        return currentMonth == format.format(date)
    }

    /**
     * isCurrentDay
     * @param date
     * @return Boolean
     */
    fun isCurrentDay(date: Date): Boolean {
        Log.d(TAG, "isCurrentDay <start>")
        val currentTime = Util.getCurrentLocalDateTime().truncatedTo(ChronoUnit.DAYS)
        val selectTime = Util.toLocalDateTime(date).truncatedTo(ChronoUnit.DAYS)
        Log.d(TAG, "isCurrentDay <end>")
        return currentTime == selectTime
    }

    /**
     * getDayOfWeek
     * @param date
     * @return getDayOfWeek
     */
    fun getDayOfWeek(date: Date): Int {
        Log.d(TAG, "getDayOfWeek <start>")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        Log.d(TAG, "getDayOfWeek <end>")
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun nextMonth() {
        Log.d(TAG, "nextMonth <start>")
        mCalendar.add(Calendar.MONTH, 1)
        Log.d(TAG, "nextMonth <end>")
    }

    fun lastMonth() {
        Log.d(TAG, "lastMonth <start>")
        mCalendar.add(Calendar.MONTH, -1)
        Log.d(TAG, "lastMonth <end>")
    }

}