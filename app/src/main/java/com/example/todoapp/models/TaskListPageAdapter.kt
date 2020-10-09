package com.example.todoapp.models

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.todoapp.Util
import com.example.todoapp.controller.TaskListFragment
import com.example.todoapp.entitys.TaskEntity

/**
 * TaskListPageAdapter - Adapter for task list pager
 */
class TaskListPageAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val SCROLL_SIZE = 5
    }

    private lateinit var mDateString: String
    private lateinit var mArrayList: ArrayList<TaskEntity>

    /** @inheritDoc */
    override fun getCount(): Int {
        return SCROLL_SIZE
    }

    /** @inheritDoc */
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
            }
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
        }
        return TaskListFragment().newInstance(mDateString, mArrayList)
    }

    /**
     * initializeData
     * @param date
     */
    fun initializeData(date: String) {
        val currentTime = Util.getCurrentLocalDateTime()
        mDateString = date
        mArrayList = arrayListOf(TaskEntity(currentTime, currentTime, "test"))

        for (i in 0 until 5) {
//            imageList.add(imageFetcher.fetchRandomImage())
        }
    }

    /**
     * forwardData
     */
    fun forwardData() {
        Log.d(TAG, "forwardData")
        for (i in 0 until 3) {
//            imageList.removeAt(0)
        }

        for (i in 0 until 3) {
//            imageList.add(imageFetcher.fetchRandomImage())
        }
    }

    /**
     * rewindData
     */
    fun rewindData() {
        Log.d(TAG, "rewindData")
        for (i in 0 until 3) {
//            imageList.removeAt(5 - i - 1)
        }

        for (i in 0 until 3) {
//            imageList.add(0, imageFetcher.fetchRandomImage())
        }
    }
}