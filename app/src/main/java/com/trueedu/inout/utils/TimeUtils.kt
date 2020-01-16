package com.trueedu.inout.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

fun toDateString(timestamp: Long): String {
    return stringOf(toDate(timestamp))
}

fun toDate(timestamp: Long) : Date {
    return Date(timestamp)
}

@SuppressLint("SimpleDateFormat")
private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

fun stringOf(date: Date): String {
    return DATE_FORMAT.format(date)
}