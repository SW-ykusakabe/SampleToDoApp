package com.example.todoapp.models

interface OnSelectDateListener {

    /**
     * selectDate
     * @param count
     */
    fun changeDate(count: Int)

    /**
     * selectDate
     * @param count
     */
    fun changeMonth(count: Int)
}