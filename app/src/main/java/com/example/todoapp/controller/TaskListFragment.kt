package com.example.todoapp.controller

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.todoapp.models.OnTaskListListener
import com.example.todoapp.R
import com.example.todoapp.entitys.TaskEntity
import com.example.todoapp.models.TaskListAdapter
import com.example.todoapp.Util
import java.time.LocalDateTime

/**
 * TaskListFragment -  Fragment for task list
 */
class TaskListFragment: Fragment(), AdapterView.OnItemLongClickListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
    }

    private lateinit var mTaskListAdapter: TaskListAdapter
    private lateinit var mTaskListListener: OnTaskListListener

    /**
     * newInstance - return to this instance
     * @param array　ArrayList of tasks to display
     * @return This instance
     */
    fun newInstance(date: String, array: ArrayList<TaskEntity>): TaskListFragment {
        val args = Bundle()
        val fragment = TaskListFragment()
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
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        val args = arguments
        var array: ArrayList<TaskEntity> = arrayListOf()
        val currentTime = Util.getCurrentLocalDateTime()
        if (args != null) {
            val dateString = args.getString(KEY_ARGS_TASK_DATE, Util.toString(currentTime, FORMAT_PATTERN_DATE_ALL))
            val data = Util.toLocalDateTime(dateString, FORMAT_PATTERN_DATE_ALL)
            val activity = activity as MainActivity
            activity.setToolBarText(data)
            array = mTaskListListener.getTodayList(data)

        }

        // set list view
        val listView = view.findViewById<ListView>(R.id.task_list)
        listView.onItemLongClickListener = this
        mTaskListAdapter = TaskListAdapter(view.context, array)
        listView.adapter = mTaskListAdapter
        mTaskListAdapter.setSelectTime(currentTime)

        return view
    }

    /** @inheritDoc */
    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val strList = arrayOf("削除", "編集")

        AlertDialog.Builder(activity)
            .setTitle("title")
            .setItems(strList) { dialog, which ->
                when (which) {
                    0 -> {
                        val array = mTaskListListener.onRemoveListItem(position)
                        mTaskListAdapter.updateList(array)
                    }
                    1 -> {
                        mTaskListListener.onEditListItem(position)
                    }
                }
            }
            .setPositiveButton("Cancel") { dialog, which ->
            }
            .show()
        return true
    }

    /**
     * updateAdapter - Update adapter with arrayList of arguments
     * @param array ArrayList to update
     */
    fun updateAdapter(array: ArrayList<TaskEntity>) {
        mTaskListAdapter.updateList(array)
    }

    /**
     * setSelectTime -　Set the date of the argument to the adapter
     * @param selectTime Selected date
     */
    fun setSelectTime(selectTime: LocalDateTime) {
        mTaskListAdapter.setSelectTime(selectTime)
    }
}