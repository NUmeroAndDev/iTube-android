package com.numero.itube.extension

import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.numero.itube.R
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

@ColorInt
fun AppCompatActivity.getAttrColor(@AttrRes attr: Int): Int = with(TypedValue()) {
    theme.resolveAttribute(attr, this, true)
    this.data
}

fun AppCompatActivity.getTintedDrawable(@DrawableRes drawable: Int, @ColorInt color: Int): Drawable? {
    val d = ContextCompat.getDrawable(this, drawable) ?: return null
    DrawableCompat.setTint(d, color)
    return d
}