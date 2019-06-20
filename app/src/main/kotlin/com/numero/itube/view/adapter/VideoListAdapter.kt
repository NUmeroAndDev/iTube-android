package com.numero.itube.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.Video
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_video.*

class VideoListAdapter : ListAdapter<Video.Search, VideoListAdapter.VideoViewHolder>(diffCallback) {

    private var onItemClickListener: ((video: Video.Search) -> Unit)? = null

    fun setOnItemClickListener(listener: ((video: Video.Search) -> Unit)) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_video, parent, false))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.setVideo(video)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(video)
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
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Video.Search>() {
            override fun areItemsTheSame(oldItem: Video.Search, newItem: Video.Search): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Video.Search, newItem: Video.Search): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}