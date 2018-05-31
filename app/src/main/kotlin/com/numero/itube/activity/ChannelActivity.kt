package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.numero.itube.R
import com.numero.itube.contract.ChannelDetailContract
import com.numero.itube.extension.component
import com.numero.itube.presenter.ChannelDetailPresenter
import com.numero.itube.repository.YoutubeRepository
import kotlinx.android.synthetic.main.activity_channel.*
import javax.inject.Inject

class ChannelActivity : AppCompatActivity(), ChannelDetailContract.View {

    @Inject
    lateinit var youtubeApiRepository: YoutubeRepository

    private lateinit var presenter: ChannelDetailContract.Presenter

    private val channelName: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_NAME) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = channelName
        }

        ChannelDetailPresenter(this, youtubeApiRepository, channelId)

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