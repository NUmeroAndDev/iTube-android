package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.numero.itube.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private var listener: MainSettingsFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MainSettingsFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        licenseItemView.setOnClickListener {
            listener?.showLicenses()
        }
    }

    interface MainSettingsFragmentListener {
        fun showLicenses()
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}