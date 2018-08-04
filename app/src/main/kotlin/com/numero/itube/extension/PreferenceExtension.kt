package com.numero.itube.extension

import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.Preference

fun Preference.tintIcon(@AttrRes attr: Int) {
    val drawable = icon ?: return
    val color = with(TypedValue()) {
        context.theme.resolveAttribute(attr, this, true)
        this.data
    }
    DrawableCompat.setTint(drawable, color)
    icon = drawable
}