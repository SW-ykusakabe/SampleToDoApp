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
import java.time.LocalDateTime

/**
 * TaskCalendarFragment -  Fragment for task calendar
 */
class TaskCalendarFragment: Fragment(), CalendarView.OnDateChangeListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
    }

    private lateinit var mTaskListListener: OnTaskListListener
    private lateinit var mFragment: TaskListFragment

    /**
     * newInstance - return to this instance
     * @param date　String of today date
     * @return This instance
     */
    fun newInstance(date: String): TaskCalendarFragment {
        Log.d(TAG, "newInstance <start>")
        val args = Bundle()
        val fragment = TaskCalendarFragment()
        args.putString(KEY_ARGS_TASK_DATE, date)
        fragment.arguments = args
        Log.d(TAG, "newInstance <end>")
        return fragment
    }

    /** @inheritDoc */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView <start>")
        mTaskListListener = context as OnTaskListListener
        val view = inflater.inflate(R.layout.fragment_task_calendar, container, false)

        val calendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        calendarView.setOnDateChangeListener(this)
        Log.d(TAG, "onCreateView <end>")
        return view
    }

    /** @inheritDoc */
    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        Log.d(TAG, "onSelectedDayChange <start>")
        Log.d(TAG, "onSelectedDayChange : year:$year, month:${month + 1}, dayOfMonth:$dayOfMonth")

//        val selectTimeLocalDateTime = LocalDateTime.of(year, month, dayOfMonth, 0, 0)
//        mTaskListListener.getTodayList(selectTimeLocalDateTime)

//        mTaskListListener.onChangeListItem(year, month + 1, dayOfMonth)
//        mFragment.setSelectTime(LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0))
        Log.d(TAG, "onSelectedDayChange <end>")
    }
}