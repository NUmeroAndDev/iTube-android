package com.numero.itube.extension

import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.numero.itube.di.ApplicationComponent
import com.numero.itube.iTubeApplication

fun AppCompatActivity.replace(@IdRes res: Int, fragment: Fragment, isAddBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().apply {
        replace(res, fragment, fragment::class.java.simpleName)
        if (isAddBackStack) {
            addToBackStack(null)
        }
    }.commit()
}

fun AppCompatActivity.hideKeyboard() {
    val inputMethodManager = getSystemService<InputMethodManager>()
    val view = currentFocus ?: return
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun AppCompatActivity.findFragment(@IdRes byId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(byId)
}

val AppCompatActivity.component: ApplicationComponent?
    get() {
        return (application as? iTubeApplication)?.applicationComponent
    }