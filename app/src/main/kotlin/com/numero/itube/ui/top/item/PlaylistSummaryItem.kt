package com.numero.itube.ui.top.item

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.PlaylistSummary
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_holder_video.*

class PlaylistSummaryItem(
        val playlistSummary: PlaylistSummary
) : Item() {

    override fun getLayout(): Int = R.layout.view_holder_video

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.root.context
        viewHolder.titleTextView.text = playlistSummary.title

        val thumbnailUrl = playlistSummary.thumbnailUrl
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