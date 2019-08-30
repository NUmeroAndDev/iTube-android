package com.numero.itube.ui.top.item

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.PlaylistSummary
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_playlist_summary.*

class PlaylistSummaryItem(
        val playlistSummary: PlaylistSummary
) : Item() {

    override fun getLayout(): Int = R.layout.item_playlist_summary

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.root.context
        viewHolder.titleTextView.text = playlistSummary.title
        viewHolder.videoCountTextView.text = "${playlistSummary.totalVideoCount}件の動画"

        val thumbnailUrl = playlistSummary.video?.thumbnailUrl
        if (thumbnailUrl == null) {
            viewHolder.thumbnailImageView.setImageDrawable(null)
        } else {
            val cornerRadius = context.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radius)
            GlideApp.with(context)
                    .load(thumbnailUrl.value)
                    .transforms(CenterCrop(), RoundedCorners(cornerRadius))
                    .into(viewHolder.thumbnailImageView)
        }
    }

}