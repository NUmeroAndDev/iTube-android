package com.numero.itube.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.numero.itube.R
import kotlinx.android.synthetic.main.view_settings_switch_item.view.*

class SettingsSwitchItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var isChecked: Boolean
        get() = switchView.isChecked
        set(value) {
            switchView.isChecked = value
        }

    init {
        View.inflate(context, R.layout.view_settings_switch_item, this)

        context.withStyledAttributes(attrs, R.styleable.SettingsSwitchItemView) {
            titleTextView.text = getString(R.styleable.SettingsSwitchItemView_title)
        }

        setOnClickListener {
            switchView.isChecked = switchView.isChecked.not()
        }
    }
}