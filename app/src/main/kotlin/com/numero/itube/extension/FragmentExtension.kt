package com.numero.itube.extension

import android.view.inputmethod.InputMethodManager
import androidx.core.content.systemService
import androidx.fragment.app.Fragment
import com.numero.itube.di.ApplicationComponent
import com.numero.itube.iTubeApplication

fun Fragment.hideKeyboard() {
    val inputMethodManager = activity?.systemService() as? InputMethodManager
    val view = activity?.currentFocus ?: return
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

val Fragment.component: ApplicationComponent?
    get() {
        return (activity?.application as? iTubeApplication)?.applicationComponent
    }