package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.getAttrColor
import com.numero.itube.extension.getTintedDrawable
import com.numero.itube.fragment.SettingsFragment
import com.numero.itube.repository.ConfigRepository
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(),
        SettingsFragment.SettingsFragmentListener {

    @Inject
    lateinit var configRepository: ConfigRepository
    private val isChangedTheme: Boolean by lazy { intent.getBooleanExtra(INTENT_CHANGED_THEME, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setTheme(configRepository.theme)
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

    override fun onChangedTheme() {
        startActivity(createClearTopIntent(this))
        overridePendingTransition(0, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (isChangedTheme) {
            //startActivity(TopActivity.createClearTopIntent(this))
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        private const val INTENT_CHANGED_THEME = "INTENT_CHANGED_THEME"

        fun createIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)

        fun createClearTopIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java).apply {
            putExtra(INTENT_CHANGED_THEME, true)
        }
    }
}