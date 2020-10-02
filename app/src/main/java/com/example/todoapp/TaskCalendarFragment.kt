package com.example.todoapp

import TaskEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class TaskCalendarFragment: Fragment(), CalendarView.OnDateChangeListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }
    private val KYE_ARGS_TASK_LIST: String = "ARGS_TASK_LIST"

    private lateinit var mTaskListListener: OnTaskListListener
    private lateinit var mFragment: TaskListFragment

    fun newInstance(array: ArrayList<TaskEntity>): TaskCalendarFragment {
        val args = Bundle()
        val fragment = TaskCalendarFragment()
        args.putParcelableArrayList(KYE_ARGS_TASK_LIST, array)
        fragment.arguments = args
        return fragment
    }

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

    override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
        DLog(TAG, "onSelectedDayChange", "year:$year, month:${month + 1}, dayOfMonth:$dayOfMonth")
        mTaskListListener.onChangeListItem(year, month + 1, dayOfMonth)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.list_container, fragment)
        fragmentTransaction.commit()
    }

    fun updateAdapter(array: ArrayList<TaskEntity>) {
        mFragment.updateAdapter(array)
    }
}