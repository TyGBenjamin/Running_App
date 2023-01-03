package com.androiddevs.runningappyt.other

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Extension function to display [Snackbar] using view.
 *
 * @param msg to be displayed
 * @param duration in time which message is displayed
 */
fun View.showSnack(msg: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, duration).show()
}
