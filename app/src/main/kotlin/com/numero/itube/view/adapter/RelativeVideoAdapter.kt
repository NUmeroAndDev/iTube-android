package com.numero.itube.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.Video
import com.numero.itube.model.VideoDetail
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_video.*
import kotlinx.android.synthetic.main.view_holder_video_detail.*

class RelativeVideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var videoDetail: VideoDetail? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((video: Video.Search) -> Unit)? = null
    private var onChannelClickListener: ((imageView: AppCompatImageView, channelName: String, imageUrl: String) -> Unit)? = null

    fun setOnItemClickListener(listener: ((video: Video.Search) -> Unit)) {
        onItemClickListener = listener
    }

    fun setOnChannelClickListener(listener: ((imageView: AppCompatImageView, channelName: String, imageUrl: String) -> Unit)) {
        onChannelClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_VIDEO_DETAIL) {
            VideoDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_video_detail, parent, false))
        } else {
            VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_video, parent, false))
        }
    }

    override fun getItemCount(): Int {
        val videoDetail = videoDetail ?: return 0
        val videoList = videoDetail.relativeVideoList
        return videoList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val videoDetail = videoDetail ?: return
        if (holder is VideoDetailViewHolder) {
            updateVideoDetailViewHolder(holder, videoDetail)
        } else if (holder is VideoViewHolder) {
            val video = videoDetail.relativeVideoList[position - 1]
            holder.setVideo(video)
            holder.itemView.setOnClickListener {
                if (video is Video.Search) {
                    onItemClickListener?.invoke(video)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_VIDEO_DETAIL
        } else {
            VIEW_TYPE_RELATIVE_VIDEO
        }
    }

    private fun updateVideoDetailViewHolder(holder: VideoDetailViewHolder, videoDetail: VideoDetail) {
        val channelDetail = videoDetail.channelDetail
        holder.setVideoDetail(videoDetail)
        holder.setChannelDetail(channelDetail)
        holder.videoDetailView.setOnChannelClickListener {
            onChannelClickListener?.invoke(it, channelDetail.title, channelDetail.thumbnailUrl.value)
        }
    }

    class VideoDetailViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setVideoDetail(videoDetail: VideoDetail) {
            videoDetailView.setTitle(videoDetail.title)
            videoDetailView.setDescription(videoDetail.description)
        }

        fun setChannelDetail(channel: ChannelDetail) {
            videoDetailView.setChannelName(channel.title)
            videoDetailView.setChannelImageUrl(channel.thumbnailUrl.value)
        }
    }

    class VideoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun setVideo(video: Video) {
            titleTextView.text = video.title

            val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radius)
            GlideApp.with(itemView.context)
                    .load(video.thumbnailUrl.value)
                    .transforms(CenterCrop(), RoundedCorners(cornerRadius))
                    .into(thumbnailImageView)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
        }
    }

    companion object {
        private const val VIEW_TYPE_VIDEO_DETAIL = 0
        private const val VIEW_TYPE_RELATIVE_VIDEO = 1
    }
}