package com.example.todoapp.controler

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todoapp.*
import com.example.todoapp.entitys.TaskEntity
import com.example.todoapp.entitys.Tasks
import com.example.todoapp.models.AppDatabase
import com.example.todoapp.utils.DLog
import com.example.todoapp.utils.Util
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList


class MainActivity: AppCompatActivity(), View.OnClickListener, OnTaskListListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }

    private lateinit var mAllTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mSelectedTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mFragmentOnActivity: Fragment

    private val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"
    private val FORMAT_PATTERN_DATE_WEEK: String = "yyyy/MM/dd(E)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
        today_text.text = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)

        // get data
        val data = AppDatabase.getInstance(this)
        val taskDao = data.taskDao()
        taskDao.deleteAll()
        taskDao.insert(Tasks(0, "2020/10/05(2)-12:00", "2020/10/06(3)-19:00", "Sample Title"))
        taskDao.insert(Tasks(0, "2020/10/06(3)-12:00", "2020/10/06(3)-19:00", "Sample Title"))
        taskDao.insert(Tasks(0, "2020/10/06(3)-13:00", "2020/10/07(4)-19:00", "Sample Title"))
        taskDao.insert(Tasks(0, "2020/10/07(4)-13:00", "2020/10/07(4)-19:00", "Sample Title"))
        val array = taskDao.getAll()

        mAllTaskArrayList = arrayListOf()
        for (i in array.indices) {
            mAllTaskArrayList.add(
                TaskEntity(
                    Util.toLocalDateTime(array[i].startTime, FORMAT_PATTERN_DATE_ALL)
                    , Util.toLocalDateTime(array[i].endTime, FORMAT_PATTERN_DATE_ALL)
                    ,array[i].title)
            )
        }

        // set listener
        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

        sortList(mAllTaskArrayList)

        mSelectedTaskArrayList = arrayListOf()
        extractTasks(currentTimeLocalDateTime)

        // fragment
        mFragmentOnActivity = TaskListFragment().newInstance(mSelectedTaskArrayList)
        replaceFragment(mFragmentOnActivity)
    }

    override fun onCreateListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String) {
        DLog(TAG, "onCreateListItem", "startDate:$startDate, endDate:$endDate, title:$title")
        mAllTaskArrayList.add(TaskEntity(startDate, endDate, title))
        sortList(mAllTaskArrayList)

        val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
        val truncateSelectedDateToDays = currentTimeLocalDateTime.truncatedTo(ChronoUnit.DAYS)
        if ((truncateSelectedDateToDays.isEqual(startDate.truncatedTo(ChronoUnit.DAYS))
                    || truncateSelectedDateToDays.isAfter(startDate))
            && (truncateSelectedDateToDays.isEqual(endDate.truncatedTo((ChronoUnit.DAYS)))
                    || truncateSelectedDateToDays.isBefore(endDate))) {
            mSelectedTaskArrayList.add(TaskEntity(startDate, endDate, title))
            sortList(mSelectedTaskArrayList)
        }

        // update list
        updateList()
    }

    override fun onRemoveListItem(position: Int): ArrayList<TaskEntity> {
        DLog(TAG, "onRemoveListItem", "position:$position")
        if (position == -1) {
            mAllTaskArrayList.removeAll(mAllTaskArrayList)
            mSelectedTaskArrayList.removeAll(mSelectedTaskArrayList)
        } else {
            mAllTaskArrayList.remove(mSelectedTaskArrayList[position])
            mSelectedTaskArrayList.removeAt(position)
        }
        return  mSelectedTaskArrayList
    }

    override fun onEditListItem(position: Int) {
        val startTime = Util.toString(
            mSelectedTaskArrayList[position].startTime,
            FORMAT_PATTERN_DATE_ALL
        )
        val endTime = Util.toString(
            mSelectedTaskArrayList[position].endTime,
            FORMAT_PATTERN_DATE_ALL
        )
        val title = mSelectedTaskArrayList[position].title
        DLog(
            TAG,
            "onEditListItem",
            "position:$position, startTime:${startTime}, endTime:${endTime}, title:$title"
        )

        val dialog = TaskCreateDialogFragment().newInstance(
            true,
            position,
            startTime,
            endTime,
            title
        )
        dialog.show(supportFragmentManager, "create")
    }

    override fun onChangeListItem(year: Int, month: Int, dayOfWeek: Int) {
        DLog(TAG, "onChangeListItem", "year:$year, month:$month, dayOfWeek:$dayOfWeek")
        val dayTimeMaxLength = resources.getInteger(R.integer.day_and_time_max_length)
        val monthStr = Util.paddingLeftToString(month.toString(), dayTimeMaxLength)
        val dayOfWeekStr = Util.paddingLeftToString(dayOfWeek.toString(), dayTimeMaxLength)
        val selectDay = "$year/$monthStr/$dayOfWeekStr"

        val selectDayAndWeek = "$selectDay(${Util.getWeekAsString(year, month, dayOfWeek)})"
        today_text.text = selectDayAndWeek

        val selectTimeLocalDateTime = LocalDateTime.of(year, month, dayOfWeek, 0, 0)
        mSelectedTaskArrayList.removeAll(mAllTaskArrayList)
        extractTasks(selectTimeLocalDateTime)

        // update list
        updateList()
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
                task_add_button.visibility = View.VISIBLE
                val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
                today_text.text = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)

                mSelectedTaskArrayList.removeAll(mAllTaskArrayList)
                extractTasks(currentTimeLocalDateTime)

                mFragmentOnActivity = TaskListFragment().newInstance(mSelectedTaskArrayList)
                replaceFragment(mFragmentOnActivity)
            }
            R.id.calendar_week_button -> {
                task_add_button.visibility = View.VISIBLE
                val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
                today_text.text = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)

                mSelectedTaskArrayList.removeAll(mAllTaskArrayList)
                extractTasks(currentTimeLocalDateTime)

                mFragmentOnActivity = TaskCalendarFragment().newInstance(mSelectedTaskArrayList)
                replaceFragment(mFragmentOnActivity)
            }
            R.id.setting_button -> {
                task_add_button.visibility = View.INVISIBLE
                today_text.text = "Settings"
                mFragmentOnActivity = SettingsFragment().newInstance()
                replaceFragment(mFragmentOnActivity)
            }
            else -> {

            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    private fun extractTasks(selectDate: LocalDateTime) {
        val truncateSelectedDateToDays = selectDate.truncatedTo(ChronoUnit.DAYS)
        for (i in 0 until mAllTaskArrayList.size) {
            val startTime = mAllTaskArrayList[i].startTime
            val endTime = mAllTaskArrayList[i].endTime
            if ((truncateSelectedDateToDays.isEqual(startTime.truncatedTo(ChronoUnit.DAYS))
                        || truncateSelectedDateToDays.isAfter(startTime))
                && (truncateSelectedDateToDays.isEqual(endTime.truncatedTo((ChronoUnit.DAYS)))
                        || truncateSelectedDateToDays.isBefore(endTime))) {
                mSelectedTaskArrayList.add(mAllTaskArrayList[i])
                sortList(mSelectedTaskArrayList)
            }
        }
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

    private fun sortList(array: ArrayList<TaskEntity>) {
        Collections.sort(array, TaskListComparator())
    }
}