package com.example.todoapp

import com.example.todoapp.entitys.TaskEntity
import java.time.LocalDateTime

interface OnTaskListListener {
    fun onCreateListItem(startDate: LocalDateTime, endDate: LocalDateTime, title: String)
    fun onRemoveListItem(position: Int): ArrayList<TaskEntity>
    fun onEditListItem(position: Int)
    fun onChangeListItem(year: Int, month: Int, dayOfWeek: Int)
}