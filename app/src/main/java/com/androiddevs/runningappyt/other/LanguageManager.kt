package com.androiddevs.runningappyt.other

import android.content.Context
import java.util.*

/**
 * Class which handles language change.
 * @function updateResource receives language code and updates configuration.
 * */

@SuppressWarnings("deprecation")
class LanguageManager(private val ct: Context) {
    /**
     *@function updateResource receives language code and updates configuration
     * */
    fun updateResource(code: String?) {
        val locale = Locale(code)
        Locale.setDefault(locale)
        val resources = ct.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}
