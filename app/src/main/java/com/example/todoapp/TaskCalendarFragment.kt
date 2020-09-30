package com.example.todoapp

import TaskEntity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class TaskCalendarFragment() : Fragment() {
    companion object {
        private val TAG : String = this::class.java.simpleName
    }

    fun newInstance(str: String) : TaskCalendarFragment{
        val args = Bundle()
        val fragment = TaskCalendarFragment()
        args.putString("ARGS_NAME", str);
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_calendar, container, false)
        val args = arguments
        var array : ArrayList<TaskEntity> = arrayListOf()
        if (args != null) {
            val str = args.getString("ARGS_NAME")
            array = args.getParcelableArrayList<TaskEntity>("KEY_TASK_LIST") as ArrayList<TaskEntity>
        }
        replaceFragment(TaskListFragment().newInstance("Fragment"), array)

        return view
    }

    private fun replaceFragment(fragment: Fragment, arrayList: ArrayList<TaskEntity>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("KEY_TASK_LIST", arrayList)
        fragment.arguments = bundle

        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.list_container, fragment)
        fragmentTransaction.commit()
    }
}