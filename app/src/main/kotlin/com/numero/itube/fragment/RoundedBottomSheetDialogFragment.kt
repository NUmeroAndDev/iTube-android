package com.numero.itube.fragment

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.repository.ConfigRepository
import javax.inject.Inject

open class RoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var configRepository: ConfigRepository

    override fun getTheme(): Int = if (configRepository.isUseDarkTheme) {
        R.style.BottomSheetDialogTheme_Rounded_Dark
    } else {
        R.style.BottomSheetDialogTheme_Rounded
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, theme)
        return super.onCreateDialog(savedInstanceState)
    }
}