package com.numero.itube.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_relative_video.*

class FavoriteVideoAdapter : RecyclerView.Adapter<FavoriteVideoAdapter.VideoViewHolder>() {

    var videoList: List<FavoriteVideo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var currentVideoId: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((video: FavoriteVideo) -> Unit)? = null

    fun setOnItemClickListener(listener: ((video: FavoriteVideo) -> Unit)) {
        onItemClickListener = listener
    }

    fun playNextVideo() {
        val currentVideoPosition = videoList.indexOfFirst { it.id == currentVideoId }
        if (currentVideoPosition == -1) {
            return
        }
        if (currentVideoPosition == videoList.size - 1) {
            onItemClickListener?.invoke(videoList[0])
            return
        }
        onItemClickListener?.invoke(videoList[currentVideoPosition + 1])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_relative_video, parent, false))
    }

    override fun getItemCount(): Int = videoList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.setVideo(video)
        holder.isCurrentVideo = video.id == currentVideoId
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
                playingViewGroup.visibility = if (field) View.VISIBLE else View.GONE
            }

        fun setVideo(video: FavoriteVideo) {
            titleTextView.text = video.title

            val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radius)
            GlideApp.with(itemView.context)
                    .load(video.thumbnailUrl)
                    .transform(RoundedCorners(cornerRadius))
                    .into(thumbnailImageView)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
        }
    }
}