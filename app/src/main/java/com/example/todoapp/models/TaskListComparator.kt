package com.example.todoapp.models

import com.example.todoapp.entitys.TaskEntity

class TaskListComparator: Comparator<TaskEntity> {
    override fun compare(sortDataFirst: TaskEntity, sortDataSecond: TaskEntity): Int {
        val first = sortDataFirst.startTime
        val second = sortDataSecond.startTime

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