package com.example.todoapp.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.TaskDao
import com.example.todoapp.entitys.Tasks

@Database(entities = [Tasks::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {

        private var appDatabase: AppDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "Tasks.db")
                        .allowMainThreadQueries()
                        .build()
                }
                return appDatabase!!
            }
        }
    }
}