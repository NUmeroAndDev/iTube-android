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
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_video.*
import kotlinx.android.synthetic.main.view_holder_video_detail.*

class RelativeVideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var relativeResponse: RelativeResponse? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((video: SearchResponse.Video) -> Unit)? = null
    private var onChannelClickListener: ((imageView: AppCompatImageView, channelName: String, imageUrl: String) -> Unit)? = null

    fun setOnItemClickListener(listener: ((video: SearchResponse.Video) -> Unit)) {
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
        val response = relativeResponse ?: return 0
        val videoList = response.searchResponse.items
        return videoList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val response = relativeResponse ?: return
        if (holder is VideoDetailViewHolder) {
            updateVideoDetailViewHolder(holder, response)
        } else if (holder is VideoViewHolder) {
            val video = response.searchResponse.items[position - 1]
            holder.setVideo(video)
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(video)
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

    private fun updateVideoDetailViewHolder(holder: VideoDetailViewHolder, response: RelativeResponse) {
        val videoDetail = response.videoDetailResponse.items[0]
        val channelDetail = response.channelResponse.items[0]
        holder.setVideoDetail(videoDetail)
        holder.setChannelDetail(channelDetail)
        holder.videoDetailView.setOnChannelClickListener {
            onChannelClickListener?.invoke(it, videoDetail.snippet.title, channelDetail.snippet.thumbnails.medium.url)
        }
    }

    class VideoDetailViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun setVideoDetail(videoDetail: VideoDetailResponse.VideoDetail) {
            videoDetailView.setTitle(videoDetail.snippet.title)
            videoDetailView.setDescription(videoDetail.snippet.description)
        }

        fun setChannelDetail(channel: ChannelResponse.Channel) {
            videoDetailView.setChannelName(channel.snippet.title)
            videoDetailView.setChannelImageUrl(channel.snippet.thumbnails.medium.url)
        }
    }

    class VideoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun setVideo(video: SearchResponse.Video) {
            titleTextView.text = video.snippet.title

            val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radius)
            GlideApp.with(itemView.context)
                    .load(video.snippet.thumbnails.high.url)
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