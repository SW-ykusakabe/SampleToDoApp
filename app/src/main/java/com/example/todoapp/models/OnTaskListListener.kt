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
    fun onAddListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String)

    /**
     * onRemoveListItem - Remove the task
     * @param taskEntity TaskEntity
     * @param date
     * @return list after remove
     */
    fun onRemoveListItem(taskEntity: TaskEntity?, date: LocalDateTime)

    /**
     * onEditListItem - Edit a task
     * @param taskEntity TaskEntity
     */
    fun onEditListItem(taskEntity: TaskEntity)

    /**
     * onChangeListItem - Change display list by selection
     * @param date date to change
     */
    fun onSelectDateToChange(date: LocalDateTime)

    /**
     * onChangeListItem - Change the display list by scrolling
     * @param date date to change
     */
    fun onScrollListToChange(date: LocalDateTime)
}