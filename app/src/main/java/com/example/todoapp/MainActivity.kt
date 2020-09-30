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
import java.util.*


class MainActivity: AppCompatActivity(), View.OnClickListener, TaskListFragment.OnListItemClickImpl {
    companion object {
        val TAG: String = object : Any() {}.javaClass.enclosingClass.name
    }
    private lateinit var mTaskArrayList: ArrayList<TaskEntity>
    private lateinit var mFragmentOnActivity: Fragment

    private val YEAR_FORMAT_PATTERN_ALL: String = "yyyy/MM/dd(E)-HH:mm"
    private val YEAR_FORMAT_PATTERN_DATE: String = "yyyy/MM/dd(E)"
    private val YEAR_FORMAT_PATTERN_YYYY: String = "yyyy"
    private val MONTH_FORMAT_PATTERN_MM: String = "MM"
    private val DAY_FORMAT_PATTERN_DD: String = "dd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        today_text.text = Util().getDayTime(YEAR_FORMAT_PATTERN_DATE)

        // set listener
        task_add_button.setOnClickListener(this)
        calendar_day_button.setOnClickListener(this)
        calendar_week_button.setOnClickListener(this)
        setting_button.setOnClickListener(this)

        // get data
        mTaskArrayList = arrayListOf(TaskEntity("10 : 00", "11 : 00", "No Title"))

        // fragment
        mFragmentOnActivity = TaskListFragment().newInstance("Fragment")
        replaceFragment(mFragmentOnActivity)
    }

    override fun onListItemClick(position: Int): ArrayList<TaskEntity> {
        Log.d(TAG, "onListItemClick: pos:${position}")
        mTaskArrayList.removeAt(position)
        return  mTaskArrayList
    }

    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("KEY_TASK_LIST", mTaskArrayList)
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

                val year = Util().getDayTime(YEAR_FORMAT_PATTERN_YYYY)
                val month = Util().getDayTime(MONTH_FORMAT_PATTERN_MM)
                val day = Util().getDayTime(DAY_FORMAT_PATTERN_DD)

                val startYearEditText = dialogView.findViewById<EditText>(R.id.start_year_edit_text)
                startYearEditText.setText(year)
                val startMonthEditText =
                    dialogView.findViewById<EditText>(R.id.start_month_edit_text)
                startMonthEditText.setText(month)
                val startDayEditText = dialogView.findViewById<EditText>(R.id.start_day_edit_text)
                startDayEditText.setText(day)
                val startHourEditText = dialogView.findViewById<EditText>(R.id.start_hour_edit_text)
                val startMinuitEditText =
                    dialogView.findViewById<EditText>(R.id.start_minuit_edit_text)

                val endYearEditText = dialogView.findViewById<EditText>(R.id.end_year_edit_text)
                endYearEditText.setText(year)
                val endMonthEditText = dialogView.findViewById<EditText>(R.id.end_month_edit_text)
                endMonthEditText.setText(month)
                val endDayEditText = dialogView.findViewById<EditText>(R.id.end_day_edit_text)
                endDayEditText.setText(day)
                val endHourEditText = dialogView.findViewById<EditText>(R.id.end_hour_edit_text)
                val endMinuitEditText = dialogView.findViewById<EditText>(R.id.end_minuit_edit_text)

                val titleEditText = dialogView.findViewById<EditText>(R.id.dialog_title_text)

                builder.setView(dialogView)
                    .setTitle("Crate Task")
                    .setPositiveButton("Crate") { dialog, id ->

                        val dayTimeMaxLength =
                            resources.getInteger(R.integer.day_and_time_max_length)

                        // append task start time
                        var startHour = startHourEditText.text.toString()
                        var startMinuit = startMinuitEditText.text.toString()
                        startHour = Util().paddingLeftToString(startHour, dayTimeMaxLength)
                        startMinuit = Util().paddingLeftToString(startMinuit, dayTimeMaxLength)
                        val startTime = "$startHour : $startMinuit"

                        // append task end time
                        var endHour = endHourEditText.text.toString()
                        if (endHour.isEmpty()) {
                            endHour = "${(startHour.toInt() + 1) % 24}"
                        }
                        var endMinuit = endMinuitEditText.text.toString()
                        if (endMinuit.isEmpty()) {
                            endMinuit = startMinuit
                        }
                        endHour = Util().paddingLeftToString(endHour, dayTimeMaxLength)
                        endMinuit = Util().paddingLeftToString(endMinuit, dayTimeMaxLength)
                        val endTime = "$endHour : $endMinuit"

                        // title
                        var title = titleEditText.text.toString()
                        if (title.isEmpty()) {
                            title = "No Title"
                        }

                        val startYear = startYearEditText.text.toString()
                        val startMonth = startMonthEditText.text.toString()
                        val startDay = startDayEditText.text.toString()
                        val startWeek = Util().getWeek(startYear.toInt(), startMonth.toInt(), startDay.toInt())
                        val startDate = "${startYear}/${startMonth}/${startDay}(${startWeek})-${startHour}:${startMinuit}"

                        val endYear = endYearEditText.text.toString()
                        val endMonth = endMonthEditText.text.toString()
                        val endDay = endDayEditText.text.toString()
                        val endWeek = Util().getWeek(endYear.toInt(), endMonth.toInt(), endDay.toInt())
                        val endDate = "${endYear}/${endMonth}/${endDay}(${endWeek})-${endHour}:${endMinuit}"

                        Log.d(TAG, "startTime:$startTime, endTime:$endTime, title:$title")
                        mTaskArrayList.add(TaskEntity(startTime, endTime, title))

                        // update list
                        if (mFragmentOnActivity is TaskListFragment) {
                            val fragment = mFragmentOnActivity as TaskListFragment
                            fragment.updateAdapter(mTaskArrayList)
                        } else if (mFragmentOnActivity is TaskCalendarFragment) {
                            val fragment = mFragmentOnActivity as TaskCalendarFragment
                            fragment.updateAdapter(mTaskArrayList)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, id ->

                    }
                builder.create()
                builder.show()
            }
            R.id.calendar_day_button -> {
                mFragmentOnActivity = TaskListFragment().newInstance("Fragment")
                replaceFragment(mFragmentOnActivity)
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