package com.example.todoapp.controller

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.todoapp.models.OnTaskListListener
import com.example.todoapp.R
import com.example.todoapp.Util

/**
 * SettingsFragment - Fragment for setting
 */
class SettingsFragment: Fragment(), View.OnClickListener {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }
    private lateinit var mTaskListListener: OnTaskListListener

    /**
     * newInstance - return to this instance
     * @return This instance
     */
    fun newInstance(): SettingsFragment {
        val args = Bundle()
        val fragment = SettingsFragment()
        args.putString("ARGS_KEY", "")
        fragment.arguments = args
        return fragment
    }

    /** @inheritDoc */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mTaskListListener = context as OnTaskListListener
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
//        val args = arguments
//        if (args != null) {
//            val argsStr = args.getString("ARGS_KEY")
//        }

        // setting view
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener(this)

        return view
    }

    /** @inheritDoc */
    override fun onClick(v: View) {
        when(v.id) {
            R.id.button -> {
                Log.d(TAG, "onClick : button")
                AlertDialog.Builder(activity)
                    .setTitle("Delete all list")
                    .setMessage("Do you really want to delete task list?")
                    .setPositiveButton("OK") { dialog, which ->
                        mTaskListListener.onRemoveListItem(-1)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }
}