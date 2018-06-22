package com.numero.itube.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.R
import com.numero.itube.api.response.SearchResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_relative_video.*

class RelativeVideoListAdapter : RecyclerView.Adapter<RelativeVideoListAdapter.VideoViewHolder>() {

    var videoList: List<SearchResponse.Video> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ((video: SearchResponse.Video) -> Unit)? = null

    fun setOnItemClickListener(listener: ((video: SearchResponse.Video) -> Unit)) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_relative_video, parent, false))
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

        fun setVideo(video: SearchResponse.Video) {
            titleTextView.text = video.snippet.title

            val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radius)
            Glide.with(itemView.context)
                    .load(video.snippet.thumbnails.high.url)
                    .apply(RequestOptions().transform(RoundedCorners(cornerRadius)))
                    .into(thumbnailImageView)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
        }
    }
}