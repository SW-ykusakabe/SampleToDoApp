package com.example.todoapp.entitys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TaskDBEntity - Data class for server
 */
@Entity(tableName = "tasks")
data class TaskDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "start_time") val startTime: String,
    @ColumnInfo(name = "end_time") val endTime: String,
    @ColumnInfo(name = "title") val title: String
)