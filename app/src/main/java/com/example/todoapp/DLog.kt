package com.example.todoapp

import android.util.Log

/**
 * DLog
 * @param TAG call class
 * @param method call method
 * @param message message
 */
class DLog constructor(TAG: String, method: String, message: String) {
    init {
        Log.d("<<ToDoApp>>$TAG", "$method: $message")
    }
}