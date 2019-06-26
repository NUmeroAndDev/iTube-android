package com.numero.itube.ui.top.item

import com.numero.itube.R
import com.numero.itube.model.PlaylistDetail
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_holder_video.*

class PlaylistHeaderItem(
        private val playlistDetail: PlaylistDetail
) : Item() {

    override fun getLayout(): Int = R.layout.item_playlist_header

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleTextView.text = playlistDetail.title
    }

}