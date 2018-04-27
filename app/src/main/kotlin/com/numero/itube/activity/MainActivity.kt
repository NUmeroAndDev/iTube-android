package com.numero.itube.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.FavoriteFragment
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FavoriteFragment.FavoriteFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragment = findFragment(R.id.container)
        if (fragment == null) {
            replace(R.id.container, FavoriteFragment.newInstance(), false)
        }

        bottomAppBar.replaceMenu(R.menu.navigation)
        fab.setOnClickListener {
            startActivity(SearchActivity.createIntent(this))
        }
    }

    override fun showVideo(video: FavoriteVideo) {
        // TODO 再生画面
    }
}
