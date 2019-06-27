package com.numero.itube.ui.video.detail

import com.numero.itube.model.Playlist
import com.numero.itube.model.Video
import com.numero.itube.model.VideoDetail

interface DetailCallback {

    fun addPlaylist(playlist: Playlist, videoDetail: VideoDetail)

    fun showVideo(video: Video)
}