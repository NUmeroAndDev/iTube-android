package com.numero.itube.extension

import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.numero.itube.di.ApplicationComponent
import com.numero.itube.iTubeApplication

fun Fragment.hideKeyboard() {
    val inputMethodManager = activity?.getSystemService<InputMethodManager>()
    val view = activity?.currentFocus ?: return
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

val Fragment.component: ApplicationComponent?
    get() {
        return (activity?.application as? iTubeApplication)?.applicationComponent
    }

fun Fragment.replace(@IdRes res: Int, fragment: Fragment, isAddBackStack: Boolean = false) {
    val fragmentManager = fragmentManager ?: return
    fragmentManager.beginTransaction().apply {
        replace(res, fragment, fragment::class.java.simpleName)
        if (isAddBackStack) {
            addToBackStack(null)
        }
    }.commit()
}

fun Fragment.remove(fragment: Fragment) {
    val fragmentManager = fragmentManager ?: return
    fragmentManager.beginTransaction().remove(fragment).commit()
}

fun Fragment.findFragment(@IdRes byId: Int): Fragment? {
    return fragmentManager?.findFragmentById(byId)
}