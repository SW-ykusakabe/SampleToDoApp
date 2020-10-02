package com.example.todoapp

import TaskEntity
import java.time.LocalDateTime

interface OnTaskListListener {
    fun getListItem(): ArrayList<TaskEntity>
    fun onCreateListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String)
    fun onRemoveListItem(position: Int): ArrayList<TaskEntity>
    fun onEditListItem(position: Int): ArrayList<TaskEntity>
    fun onChangeListItem(year: Int, month: Int, dayOfWeek: Int)
}