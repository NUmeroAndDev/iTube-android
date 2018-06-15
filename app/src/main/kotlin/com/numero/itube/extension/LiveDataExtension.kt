package com.numero.itube.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) =
        this.observe(owner) {
            if (it != null) {
                observer(it)
            }
        }

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) =
        observe(owner, Observer { observer(it) })
