package io.github.alekseygett.mviboilerplate.utils

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import java.util.*

private const val DEFAULT_THROTTLE_DELAY = 300L

fun View.setThrottledClickListener(delay: Long = DEFAULT_THROTTLE_DELAY, onClick: (View) -> Unit) {
    setOnClickListener {
        throttle(delay) {
            onClick(it)
        }
    }
}

private var lastClickTimestamp = 0L
fun throttle(delay: Long = DEFAULT_THROTTLE_DELAY, action: () -> Unit): Boolean {
    val currentTimestamp = System.currentTimeMillis()
    val delta = currentTimestamp - lastClickTimestamp
    if (delta !in 0L..delay) {
        lastClickTimestamp = currentTimestamp
        action()
        return true
    }
    return false
}

fun EditText.setDebouncingTextListener(
    debouncePeriod: Long = 300,
    onTextChange: (String) -> Unit
) {
    addTextChangedListener(object : TextWatcher {
        private var timer = Timer()

        override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            timer.cancel()
            timer = Timer()
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        post {
                            onTextChange(newText.toString())
                        }
                    }
                },
                debouncePeriod
            )
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}
    })
}

fun ImageView.loadImage(
    src: String?,
    @DrawableRes errorRes: Int,
    @DrawableRes placeholderRes: Int,
    config: RequestBuilder<Drawable>.() -> Unit = {}
) {
    Glide
        .with(context)
        .load(src)
        .error(errorRes)
        .placeholder(placeholderRes)
        .apply { config(this) }
        .into(this)
}