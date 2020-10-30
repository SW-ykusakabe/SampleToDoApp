package com.example.todoapp.controller

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.entitys.TaskDBEntity
import com.example.todoapp.entitys.TaskEntity
import com.example.todoapp.models.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * MainActivity - Activity for main
 */
class MainActivity: AppCompatActivity(), View.OnClickListener, OnTaskListListener {
    //region member variable
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)

        private const val VIEW_TASK_LIST: Int = 0
        private const val VIEW_TASK_CALENDAR: Int = 1
        private const val VIEW_SETTING: Int = 2

        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"
        private const val FORMAT_PATTERN_DATE_WEEK: String = "yyyy/MM/dd(E)"
    }

    private lateinit var mAllTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mUpperFragmentOnActivity: Fragment
    private lateinit var mLowerFragmentOnActivity: Fragment
    private lateinit var mTaskDao: TaskDao
    private var mScreenDisplay: Int = VIEW_TASK_LIST
    //endregion

    //region override
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

        // set listener
        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

        // create fragment
        val dataStr = Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_ALL)
        mLowerFragmentOnActivity = TaskListPagerFragment().newInstance(dataStr)
        replaceFragment(mLowerFragmentOnActivity)
        Log.d(TAG, "onCreate <end>")
    }

    /** @inheritDoc */
    override fun getTodayList(date: LocalDateTime): ArrayList<TaskEntity> {
        Log.d(TAG, "getTodayList <start>")
        val array: ArrayList<TaskEntity> = arrayListOf()
        val truncateSelectedDateToDays = date.truncatedTo(ChronoUnit.DAYS)

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
        updateList()

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
            mTaskDao.delete(array[0].id)
            mAllTaskArrayList.remove(taskEntity)
        }

        updateList()
        Log.d(TAG, "onRemoveListItem <end>")
    }

    /** @inheritDoc */
    override fun onEditListItem(taskEntity: TaskEntity) {
        Log.d(TAG, "onEditListItem <start>")
        val dialog = TaskCreateDialogFragment().newInstance(
            true,
            taskEntity
        )
        dialog.show(supportFragmentManager, "create")
        Log.d(TAG, "onEditListItem <end>")
    }

    /** @inheritDoc */
    override fun onSelectDateToChange(date: LocalDateTime) {
        Log.d(TAG, "onSelectListToChange <start>")
        setToolBarText(date)
        if (mLowerFragmentOnActivity is TaskListPagerFragment) {
            (mLowerFragmentOnActivity as TaskListPagerFragment).pagerReload(date)
        }
        Log.d(TAG, "onSelectListToChange <end>")
    }

    /** @inheritDoc */
    override fun onScrollListToChange(date: LocalDateTime) {
        Log.d(TAG, "onScrollListToChange <start>")
        if (mUpperFragmentOnActivity is TaskCalendarFragment) {
//            (mUpperFragmentOnActivity as TaskCalendarFragment).dayChange(date)
        }
        Log.d(TAG, "onScrollListToChange <end>")
    }

    fun notification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val channelId = "NOTIFICATION_CHANNEL_ID_SAMPLE"
        val builder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Notification Title")
            setContentText("のーてぃふぃけーしょん　てきすと")
            priority = NotificationCompat.PRIORITY_DEFAULT

            // Pending Intent の設定
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        createNotificationChannel(channelId)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(channelId: String) {
        val name = "チャンネルの名前"
        val description = "チャンネルの説明文"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }


    private fun scheduleNotification(content: String, calendar: Calendar) {
        val notificationIntent = Intent(this, AlarmBroadcastReceiver::class.java)
        notificationIntent.putExtra(AlarmBroadcastReceiver().NOTIFICATION_ID, 1)
        notificationIntent.putExtra(AlarmBroadcastReceiver().NOTIFICATION_CONTENT, content)
        val pendingIntent = PendingIntent.getBroadcast(this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    /** @inheritDoc */
    override fun onClick(v: View) {
        Log.d(TAG, "onClick <start>")
        when (v.id) {
            R.id.task_add_button -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.SECOND, 3)
                scheduleNotification("10秒後に届く通知です", calendar)


//                val calendar = Calendar.getInstance()
//                calendar.timeInMillis = System.currentTimeMillis()
//                calendar.add(Calendar.SECOND, 5)
//
//                val intent = Intent(this, AlarmBroadcastReceiver::class.java)
//
//                val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
//
//                val am = getSystemService(ALARM_SERVICE) as AlarmManager
//                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
//                Toast.makeText(this, "Set Alarm ", Toast.LENGTH_SHORT).show()





                Log.d(TAG, "onClick : clicked task_add_button")
                val dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN_DATE_WEEK)
                val localDateTime = LocalDate.parse(
                    today_text.text.toString(),
                    dateTimeFormatter
                ).atTime(LocalTime.MIN)

                val dialog = TaskCreateDialogFragment().newInstance(
                    false,
                    TaskEntity(localDateTime, localDateTime, "")
                )
                dialog.show(supportFragmentManager, "create")
            }
            R.id.calendar_day_button -> {
                Log.d(TAG, "onClick : clicked calendar_day_button")
                val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
                if (mScreenDisplay != VIEW_TASK_LIST) {
                    mScreenDisplay = VIEW_TASK_LIST
                    task_add_button.visibility = View.VISIBLE
                    val dateString =
                        Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_ALL)

                    mLowerFragmentOnActivity = TaskListPagerFragment().newInstance(dateString)
                    replaceFragment(mLowerFragmentOnActivity)
                } else {
                    (mLowerFragmentOnActivity as TaskListPagerFragment).pagerReload(
                        currentTimeLocalDateTime)
                }
            }
            R.id.calendar_week_button -> {
                Log.d(TAG, "onClick : clicked calendar_week_button")
                val currentTimeLocalDateTime = Util.getCurrentLocalDateTime()
                if (mScreenDisplay != VIEW_TASK_CALENDAR) {
                    mScreenDisplay = VIEW_TASK_CALENDAR
                    task_add_button.visibility = View.VISIBLE
                    val dateString =
                        Util.toString(currentTimeLocalDateTime, FORMAT_PATTERN_DATE_ALL)

                    setToolBarText(date = currentTimeLocalDateTime)

                    mLowerFragmentOnActivity = TaskListPagerFragment().newInstance(dateString)
                    mUpperFragmentOnActivity = TaskCalendarFragment().newInstance(dateString)
//                mUpperFragmentOnActivity = TaskCalendarPagerFragment().newInstance(dateString)
//                replaceFragment(mUpperFragmentOnActivity)
                    replaceFragment(mUpperFragmentOnActivity, mLowerFragmentOnActivity)
                } else {
                    (mLowerFragmentOnActivity as TaskListPagerFragment).pagerReload(
                        currentTimeLocalDateTime)
                    (mUpperFragmentOnActivity as TaskCalendarFragment).pageReload()
                }
            }
            R.id.setting_button -> {
                Log.d(TAG, "onClick : clicked setting_button")
                if (mScreenDisplay != VIEW_SETTING) {
                    mScreenDisplay = VIEW_SETTING
                    task_add_button.visibility = View.INVISIBLE
                    today_text.text = this.resources.getString(R.string.settings_title)
                    mLowerFragmentOnActivity = SettingsFragment().newInstance()
                    replaceFragment(mLowerFragmentOnActivity)
                }
            }
            else -> {

            }
        }
        Log.d(TAG, "onClick <end>")
    }
    //endregion

    //region public method
    /**
     * setToolBarText - Set date on toolbar
     * @param date LocalDateTime to set
     */
    fun setToolBarText(date: LocalDateTime) {
        Log.d(TAG, "setToolBarText <start>")
        today_text.text = Util.toString(date, FORMAT_PATTERN_DATE_WEEK)
        Log.d(TAG, "setToolBarText <end>")
    }

    fun sendDayChanged(count: Int) {
        Log.d(TAG, "sendDayChanged <start>")
        if (mScreenDisplay == VIEW_TASK_CALENDAR && mUpperFragmentOnActivity is TaskCalendarFragment) {
            (mUpperFragmentOnActivity as TaskCalendarFragment).dayChange(count)
        }
        Log.d(TAG, "sendDayChanged <end>")
    }
    //endregion

    //region private method
    /**
     * replaceFragment - Replace the inside of the container with an argument fragment
     * @param fragment Fragment to display
     */
    private fun replaceFragment(fragment: Fragment) {
        Log.d(TAG, "replaceFragment <start>")
        upper_container.visibility = View.GONE
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.lower_container, fragment)
        fragmentTransaction.commit()
        Log.d(TAG, "replaceFragment <end>")
    }

    /**
     * replaceFragment - Replace the inside of the container with an argument fragment
     * @param upperFragment Fragment to display
     */
    private fun replaceFragment(upperFragment: Fragment, lowerFragment: Fragment) {
        Log.d(TAG, "replaceFragment <start>")
        upper_container.visibility = View.VISIBLE
        val fragmentManager: FragmentManager = supportFragmentManager
        val upperFragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        upperFragmentTransaction.replace(R.id.upper_container, upperFragment)
        upperFragmentTransaction.commit()

        val lowerFragmentTransaction = fragmentManager.beginTransaction()
        lowerFragmentTransaction.replace(R.id.lower_container, lowerFragment)
        lowerFragmentTransaction.commit()
        Log.d(TAG, "replaceFragment <end>")
    }

    /**
     * updateList - Update the displayed list
     */
    private fun updateList() {
        Log.d(TAG, "updateList <start>")
        if (mLowerFragmentOnActivity is TaskListPagerFragment) {
            val fragment = mLowerFragmentOnActivity as TaskListPagerFragment
            fragment.pagerReload()
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
    //endregion
}