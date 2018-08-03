package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.numero.itube.R
import com.numero.itube.extension.getAttrColor
import com.numero.itube.extension.getTintedDrawable
import com.numero.itube.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(),
        SettingsFragment.SettingsFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val colorOnPrimary = getAttrColor(R.attr.colorOnPrimary)
            val drawable = getTintedDrawable(R.drawable.ic_arrow_back, colorOnPrimary) ?: return
            setHomeAsUpIndicator(drawable)
        }
    }

    override fun showLicenses() {
        startActivity(LicensesActivity.createIntent(this))
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
        fun createIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}