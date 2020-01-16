package com.trueedu.inout.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object Prefs {
    private lateinit var prefs: SharedPreferences
    fun init(applicationContext: Context) {
        prefs = applicationContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }

    private val PREFS_NAME = Prefs::class.java.simpleName

}

enum class PrefsKey(val value: String) {
    FIRST_LAUNCH("FIRST_LAUNCH"),
}