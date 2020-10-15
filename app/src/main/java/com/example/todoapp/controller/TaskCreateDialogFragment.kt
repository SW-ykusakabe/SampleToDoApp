package com.example.todoapp.controller

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.todoapp.models.OnTaskListListener
import com.example.todoapp.R
import com.example.todoapp.entitys.TaskEntity
import com.example.todoapp.Util
import java.time.DateTimeException
import java.time.LocalDateTime
import java.util.*
import kotlin.Any as KotlinAny

/**
 * TaskCreateDialogFragment -  Dialog fragment for task creation
 */
class TaskCreateDialogFragment: DialogFragment() {
    companion object {
        private val TAG: String = Util.getClassName(object : KotlinAny() {}.javaClass.enclosingClass.name)

        private const val FORMAT_PATTERN_YEAR: String = "yyyy"
        private const val FORMAT_PATTERN_MONTH: String = "MM"
        private const val FORMAT_PATTERN_DAY: String = "dd"
        private const val FORMAT_PATTERN_HOUR: String = "HH"
        private const val FORMAT_PATTERN_MINUTE: String = "mm"

        private const val KEY_ARGS_EDIT: String = "ARGS_EDIT"
        private const val KEY_ARGS_EDIT_POSITION: String = "ARGS_EDIT_POSITION"
        private const val KEY_ARGS_TASK_ENTITY: String = "KEY_ARGS_TASK_ENTITY"
    }

    private lateinit var mTaskListListener: OnTaskListListener

    /**
     * newInstance - return to this instance
     * @param isEdit is edit?
     * @param pos Selected list position
     * @param taskEntity taskEntity of the selected task
     * @return This instance
     */
    fun newInstance(isEdit: Boolean, pos: Int, taskEntity: TaskEntity): TaskCreateDialogFragment {
        Log.d(TAG, "newInstance <start>")
        val args = Bundle()
        val fragment = TaskCreateDialogFragment()
        args.putBoolean(KEY_ARGS_EDIT, isEdit)
        args.putInt(KEY_ARGS_EDIT_POSITION, pos)
        args.putParcelable(KEY_ARGS_TASK_ENTITY, taskEntity)
        fragment.arguments = args
        Log.d(TAG, "newInstance <end>")
        return fragment
    }

    /** @inheritDoc */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog <start>")
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
        val currentTime = Util.getCurrentLocalDateTime()
        var taskEntity: TaskEntity? = TaskEntity(currentTime, currentTime, "")

        if (args != null) {
            isEdit = args.getBoolean(KEY_ARGS_EDIT, false)
            editPosition = args.getInt(KEY_ARGS_EDIT_POSITION, -1)

            taskEntity = args.getParcelable(KEY_ARGS_TASK_ENTITY) as TaskEntity?
        }
        if (taskEntity == null) {
            val time = Util.getCurrentLocalDateTime()
            taskEntity = TaskEntity(time, time, "null")
        }
        val startTime = taskEntity.startTime
        val endTime = taskEntity.endTime
        startYearEditText.setText(Util.toString(startTime, FORMAT_PATTERN_YEAR))
        startMonthEditText.setText(Util.toString(startTime, FORMAT_PATTERN_MONTH))
        startDayEditText.setText(Util.toString(startTime, FORMAT_PATTERN_DAY))

        endYearEditText.setText(Util.toString(endTime, FORMAT_PATTERN_YEAR))
        endMonthEditText.setText(Util.toString(endTime, FORMAT_PATTERN_MONTH))
        endDayEditText.setText(Util.toString(endTime, FORMAT_PATTERN_DAY))

        if (isEdit) {
            startHourEditText.setText(Util.toString(startTime, FORMAT_PATTERN_HOUR))
            startMinuitEditText.setText(Util.toString(startTime, FORMAT_PATTERN_MINUTE))

            endHourEditText.setText(Util.toString(endTime, FORMAT_PATTERN_HOUR))
            endMinuitEditText.setText(Util.toString(endTime, FORMAT_PATTERN_MINUTE))

            titleEditText.setText(taskEntity.title)
        }
        Log.d(TAG,"onCreateDialog : startTime:${startTime}, endTime:${endTime}")

        builder.setView(dialogView)
            .setTitle("Crate Task")
            .setPositiveButton("Crate") { dialog, id ->
                val clickCurrentTime = Util.getCurrentLocalDateTime()
                
                // append task start time
                val startYear = startYearEditText.text.toString()
                val startMonth = startMonthEditText.text.toString()
                val startDay = startDayEditText.text.toString()
                var startHour = startHourEditText.text.toString()
                if (startHour.isEmpty()) {
                    startHour = Util.toString(clickCurrentTime, FORMAT_PATTERN_HOUR)
                }
                var startMinuit = startMinuitEditText.text.toString()
                if (startMinuit.isEmpty()) {
                    startMinuit = Util.toString(clickCurrentTime, FORMAT_PATTERN_MINUTE)
                }
                val startLocalDateTime = try {
                    LocalDateTime.of(
                        startYear.toInt(),
                        startMonth.toInt(),
                        startDay.toInt(),
                        startHour.toInt(),
                        startMinuit.toInt()
                    )
                } catch (e: DateTimeException) {
                    Log.e(TAG, "e:${e}")
                    simpleDialog("start time set error", "$e")
                    return@setPositiveButton
                }
                val startCalendar = Calendar.getInstance()
                startCalendar.set(
                    startYear.toInt(),
                    startMonth.toInt() - 1,
                    startDay.toInt(),
                    startHour.toInt(),
                    startMinuit.toInt()
                )

                // append task end time
                val endYear = endYearEditText.text.toString()
                val endMonth = endMonthEditText.text.toString()
                val endDay = endDayEditText.text.toString()
                val endHour = endHourEditText.text.toString()
                var endMinuit = endMinuitEditText.text.toString()
                if (endMinuit.isEmpty()) {
                    endMinuit = startMinuit
                }

                val endLocalDateTime = if (endHour.isEmpty()) {
                    startCalendar.add(Calendar.HOUR_OF_DAY, 1)
                    val clickStartTime = startCalendar.time

                    Util.toLocalDateTime(date = clickStartTime)
                } else {
                    try {
                        LocalDateTime.of(
                            endYear.toInt(),
                            endMonth.toInt(),
                            endDay.toInt(),
                            endHour.toInt(),
                            endMinuit.toInt()
                        )
//                        LocalDateTime.of(year = 2020, month = 10, dayOfMonth = 1, hour = 11, minute = 0)
                    } catch (e: DateTimeException) {
                        Log.e(TAG, "e:${e}")
                        simpleDialog("end time set error", "$e")
                        return@setPositiveButton
                    }
                }

                // title
                var title = titleEditText.text.toString()
                if (title.isEmpty()) {
                    title = "No Title"
                }

                if (isEdit) {
                    mTaskListListener.onRemoveListItem(taskEntity, Util.getCurrentLocalDateTime())
                }
                mTaskListListener.onAddListItem(startLocalDateTime, endLocalDateTime, title)
            }
            .setNegativeButton("Cancel") { _, _ ->
            }
        Log.d(TAG, "onCreateDialog <end>")
        return builder.create()
    }

    private fun simpleDialog(title: String, message: String, buttonName: String = "close") {
        Log.d(TAG, "simpleDialog <start>")
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonName) { _, _ ->
            }
            .show()
        Log.d(TAG, "simpleDialog <end>")
    }

}