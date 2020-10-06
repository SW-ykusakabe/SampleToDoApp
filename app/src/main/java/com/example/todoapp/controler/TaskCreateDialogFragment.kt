package com.example.todoapp.controler

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.todoapp.OnTaskListListener
import com.example.todoapp.R
import com.example.todoapp.utils.DLog
import com.example.todoapp.utils.Util
import java.time.LocalDateTime
import java.util.*

class TaskCreateDialogFragment: DialogFragment() {
    companion object {
        private val TAG: String = Util.getClassName(object :
            Any() {}.javaClass.enclosingClass.name)
    }

    private val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"
    private val KEY_ARGS_EDIT: String = "EXTRA_EDIT"
    private val KEY_ARGS_EDIT_POSITION: String = "EXTRA_EDIT_POSITION"
    private val KEY_ARGS_START_TIME: String = "EXTRA_START_TIME"
    private val KEY_ARGS_END_TIME: String = "EXTRA_END_TIME"
    private val KEY_ARGS_TITLE: String = "EXTRA_TITLE"

    private lateinit var mTaskListListener: OnTaskListListener

    fun newInstance(isEdit: Boolean, pos: Int, startTime: String, endTime: String, title: String): TaskCreateDialogFragment {
        val args = Bundle()
        val fragment = TaskCreateDialogFragment()
        args.putBoolean(KEY_ARGS_EDIT, isEdit)
        args.putInt(KEY_ARGS_EDIT_POSITION, pos)
        args.putString(KEY_ARGS_START_TIME, startTime)
        args.putString(KEY_ARGS_END_TIME, endTime)
        args.putString(KEY_ARGS_TITLE, title)
        fragment.arguments = args
        return fragment
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mTaskListListener = context as OnTaskListListener

        val args = arguments

        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_task_dialog_item, null)

        val startYearEditText = dialogView.findViewById<EditText>(R.id.start_year_edit_text)
        val startMonthEditText = dialogView.findViewById<EditText>(R.id.start_month_edit_text)
        val startDayEditText = dialogView.findViewById<EditText>(R.id.start_day_edit_text)
        val startHourEditText = dialogView.findViewById<EditText>(R.id.start_hour_edit_text)
        val startMinuitEditText = dialogView.findViewById<EditText>(R.id.start_minuit_edit_text)

        val endYearEditText = dialogView.findViewById<EditText>(R.id.end_year_edit_text)
        val endMonthEditText = dialogView.findViewById<EditText>(R.id.end_month_edit_text)
        val endDayEditText = dialogView.findViewById<EditText>(R.id.end_day_edit_text)
        val endHourEditText = dialogView.findViewById<EditText>(R.id.end_hour_edit_text)
        val endMinuitEditText = dialogView.findViewById<EditText>(R.id.end_minuit_edit_text)

        val titleEditText = dialogView.findViewById<EditText>(R.id.dialog_title_text)

        var isEdit = false
        var editPosition = -1

        if (args != null) {
            isEdit = args.getBoolean(KEY_ARGS_EDIT, false)
            editPosition = args.getInt(KEY_ARGS_EDIT_POSITION, -1)

            val startTime = args.getString(
                KEY_ARGS_START_TIME, Util.toString(
                    LocalDateTime.now(),
                    FORMAT_PATTERN_DATE_ALL
                )
            ).toString()
            startYearEditText.setText(Util.extractToYear(startTime))
            startMonthEditText.setText(Util.extractToMonth(startTime))
            startDayEditText.setText(Util.extractToDay(startTime))

            val endTime = args.getString(
                KEY_ARGS_END_TIME,
                Util.toString(LocalDateTime.now(), FORMAT_PATTERN_DATE_ALL)
            ).toString()
            endYearEditText.setText(Util.extractToYear(endTime))
            endMonthEditText.setText(Util.extractToMonth(endTime))
            endDayEditText.setText(Util.extractToDay(endTime))

            if (isEdit) {
                startHourEditText.setText(Util.extractToHour(startTime))
                startMinuitEditText.setText(Util.extractToMinuit(startTime))

                endHourEditText.setText(Util.extractToHour(endTime))
                endMinuitEditText.setText(Util.extractToMinuit(endTime))

                titleEditText.setText(args.getString(KEY_ARGS_TITLE))
            }
            DLog(TAG, "onCreateDialog", "startTime:${startTime}, endTime:${endTime}")
        }

        builder.setView(dialogView)
            .setTitle("Crate Task")
            .setPositiveButton("Crate") { dialog, id ->
                val dayTimeMaxLength =
                    resources.getInteger(R.integer.day_and_time_max_length)

                // append task start time
                val startYear = startYearEditText.text.toString()
                val startMonth = startMonthEditText.text.toString()
                val startDay = startDayEditText.text.toString()
                val startWeek = Util.getWeekAsInt(
                    startYear.toInt(),
                    startMonth.toInt(),
                    startDay.toInt()
                )
                var startHour = startHourEditText.text.toString()
                var startMinuit = startMinuitEditText.text.toString()
                startHour = Util.paddingLeftToString(startHour, dayTimeMaxLength)
                startMinuit = Util.paddingLeftToString(startMinuit, dayTimeMaxLength)
                val startDate = "${startYear}/${startMonth}/${startDay}(${startWeek})-$startHour:$startMinuit"
                val startLocalDateTime = Util.toLocalDateTime(startDate, FORMAT_PATTERN_DATE_ALL)
                val startCalendar: Calendar = Calendar.getInstance()
                startCalendar.set(startYear.toInt(), startMonth.toInt() - 1, startDay.toInt(),startHour.toInt(), startMinuit.toInt())

                // append task end time
                val endYear = endYearEditText.text.toString()
                val endMonth = endMonthEditText.text.toString()
                var endDay = endDayEditText.text.toString()
                var endWeek = Util.getWeekAsInt(endYear.toInt(), endMonth.toInt(), endDay.toInt())
                var endHour = endHourEditText.text.toString()
                if (endHour.isEmpty()) {
                    startCalendar.add(Calendar.HOUR_OF_DAY, 1)
                    val endTime = Util.toString(startCalendar.time, FORMAT_PATTERN_DATE_ALL)

                    endDay = Util.extractToDay(endTime)
                    endDay = Util.paddingLeftToString(endDay, dayTimeMaxLength)

                    val year = Util.extractToYear(endTime)
                    val month = Util.extractToMonth(endTime)
                    val day = Util.extractToDay(endTime)
                    endWeek = Util.getWeekAsInt(year.toInt(), month.toInt(), day.toInt())

                    endHour = Util.extractToHour(endTime)
                }
                endHour = Util.paddingLeftToString(endHour, dayTimeMaxLength)
                var endMinuit = endMinuitEditText.text.toString()
                if (endMinuit.isEmpty()) {
                    endMinuit = startMinuit
                }
                endMinuit = Util.paddingLeftToString(endMinuit, dayTimeMaxLength)
                val endDate = "${endYear}/${endMonth}/${endDay}(${endWeek})-$endHour:$endMinuit"
                val endLocalDateTime = Util.toLocalDateTime(endDate, FORMAT_PATTERN_DATE_ALL)

                // title
                var title = titleEditText.text.toString()
                if (title.isEmpty()) {
                    title = "No Title"
                }

                if (isEdit) {
                    mTaskListListener.onRemoveListItem(editPosition)
                }
                mTaskListListener.onCreateListItem(startLocalDateTime, endLocalDateTime, title)
            }
            .setNegativeButton("Cancel") { dialog, id ->
            }

        return builder.create()
    }
}