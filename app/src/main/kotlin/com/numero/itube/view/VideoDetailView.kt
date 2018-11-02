package com.numero.itube.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.numero.itube.GlideApp
import com.numero.itube.R
import kotlinx.android.synthetic.main.view_video_detail.view.*

class VideoDetailView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.view_video_detail, this)

        titleLayout.setOnClickListener {
            if (detailMotionLayout.progress > 0.5f) {
                detailMotionLayout.transitionToStart()
            } else {
                detailMotionLayout.transitionToEnd()
            }
        }
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setDescription(description: String) {
        descriptionTextView.text = description
    }

    fun setChannelName(channelName: String) {
        channelNameTextView.text = channelName
    }

    fun setChannelImageUrl(url: String) {
        GlideApp.with(this).load(url).circleCrop().into(channelImageView)
    }

    fun setOnChannelClickListener(listener: (imageView: AppCompatImageView) -> Unit) {
        channelLayout.setOnClickListener {
            listener(channelImageView)
        }
    }
}