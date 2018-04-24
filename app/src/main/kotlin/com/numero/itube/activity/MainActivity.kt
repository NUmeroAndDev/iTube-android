package com.numero.itube.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.SearchFragment
import com.numero.itube.model.Video

class MainActivity : AppCompatActivity(), SearchFragment.SearchFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = findFragment(R.id.container)
        if (fragment == null) {
            replace(R.id.container, SearchFragment.newInstance(), false)
        }
    }

    override fun showVideo(video: Video) {
        startActivity(PlayerActivity.createIntent(this, video))
    }
}
