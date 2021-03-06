package com.numero.itube.ui.video.detail.item

import androidx.core.view.isVisible
import com.numero.itube.R
import com.numero.itube.model.VideoDetail
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_video_detail_header.*

class VideoDetailItem(
        private val videoDetail: VideoDetail,
        private val inPlaylist: Boolean,
        private val onClickAddPlaylistListener: (() -> Unit)?
) : Item() {

    override fun getLayout(): Int = R.layout.item_video_detail_header

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            titleTextView.text = videoDetail.title
            addPlaylistButton.isVisible = inPlaylist.not()
            addPlaylistButton.setOnClickListener {
                onClickAddPlaylistListener?.invoke()
            }
        }
    }

}