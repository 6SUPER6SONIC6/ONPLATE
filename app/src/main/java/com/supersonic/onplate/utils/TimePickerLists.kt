package com.supersonic.onplate.utils


object TimePickerLists {
    fun getTimePickerListHours(): List<Int> {
        return (0..24).toList()
    }

    fun getTimePickerListMinutes(): List<Int> {
        return (0..59).toList()
    }
}