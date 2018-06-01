package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.ChannelVideoFragment
import com.numero.itube.model.Video
import kotlinx.android.synthetic.main.activity_channel_detail.*

class ChannelDetailActivity : AppCompatActivity(), ChannelVideoFragment.ChannelVideoFragmentListener {

    private val channelName: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_NAME) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        channelNameTextView.text = channelName

        if (findFragment(R.id.container) == null) {
            replace(R.id.container, ChannelVideoFragment.newInstance(channelId), false)
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
        Glide.with(this).load(urlString).apply(RequestOptions().circleCrop()).into(channelImageView)
    }

    override fun showVideo(video: Video) {
        startActivity(PlayerActivity.createIntent(this, video))
    }

    companion object {

        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"
        private const val BUNDLE_CHANNEL_NAME = "BUNDLE_CHANNEL_NAME"

        fun createIntent(context: Context, channelName: String, channelId: String): Intent = Intent(context, ChannelDetailActivity::class.java).apply {
            putExtra(BUNDLE_CHANNEL_NAME, channelName)
            putExtra(BUNDLE_CHANNEL_ID, channelId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}