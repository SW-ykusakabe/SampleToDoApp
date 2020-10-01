package com.example.todoapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.time.LocalDateTime
import java.util.*

class TaskCreateDialogFragment: DialogFragment() {
    companion object {
        private val TAG: String = Util().getClassName(object :
            Any() {}.javaClass.enclosingClass.name)
    }

    private val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"
    private val FORMAT_PATTERN_DATE: String = "yyyy/MM/dd(e)"
    private val FORMAT_PATTERN_YYYY: String = "yyyy"
    private val FORMAT_PATTERN_MM: String = "MM"
    private val FORMAT_PATTERN_DD: String = "dd"
    private val FORMAT_PATTERN_TIME: String = "HH:mm"

    private lateinit var mTaskListListener: OnTaskListListener

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
            isEdit = args.getBoolean("EXTRA_EDIT", false)
            editPosition = args.getInt("EXTRA_EDIT_POSITION", -1)

            val startTime = args.getString(
                "EXTRA_START_TIME", Util().getLocalDayTime(
                    LocalDateTime.now(),
                    FORMAT_PATTERN_DATE_ALL
                )
            ).toString()
            startYearEditText.setText(Util().extractionToYear(startTime))
            startMonthEditText.setText(Util().extractionToMonth(startTime))
            startDayEditText.setText(Util().extractionToDay(startTime))

            val endTime = args.getString(
                "EXTRA_END_TIME",
                Util().getLocalDayTime(LocalDateTime.now(), FORMAT_PATTERN_DATE_ALL)
            ).toString()
            endYearEditText.setText(Util().extractionToYear(endTime))
            endMonthEditText.setText(Util().extractionToMonth(endTime))
            endDayEditText.setText(Util().extractionToDay(endTime))

            if (isEdit) {
                startHourEditText.setText(Util().extractionToHour(startTime))
                startMinuitEditText.setText(Util().extractionToMinuit(startTime))

                endHourEditText.setText(Util().extractionToHour(endTime))
                endMinuitEditText.setText(Util().extractionToMinuit(endTime))
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
                val startWeek = Util().getWeek(
                    startYear.toInt(),
                    startMonth.toInt(),
                    startDay.toInt()
                )
                var startHour = startHourEditText.text.toString()
                var startMinuit = startMinuitEditText.text.toString()
                startHour = Util().paddingLeftToString(startHour, dayTimeMaxLength)
                startMinuit = Util().paddingLeftToString(startMinuit, dayTimeMaxLength)
                val startDate = "${startYear}/${startMonth}/${startDay}(${startWeek})-$startHour:$startMinuit"
                val startCalendar: Calendar = Calendar.getInstance()
                startCalendar.set(startYear.toInt(), startMonth.toInt() - 1, startDay.toInt(),startHour.toInt(), startMinuit.toInt())

                // append task end time
                val endYear = endYearEditText.text.toString()
                val endMonth = endMonthEditText.text.toString()
                var endDay = endDayEditText.text.toString()
                val endWeek = Util().getWeek(endYear.toInt(), endMonth.toInt(), endDay.toInt())
                var endHour = endHourEditText.text.toString()
                if (endHour.isEmpty()) {

                    val localDateTime = Util().toLocalDateTime(startCalendar.time)
                    val time = Util().getLocalDayTime(localDateTime, FORMAT_PATTERN_DATE_ALL)

                    endDay = startCalendar.add(Calendar.DAY_OF_MONTH, 1).toString()
                    DLog(TAG, "", "endDay:${endDay}")
                    endDay = Util().paddingLeftToString(endDay, dayTimeMaxLength)
                    DLog(TAG, "", "hour:${startCalendar.get(Calendar.HOUR_OF_DAY)}")
                    DLog(TAG, "", "hour:${startCalendar.add(Calendar.HOUR_OF_DAY, -1)}")
                    endHour = startCalendar.add(Calendar.HOUR_OF_DAY, -1).toString()
                    DLog(TAG, "", "hour:$endHour")
//                        "${(startHour.toInt() + 1) % 24}"
                }
                var endMinuit = endMinuitEditText.text.toString()
                if (endMinuit.isEmpty()) {
                    endMinuit = startMinuit
                }
                endHour = Util().paddingLeftToString(endHour, dayTimeMaxLength)
                endMinuit = Util().paddingLeftToString(endMinuit, dayTimeMaxLength)
                val endDate = "${endYear}/${endMonth}/${endDay}(${endWeek})-$endHour:$endMinuit"
                val endCalendar: Calendar = Calendar.getInstance()
//                endCalendar.set(endYear.toInt(), endMonth.toInt(), endDay.toInt(),endHour.toInt(), endMinuit.toInt())

                // title
                var title = titleEditText.text.toString()
                if (title.isEmpty()) {
                    title = "No Title"
                }

                if (isEdit) {
                    mTaskListListener.onRemoveListItem(editPosition)
                }
                mTaskListListener.onCreateListItem(startDate, endDate, title)
            }
            .setNegativeButton("Cancel") { dialog, id ->
            }

        return builder.create()
    }
}