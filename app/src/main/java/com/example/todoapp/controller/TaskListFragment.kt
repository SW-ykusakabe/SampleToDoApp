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

    private lateinit var mDate: LocalDateTime
    private lateinit var mSelectedTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mTaskListAdapter: TaskListAdapter
    private lateinit var mTaskListListener: OnTaskListListener

    /**
     * newInstance - return to this instance
     * @param date　String of today date
     * @return This instance
     */
    fun newInstance(date: String): TaskListFragment {
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
        val currentTime = Util.getCurrentLocalDateTime()
        if (args != null) {
            val dateString = args.getString(KEY_ARGS_TASK_DATE, Util.toString(currentTime, FORMAT_PATTERN_DATE_ALL))
            mDate = Util.toLocalDateTime(dateString, FORMAT_PATTERN_DATE_ALL)
//            val activity = activity as MainActivity
//            Log.d(TAG, "date:$dateString")
//            activity.setToolBarText(data)
            mSelectedTaskArrayList = mTaskListListener.getTodayList(mDate)
            Log.d(TAG, "TaskListPageAdapter: dataSize=${mSelectedTaskArrayList.size}")

        } else {
            Log.d(TAG, "TaskListPageAdapter: args is null")
        }

        // set list view
        val listView = view.findViewById<ListView>(R.id.task_list)
        listView.onItemLongClickListener = this
        mTaskListAdapter = TaskListAdapter(view.context, mSelectedTaskArrayList)
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
                        mSelectedTaskArrayList = mTaskListListener.onRemoveListItem(mSelectedTaskArrayList[position], mDate)
                        mTaskListAdapter.updateList(mSelectedTaskArrayList)
                    }
                    1 -> {
                        mTaskListListener.onEditListItem(mSelectedTaskArrayList[position], position)
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
    fun updateAdapter() {
        mTaskListAdapter.updateList(mSelectedTaskArrayList)
    }

    /**
     * setSelectTime -　Set the date of the argument to the adapter
     * @param selectTime Selected date
     */
    fun setSelectTime(selectTime: LocalDateTime) {
        mTaskListAdapter.setSelectTime(selectTime)
    }
}