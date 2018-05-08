package com.numero.itube.view.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.numero.itube.R
import kotlinx.android.synthetic.main.view_settings_item.view.*

class SettingsItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var title: String
        get() = titleTextView.text.toString()
        set(value) {
            titleTextView.text = value
        }

    init {
        View.inflate(context, R.layout.view_settings_item, this)

        context.withStyledAttributes(attrs, R.styleable.SettingsItemView) {
            titleTextView.text = getString(R.styleable.SettingsItemView_title)
            dividerView.visibility = if (getBoolean(R.styleable.SettingsItemView_showDivider, true)) View.VISIBLE else View.GONE
        }
    }
}