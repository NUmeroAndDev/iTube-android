package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.R
import com.numero.itube.contract.ChannelVideoListContract
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.presenter.ChannelVideoListPresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import kotlinx.android.synthetic.main.activity_channel_detail.*
import javax.inject.Inject

class ChannelDetailActivity : AppCompatActivity() {

    private val channelName: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_NAME) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }
    private val thumbnailUrl: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_THUMBNAIL_URL) }

    @Inject
    lateinit var youtubeApiRepository: YoutubeRepository
    private lateinit var presenter: ChannelVideoListContract.Presenter
    private lateinit var viewModel: ChannelVideoListViewModel

    private val videoListAdapter: VideoListAdapter = VideoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setContentView(R.layout.activity_channel_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayShowTitleEnabled(false)
        }

        initViews()
        initViewModel()

        presenter = ChannelVideoListPresenter(viewModel, channelId, youtubeApiRepository)

        if (savedInstanceState == null) {
            // 画面回転時には以前のデータが復帰される
            presenter.loadChannelVideo(getString(R.string.api_key))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        channelNameTextView.text = channelName
        Glide.with(this).load(thumbnailUrl).apply(RequestOptions().circleCrop()).into(channelImageView)

        videoListAdapter.setOnItemClickListener {
            startActivity(PlayerActivity.createIntent(this, it))
        }
        videoRecyclerView.apply {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addOnScrollListener(EndlessScrollListener(manager) {
                val hasNextPage = viewModel.hasNextPage.value
                if (hasNextPage != null && hasNextPage.not()) {
                    return@EndlessScrollListener
                }
                presenter.loadMoreVideo(getString(R.string.api_key), viewModel.nextPageToken.value)
            })
            adapter = videoListAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChannelVideoListViewModel::class.java)
        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.submitList(it)
        }
        viewModel.isShowProgress.observeNonNull(this) {
            progressBar.isInvisible = it.not()
        }
        viewModel.channelDetail.observeNonNull(this) {
            val urlString = it.branding.image.bannerTvMediumImageUrl
            Glide.with(this).load(urlString).into(thumbnailImageView)
        }
    }

    companion object {

        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"
        private const val BUNDLE_CHANNEL_NAME = "BUNDLE_CHANNEL_NAME"
        private const val BUNDLE_CHANNEL_THUMBNAIL_URL = "BUNDLE_CHANNEL_THUMBNAIL_URL"

        fun createIntent(context: Context, channelName: String, channelId: String, thumbnailUrl: String): Intent = Intent(context, ChannelDetailActivity::class.java).apply {
            putExtra(BUNDLE_CHANNEL_NAME, channelName)
            putExtra(BUNDLE_CHANNEL_ID, channelId)
            putExtra(BUNDLE_CHANNEL_THUMBNAIL_URL, thumbnailUrl)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}