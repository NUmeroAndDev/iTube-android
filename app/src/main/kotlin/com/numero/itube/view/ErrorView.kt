package com.numero.itube.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.numero.itube.R
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var listener: (() -> Unit)? = null

    fun setOnRetryListener(listener: (() -> Unit)) {
        this.listener = listener
    }

    init {
        View.inflate(context, R.layout.view_error, this)

        retryButton.setOnClickListener {
            listener?.invoke()
        }
    }
}