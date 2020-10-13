package com.example.todoapp.controller

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.models.TaskListPageAdapter
import kotlinx.android.synthetic.main.fragment_viewpager.*
import java.time.LocalDateTime

/**
 * TaskListPagerFragment - Fragment for task list pager
 */
class TaskListPagerFragment: Fragment(), ViewPager.OnPageChangeListener {
    companion object {
        private val TAG: String = Util.getClassName(tag = object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
        private const val START_POSITION: Int = 2
    }

    private lateinit var mAdapter: TaskListPageAdapter
    private lateinit var mListener: ViewPager.OnPageChangeListener
    private lateinit var mDisplayedDate: LocalDateTime
    private var mPosition = -1
    private var mScrollSize = -1
    private var jumpPosition = -1;

    /**
     * newInstance - return to this instance
     * @param array　ArrayList of tasks to display
     * @return This instance
     */
    fun newInstance(date: String): TaskListPagerFragment {
        val args = Bundle()
        val fragment = TaskListPagerFragment()
        args.putString(KEY_ARGS_TASK_DATE, date)
        fragment.arguments = args
        return fragment
    }

    /** @inheritDoc */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListener = this
        val view = inflater.inflate(R.layout.fragment_viewpager, container, false)

        // setting viewPager
        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)

        mAdapter = TaskListPageAdapter(childFragmentManager, 0)

        // get data
        val args = arguments
        if (args != null) {
            val dateString = args.getString(KEY_ARGS_TASK_DATE, Util.toString(Util.getCurrentLocalDateTime(), FORMAT_PATTERN_DATE_ALL))
            val data = Util.toLocalDateTime(dateString, FORMAT_PATTERN_DATE_ALL)
            mAdapter.initializeData(data)
            mDisplayedDate = data
        }

        mScrollSize = mAdapter.count
        viewPager.adapter = mAdapter
        viewPager.setCurrentItem(START_POSITION, false)
        mPosition = START_POSITION

        viewPager.addOnPageChangeListener(mListener)

        return view
    }

    /** @inheritDoc */
    override fun onPageSelected(position: Int) {
        Log.d(TAG, "onPageSelected : position:$position")

        val activity = activity as MainActivity
        if(position - mPosition == 1) {
            mDisplayedDate = mDisplayedDate.plusDays(1)
            Log.d(TAG,"test1:$mDisplayedDate")
            activity.setToolBarText(mDisplayedDate)
        } else if(position - mPosition == -1) {
            mDisplayedDate = mDisplayedDate.minusDays(1)
            Log.d(TAG,"test2:$mDisplayedDate")
            activity.setToolBarText(mDisplayedDate)
        }

        if (mScrollSize == -1) {
            mScrollSize = mAdapter.count
        }
        if (position == 0) {
            jumpPosition = mScrollSize - 2
            mAdapter.rewindData(mDisplayedDate)
        } else if (position == mScrollSize - 1) {
            jumpPosition = 1;
            mAdapter.forwardData(mDisplayedDate)
        }
        mPosition = position
    }

    /** @inheritDoc */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    /** @inheritDoc */
    override fun onPageScrollStateChanged(state: Int) {
        Log.d(TAG, "onPageScrollStateChanged : state:$state")
        if (state == ViewPager.SCROLL_STATE_IDLE && jumpPosition >= 0) {
            view_pager.setCurrentItem(jumpPosition, false);
            jumpPosition = -1;
        }
    }
}