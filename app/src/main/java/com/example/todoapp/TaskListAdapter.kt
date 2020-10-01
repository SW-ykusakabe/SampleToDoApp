package com.example.todoapp

import TaskEntity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TaskListAdapter(context: Context, private var mTaskList: ArrayList<TaskEntity>): ArrayAdapter<TaskEntity>(context, 0, mTaskList) {
    companion object {
        private val TAG: String = Util().getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = mTaskList[position]

        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.task_list_item, parent, false)
        }

        val startTime = view?.findViewById<TextView>(R.id.start_time_text)
        startTime?.text = Util().insertWhitespace(task.startTime.toString())

        val endTime = view?.findViewById<TextView>(R.id.end_time_text)
        endTime?.text = Util().insertWhitespace(task.endTime.toString())

        val title = view?.findViewById<TextView>(R.id.title_list_item_text)
        title?.text = task.title

        return view!!
    }

    fun updateAnimalList(taskList: ArrayList<TaskEntity>) {
        mTaskList = taskList
        // 再描画
        notifyDataSetChanged()
    }
}