package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.numero.itube.BuildConfig
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.repository.ConfigRepository
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    private var listener: SettingsFragmentListener? = null
    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        component?.inject(this)
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
        findPreference("useDarkTheme").setOnPreferenceChangeListener { preference, newValue ->
            if (newValue is Boolean) {
                configRepository.isUseDarkTheme = newValue
                listener?.onChangedTheme()
            }
            true
        }
    }

    interface SettingsFragmentListener {
        fun showLicenses()

        fun onChangedTheme()
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}