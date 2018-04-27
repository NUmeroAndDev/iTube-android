package com.numero.itube.extension

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.replace(@IdRes res: Int, fragment: Fragment, isAddBackStack: Boolean) {
    supportFragmentManager.beginTransaction().apply {
        replace(res, fragment, fragment::class.java.simpleName)
        if (isAddBackStack) {
            addToBackStack(null)
        }
    }.commit()
}

fun AppCompatActivity.findFragment(@IdRes byId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(byId)
}