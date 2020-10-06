package com.example.todoapp

import androidx.room.*
import com.example.todoapp.entitys.Tasks

@Dao
interface TaskDao {
    @Insert
    fun insert(task : Tasks)

    @Update
    fun update(task : Tasks)

    @Delete
    fun delete(task : Tasks)

    @Query("delete from tasks")
    fun deleteAll()

    @Query("select * from tasks")
    fun getAll(): List<Tasks>

    @Query("select * from tasks where id = :id")
    fun getTasks(id: Int): Tasks
}