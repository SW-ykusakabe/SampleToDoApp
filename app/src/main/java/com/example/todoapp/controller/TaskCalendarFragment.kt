package com.example.todoapp.controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todoapp.R
import com.example.todoapp.Util

/**
 * TaskCalendarFragment -  Fragment for task calendar
 */
class TaskCalendarFragment: Fragment(), View.OnClickListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
    }

    private lateinit var mFragment: TaskCalendarPagerFragment
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
        val view = inflater.inflate(R.layout.fragment_task_calendar, container, false)

        val args = arguments
        val dateString = args?.getString(KEY_ARGS_TASK_DATE, Util.getCurrentTimeOfString(FORMAT_PATTERN_DATE_ALL))
        val data = Util.toLocalDateTime(dateString!!, FORMAT_PATTERN_DATE_ALL)

        val titleText = view.findViewById<TextView>(R.id.title_text)
        titleText.text = Util.toString(data, "yyyy/MM")

        val lastButton = view.findViewById<Button>(R.id.last_button)
        val nextButton = view.findViewById<Button>(R.id.next_button)

        lastButton.setOnClickListener(this)
        nextButton.setOnClickListener(this)

        mFragment = TaskCalendarPagerFragment().newInstance(dateString)
        replaceFragment(mFragment)

        Log.d(TAG, "onCreateView <end>")
        return view
    }

    /** @inheritDoc */
    override fun onClick(v: View) {
        Log.d(TAG, "onClick <start>")
        when(v.id) {
            R.id.last_button -> {
                Log.d(TAG, "onClick : last_button")
                mFragment.lastMonth()
            }
            R.id.next_button -> {
                Log.d(TAG, "onClick : next_button")
                mFragment.nextMonth()
            }
        }
        Log.d(TAG, "onClick <end>")
    }

    fun dayChange(count: Int) {
        mFragment.changeScrollDate(count)
    }

    /**
     * replaceFragment - Replace the inside of the container with an argument fragment
     * @param fragment Fragment to display
     */
    private fun replaceFragment(fragment: Fragment) {
        Log.d(TAG, "replaceFragment <start>")
        val fragmentManager: FragmentManager = childFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.day_container, fragment)
        fragmentTransaction.commit()
        Log.d(TAG, "replaceFragment <end>")
    }
}