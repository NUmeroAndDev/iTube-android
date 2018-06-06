package com.numero.itube.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class ConfigRepository(context: Context) : IConfigRepository {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override val isLoop: Boolean
        get() = preferences.getBoolean(KEY_IS_LOOP, false)

    companion object {
        private const val KEY_IS_LOOP = "key_is_play_loop"
    }
}