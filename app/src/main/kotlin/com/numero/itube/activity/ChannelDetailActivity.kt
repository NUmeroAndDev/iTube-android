package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.model.ChannelId
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.ui.video.detail.VideoDetailActivity
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import kotlinx.android.synthetic.main.activity_channel_detail.*
import javax.inject.Inject

class ChannelDetailActivity : AppCompatActivity() {

    private val channelName: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_NAME) }
    private val channelId: ChannelId by lazy {
        val id = intent.getStringExtra(BUNDLE_CHANNEL_ID)
        ChannelId(id)
    }
    private val thumbnailUrl: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_THUMBNAIL_URL) }

    @Inject
    lateinit var configRepository: ConfigRepository
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ChannelVideoListViewModel::class.java)
    }

    private val videoListAdapter: VideoListAdapter = VideoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setTheme(configRepository.theme)
        setContentView(R.layout.activity_channel_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        initViews()
        setupObserve()
        if (savedInstanceState == null) {
            // 画面回転時には以前のデータが復帰される
            viewModel.executeLoadChannelVideo(channelId)
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

        GlideApp.with(this).load(thumbnailUrl).circleCrop().into(channelImageView)

        videoListAdapter.setOnItemClickListener {
            startActivity(VideoDetailActivity.createIntent(this, it))
        }
        videoRecyclerView.apply {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addOnScrollListener(EndlessScrollListener(manager) {
                viewModel.executeMoreLoad()
            })
            adapter = videoListAdapter
        }
    }

    private fun setupObserve() {
        viewModel.channelVideoListLiveData.observe(this) {
            videoListAdapter.submitList(it.videoList)
        }
        viewModel.channelDetail.observe(this) {
            GlideApp.with(this).load(it.bannerUrl.value).into(thumbnailImageView)
        }
        viewModel.isShowProgress.observe(this) {
            progressBar.isInvisible = it.not()
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