package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.SearchFragment
import com.numero.itube.model.Video

class SearchActivity : AppCompatActivity(), SearchFragment.SearchFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val fragment = findFragment(R.id.container)
        if (fragment == null) {
            replace(R.id.container, SearchFragment.newInstance(), false)
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

    override fun showVideo(video: Video) {
        startActivity(PlayerActivity.createIntent(this, video))
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, SearchActivity::class.java)
    }
}
