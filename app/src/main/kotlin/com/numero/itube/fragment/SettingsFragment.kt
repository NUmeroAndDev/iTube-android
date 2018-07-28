package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.numero.itube.BuildConfig
import com.numero.itube.R

class SettingsFragment : PreferenceFragmentCompat() {

    private var listener: SettingsFragmentListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SettingsFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val licensesScreen = findPreference("licenses")
        licensesScreen.setOnPreferenceClickListener {
            listener?.showLicenses()
            false
        }
        findPreference("version").summary = BuildConfig.VERSION_NAME
    }

    interface SettingsFragmentListener {
        fun showLicenses()
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}