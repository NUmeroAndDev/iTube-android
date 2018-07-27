package com.numero.itube.extension

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

fun View.showKeyboard() {
    val inputMethodManager = context.getSystemService<InputMethodManager>()
    if (inputMethodManager != null) {
        inputMethodManager.showSoftInput(this, 0)
    }
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService<InputMethodManager>()
    if (inputMethodManager != null) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}