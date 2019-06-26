package com.numero.itube.ui.video.detail

import com.numero.itube.model.Video

interface DetailCallback {
    fun showSelectPlaylist()

    fun showVideo(video: Video)
}