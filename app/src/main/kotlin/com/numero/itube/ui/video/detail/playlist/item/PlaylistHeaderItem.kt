package com.numero.itube.ui.video.detail.playlist.item

import com.numero.itube.R
import com.numero.itube.model.PlaylistDetail
import com.numero.itube.model.PlaylistId
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_playlist_header.*

class PlaylistHeaderItem(
        private val playlistDetail: PlaylistDetail,
        private val onClickEditPlaylistListener: (playlistId: PlaylistId) -> Unit
) : Item() {

    override fun getLayout(): Int = R.layout.item_playlist_header

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleTextView.text = playlistDetail.title
        viewHolder.editPlayListButton.setOnClickListener {
            onClickEditPlaylistListener(playlistDetail.id)
        }
    }

}