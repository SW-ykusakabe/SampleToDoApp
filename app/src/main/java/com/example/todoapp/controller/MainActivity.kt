package com.example.todoapp.controller

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todoapp.*
import com.example.todoapp.entitys.TaskEntity
import com.example.todoapp.entitys.TaskDBEntity
import com.example.todoapp.Util
import com.example.todoapp.models.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

/**
 * MainActivity - Activity for main
 */
class MainActivity: AppCompatActivity(), View.OnClickListener, OnTaskListListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)

        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"
        private const val FORMAT_PATTERN_DATE_WEEK: String = "yyyy/MM/dd(E)"
    }

    private lateinit var mAllTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mFragmentOnActivity: Fragment
    private lateinit var mTaskDao: TaskDao

    /** @inheritDoc */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate <start>")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
        setToolBarText(currentTimeLocalDateTime)

        // get data
        val appDatabase = AppDatabase.newInstance(this)
        mTaskDao = appDatabase.taskDao()
        if (mTaskDao.getAll().isEmpty()) {
            Log.d(TAG, "onCreate : getData is empty")

            // --------------------------------------test data--------------------------------------
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val yesterdayLocalDateTime = Util.toLocalDateTime(date = calendar.time)

            calendar.add(Calendar.DAY_OF_MONTH, 2)
            val tomorrowLocalDateTime = Util.toLocalDateTime(date = calendar.time)

            val yesterdayStr = Util.toString(yesterdayLocalDateTime, "yyyy/MM/dd(e)-")
            val currentStr = Util.toString(currentTimeLocalDateTime, "yyyy/MM/dd(e)-")
            val tomorrowStr = Util.toString(tomorrowLocalDateTime, "yyyy/MM/dd(e)-")
            val title = "Test data"

            mTaskDao.insert(TaskDBEntity(0, "${yesterdayStr}08:00", "${tomorrowStr}23:00", title))
            mTaskDao.insert(TaskDBEntity(0, "${yesterdayStr}08:00", "${currentStr}23:00", title))
            mTaskDao.insert(TaskDBEntity(0, "${currentStr}08:00", "${currentStr}23:00", title))
            mTaskDao.insert(TaskDBEntity(0, "${currentStr}08:00", "${tomorrowStr}23:00", title))
            // --------------------------------------test data--------------------------------------
        }
        val array = mTaskDao.getAll()
        Log.d(TAG, "onCreate : array.size:${array.size}")

        // get all data
        mAllTaskArrayList = arrayListOf()
        for (i in array.indices) {
            mAllTaskArrayList.add(
                TaskEntity(
                    Util.toLocalDateTime(array[i].startTime, FORMAT_PATTERN_DATE_ALL),
                    Util.toLocalDateTime(array[i].endTime, FORMAT_PATTERN_DATE_ALL),
                    array[i].title)
            )
        }
        sortList(mAllTaskArrayList)

//        mSelectedTaskArrayList = arrayListOf()
//        extractTasks(currentTimeLocalDateTime)

        // set listener
        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

        // create fragment
        val dataStr = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_ALL)
        mFragmentOnActivity = TaskListPagerFragment().newInstance(dataStr)
