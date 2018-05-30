package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.numero.itube.R
import kotlinx.android.synthetic.main.activity_player.*

class ChannelActivity : AppCompatActivity() {

    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
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

    companion object {

        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"

        fun createIntent(context: Context, channelId: String): Intent = Intent(context, ChannelActivity::class.java).apply {
            putExtra(BUNDLE_CHANNEL_ID, channelId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}