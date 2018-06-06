package com.numero.itube.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.numero.itube.R

class PlayerSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.player_settings, rootKey)
    }
}