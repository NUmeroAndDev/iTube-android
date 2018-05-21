package com.numero.itube.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.repository.ConfigRepository
import kotlinx.android.synthetic.main.fragment_player_settings.view.*
import javax.inject.Inject

class PlayerSettingsBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
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