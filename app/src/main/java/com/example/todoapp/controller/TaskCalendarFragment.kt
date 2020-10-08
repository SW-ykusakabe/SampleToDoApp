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
        private const val KYE_ARGS_TASK_LIST: String = "ARGS_TASK_LIST"
    }

    private lateinit var mTaskListListener: OnTaskListListener
    private lateinit var mFragment: TaskListFragment

    /**
     * newInstance - return to this instance
     * @param array　ArrayList of tasks to display
     * @return This instance
     */
    fun newInstance(array: ArrayList<TaskEntity>): TaskCalendarFragment {
        val args = Bundle()
        val fragment = TaskCalendarFragment()
        args.putParcelableArrayList(KYE_ARGS_TASK_LIST, array)
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
        var array: ArrayList<TaskEntity> = arrayListOf()
        if (args != null) {
            array = args.getParcelableArrayList<TaskEntity>(KYE_ARGS_TASK_LIST) as ArrayList<TaskEntity>
        }
        mFragment = TaskListFragment().newInstance(array)
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
    fun updateAdapter(array: ArrayList<TaskEntity>) {
        mFragment.updateAdapter(array)
    }
}