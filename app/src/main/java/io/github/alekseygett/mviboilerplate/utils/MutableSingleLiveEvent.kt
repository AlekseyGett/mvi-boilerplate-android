package io.github.alekseygett.mviboilerplate.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class MutableSingleLiveEvent<T> : MutableLiveData<T>(), SingleLiveEvent<T> {

    private val isPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { value ->
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }
    }

    @MainThread
    override fun setValue(value: T?) {
        isPending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() {
        value = null
    }

}