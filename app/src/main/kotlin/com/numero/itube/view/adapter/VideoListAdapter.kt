package com.numero.itube.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.model.Video
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_video.*

class VideoListAdapter : RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

    var videoList: List<Video> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((video: Video) -> Unit)? = null

    fun setOnItemClickListener(listener: ((video: Video) -> Unit)) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_video, parent, false))
    }

    override fun getItemCount(): Int = videoList.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.setVideo(video)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(video)
        }
    }

    class VideoViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun setVideo(video: Video) {
            titleTextView.text = video.snippet.title
            GlideApp.with(itemView.context).load(video.snippet.thumbnails.high.url).diskCacheStrategy(DiskCacheStrategy.NONE).into(thumbnailImageView)
        }
    }
}