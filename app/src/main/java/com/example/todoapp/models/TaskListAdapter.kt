package com.example.todoapp.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.todoapp.R
import com.example.todoapp.entitys.TaskEntity
import com.example.todoapp.Util
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * TaskListAdapter - Adapter for task list
 */
class TaskListAdapter(context: Context,
                      private var mTaskList: ArrayList<TaskEntity>,
                      private var mDate: LocalDateTime):
    ArrayAdapter<TaskEntity>(context, 0, mTaskList) {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)

        private const val FORMAT_PATTERN_TIME: String = "HH : mm"
    }

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /** @inheritDoc */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG, "getView <start>")
        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.item_task_list, parent, false)
        }
        if (mTaskList.size > position) {
            val task = mTaskList[position]

            Log.d(TAG, "getView : mDate=$mDate")
            val selectDay = mDate.truncatedTo(ChronoUnit.DAYS)

            // set start time
            val startTime = view?.findViewById<TextView>(R.id.start_time_text)
            if (selectDay.isEqual(task.startTime.truncatedTo(ChronoUnit.DAYS))) {
                startTime?.text = Util.toString(task.startTime, FORMAT_PATTERN_TIME)
                startTime?.visibility = View.VISIBLE
            } else {
                startTime?.visibility = View.GONE
            }

            // set end time
            val endTime = view?.findViewById<TextView>(R.id.end_time_text)
            if (selectDay.isEqual(task.endTime.truncatedTo(ChronoUnit.DAYS))) {
                endTime?.text = Util.toString(task.endTime, FORMAT_PATTERN_TIME)
                endTime?.visibility = View.VISIBLE
            } else {
                endTime?.visibility = View.GONE
            }

            // set title
            val title = view?.findViewById<TextView>(R.id.title_list_item_text)
            title?.text = task.title
        }
        Log.d(TAG, "getView <end>")
        return view!!
    }

    /**
     * setSelectTime -　Set the date of the argument to the adapter
     * @param selectTime Selected date
     */
    fun setSelectTime(selectTime: LocalDateTime) {
        Log.d(TAG, "setSelectTime <start>")
        mDate = selectTime
        Log.d(TAG, "setSelectTime <end>")
    }
}