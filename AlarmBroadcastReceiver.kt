package com.example.todoapp.models

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // toast で受け取りを確認
        Toast.makeText(context, "Received", Toast.LENGTH_LONG).show()
    }
}