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
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }

    private lateinit var mAllTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mSelectedTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mFragmentOnActivity: Fragment

    private val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"
    private val FORMAT_PATTERN_DATE_WEEK: String = "yyyy/MM/dd(E)"
    private val FORMAT_PATTERN_DATE: String = "yyyy/MM/dd"
    private val FORMAT_PATTERN_YYYY: String = "yyyy"
    private val FORMAT_PATTERN_MM: String = "MM"
    private val FORMAT_PATTERN_DD: String = "dd"
    private val FORMAT_PATTERN_TIME: String = "HH:mm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
        val currentTimeString = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)
        today_text.text = currentTimeString

        // set listener
        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

        // get data
        mAllTaskArrayList = arrayListOf()
        for (i in 0..6) {
            mAllTaskArrayList.add(TaskEntity(currentTimeLocalDateTime, currentTimeLocalDateTime, "Sample$i"))
        }

        mSelectedTaskArrayList = arrayListOf()
        for (i in 0 until mAllTaskArrayList.size) {
            if (Util.toString(mAllTaskArrayList[i].startTime, FORMAT_PATTERN_DATE_WEEK) == currentTimeString) {
                mSelectedTaskArrayList.add(mAllTaskArrayList[i])
            }
        }

        // fragment
        mFragmentOnActivity = TaskListFragment().newInstance(mSelectedTaskArrayList)
        replaceFragment(mFragmentOnActivity)
    }

    override fun getListItem(): ArrayList<TaskEntity> {
        return  mSelectedTaskArrayList
    }

    override fun onCreateListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String) {
        DLog(TAG, "onCreateListItem", "startDate:$startDate, endDate:$endDate, title:$title")
        mAllTaskArrayList.add(TaskEntity(startDate, endDate, title))
        val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
        val currentTimeString = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)
        if (Util.toString(startDate, FORMAT_PATTERN_DATE_WEEK) == currentTimeString) {
            mSelectedTaskArrayList.add(TaskEntity(startDate, endDate, title))
        }

        // update list
        updateList()
    }

    override fun onRemoveListItem(position: Int): ArrayList<TaskEntity> {
        DLog(TAG, "onRemoveListItem", "position:$position")
        mAllTaskArrayList.remove(mSelectedTaskArrayList[position])
        mSelectedTaskArrayList.removeAt(position)
        return  mSelectedTaskArrayList
    }

    override fun onEditListItem(position: Int): ArrayList<TaskEntity> {
        val startTime = Util.toString(mSelectedTaskArrayList[position].startTime, FORMAT_PATTERN_DATE_ALL)
        val endTime = Util.toString(mSelectedTaskArrayList[position].endTime, FORMAT_PATTERN_DATE_ALL)
        val title = mSelectedTaskArrayList[position].title
        DLog(TAG, "onEditListItem", "position:$position, startTime:${startTime}, endTime:${endTime}, title:$title")

        val dialog = TaskCreateDialogFragment().newInstance(true, position, startTime, endTime, title)
        dialog.show(supportFragmentManager, "create")
        return mSelectedTaskArrayList
    }

    override fun onChangeListItem(year: Int, month: Int, dayOfWeek: Int) {
        DLog(TAG, "onChangeListItem", "year:$year, month:$month, dayOfWeek:$dayOfWeek")
        val monthStr = Util.paddingLeftToString(month.toString(), 2)
        val dayOfWeekStr = Util.paddingLeftToString(dayOfWeek.toString(), 2)
        val selectDay = "$year/$monthStr/$dayOfWeekStr"

        val selectDayAndWeek = "$selectDay(${Util.getWeekAsString(year, month, dayOfWeek)})"
        today_text.text = selectDayAndWeek

        mSelectedTaskArrayList.removeAll(mAllTaskArrayList)
        for (i in 0 until mAllTaskArrayList.size) {
            if (Util.toString(mAllTaskArrayList[i].startTime, FORMAT_PATTERN_DATE) == selectDay) {
                mSelectedTaskArrayList.add(mAllTaskArrayList[i])
            }
        }

        // update list
        updateList()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    private fun updateList() {
        if (mFragmentOnActivity is TaskListFragment) {
            val fragment = mFragmentOnActivity as TaskListFragment
            fragment.updateAdapter(mSelectedTaskArrayList)
        } else if (mFragmentOnActivity is TaskCalendarFragment) {
            val fragment = mFragmentOnActivity as TaskCalendarFragment
            fragment.updateAdapter(mSelectedTaskArrayList)
        }
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
                val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
                val currentTimeString = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)
                today_text.text = currentTimeString

                mSelectedTaskArrayList = arrayListOf()
                for (i in 0 until mAllTaskArrayList.size) {
                    if (Util.toString(mAllTaskArrayList[i].startTime, FORMAT_PATTERN_DATE_WEEK) == currentTimeString) {
                        mSelectedTaskArrayList.add(mAllTaskArrayList[i])
                    }
                }
                mFragmentOnActivity = TaskListFragment().newInstance(mSelectedTaskArrayList)
                replaceFragment(mFragmentOnActivity)

            }
            R.id.calendar_week_button -> {
                mFragmentOnActivity = TaskCalendarFragment().newInstance(mSelectedTaskArrayList)
                replaceFragment(mFragmentOnActivity)
            }
            R.id.setting_button -> {

            }
            else -> {

            }
        }
    }
}