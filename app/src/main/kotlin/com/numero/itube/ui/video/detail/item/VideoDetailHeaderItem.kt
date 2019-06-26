package com.numero.itube.ui.video.detail.item

import com.numero.itube.R
import com.numero.itube.model.VideoDetail
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_video_detail_header.*

class VideoDetailHeaderItem(
        private val videoDetail: VideoDetail
) : Item() {

    override fun getLayout(): Int = R.layout.item_video_detail_header

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.videoDetailView.apply {
            setTitle(videoDetail.title)
            setDescription(videoDetail.description)
            setChannelImageUrl(videoDetail.channelDetail.thumbnailUrl.value)
            setChannelName(videoDetail.channelDetail.title)
        }
    }

}