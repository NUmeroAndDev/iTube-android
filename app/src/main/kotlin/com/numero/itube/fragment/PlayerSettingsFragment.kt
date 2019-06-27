package com.numero.itube.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.numero.itube.R
import com.numero.itube.extension.tintIcon

class PlayerSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.player_settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findPreference<SwitchPreference>("key_is_play_loop")?.tintIcon(R.attr.colorOnBackground)
    }
}