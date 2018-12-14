package com.numero.itube.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.remove

class PlayerSettingsBottomSheetFragment : BottomSheetDialogFragment() {

    override fun setupDialog(dialog: Dialog?, style: Int) {
        val view = View.inflate(context, R.layout.fragment_player_settings, null)
        dialog?.setContentView(view)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        val fragment = findFragment(R.id.playerSettingsFragment)
        if (fragment != null) {
            remove(fragment)
        }
    }

    fun show(manager: FragmentManager?) {
        show(manager, TAG)
    }

    companion object {
        private const val TAG: String = "PlayerSettingsBottomSheetFragment"

        fun newInstance(): PlayerSettingsBottomSheetFragment = PlayerSettingsBottomSheetFragment()
    }
}