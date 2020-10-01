package com.example.todoapp

import android.util.Log

class DLog constructor(TAG: String, method: String, message: String) {
    init {
        Log.d("<<ToDoApp>>$TAG", "$method: $message")
    }
}