package com.example.todoapp

import TaskEntity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TaskListAdapter(context: Context, var mTaskList: List<TaskEntity>) : ArrayAdapter<TaskEntity>(context, 0, mTaskList) {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = mTaskList[position]

        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.task_list_item, parent, false)
        }

        val time = view?.findViewById<TextView>(R.id.time_list_item_text)
        time?.text = task.time

        val title = view?.findViewById<TextView>(R.id.title_list_item_text)
        title?.text = task.title

        return view!!
    }

    fun updateAnimalList(taskList: List<TaskEntity>) {
        mTaskList = taskList
        // 再描画
        notifyDataSetChanged()
    }
}