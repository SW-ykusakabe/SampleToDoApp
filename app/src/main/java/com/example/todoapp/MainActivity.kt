package com.example.todoapp

import TaskEntity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener, TaskListFragment.OnListItemClickImpl {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }
    lateinit var mArrayList: ArrayList<TaskEntity>
    var taskListFragment = TaskListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

//        replaceFragment(TaskListFragment().newInstance("Fragment"))
        mArrayList = arrayListOf(TaskEntity("time", "title"))
        replaceFragment(taskListFragment)
    }

    override fun onListItemClick(position: Int) : ArrayList<TaskEntity> {
        Log.d(TAG, "pos:${position}")
        mArrayList.removeAt(position)
        return  mArrayList
    }

    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("KEY_TASK_LIST", mArrayList)
        fragment.arguments = bundle

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.task_add_button -> {
                val builder = AlertDialog.Builder(this)
                val inflater = this.layoutInflater
                val dialogView = inflater.inflate(R.layout.add_task_dialog_item, null)

                builder.setView(dialogView)
                    .setTitle("Crate Task")
                    .setPositiveButton("Crate") { dialog, id ->
                        val time = dialogView.findViewById<EditText>(R.id.dialog_time_text).text.toString()
                        val title = dialogView.findViewById<EditText>(R.id.dialog_title_text).text.toString()
                        Log.d(TAG, "time:${time}, title:${title}")
                        mArrayList.add(TaskEntity(time, title))
                        taskListFragment.updateAdapter(mArrayList)
                    }
                    .setNegativeButton("Cancel") { dialog, id ->

                    }
                builder.create()
                builder.show()
            }

            R.id.calendar_day_button -> {
                replaceFragment(TaskListFragment().newInstance("ListFragment"))

            }

            R.id.calendar_week_button -> {
                replaceFragment(TaskCalendarFragment().newInstance("CalendarFragment"))

            }

            R.id.setting_button -> {

            }

            else -> {

            }
        }
    }
}