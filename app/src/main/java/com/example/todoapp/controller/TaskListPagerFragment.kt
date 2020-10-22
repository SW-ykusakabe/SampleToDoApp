package com.example.todoapp.controller

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.models.TaskListPageAdapter
import kotlinx.android.synthetic.main.fragment_pager.*
import java.time.LocalDateTime

/**
 * TaskListPagerFragment - Fragment for task list pager
 */
class TaskListPagerFragment: Fragment(), ViewPager.OnPageChangeListener {

    companion object {
        private val TAG: String = Util.getClassName(tag = object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
        private const val START_POSITION: Int = 1
    }

    private lateinit var mAdapter: TaskListPageAdapter
    private lateinit var mListener: ViewPager.OnPageChangeListener
    private lateinit var mDisplayedDate: LocalDateTime
    private var mPosition = -1
    private var mScrollSize = -1
    private var jumpPosition = -1

    /**
     * newInstance - return to this instance
     * @param date　String of today date
     * @return This instance
     */
    fun newInstance(date: String): TaskListPagerFragment {
        Log.d(TAG, "newInstance <start>")
        val args = Bundle()
        val fragment = TaskListPagerFragment()
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
        mListener = this
        val view = inflater.inflate(R.layout.fragment_pager, container, false)

        // setting viewPager
        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)

        mAdapter = TaskListPageAdapter(childFragmentManager, 0)

        // get data
        val args = arguments
        if (args != null) {
            val dateString = args.getString(KEY_ARGS_TASK_DATE, Util.getCurrentTimeOfString(FORMAT_PATTERN_DATE_ALL))
            val date = Util.toLocalDateTime(dateString, FORMAT_PATTERN_DATE_ALL)
            mAdapter.initializeData(date)
            mDisplayedDate = date
            val activity = activity as MainActivity
            activity.setToolBarText(mDisplayedDate)

        }

        mScrollSize = mAdapter.count
        viewPager.adapter = mAdapter
        viewPager.setCurrentItem(START_POSITION, false)
        mPosition = START_POSITION

        viewPager.addOnPageChangeListener(mListener)

        Log.d(TAG, "onCreateView <end>")
        return view
    }

    /** @inheritDoc */
    override fun onPageSelected(position: Int) {
        Log.d(TAG, "onPageSelected <start>")
        Log.d(TAG, "onPageSelected : position:$position")

        // display date
        val activity = activity as MainActivity
        val count = position - mPosition
        if(count == 1) {
            mDisplayedDate = mDisplayedDate.plusDays(1)
        } else if(count == -1) {
            mDisplayedDate = mDisplayedDate.minusDays(1)
        }
        activity.setToolBarText(mDisplayedDate)
        activity.sendDayChanged(count)
        mPosition = position

        // scroll jump
        if (mScrollSize == -1) {
            mScrollSize = mAdapter.count
        }
        if (position == 0) {
            jumpPosition = mScrollSize - 2
            mAdapter.rewindData(mDisplayedDate)
        } else if (position == mScrollSize - 1) {
            jumpPosition = 1
            mAdapter.forwardData(mDisplayedDate)
        }
        Log.d(TAG, "onPageSelected <end>")
    }

    /** @inheritDoc */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    /** @inheritDoc */
    override fun onPageScrollStateChanged(state: Int) {
        Log.d(TAG, "onPageScrollStateChanged <start>")
        Log.d(TAG, "onPageScrollStateChanged : state:$state")
        if (state == ViewPager.SCROLL_STATE_IDLE && jumpPosition >= 0) {
            view_pager.setCurrentItem(jumpPosition, false)
            jumpPosition = -1
        }
        Log.d(TAG, "onPageScrollStateChanged <end>")
    }

    /**
     * pagerReload - Pager view reload
     */
    fun pagerReload() {
        Log.d(TAG, "pagerReload <start>")
        mAdapter.notifyDataSetChanged()
        Log.d(TAG, "pagerReload <end>")
    }

    /**
     * pagerReload - Pager view reload
     * @param date
     */
    fun pagerReload(date: LocalDateTime) {
        Log.d(TAG, "pagerReload <start>")
        mAdapter.initializeData(date)
        mDisplayedDate = date
        view_pager.setCurrentItem(START_POSITION, false)
        mAdapter.notifyDataSetChanged()
        Log.d(TAG, "pagerReload <end>")
    }
}