package com.numero.itube.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.numero.itube.R
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_video.*

class FavoriteVideoListAdapter : RecyclerView.Adapter<FavoriteVideoListAdapter.VideoViewHolder>() {

    var videoList: List<FavoriteVideo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((favoriteVideo: FavoriteVideo) -> Unit)? = null

    fun setOnItemClickListener(listener: ((favoriteVideo: FavoriteVideo) -> Unit)) {
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

    class VideoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun setVideo(video: FavoriteVideo) {
            titleTextView.text = video.title
            Glide.with(itemView.context).load(video.thumbnailUrl).into(thumbnailImageView)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
        }
    }
}