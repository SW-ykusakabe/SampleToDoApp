package com.example.todoapp.models

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.todoapp.Util
import com.example.todoapp.controller.TaskListFragment
import java.time.LocalDateTime

/**
 * TaskListPageAdapter - Adapter for task list pager
 */
class TaskListPageAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val SCROLL_SIZE = 6
    }

    private lateinit var mDate: LocalDateTime

    /** @inheritDoc */
    override fun getCount(): Int {
        return SCROLL_SIZE
    }

    /** @inheritDoc */
    override fun getItem(position: Int): Fragment {
        Log.d(TAG, "getItem <start>")
        val dateStr: String = when (position) {
            0 -> {
                Log.d(TAG, "getItem:0, time:${mDate.minusDays(2)}")
                Util.toString(mDate.minusDays(2), FORMAT_PATTERN_DATE_ALL)
            }
            1 -> {
                Log.d(TAG, "getItem:1, time:${mDate.minusDays(1)}")
                Util.toString(mDate.minusDays(1), FORMAT_PATTERN_DATE_ALL)
            }
            2 -> {
                Log.d(TAG, "getItem:2, time:${mDate}")
                Util.toString(mDate, FORMAT_PATTERN_DATE_ALL)
            }
            3 -> {
                Log.d(TAG, "getItem:3, time:${mDate.plusDays(1)}")
                Util.toString(mDate.plusDays(1), FORMAT_PATTERN_DATE_ALL)
            }
            4 -> {
                Log.d(TAG, "getItem:4, time:${mDate.plusDays(2)}")
                Util.toString(mDate.plusDays(2), FORMAT_PATTERN_DATE_ALL)
            }
            5 -> {
                Log.d(TAG, "getItem:5, time:${mDate.plusDays(3)}")
                Util.toString(mDate.plusDays(3), FORMAT_PATTERN_DATE_ALL)
            }
            else -> {
                Util.toString(Util.getCurrentLocalDateTime(), FORMAT_PATTERN_DATE_ALL)
            }
        }
        Log.d(TAG, "getItem <end>")
        return TaskListFragment().newInstance(dateStr)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        Log.d(TAG, "setPrimaryItem <start>")
        super.setPrimaryItem(container, position, `object`)
        Log.d(TAG, "setPrimaryItem <end>")
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
     * @param data
     */
    fun forwardData(date: LocalDateTime) {
        Log.d(TAG, "forwardData <start>")
        mDate = date.plusDays(1)
        Log.d(TAG, "forwardData <end>")
    }

    /**
     * rewindData
     */
    fun rewindData(date: LocalDateTime) {
        Log.d(TAG, "rewindData <start>")
        mDate = date.minusDays(1)
        Log.d(TAG, "rewindData <end>")
    }
}