package com.numero.itube.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.Video
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_video.*

class FavoriteVideoAdapter : RecyclerView.Adapter<FavoriteVideoAdapter.VideoViewHolder>() {

    var videoList: List<Video.Favorite> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var currentVideoId: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((video: Video.Favorite) -> Unit)? = null

    fun setOnItemClickListener(listener: ((video: Video.Favorite) -> Unit)) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_video, parent, false))
    }

    override fun getItemCount(): Int = videoList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.setVideo(video)
        holder.isCurrentVideo = video.id.value == currentVideoId
        holder.itemView.setOnClickListener {
            if (holder.isCurrentVideo.not()) {
                onItemClickListener?.invoke(video)
            }
        }
    }

    class VideoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        var isCurrentVideo: Boolean = false
            set(value) {
                field = value
                playingViewGroup.isVisible = field
            }

        fun setVideo(video: Video.Favorite) {
            titleTextView.text = video.title

            val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radius)
            GlideApp.with(itemView.context)
                    .load(video.thumbnailUrl.value)
                    .transforms(CenterCrop(), RoundedCorners(cornerRadius))
                    .into(thumbnailImageView)
        }
    }
}