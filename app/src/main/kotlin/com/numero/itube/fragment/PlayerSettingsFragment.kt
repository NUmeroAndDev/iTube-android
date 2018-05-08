package com.numero.itube.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.numero.itube.R
import com.numero.itube.repository.ConfigRepository
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_player_settings.*
import javax.inject.Inject

class PlayerSettingsFragment : Fragment() {

    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loopItemView.apply {
            isChecked = configRepository.isLoop
            setOnCheckedChangeListener {
                configRepository.isLoop = it
            }
        }
    }

    companion object {
        fun newInstance(): PlayerSettingsFragment = PlayerSettingsFragment()
    }
}