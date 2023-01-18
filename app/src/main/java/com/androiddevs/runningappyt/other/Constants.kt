package com.androiddevs.runningappyt.other

import android.graphics.Color

/**
 * Holds constant values for project.
 *
 * @constructor Create instance of [Constants]
 */
object Constants {

    const val RUNNING_DATABASE_NAME = "running_db"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val TIMER_UPDATE_INTERVAL = 50L

    const val SHARED_PREFERENCES_NAME = "sharedPref"
    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_WEIGHT = "KEY_WEIGHT"
    const val RADIO_DEFAULT_MODE = "RADIO_DEFAULT_MODE"
    const val RADIO_LIGHT_MODE = "RADIO_LIGHT_MODE"
    const val RADIO_DARK_MODE = "RADIO_DARK_MODE"
    const val USE_CHART_CUSTOM_COLOR = " USE_CHART_CUSTOM_COLOR"
    const val CHART_COLOR = "CHART_COLOR"
    const val BAR_COLOR = "BAR_COLOR"
    const val CHART_LINE_COLOR = "CHART_LINE_COLOR"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val DYPOS = 10
    val DYNEG = -10

    /**
     * Time related constants.
     *
     * @constructor Create instance of [Time]
     */
    object Time {
        const val MILLI_SEC = 1000L
    }

    /**
     * Default values.
     *
     * @constructor Create instance of [Default]
     */
    object Default {
        const val WEIGHT = 80f
    }

    /**
     * Float values.
     *
     * @constructor Create instance of [Float]
     */
    object Float {
        const val THOUSAND = 1000f
        const val TEN = 10f
    }
}
