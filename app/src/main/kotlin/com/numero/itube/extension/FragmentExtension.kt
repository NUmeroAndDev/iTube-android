package com.numero.itube.extension

import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.core.content.systemService


fun Fragment.hideKeyboard() {
    val inputMethodManager = activity?.systemService() as? InputMethodManager
    val view = activity?.currentFocus ?: return
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}