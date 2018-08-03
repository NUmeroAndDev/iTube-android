package com.numero.itube.extension

import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

fun MenuItem.setTint(@ColorInt color: Int) {
    val drawable = DrawableCompat.wrap(icon)
    DrawableCompat.setTint(drawable, color)
    icon = drawable
}