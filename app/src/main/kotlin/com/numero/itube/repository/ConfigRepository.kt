package com.numero.itube.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ConfigRepository(context: Context) : IConfigRepository {

    private val preferences: SharedPreferences = context.getSharedPreferences(CONFIG_PREFERENCE, Context.MODE_PRIVATE)

    override var isLoop: Boolean
        get() = preferences.getBoolean(KEY_IS_LOOP, false)
        set(value) {
            preferences.edit {
                putBoolean(KEY_IS_LOOP, value)
            }
        }

    companion object {
        private const val CONFIG_PREFERENCE = "CONFIG_PREFERENCE"

        private const val KEY_IS_LOOP = "KEY_IS_LOOP"
    }
}