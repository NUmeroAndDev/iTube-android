package com.numero.itube.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
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

    var title: String
        get() = titleTextView.text.toString()
        set(value) {
            titleTextView.text = value
        }

    private var onCheckedChangeListener: ((Boolean) -> Unit)? = null

    init {
        View.inflate(context, R.layout.view_settings_switch_item, this)

        context.withStyledAttributes(attrs, R.styleable.SettingsSwitchItemView) {
            titleTextView.text = getString(R.styleable.SettingsSwitchItemView_title)
            dividerView.visibility = if (getBoolean(R.styleable.SettingsSwitchItemView_showDivider, true)) View.VISIBLE else View.GONE
        }

        switchView.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            onCheckedChangeListener?.invoke(isChecked)
        }

        setOnClickListener {
            switchView.isChecked = switchView.isChecked.not()
        }
    }

    fun setOnCheckedChangeListener(listener: ((Boolean) -> Unit)) {
        onCheckedChangeListener = listener
    }
}