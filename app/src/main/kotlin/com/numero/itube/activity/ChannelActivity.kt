package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.R
import com.numero.itube.contract.ChannelDetailContract
import com.numero.itube.extension.component
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.Video
import com.numero.itube.presenter.ChannelDetailPresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import kotlinx.android.synthetic.main.activity_channel.*
import javax.inject.Inject

class ChannelActivity : AppCompatActivity(), ChannelDetailContract.View {

    @Inject
    lateinit var youtubeApiRepository: YoutubeRepository

    private lateinit var presenter: ChannelDetailContract.Presenter
    private val videoListAdapter: VideoListAdapter = VideoListAdapter()
    private val channelName: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_NAME) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }
    private var nextPageToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ChannelDetailPresenter(this, youtubeApiRepository, channelId)

        channelNameTextView.text = channelName
        videoRecyclerView.apply {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addOnScrollListener(EndlessScrollListener(manager) {
                val nextPageToken = nextPageToken ?: return@EndlessScrollListener
                presenter.loadNextVideo(getString(R.string.api_key), nextPageToken)
            })
            adapter = videoListAdapter
        }

        presenter.loadChannelDetail(getString(R.string.api_key))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showBannerImage(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(bannerImageView)
    }

    override fun showChannelThumbnail(thumbnail: ChannelDetail.Thumbnails.Thumbnail) {
        Glide.with(this).load(thumbnail.url).apply(RequestOptions().circleCrop()).into(channelImageView)
    }

    override fun showVideoList(videoList: List<Video>, nextPageToken: String?) {
        this.nextPageToken = nextPageToken
        videoListAdapter.videoList = videoList.toMutableList()
    }

    override fun showAddedVideoList(videoList: List<Video>, nextPageToken: String?) {
        this.nextPageToken = nextPageToken
        videoListAdapter.addVideoList(videoList)
    }

    override fun showErrorMessage(e: Throwable?) {
    }

    override fun showProgress() {
    }

    override fun dismissProgress() {
    }

    override fun setPresenter(presenter: ChannelDetailContract.Presenter) {
        this.presenter = presenter
    }

    companion object {

        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"
        private const val BUNDLE_CHANNEL_NAME = "BUNDLE_CHANNEL_NAME"

        fun createIntent(context: Context, channelName: String, channelId: String): Intent = Intent(context, ChannelActivity::class.java).apply {
            putExtra(BUNDLE_CHANNEL_NAME, channelName)
            putExtra(BUNDLE_CHANNEL_ID, channelId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}