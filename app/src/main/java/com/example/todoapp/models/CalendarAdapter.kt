package com.example.todoapp.models

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.entitys.DateManager
import java.text.SimpleDateFormat
import java.util.*


class CalendarAdapter(private val mContext: Context) : BaseAdapter() {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }
    private var mDateArray: List<Date> = ArrayList<Date>()
    private val mDateManager: DateManager = DateManager()
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private lateinit var mPreviouslySelectedView: View
    private lateinit var mPreviouslySelectedDate: Date
    val title: String
        get() {
            val format = SimpleDateFormat("yyyy.MM", Locale.JAPAN)
            return format.format(mDateManager.mCalendar.time)
        }

    init {
        Log.d(TAG, "init <start>")
        mDateArray = mDateManager.days
        Log.d(TAG, "init <end>")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        Log.d(TAG, "getView <start>")
        var view = convertView
        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.item_calendar_cell, parent, false)
        }

        // set display size
        val dp = mContext.resources.displayMetrics.density
        val params = AbsListView.LayoutParams(parent.width / 7 - dp.toInt(),
//            100)
            (parent.height - dp.toInt() * mDateManager.weeks) / mDateManager.weeks + 1)
        Log.d(TAG, "test: density=${dp}, parent=${parent.height}, weeks=${mDateManager.weeks}")
        Log.d(TAG, "test: height=${(parent.height - dp.toInt() * mDateManager.weeks) / mDateManager.weeks}")
        view?.layoutParams = params

        // set date text
        val textView = view?.findViewById<TextView>(R.id.date_text)
        val dateFormat = SimpleDateFormat("d", Locale.JAPAN)
        textView?.text = dateFormat.format(mDateArray[position])

        // set background color
        when {
            mDateManager.isCurrentDay(mDateArray[position]) -> {
                view?.setBackgroundColor(Color.argb(255 ,255, 240, 170))
                mPreviouslySelectedView = view!!
                mPreviouslySelectedDate = mDateArray[position]
            }
            mDateManager.isCurrentMonth(mDateArray[position]) -> {
                view?.setBackgroundColor(Color.WHITE)
            }
            else -> {
                view?.setBackgroundColor(Color.LTGRAY)
            }
        }

        // set text color
        val colorId: Int = when (mDateManager.getDayOfWeek(mDateArray[position])) {
            1 -> Color.RED
            7 -> Color.BLUE
            else -> Color.BLACK
        }
        textView?.setTextColor(colorId)
        Log.d(TAG, "getView <end>")
        return view
    }

    override fun getCount(): Int {
        Log.d(TAG, "getCount <start>")
        Log.d(TAG, "getCount <end>")
        return mDateArray.size
    }

    override fun getItem(position: Int): Any? {
        Log.d(TAG, "getItem <start>")
        Log.d(TAG, "getItem <end>")
        return null
    }

    override fun getItemId(position: Int): Long {
        Log.d(TAG, "getItemId <start>")
        Log.d(TAG, "getItemId <end>")
        return 0
    }

    fun nextMonth() {
        Log.d(TAG, "nextMonth <start>")
        mDateManager.nextMonth()
        mDateArray = mDateManager.days
        notifyDataSetChanged()
        Log.d(TAG, "nextMonth <end>")
    }

    fun lastMonth() {
        Log.d(TAG, "lastMonth <start>")
        mDateManager.lastMonth()
        mDateArray = mDateManager.days
        notifyDataSetChanged()
        Log.d(TAG, "lastMonth <end>")
    }

    fun date(position: Int): Date {
        return mDateArray[position]
    }

    fun changeColor(view: View, position: Int) {
        Log.d(TAG, "changeColor <start>")
        if (mDateManager.isCurrentMonth(mPreviouslySelectedDate)) {
            mPreviouslySelectedView.setBackgroundColor(Color.WHITE)
        } else {
            mPreviouslySelectedView.setBackgroundColor(Color.LTGRAY)
        }
        if (mDateManager.isCurrentMonth(mDateArray[position])) {
            view.setBackgroundColor(Color.argb(255, 255, 240, 170))
        } else {
            view.setBackgroundColor(Color.argb(100, 255, 240, 170))
        }
        mPreviouslySelectedView = view
        mPreviouslySelectedDate = mDateArray[position]
        Log.d(TAG, "changeColor <end>")
    }
}