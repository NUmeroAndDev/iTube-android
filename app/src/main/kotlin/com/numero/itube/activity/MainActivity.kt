package com.numero.itube.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.FavoriteVideoListFragment
import com.numero.itube.fragment.SettingsBottomSheetFragment
import com.numero.itube.fragment.SettingsFragment
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        FavoriteVideoListFragment.FavoriteFragmentListener,
        SettingsFragment.SettingsFragmentListener,
        Toolbar.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (findFragment(R.id.container) == null) {
            replace(R.id.container, FavoriteVideoListFragment.newInstance(), false)
        }

        bottomAppBar.apply {
            replaceMenu(R.menu.menu_main)
            setOnMenuItemClickListener(this@MainActivity)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        item ?: return false
        when (item.itemId) {
            R.id.action_settings -> {
                SettingsBottomSheetFragment.newInstance().show(supportFragmentManager)
            }
            R.id.action_search -> {
                startActivity(SearchActivity.createIntent(this))
            }
        }
        return true
    }

    override fun showLicenses() {
        startActivity(LicensesActivity.createIntent(this))
    }

    override fun showVideo(video: FavoriteVideo) {
        startActivity(PlayerActivity.createIntent(this, video))
    }
}
