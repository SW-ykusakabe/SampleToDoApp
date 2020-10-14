package com.example.todoapp.models

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.Util
import com.example.todoapp.entitys.TaskDBEntity

/**
 * AppDatabase - Data access object
 */
@Database(entities = [TaskDBEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private val TAG: String = Util.getClassName(object : Any() {}.javaClass.enclosingClass.name)

        private var appDatabase: AppDatabase? = null
        private val lock = Any()

        /**
         * newInstance - return to this instance
         * @param context context
         * @return This instance
         */
        fun newInstance(context: Context): AppDatabase {
            Log.d(TAG, "newInstance <start>")
            synchronized(lock) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "Tasks.db")
                        .allowMainThreadQueries()
                        .build()
                }
                Log.d(TAG, "newInstance <end>")
                return appDatabase!!
            }
        }
    }
}