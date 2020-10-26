package com.example.todoapp.controller

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.models.CalendarAdapter
import com.example.todoapp.models.OnTaskListListener

/**
 * TaskCalendarDateFragment -  Fragment for task calendar
 */
class TaskCalendarDateFragment: Fragment(), AdapterView.OnItemClickListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
    }

    private lateinit var mGridView: GridView
    private lateinit var mTaskListListener: OnTaskListListener
    private lateinit var mCalendarAdapter: CalendarAdapter

    /**
     * newInstance - return to this instance
     * @param date　String of today date
     * @return This instance
     */
    fun newInstance(date: String): TaskCalendarDateFragment {
        Log.d(TAG, "newInstance <start>")
        val args = Bundle()
        val fragment = TaskCalendarDateFragment()
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
//        TaskCalendarFragment().setListener(this)
        mTaskListListener = context as OnTaskListListener
        val view = inflater.inflate(R.layout.fragment_task_calendar_date, container, false)

        val args = arguments
        val dateString = args?.getString(KEY_ARGS_TASK_DATE, Util.getCurrentTimeOfString(FORMAT_PATTERN_DATE_ALL))
        val localDateTime = Util.toLocalDateTime(dateString!!, FORMAT_PATTERN_DATE_ALL)

        mGridView = view.findViewById<GridView>(R.id.calendar_grid_view)
        mCalendarAdapter = CalendarAdapter(view.context, localDateTime)
        mGridView.adapter = mCalendarAdapter
        mGridView.onItemClickListener = this

        Log.d(TAG, "onCreateView <end>")
        return view
    }

    /** @inheritDoc */
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        Log.d(TAG, "onItemClick <start>")
        Log.d(TAG, "onItemClick : position=$position")
        mCalendarAdapter.changeSelectDate(view, position)

        val date = Util.toLocalDateTime(mCalendarAdapter.getSelectDate(position))
        mTaskListListener.onSelectDateToChange(date = date)
        Log.d(TAG, "onItemClick <end>")
    }

    /**
     * changeScrollDate
     * @param count
     */
    fun changeScrollDate(count: Int) {
        Log.d(TAG, "changeScrollDate <start>")
        val targetView: View = mGridView.getChildAt(mCalendarAdapter.getSelectPosition() + count)
        mCalendarAdapter.changeScrollDate(targetView, count)
        Log.d(TAG, "changeScrollDate <end>")
    }
}