//        mFragmentOnActivity = TaskListFragment().newInstance(mSelectedTaskArrayList)
        replaceFragment(mFragmentOnActivity)
        Log.d(TAG, "onCreate <end>")
    }

    /** @inheritDoc */
    override fun getTodayList(date: LocalDateTime): ArrayList<TaskEntity> {
        Log.d(TAG, "getTodayList <start>")
        val array = extractTasks(date)
        Log.d(TAG, "getTodayList <end>")
        return array
    }

    /** @inheritDoc */
    override fun onAddListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String) {
        Log.d(TAG, "onAddListItem <start>")
        Log.d(TAG, "onAddListItem : startDate:$startDate, endDate:$endDate, title:$title")
        mTaskDao.insert(TaskDBEntity(
            0,
            Util.toString(startDate, FORMAT_PATTERN_DATE_ALL),
            Util.toString(endDate, FORMAT_PATTERN_DATE_ALL),
            title))
        mAllTaskArrayList.add(TaskEntity(startDate, endDate, title))
        sortList(mAllTaskArrayList)

        Log.d(TAG, "onAddListItem <end>")
    }

    /** @inheritDoc */
    override fun onRemoveListItem(taskEntity: TaskEntity?, date: LocalDateTime) {
        Log.d(TAG, "onRemoveListItem <start>")
        Log.d(TAG, "onRemoveListItem : date:$date")
        if (taskEntity == null) {
            Log.d(TAG, "onRemoveListItem : taskEntity is null")
            mTaskDao.deleteAll()
            mAllTaskArrayList.removeAll(mAllTaskArrayList)
        } else {
            Log.d(TAG, "onRemoveListItem : remove start")
            val array = mTaskDao.getTasksAll(
                Util.toString(taskEntity.startTime, FORMAT_PATTERN_DATE_ALL),
                Util.toString(taskEntity.endTime, FORMAT_PATTERN_DATE_ALL),
                taskEntity.title)
            Log.d(TAG, "onRemoveListItem : arraySize=${array.size}")
            mTaskDao.delete(array[0].id)
            mAllTaskArrayList.remove(taskEntity)
            Log.d(TAG, "onRemoveListItem : mAllTaskArrayList.size=${mAllTaskArrayList.size}")
            Log.d(TAG, "onRemoveListItem : remove end")
        }
        Log.d(TAG, "onRemoveListItem <end>")
    }

    /** @inheritDoc */
    override fun onEditListItem(taskEntity: TaskEntity, position: Int) {
        Log.d(TAG, "onEditListItem <start>")
        val dialog = TaskCreateDialogFragment().newInstance(
            true,
            position,
            taskEntity
        )
        dialog.show(supportFragmentManager, "create")
        Log.d(TAG, "onEditListItem <end>")
    }

    /** @inheritDoc */
    override fun onChangeListItem(year: Int, month: Int, dayOfWeek: Int) {
        Log.d(TAG, "onEditListItem <start>")
        Log.d(TAG, "onChangeListItem : year:$year, month:$month, dayOfWeek:$dayOfWeek")

        val selectTimeLocalDateTime = LocalDateTime.of(year, month, dayOfWeek, 0, 0)
        setToolBarText(selectTimeLocalDateTime)

        extractTasks(selectTimeLocalDateTime)

        // update list
        updateList()
        Log.d(TAG, "onEditListItem <end>")
    }

    @Override
    override fun onClick(v: View) {
        Log.d(TAG, "onClick <start>")
        when (v.id) {
            R.id.task_add_button -> {
                val args = Bundle()
                val dialog = TaskCreateDialogFragment()
                dialog.arguments = args
                dialog.show(supportFragmentManager, "create")
            }
            R.id.calendar_day_button -> {
                task_add_button.visibility = View.VISIBLE
                val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
                val dateString = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_ALL)

//                mSelectedTaskArrayList.removeAll(mAllTaskArrayList)
                extractTasks(currentTimeLocalDateTime)

//                mFragmentOnActivity = TaskListFragment().newInstance(dateString)
                mFragmentOnActivity = TaskListPagerFragment().newInstance(dateString)
                replaceFragment(mFragmentOnActivity)
            }
            R.id.calendar_week_button -> {
                task_add_button.visibility = View.VISIBLE
                val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
                val dateString = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_ALL)

//                mSelectedTaskArrayList.removeAll(mAllTaskArrayList)
                extractTasks(currentTimeLocalDateTime)

                mFragmentOnActivity = TaskCalendarFragment().newInstance(dateString)
                replaceFragment(mFragmentOnActivity)
            }
            R.id.setting_button -> {
                task_add_button.visibility = View.INVISIBLE
                today_text.text = this.resources.getString(R.string.settings_title)
                mFragmentOnActivity = SettingsFragment().newInstance()
                replaceFragment(mFragmentOnActivity)
            }
            else -> {

            }
        }
        Log.d(TAG, "onClick <end>")
    }

    /**
     * setToolBarText - Set date on toolbar
     * @param date LocalDateTime to set
     */
    fun setToolBarText(date: LocalDateTime) {
        Log.d(TAG, "setToolBarText <start>")
        today_text.text = Util.toString(date, FORMAT_PATTERN_DATE_WEEK)
        Log.d(TAG, "setToolBarText <end>")
    }

    /**
     * replaceFragment - Replace the inside of the container with an argument fragment
     * @param fragment Fragment to display
     */
    private fun replaceFragment(fragment: Fragment) {
        Log.d(TAG, "replaceFragment <start>")
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
        Log.d(TAG, "replaceFragment <end>")
    }

    /**
     * extractTasks - Display the task with the date of the argument
     * @param selectDate　Date to display
     */
    private fun extractTasks(selectDate: LocalDateTime): ArrayList<TaskEntity> {
        Log.d(TAG, "extractTasks <start>")
        val truncateSelectedDateToDays = selectDate.truncatedTo(ChronoUnit.DAYS)
        val array: ArrayList<TaskEntity> = arrayListOf()
        for (i in 0 until mAllTaskArrayList.size) {
            val startTime = mAllTaskArrayList[i].startTime
            val endTime = mAllTaskArrayList[i].endTime
            if ((truncateSelectedDateToDays.isEqual(startTime.truncatedTo(ChronoUnit.DAYS))
                        || truncateSelectedDateToDays.isAfter(startTime))
                && (truncateSelectedDateToDays.isEqual(endTime.truncatedTo(ChronoUnit.DAYS))
                        || truncateSelectedDateToDays.isBefore(endTime))) {
                array.add(mAllTaskArrayList[i])
                sortList(array)
            }
        }
        Log.d(TAG, "extractTasks <end>")
        return array
    }

    /**
     * updateList - Update the displayed list
     */
    private fun updateList() {
        Log.d(TAG, "updateList <start>")
        if (mFragmentOnActivity is TaskListFragment) {
            val fragment = mFragmentOnActivity as TaskListFragment
            fragment.updateAdapter()
//            mFragmentOnActivity = TaskListPagerFragment().newInstance("dateString", mSelectedTaskArrayList)
        } else if (mFragmentOnActivity is TaskCalendarFragment) {
            val fragment = mFragmentOnActivity as TaskCalendarFragment
            fragment.updateAdapter()
        }
        Log.d(TAG, "updateList <end>")
    }

    /**
     * sortList - Sort the list of arguments
     * @param array ArrayList of TaskEntity class you want to sort
     */
    private fun sortList(array: ArrayList<TaskEntity>) {
        Log.d(TAG, "sortList <start>")
        Collections.sort(array, TaskListComparator())
        Log.d(TAG, "sortList <end>")
    }
}