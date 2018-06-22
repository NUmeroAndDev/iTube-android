package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.numero.itube.R
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.FavoriteVideoListFragment
import com.numero.itube.fragment.SearchFragment
import com.numero.itube.repository.db.FavoriteVideo

class SearchActivity : AppCompatActivity(),
        SearchFragment.SearchFragmentListener,
        FavoriteVideoListFragment.FavoriteFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (findFragment(R.id.container) == null) {
            replace(R.id.container, SearchFragment.newInstance(), false)
        }
    }

    override fun showVideo(video: SearchResponse.Video) {
        startActivity(PlayerActivity.createIntent(this, video))
    }

    override fun showVideo(video: FavoriteVideo) {
        startActivity(PlayerActivity.createIntent(this, video))
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SearchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
