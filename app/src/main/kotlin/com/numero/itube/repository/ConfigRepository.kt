package com.numero.itube.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.numero.itube.R

class ConfigRepository(context: Context) : IConfigRepository {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override val isLoop: Boolean
        get() = preferences.getBoolean(KEY_IS_LOOP, false)

    override val apiKey: String = context.getString(R.string.api_key)

    companion object {
        private const val KEY_IS_LOOP = "key_is_play_loop"
    }
}