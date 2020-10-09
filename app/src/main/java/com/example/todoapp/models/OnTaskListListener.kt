package com.example.todoapp.models

import com.example.todoapp.entitys.TaskEntity
import java.time.LocalDateTime

interface OnTaskListListener {

    /**
     * getTodayList - Get task list today
     * @param date date
     * @return today task list
     */
    fun getTodayList(date: LocalDateTime): ArrayList<TaskEntity>

    /**
     * onCreateListItem - Create a task
     * @param startDate start time
     * @param endDate end time
     * @param title title
     */
    fun onCreateListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String)

    /**
     * onRemoveListItem - Remove the task
     * @param position Location of list to remove
     * @return list after remove
     */
    fun onRemoveListItem(position: Int): ArrayList<TaskEntity>

    /**
     * onEditListItem - Edit a task
     * @param position Location of list to edit
     */
    fun onEditListItem(position: Int)

    /**
     * onChangeListItem - Change the task to be displayed
     * @param year Year to change
     * @param month　Month to change
     * @param dayOfWeek DayOfWeek to change
     */
    fun onChangeListItem(year: Int, month: Int, dayOfWeek: Int)
}