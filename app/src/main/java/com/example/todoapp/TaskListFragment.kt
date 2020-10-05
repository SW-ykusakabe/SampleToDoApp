package com.example.todoapp

import TaskEntity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import java.time.LocalDateTime


class TaskListFragment: Fragment(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }
    private val KYE_ARGS_TASK_LIST: String = "ARGS_TASK_LIST"

    private lateinit var mTaskListAdapter: TaskListAdapter
    private lateinit var mTaskListListener: OnTaskListListener

    fun newInstance(array: ArrayList<TaskEntity>): TaskListFragment {
        val args = Bundle()
        val fragment = TaskListFragment()
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
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        val args = arguments
        var array: ArrayList<TaskEntity> = arrayListOf()
        if (args != null) {
            array = args.getParcelableArrayList<TaskEntity>(KYE_ARGS_TASK_LIST) as ArrayList<TaskEntity>
        }

        // set list view
        val listView = view.findViewById<ListView>(R.id.task_list)
        listView.onItemClickListener = this
        listView.onItemLongClickListener = this
        mTaskListAdapter = TaskListAdapter(view.context, array)
        listView.adapter = mTaskListAdapter
        mTaskListAdapter.setSelectTime(Util.getCurrentLocalDateTime())

        return view
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val strList = arrayOf("削除","編集")

        AlertDialog.Builder(activity)
            .setTitle("title")
            .setItems(strList) { dialog, which ->
                when (which) {
                    0 -> {
                        val array = mTaskListListener.onRemoveListItem(position)
                        mTaskListAdapter.updateAnimalList(array)
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

    fun updateAdapter(array: ArrayList<TaskEntity>) {
        mTaskListAdapter.updateAnimalList(array)
    }

    fun setSelectTime(selectTime: LocalDateTime) {
        mTaskListAdapter.setSelectTime(selectTime)
    }
}