package com.numero.itube.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.numero.itube.R

class ConfigRepository(context: Context) : IConfigRepository {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override val isLoop: Boolean
        get() = preferences.getBoolean(KEY_IS_LOOP, false)

    override val apiKey: String = context.getString(R.string.api_key)

    override val isUseDarkTheme: Boolean
        get() = preferences.getBoolean(KEY_USE_DARK_THEME, false)

    override val theme: Int
        get() = if (isUseDarkTheme) {
            R.style.DarkTheme_DarkWindow
        } else {
            R.style.LightTheme_LightWindow
        }

    companion object {
        private const val KEY_IS_LOOP = "key_is_play_loop"

        private const val KEY_USE_DARK_THEME = "key_is_use_dark_theme"
    }
}