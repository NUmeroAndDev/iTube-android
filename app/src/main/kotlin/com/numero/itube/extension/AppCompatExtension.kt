package com.numero.itube.extension

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.numero.itube.di.ApplicationComponent
import com.numero.itube.iTubeApplication

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

val AppCompatActivity.component: ApplicationComponent?
    get() {
        return (application as? iTubeApplication)?.applicationComponent
    }