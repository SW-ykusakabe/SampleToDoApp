package com.example.todoapp.models

import android.util.Log
import com.example.todoapp.Util
import com.example.todoapp.entitys.TaskEntity

class TaskListComparator: Comparator<TaskEntity> {
    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)
    }

    override fun compare(sortDataFirst: TaskEntity, sortDataSecond: TaskEntity): Int {
        Log.d(TAG, "compare <start>")
        val first = sortDataFirst.startTime
        val second = sortDataSecond.startTime

        Log.d(TAG, "compare <end>")
        return when {
            first.isAfter(second) -> {
                1
            }
            first.isEqual(second) -> {
                0
            }
            else -> {
                -1
            }
        }
    }
}