package com.example.todoapp

import TaskEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment


class TaskListFragment : Fragment(), AdapterView.OnItemClickListener{
    companion object {
        private val TAG : String = this::class.java.simpleName
    }
    lateinit var mTaskListAdapter: TaskListAdapter

    interface OnListItemClickImpl {
        fun onListItemClick(position: Int) : ArrayList<TaskEntity>
    }

    lateinit var mListener : OnListItemClickImpl

    fun newInstance(str: String) : TaskListFragment {
        val args = Bundle()
        val fragment = TaskListFragment()
        args.putString("ARGS_NAME", str);
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListener = context as OnListItemClickImpl
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        val args = arguments
        var array : ArrayList<TaskEntity> = arrayListOf()
        if (args != null) {
            val str = args.getString("ARGS_NAME")
            array = args.getParcelableArrayList<TaskEntity>("KEY_TASK_LIST") as ArrayList<TaskEntity>
        }

        val listView = view.findViewById<ListView>(R.id.task_list)
        listView.onItemClickListener = this
        mTaskListAdapter = TaskListAdapter(view.context, array)
        listView.adapter = mTaskListAdapter

        return view
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val array = mListener.onListItemClick(position)
        mTaskListAdapter.updateAnimalList(array);
    }

    fun updateAdapter(array : ArrayList<TaskEntity>) {
        mTaskListAdapter.updateAnimalList(array);

    }
}