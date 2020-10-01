package com.example.todoapp

import TaskEntity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime


class MainActivity: AppCompatActivity(), View.OnClickListener, OnTaskListListener {
    companion object {
        private val TAG: String = Util().getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }

    private lateinit var mAllTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mSelectedTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mFragmentOnActivity: Fragment

    private val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"
    private val FORMAT_PATTERN_DATE: String = "yyyy/MM/dd(e)"
    private val FORMAT_PATTERN_YYYY: String = "yyyy"
    private val FORMAT_PATTERN_MM: String = "MM"
    private val FORMAT_PATTERN_DD: String = "dd"
    private val FORMAT_PATTERN_TIME: String = "HH:mm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        today_text.text = Util().getLocalDayTime(LocalDateTime.now(), "yyyy/MM/dd(E)")

        // set listener
        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

        // get data
        mAllTaskArrayList = arrayListOf(
            TaskEntity(
                FORMAT_PATTERN_DATE_ALL,
                FORMAT_PATTERN_DATE_ALL,
                "Sample"
            )
        )

        // fragment
        mFragmentOnActivity = TaskListFragment().newInstance("Fragment")
        replaceFragment(mFragmentOnActivity)
    }

    override fun getListItem(): ArrayList<TaskEntity> {
        return  mAllTaskArrayList
    }

    override fun onCreateListItem(startDate: String, endDate: String, title: String) {
        DLog(TAG, "onCreateListItem", "startDate:$startDate, endDate:$endDate, title:$title")
        mAllTaskArrayList.add(TaskEntity(startDate, endDate, title))

        // update list
        if (mFragmentOnActivity is TaskListFragment) {
            val fragment = mFragmentOnActivity as TaskListFragment
            fragment.updateAdapter(mAllTaskArrayList)
        } else if (mFragmentOnActivity is TaskCalendarFragment) {
            val fragment = mFragmentOnActivity as TaskCalendarFragment
            fragment.updateAdapter(mAllTaskArrayList)
        }
    }

    override fun onRemoveListItem(position: Int): ArrayList<TaskEntity> {
        DLog(TAG, "onRemoveListItem", "position:$position")
        mAllTaskArrayList.removeAt(position)
        return  mAllTaskArrayList
    }

    override fun onEditListItem(position: Int): ArrayList<TaskEntity> {
        DLog(TAG, "onEditListItem", "position:$position")
        val startTime = mAllTaskArrayList[position].startTime.toString()
        val endTime = mAllTaskArrayList[position].endTime.toString()
        DLog(TAG, "onEditListItem", "startTime:${startTime}, endTime:${endTime}")


        val args = Bundle()
        val dialog = TaskCreateDialogFragment()
        args.putBoolean("EXTRA_EDIT", true)
        args.putInt("EXTRA_EDIT_POSITION", position)
        args.putString("EXTRA_START_TIME", startTime)
        args.putString("EXTRA_END_TIME", endTime)
        dialog.arguments = args
        dialog.show(supportFragmentManager, "create")
        return mAllTaskArrayList
    }

    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("KEY_TASK_LIST", mAllTaskArrayList)
        fragment.arguments = bundle

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.task_add_button -> {
                val args = Bundle()
                val dialog = TaskCreateDialogFragment()
                args.putBoolean("EDIT", false)
                dialog.arguments = args
                dialog.show(supportFragmentManager, "create")
            }
            R.id.calendar_day_button -> {
                mFragmentOnActivity = TaskListFragment().newInstance("Fragment")
                replaceFragment(mFragmentOnActivity)
                today_text.text = Util().getLocalDayTime(LocalDateTime.now(), FORMAT_PATTERN_DATE)
            }
            R.id.calendar_week_button -> {
                mFragmentOnActivity = TaskCalendarFragment().newInstance("Fragment")
                replaceFragment(mFragmentOnActivity)
            }
            R.id.setting_button -> {

            }
            else -> {

            }
        }
    }
}