package com.numero.itube.ui.video.detail.item

import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.ChannelDetail
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_channel.*

class ChannelItem(
        val channelDetail: ChannelDetail
) : Item() {

    override fun getLayout(): Int = R.layout.item_channel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.root.context
        viewHolder.apply {
            nameTextView.text = channelDetail.title
            GlideApp.with(context).load(channelDetail.thumbnailUrl.value).circleCrop().into(iconImageView)
        }
    }

}