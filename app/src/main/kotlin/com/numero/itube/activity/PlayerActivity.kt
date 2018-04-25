package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.DetailFragment
import com.numero.itube.model.Video

class PlayerActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    private val video: Video by lazy { intent.getSerializableExtra(BUNDLE_VIDEO) as Video }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = video.snippet.title
        }

        val youTubePlayerFragment = YouTubePlayerFragment.newInstance().apply {
            replace(R.id.playerContainer, this, false)
        }
        youTubePlayerFragment.initialize(getString(R.string.api_key), this)

        val fragment = findFragment(R.id.detailContainer)
        if (fragment == null) {
            replace(R.id.detailContainer, DetailFragment.newInstance(video), false)
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

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer?, b: Boolean) {
        if (b.not()) {
            youTubePlayer?.cueVideo(video.id.videoId)
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
    }

    companion object {
        private const val BUNDLE_VIDEO = "BUNDLE_VIDEO"

        fun createIntent(context: Context, video: Video): Intent = Intent(context, PlayerActivity::class.java).apply {
            putExtra(BUNDLE_VIDEO, video)
        }
    }
}