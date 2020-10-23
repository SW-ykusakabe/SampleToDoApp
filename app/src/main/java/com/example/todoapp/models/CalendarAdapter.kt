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
import java.time.LocalDateTime
import java.util.*


class CalendarAdapter(private val mContext: Context, private val localDateTime: LocalDateTime) : BaseAdapter() {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }
    private var mDateArray: ArrayList<Date> = ArrayList<Date>()
    private val mDateManager: DateManager
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private lateinit var mPreviouslySelectedView: View
    private var mPreviouslySelectedPosition: Int = 0

    init {
        Log.d(TAG, "init <start>")
        mDateManager = DateManager(localDateTime)
        mDateArray.addAll(mDateManager.days)
        Log.d(TAG, "init <end>")
    }

    private class ViewHolder {
        var dateText: TextView? = null
    }

    /** @inheritDoc */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        Log.d(TAG, "getView <start>")
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_calendar_cell, parent, false)
            holder = ViewHolder()
            holder.dateText = view!!.findViewById(R.id.date_text)
            view.tag = holder

            // set date text
            val dateFormat = SimpleDateFormat("d", Locale.JAPAN)
            holder.dateText?.text = dateFormat.format(mDateArray[position])

            // set background color
            var colorAlpha = 255
            when {
                mDateManager.isCurrentDay(mDateArray[position]) -> {
                    view.setBackgroundColor(Color.argb(255, 255, 240, 170))
                    mPreviouslySelectedView = view
                    mPreviouslySelectedPosition = position
                }
                mDateManager.isCurrentMonth(mDateArray[position]) -> {
                    view.setBackgroundColor(Color.WHITE)
                }
                else -> {
                    view.setBackgroundColor(Color.LTGRAY)
                    colorAlpha = 80
                }
            }

            // set text color
            val colorId: Int = when (mDateManager.getDayOfWeek(mDateArray[position])) {
                1 -> Color.argb(colorAlpha, 255, 0, 0)
                7 -> Color.argb(colorAlpha, 0, 0, 255)
                else -> Color.argb(colorAlpha, 0, 0, 0)
            }
            holder.dateText?.setTextColor(colorId)
        } else {
            holder = view.tag as ViewHolder
        }
        // set display size
        val dp = mContext.resources.displayMetrics.density
        val params = AbsListView.LayoutParams(parent.width / 7 - dp.toInt(),
            (parent.height - dp.toInt() * 6) / 6 + 1)
        view.layoutParams = params

        Log.d(TAG, "getView <end>")
        return view
    }

    /** @inheritDoc */
    override fun getCount(): Int {
        Log.d(TAG, "getCount <start>")
        Log.d(TAG, "getCount <end>")
        return mDateArray.size
    }

    /** @inheritDoc */
    override fun getItem(position: Int): Any? {
        Log.d(TAG, "getItem <start>")
        Log.d(TAG, "getItem <end>")
        return null
    }

    /** @inheritDoc */
    override fun getItemId(position: Int): Long {
        Log.d(TAG, "getItemId <start>")
        Log.d(TAG, "getItemId <end>")
        return 0
    }

//    fun nextMonth() {
//        Log.d(TAG, "nextMonth <start>")
//        mDateManager.nextMonth()
//        mDateArray = mDateManager.days
//        notifyDataSetChanged()
//        Log.d(TAG, "nextMonth <end>")
//    }
//
//    fun lastMonth() {
//        Log.d(TAG, "lastMonth <start>")
//        mDateManager.lastMonth()
//        mDateArray = mDateManager.days
//        notifyDataSetChanged()
//        Log.d(TAG, "lastMonth <end>")
//    }

    /**
     * date
     * @param position
     * @return Date
     */
    fun getSelectDate(position: Int): Date {
        return mDateArray[position]
    }

    /**
     * changeColor
     * @param view
     * @param position
     */
    fun changeCellColor(view: View, position: Int) {
        Log.d(TAG, "changeColor <start>")
        // TODO :
        if (mDateManager.isCurrentMonth(mDateArray[mPreviouslySelectedPosition])) {
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
        mPreviouslySelectedPosition = position
        Log.d(TAG, "changeColor <end>")
    }

    fun getSelectPosition(): Int {
        return mPreviouslySelectedPosition
    }

    fun forwardDate(view: View, count: Int): Int{
        Log.d(TAG, "forwardDate <start>")
        // TODO :
        if (mDateManager.isCurrentMonth(mDateArray[mPreviouslySelectedPosition])) {
            mPreviouslySelectedView.setBackgroundColor(Color.WHITE)
        } else {
            mPreviouslySelectedView.setBackgroundColor(Color.LTGRAY)
        }

        mPreviouslySelectedPosition +=count
        if (mDateManager.isCurrentMonth(mDateArray[mPreviouslySelectedPosition])) {
            view.setBackgroundColor(Color.argb(255, 255, 240, 170))
        } else {
            view.setBackgroundColor(Color.argb(100, 255, 240, 170))
        }
        mPreviouslySelectedView = view
        Log.d(TAG, "forwardDate <end>")
        return mPreviouslySelectedPosition
    }

    fun rewindDate(view: View, count: Int): Int {
        Log.d(TAG, "rewindDate <start>")
        // TODO :
        if (mDateManager.isCurrentMonth(mDateArray[mPreviouslySelectedPosition])) {
            mPreviouslySelectedView.setBackgroundColor(Color.WHITE)
        } else {
            mPreviouslySelectedView.setBackgroundColor(Color.LTGRAY)
        }

        mPreviouslySelectedPosition +=count
        if (mDateManager.isCurrentMonth(mDateArray[mPreviouslySelectedPosition])) {
            view.setBackgroundColor(Color.argb(255, 255, 240, 170))
        } else {
            view.setBackgroundColor(Color.argb(100, 255, 240, 170))
        }
        mPreviouslySelectedView = view
        Log.d(TAG, "rewindDate <end>")
        return mPreviouslySelectedPosition
    }
}