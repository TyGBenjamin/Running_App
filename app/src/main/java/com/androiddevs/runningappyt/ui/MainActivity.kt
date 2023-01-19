@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.databinding.ActivityMainBinding
import com.androiddevs.runningappyt.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.androiddevs.runningappyt.other.Constants.RADIO_DARK_MODE
import com.androiddevs.runningappyt.other.Constants.RADIO_ENGLISH
import com.androiddevs.runningappyt.other.Constants.RADIO_LIGHT_MODE
import com.androiddevs.runningappyt.other.Constants.RADIO_SPANISH
import com.androiddevs.runningappyt.other.LanguageManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.navHostFragment

/**
 * [AppCompatActivity] to host toolbar, bottom navigation and fragments.
 *
 * @constructor Create instance of [MainActivity]
 */
@AndroidEntryPoint
@Suppress("LateinitUsage")
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightMode = sharedPreferences.getBoolean(RADIO_LIGHT_MODE, false)
        val darkMode = sharedPreferences.getBoolean(RADIO_DARK_MODE, false)
        val english = sharedPreferences.getBoolean(RADIO_ENGLISH, false)
        val spanish = sharedPreferences.getBoolean(RADIO_SPANISH, false)
        var languageManager = LanguageManager(this)

        if (english) {
            languageManager.updateResource("en")
        } else if (spanish) {
            languageManager.updateResource("es")
        }

        if (darkMode) {
            setTheme(R.style.DarkTheme)
        } else if (lightMode) {
            setTheme(R.style.LightTheme)
        } else {
            when (applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.DarkTheme)
                Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.LightTheme)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateToTrackingFragmentIfNeeded(intent)

        setSupportActionBar(binding.toolbar)
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment ->
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController()
                .navigate(R.id.action_global_trackingFragment)
        }
    }
}
