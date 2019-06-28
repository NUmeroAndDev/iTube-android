package com.numero.itube.ui.playlist

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.numero.itube.R
import kotlinx.android.synthetic.main.fragment_input_playlist_title.view.*

class InputPlaylistTitleDialogFragment : DialogFragment() {

    private lateinit var inputPlaylistTextInputLayout: TextInputLayout
    private lateinit var playlistTitleEditText: TextInputEditText

    private var callback: InputPlaylistTitleCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InputPlaylistTitleCallback) {
            callback = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_input_playlist_title, null)
        initViews(view)

        return MaterialAlertDialogBuilder(requireActivity())
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .setView(view)
                .show()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? AlertDialog ?: return
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val titleText = playlistTitleEditText.text.toString()
            if (titleText.isEmpty()) {
                inputPlaylistTextInputLayout.error = "入力してください"
                return@setOnClickListener
            }
            callback?.createPlaylist(titleText)
            dismiss()
        }
    }

    private fun initViews(view: View) {
        inputPlaylistTextInputLayout = view.inputPlaylistTextInputLayout
        playlistTitleEditText = view.inputPlaylistTitleEditTextView
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(TAG) != null) {
            return
        }
        show(manager, TAG)
    }

    interface InputPlaylistTitleCallback {
        fun createPlaylist(title: String)
    }

    companion object {

        private const val TAG = "InputPlaylistTitleDialogFragment"

        fun newInstance(): InputPlaylistTitleDialogFragment = InputPlaylistTitleDialogFragment()
    }
}