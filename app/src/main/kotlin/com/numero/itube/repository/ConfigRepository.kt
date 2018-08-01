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

    override val isUseDarkTheme: Boolean
        get() = preferences.getBoolean(KEY_IS_LOOP, false)

    override val theme: Int
        get() = if (isUseDarkTheme) {
            R.style.AppTheme_Dark
        } else {
            R.style.AppTheme_Light
        }

    override val appBarTheme: Int
        get() = if (isUseDarkTheme) {
            R.style.AppTheme_AppBarOverlay
        } else {
            R.style.AppTheme_AppBarOverlay
        }

    override val toolbarTheme: Int
        get() = if (isUseDarkTheme) {
            R.style.AppTheme_PopupOverlay
        } else {
            R.style.AppTheme_PopupOverlay
        }

    companion object {
        private const val KEY_IS_LOOP = "key_is_play_loop"

        private const val KEY_USE_DARK_THEME = "key_use_dark_theme"
    }
}