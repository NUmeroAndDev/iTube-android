package com.numero.itube.ui.playlist.item

import com.numero.itube.R
import com.numero.itube.model.Playlist
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_playlist.*

class PlaylistItem(
        val playlist: Playlist
) : Item() {

    // TODO implement item design
    override fun getLayout(): Int = R.layout.item_playlist

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.titleTextView.text = playlist.title
    }

}