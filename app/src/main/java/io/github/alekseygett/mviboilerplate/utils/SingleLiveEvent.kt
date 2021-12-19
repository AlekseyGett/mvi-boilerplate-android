package io.github.alekseygett.mviboilerplate.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface SingleLiveEvent<T> {
    fun observe(owner: LifecycleOwner, observer: Observer<in T>)
}