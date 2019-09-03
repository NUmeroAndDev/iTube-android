package com.numero.itube.ui.video.detail.item

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.Video
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_holder_video.*

class RelativeVideoItem(
        val video: Video.Search
) : Item() {

    override fun getLayout(): Int = R.layout.view_holder_video

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.root.context
        viewHolder.titleTextView.text = video.title

        val cornerRadius = context.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radius)
        GlideApp.with(context)
                .load(video.thumbnailUrl.value)
                .transforms(CenterCrop(), RoundedCorners(cornerRadius))
                .into(viewHolder.thumbnailImageView)
    }

}