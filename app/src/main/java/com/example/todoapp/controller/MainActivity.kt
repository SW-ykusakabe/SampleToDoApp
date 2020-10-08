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
    private lateinit var mSelectedTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mFragmentOnActivity: Fragment
    private lateinit var  mTaskDao: TaskDao

    /** @inheritDoc */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
        today_text.text = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)

        // get data
        val appDatabase = AppDatabase.newInstance(this)
        mTaskDao = appDatabase.taskDao()
        if (mTaskDao.getAll().isEmpty()) {
            Log.d(TAG, "onCreate : getData is empty")
            mTaskDao.insert(TaskDBEntity(0, "2020/10/05(2)-12:00", "2020/10/06(3)-19:00", "Sample Title"))
            mTaskDao.insert(TaskDBEntity(0, "2020/10/06(3)-12:00", "2020/10/06(3)-19:00", "Sample Title"))
            mTaskDao.insert(TaskDBEntity(0, "2020/10/06(3)-13:00", "2020/10/07(4)-19:00", "Sample Title"))
            mTaskDao.insert(TaskDBEntity(0, "2020/10/07(4)-13:00", "2020/10/07(4)-19:00", "Sample Title"))
        }
        val array = mTaskDao.getAll()


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

        mSelectedTaskArrayList = arrayListOf()
        extractTasks(currentTimeLocalDateTime)

        // set listener
        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

        // fragment
        mFragmentOnActivity = TaskListFragment().newInstance(mSelectedTaskArrayList)
        replaceFragment(mFragmentOnActivity)
    }

    /** @inheritDoc */
    override fun onCreateListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String) {
        Log.d(TAG, "onCreateListItem : startDate:$startDate, endDate:$endDate, title:$title")
        mTaskDao.insert(TaskDBEntity(
            0,
            Util.toString(startDate, FORMAT_PATTERN_DATE_ALL),
            Util.toString(endDate, FORMAT_PATTERN_DATE_ALL),
            title))
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

    /** @inheritDoc */
    override fun onRemoveListItem(position: Int): ArrayList<TaskEntity> {
        Log.d(TAG, "onRemoveListItem : position:$position")
        if (position == -1) {
            mTaskDao.deleteAll()
            mAllTaskArrayList.removeAll(mAllTaskArrayList)
            mSelectedTaskArrayList.removeAll(mSelectedTaskArrayList)
        } else {
            val array = mTaskDao.getTasksAll(
                Util.toString(mSelectedTaskArrayList[position].startTime, FORMAT_PATTERN_DATE_ALL),
                Util.toString(mSelectedTaskArrayList[position].endTime, FORMAT_PATTERN_DATE_ALL),
                mSelectedTaskArrayList[position].title)
            mTaskDao.delete(array[0].id)
            mAllTaskArrayList.remove(mSelectedTaskArrayList[position])
            mSelectedTaskArrayList.removeAt(position)
        }
        return  mSelectedTaskArrayList
    }

    /** @inheritDoc */
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
        Log.d(
            TAG,
            "onEditListItem : position:$position, startTime:${startTime}, endTime:${endTime}, title:$title"
        )

        val dialog = TaskCreateDialogFragment().newInstance(
            true,
            position,
            mSelectedTaskArrayList[position]
        )
        dialog.show(supportFragmentManager, "create")
    }

    /** @inheritDoc */
    override fun onChangeListItem(year: Int, month: Int, dayOfWeek: Int) {
        Log.d(TAG, "onChangeListItem : year:$year, month:$month, dayOfWeek:$dayOfWeek")

        val selectTimeLocalDateTime = LocalDateTime.of(year, month, dayOfWeek, 0, 0)
        today_text.text = Util.toString(selectTimeLocalDateTime, FORMAT_PATTERN_DATE_WEEK)

        mSelectedTaskArrayList.removeAll(mAllTaskArrayList)
        extractTasks(selectTimeLocalDateTime)

        // update list
        updateList()
    }

    @Override
    override fun onClick(v: View) {
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
                today_text.text = this.resources.getString(R.string.settings_title)
                mFragmentOnActivity = SettingsFragment().newInstance()
                replaceFragment(mFragmentOnActivity)
            }
            else -> {

            }
        }
    }

    /**
     * replaceFragment - Replace the inside of the container with an argument fragment
     * @param fragment Fragment to display
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    /**
     * extractTasks - Display the task with the date of the argument
     * @param selectDate　Date to display
     */
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

    /**
     * updateList - Update the displayed list
     */
    private fun updateList() {
        if (mFragmentOnActivity is TaskListFragment) {
            val fragment = mFragmentOnActivity as TaskListFragment
            fragment.updateAdapter(mSelectedTaskArrayList)
        } else if (mFragmentOnActivity is TaskCalendarFragment) {
            val fragment = mFragmentOnActivity as TaskCalendarFragment
            fragment.updateAdapter(mSelectedTaskArrayList)
        }
    }

    /**
     * sortList - Sort the list of arguments
     * @param array ArrayList of TaskEntity class you want to sort
     */
    private fun sortList(array: ArrayList<TaskEntity>) {
        Collections.sort(array, TaskListComparator())
    }
}