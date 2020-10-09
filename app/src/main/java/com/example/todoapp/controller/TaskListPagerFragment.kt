package com.example.todoapp.controller

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.todoapp.R
import com.example.todoapp.Util
import com.example.todoapp.entitys.TaskEntity
import com.example.todoapp.models.TaskListPageAdapter
import kotlinx.android.synthetic.main.fragment_viewpager.*

/**
 * TaskListPagerFragment - Fragment for task list pager
 */
class TaskListPagerFragment: Fragment(), ViewPager.OnPageChangeListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
        private const val FORMAT_PATTERN_DATE_ALL: String = "yyyy/MM/dd(e)-HH:mm"

        private const val KEY_ARGS_TASK_DATE: String = "ARGS_TASK_DATE"
    }

    private lateinit var mAdapter: TaskListPageAdapter
    private lateinit var mListener: ViewPager.OnPageChangeListener
    private var mScrollSize = -1
    private var jumpPosition = -1;

    /**
     * newInstance - return to this instance
     * @param array　ArrayList of tasks to display
     * @return This instance
     */
    fun newInstance(date: String, array: ArrayList<TaskEntity>): TaskListPagerFragment {
        val args = Bundle()
        val fragment = TaskListPagerFragment()
        args.putString(KEY_ARGS_TASK_DATE, date)
        fragment.arguments = args
        return fragment
    }

    /** @inheritDoc */
    override fun onPageSelected(position: Int) {
        Log.d(TAG, "onPageSelected : position:$position")
        if (mScrollSize == -1) {
            mScrollSize = mAdapter.count
        }
        if (position == 0) {
            //prepare to jump to the last page
            jumpPosition = mScrollSize - 2

            mAdapter.rewindData()
        } else if (position == mScrollSize - 1) {
            //prepare to jump to the first page
            jumpPosition = 1;

            mAdapter.forwardData()
        }
    }

    /** @inheritDoc */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    /** @inheritDoc */
    override fun onPageScrollStateChanged(state: Int) {
        Log.d(TAG, "onPageScrollStateChanged : state:$state")
        //Let's wait for the animation to be completed then do the jump (if we do this in
        //onPageSelected(int position) scroll animation will be canceled).
        if (state == ViewPager.SCROLL_STATE_IDLE && jumpPosition >= 0) {
            //Jump without animation so the user is not aware what happened.
            view_pager.setCurrentItem(jumpPosition, false);
            //Reset jump position.
            jumpPosition = -1;
        }
    }

    /** @inheritDoc */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListener = this
        val view = inflater.inflate(R.layout.fragment_viewpager, container, false)

        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
        viewPager.addOnPageChangeListener(mListener)

        mAdapter = TaskListPageAdapter(childFragmentManager, 0)
        val args = arguments
        var dateString = ""
        if (args != null) {
            dateString = args.getString(KEY_ARGS_TASK_DATE, Util.toString(Util.getCurrentLocalDateTime(), FORMAT_PATTERN_DATE_ALL))
            mAdapter.initializeData(dateString)
        }
        mScrollSize = mAdapter.count
        viewPager.adapter = mAdapter
        viewPager.setCurrentItem(1, false)

        return view
    }
}