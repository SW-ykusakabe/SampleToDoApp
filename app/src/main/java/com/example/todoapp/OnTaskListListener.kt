package com.example.todoapp

import TaskEntity

interface OnTaskListListener {
    fun getListItem(): ArrayList<TaskEntity>
    fun onCreateListItem(startDate: String, endDate: String, title: String)
    fun onRemoveListItem(position: Int): ArrayList<TaskEntity>
    fun onEditListItem(position: Int): ArrayList<TaskEntity>
}