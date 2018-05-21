package com.numero.itube.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.FavoriteFragment
import com.numero.itube.fragment.SearchFragment
import com.numero.itube.model.Video
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        SearchFragment.SearchFragmentListener,
        FavoriteFragment.FavoriteFragmentListener,
        Toolbar.OnMenuItemClickListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            val fragment = findFragment(R.id.searchContainer)
                            if (fragment is SearchFragment) {
                                fragment.clearSearching()
                            }
                        }
                    }
                }
            })
        }

        if (findFragment(R.id.container) == null) {
            replace(R.id.container, FavoriteFragment.newInstance(), false)
        }
        if (findFragment(R.id.searchContainer) == null) {
            replace(R.id.searchContainer, SearchFragment.newInstance(), false)
        }

        bottomAppBar.apply {
            replaceMenu(R.menu.menu_main)
            setOnMenuItemClickListener(this@MainActivity)
        }
        fab.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        item ?: return false
        when (item.itemId) {
            R.id.action_settings -> startActivity(SettingsActivity.createIntent(this))
        }
        return true
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            return
        }
        super.onBackPressed()
    }

    override fun showVideo(video: Video) {
        startActivity(PlayerActivity.createIntent(this, video))
    }

    override fun showVideo(video: FavoriteVideo) {
        startActivity(PlayerActivity.createIntent(this, video))
    }
}
