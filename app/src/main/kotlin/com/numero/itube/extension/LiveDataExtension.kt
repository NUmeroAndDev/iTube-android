package com.numero.itube.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) =
        this.observe(owner) {
            if (it != null) {
                observer(it)
            }
        }

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) =
        observe(owner, Observer { observer(it) })

fun <A, B> zipLiveData(a: LiveData<A>, b: LiveData<B>, isPostUIThread: Boolean = true): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null) {
                val result = Pair(localLastA, localLastB)
                if (isPostUIThread) {
                    postValue(result)
                } else {
                    value = result
                }
            }
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}