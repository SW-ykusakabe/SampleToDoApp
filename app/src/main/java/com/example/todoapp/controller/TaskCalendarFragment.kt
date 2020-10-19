package com.example.todoapp.controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.todoapp.models.OnTaskListListener
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.models.CalendarAdapter
import kotlinx.android.synthetic.main.fragment_task_calendar.*
import java.time.LocalDateTime

/**
 * TaskCalendarFragment -  Fragment for task calendar
 */
class TaskCalendarFragment: Fragment(), View.OnClickListener, AdapterView.OnItemClickListener{
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
    }

    private lateinit var mTaskListListener: OnTaskListListener
    private lateinit var mCalendarAdapter: CalendarAdapter

    /**
     * newInstance - return to this instance
     * @param date　String of today date
     * @return This instance
     */
    fun newInstance(date: String): TaskCalendarFragment {
        Log.d(TAG, "newInstance <start>")
        val args = Bundle()
        val fragment = TaskCalendarFragment()
        args.putString(KEY_ARGS_TASK_DATE, date)
        fragment.arguments = args
        Log.d(TAG, "newInstance <end>")
        return fragment
    }

    /** @inheritDoc */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView <start>")
        mTaskListListener = context as OnTaskListListener
        val view = inflater.inflate(R.layout.fragment_task_calendar, container, false)

        val calendarGridView = view.findViewById<GridView>(R.id.calendar_grid_view)
        mCalendarAdapter = CalendarAdapter(view.context)
        calendarGridView.adapter = mCalendarAdapter
        val titleText = view.findViewById<TextView>(R.id.title_text)
        titleText.text = mCalendarAdapter.title

        calendarGridView.onItemClickListener = this
        val lastButton = view.findViewById<Button>(R.id.last_button)
        lastButton.setOnClickListener(this)
        val nextButton = view.findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener(this)

        Log.d(TAG, "onCreateView <end>")
        return view
    }

    /** @inheritDoc */
    override fun onClick(v: View) {
        Log.d(TAG, "onClick <start>")
        when(v.id) {
            R.id.last_button -> {
                Log.d(TAG, "onClick : last_button")
                mCalendarAdapter.lastMonth()
                title_text.text = mCalendarAdapter.title
            }
            R.id.next_button -> {
                Log.d(TAG, "onClick : next_button")
                mCalendarAdapter.nextMonth()
                title_text.text = mCalendarAdapter.title
            }
        }
        Log.d(TAG, "onClick <end>")
    }

    /** @inheritDoc */
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        Log.d(TAG, "onItemClick <start>")
        Log.d(TAG, "onItemClick : position=$position")
        mCalendarAdapter.changeColor(view, position)

        val date = Util.toLocalDateTime(mCalendarAdapter.date(position))
        mTaskListListener.onSelectListToChange(date = date)
        Log.d(TAG, "onItemClick <end>")
    }

    fun listScrolled(date: LocalDateTime) {
        Log.d(TAG, "listScrolled <start>")

        Log.d(TAG, "listScrolled <end>")

    }
}