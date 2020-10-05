package com.example.todoapp

import TaskEntity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class TaskListAdapter(context: Context, private var mTaskList: ArrayList<TaskEntity>): ArrayAdapter<TaskEntity>(context, 0, mTaskList) {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }
    private val FORMAT_PATTERN_TIME: String = "HH : mm"
    private lateinit var mSelecttime: LocalDateTime
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = mTaskList[position]

        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.task_list_item, parent, false)
        }

        // start
        val startTime = view?.findViewById<TextView>(R.id.start_time_text)
        var str: String
        val selectDay = mSelecttime.truncatedTo(ChronoUnit.DAYS)
        DLog(TAG, "test:", "$selectDay")
        DLog(TAG, "test:", "${task.startTime.truncatedTo(ChronoUnit.DAYS)}")
        DLog(TAG, "test:", "${task.startTime}")
        DLog(TAG, "test:", "----------------------")
        if (selectDay.isEqual(task.startTime.truncatedTo(ChronoUnit.DAYS))
            || mSelecttime.truncatedTo(ChronoUnit.DAYS).isBefore(task.startTime)) {
            DLog(TAG, "test:", "1")
            startTime?.text = Util.toString(task.startTime, FORMAT_PATTERN_TIME)
            startTime?.visibility = View.VISIBLE
        } else {
            DLog(TAG, "test:", "2")
            startTime?.visibility = View.GONE
        }

        // end
        val endTime = view?.findViewById<TextView>(R.id.end_time_text)
        if (selectDay.isEqual(task.endTime.truncatedTo(ChronoUnit.DAYS))
            || mSelecttime.truncatedTo(ChronoUnit.DAYS).isAfter(task.endTime)) {
            DLog(TAG, "test:", "3")
            endTime?.text = Util.toString(task.endTime, FORMAT_PATTERN_TIME)
            endTime?.visibility = View.VISIBLE
        } else {
            DLog(TAG, "test:", "4")
            endTime?.visibility = View.GONE
        }

        // title
        val title = view?.findViewById<TextView>(R.id.title_list_item_text)
        title?.text = task.title

        return view!!
    }

    fun updateAnimalList(taskList: ArrayList<TaskEntity>) {
        mTaskList = taskList
        // 再描画
        notifyDataSetChanged()
    }

    fun setSelectTime(selectTime: LocalDateTime) {
        mSelecttime = selectTime
    }
}