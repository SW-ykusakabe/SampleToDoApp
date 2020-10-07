package com.example.todoapp.models

import androidx.room.*
import com.example.todoapp.entitys.TaskDBEntity

@Dao
interface TaskDao {
    @Insert
    fun insert(task: TaskDBEntity)

    @Update
    fun update(task: TaskDBEntity)

    @Delete
    fun delete(task: TaskDBEntity)

    @Query("delete from tasks where id = :id")
    fun delete(id: Int)

    @Query("delete from tasks")
    fun deleteAll()

    @Query("select * from tasks")
    fun getAll(): List<TaskDBEntity>

    @Query("select * from tasks where start_time = :startTime and end_time = :endTime and title = :title")
    fun getTasksAll(startTime: String, endTime: String, title: String): List<TaskDBEntity>

    @Query("select * from tasks where id = :id")
    fun getTasks(id: Int): TaskDBEntity
}