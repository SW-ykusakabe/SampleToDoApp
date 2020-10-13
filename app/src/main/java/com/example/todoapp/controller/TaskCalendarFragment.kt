package com.example.todoapp.controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todoapp.models.OnTaskListListener
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.entitys.TaskEntity
import java.time.LocalDateTime

/**
 * TaskCalendarFragment -  Fragment for task calendar
 */
class TaskCalendarFragment: Fragment(), CalendarView.OnDateChangeListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
    }

    private lateinit var mTaskListListener: OnTaskListListener
    private lateinit var mFragment: TaskListFragment

    /**
     * newInstance - return to this instance
     * @param array　ArrayList of tasks to display
     * @return This instance
     */
    fun newInstance(date: String): TaskCalendarFragment {
        val args = Bundle()
        val fragment = TaskCalendarFragment()
        args.putString(KEY_ARGS_TASK_DATE, date)
        fragment.arguments = args
        return fragment
    }

    /** @inheritDoc */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mTaskListListener = context as OnTaskListListener
        val view = inflater.inflate(R.layout.fragment_task_calendar, container, false)
        val args = arguments
        var dateString = ""
        var array: ArrayList<TaskEntity> = arrayListOf()
        if (args != null) {
            dateString = args.getString(KEY_ARGS_TASK_DATE, Util.toString(Util.getCurrentLocalDateTime(), FORMAT_PATTERN_DATE_ALL))
            val data = Util.toLocalDateTime(dateString, FORMAT_PATTERN_DATE_ALL)
            array = mTaskListListener.getTodayList(data)
        }
        mFragment = TaskListFragment().newInstance(dateString)
        replaceFragment(mFragment)

        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        calendarView.setOnDateChangeListener(this)
        return view
    }

    /** @inheritDoc */
    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        Log.d(TAG, "onSelectedDayChange : year:$year, month:${month + 1}, dayOfMonth:$dayOfMonth")
        mTaskListListener.onChangeListItem(year, month + 1, dayOfMonth)
        mFragment.setSelectTime(LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0))
    }

    /**
     * replaceFragment - Replace the inside of the container with an argument fragment
     * @param fragment Fragment to display
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.list_container, fragment)
        fragmentTransaction.commit()
    }

    /**
     * updateAdapter - Update adapter with arrayList of arguments
     * @param array ArrayList to update
     */
    fun updateAdapter() {
        mFragment.updateAdapter()
    }
}