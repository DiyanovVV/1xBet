package com.bookmaker.onexbetapp.common.systemUtils

import android.content.Context
import androidx.core.content.edit

class SharedPreferences(context: Context) {
    companion object{
        private const val PREFS = "SHARED_PREFS"
        private const val AGE = "AGE"
        private const val RULES = "RULES"
        private const val LINK = "LINK"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    var ageAskPassed: Boolean
        get() = sharedPreferences.getBoolean(AGE, false)
        set(value) {sharedPreferences.edit { putBoolean(AGE, value) }}

    var rulesConfirm: Boolean
        get() = sharedPreferences.getBoolean(RULES, false)
        set(value) {sharedPreferences.edit { putBoolean(RULES, value) }}

    var link: String
        get() = sharedPreferences.getString(LINK, "")?: ""
        set(value) {sharedPreferences.edit { putString(LINK, value) }}
}