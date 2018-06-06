package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.R
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.ChannelVideoListFragment
import kotlinx.android.synthetic.main.activity_channel_detail.*

class ChannelDetailActivity : AppCompatActivity(), ChannelVideoListFragment.ChannelVideoFragmentListener {

    private val channelName: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_NAME) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }
    private val thumbnailUrl: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_THUMBNAIL_URL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayShowTitleEnabled(false)
        }

        channelNameTextView.text = channelName
        Glide.with(this).load(thumbnailUrl).apply(RequestOptions().circleCrop()).into(channelImageView)

        if (findFragment(R.id.container) == null) {
            replace(R.id.container, ChannelVideoListFragment.newInstance(channelId), false)
        }
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

    override fun showChannelThumbnail(urlString: String) {

    }

    override fun showVideo(video: SearchResponse.Video) {
        startActivity(PlayerActivity.createIntent(this, video))
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