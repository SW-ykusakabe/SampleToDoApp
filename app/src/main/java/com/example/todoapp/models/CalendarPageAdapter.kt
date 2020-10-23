package com.example.todoapp.models

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.todoapp.Util
import com.example.todoapp.controller.TaskCalendarDateFragment
import java.time.LocalDateTime

/**
 * TaskListPageAdapter - Adapter for task list pager
 */
class CalendarPageAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val SCROLL_SIZE = 6
    }

    private lateinit var mDate: LocalDateTime
    private lateinit var mFragment: TaskCalendarDateFragment

    /** @inheritDoc */
    override fun getCount(): Int {
        return SCROLL_SIZE
    }

    /** @inheritDoc */
    override fun getItem(position: Int): Fragment {
        Log.d(TAG, "getItem <start>")
        val dateStr: String = Util.toString(mDate.plusMonths(position - 1L), FORMAT_PATTERN_DATE_ALL)
        Log.d(TAG, "getItem:$position, time:${mDate.plusMonths(position - 1L)}")
        Log.d(TAG, "getItem <end>")
        mFragment = TaskCalendarDateFragment().newInstance(dateStr)
        return mFragment
    }

    /** @inheritDoc */
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    /**
     * initializeData
     * @param date
     */
    fun initializeData(date: LocalDateTime) {
        Log.d(TAG, "initializeData <start>")
        mDate = date
        Log.d(TAG, "initializeData <end>")
    }

    /**
     * forwardData
     * @param date
     */
    fun forwardMonth(date: LocalDateTime) {
        Log.d(TAG, "forwardData <start>")
        mDate = date
        Log.d(TAG, "forwardData <end>")
    }

    /**
     * rewindData
     * @param date
     */
    fun rewindMonth(date: LocalDateTime) {
        Log.d(TAG, "rewindData <start>")
        mDate = date.minusDays((count - 3).toLong())
        Log.d(TAG, "rewindData <end>")
    }
}