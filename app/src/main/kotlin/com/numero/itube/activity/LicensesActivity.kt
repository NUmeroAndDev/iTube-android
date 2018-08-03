package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.numero.itube.R
import com.numero.itube.extension.getAttrColor
import com.numero.itube.extension.getTintedDrawable
import kotlinx.android.synthetic.main.activity_licenses.*

class LicensesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licenses)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val colorOnPrimary = getAttrColor(R.attr.colorOnPrimary)
            val drawable = getTintedDrawable(R.drawable.ic_arrow_back, colorOnPrimary) ?: return
            setHomeAsUpIndicator(drawable)
        }

        webView.apply {
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            loadUrl(LICENSES_HTML_PATH)
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

        private const val LICENSES_HTML_PATH = "file:///android_asset/licenses.html"

        fun createIntent(context: Context): Intent = Intent(context, LicensesActivity::class.java)
    }
}