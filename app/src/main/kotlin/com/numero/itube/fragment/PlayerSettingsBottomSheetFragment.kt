package com.numero.itube.fragment

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.View
import com.numero.itube.R
import com.numero.itube.repository.ConfigRepository
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_player_settings.view.*

import javax.inject.Inject

class PlayerSettingsBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AndroidSupportInjection.inject(this)
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        val view = View.inflate(context, R.layout.fragment_player_settings, null).apply {
            loopItemView.apply {
                isChecked = configRepository.isLoop
                setOnCheckedChangeListener {
                    configRepository.isLoop = it
                }
            }
        }
        dialog?.setContentView(view)
    }

    companion object {
        const val TAG: String = "PlayerSettingsBottomSheetFragment"

        fun newInstance(): PlayerSettingsBottomSheetFragment = PlayerSettingsBottomSheetFragment()
    }
}