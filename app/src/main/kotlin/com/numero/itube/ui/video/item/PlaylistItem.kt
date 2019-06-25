package com.numero.itube.ui.video.item

import com.numero.itube.R
import com.numero.itube.model.Playlist
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_playlist.*

class PlaylistItem(
        val playlist: Playlist
) : Item() {

    override fun getLayout(): Int = R.layout.item_playlist

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleTextView.text = playlist.title
    }

}