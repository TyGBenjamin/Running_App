@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.other

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.androiddevs.runningappyt.services.Polyline
import java.util.concurrent.TimeUnit
import pub.devrel.easypermissions.EasyPermissions

/**
 * Utility class to handle run tracking functions.
 *
 * @constructor Create instance of [TrackingUtility]
 */
object TrackingUtility {

    private const val TEN = 10

    /**
     * Checks if required Location permissions are granted.
     *
     * @param context used to check for permissions
     * @return true if permissions are granted otherwise false
     */
    fun hasLocationPermissions(context: Context): Boolean =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    /**
     * Calculate length of polyline.
     *
     * @param polyline to calculate
     * @return length of polyline
     */
    fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f
        for (i in 0..polyline.size - 2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    /**
     * Formats [Long] time into a friendly readable [String] format.
     *
     * @param ms time to be converted
     * @param includeMillis default false
     * @return formatted time
     */
    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        val friendlyHrsFormat = "${if (hours < TEN) "0" else ""}$hours"
        val friendlyMinutesFormat = "${if (minutes < TEN) "0" else ""}$minutes"
        val friendlySecondsFormat = "${if (seconds < TEN) "0" else ""}$seconds"

        return if (!includeMillis) {
            "$friendlyHrsFormat:$friendlyMinutesFormat:$friendlySecondsFormat"
        } else {
            milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
            milliseconds /= TEN
            val friendlyMilliSecondsFormat = "${if (milliseconds < TEN) "0" else ""}$milliseconds"
            "$friendlyHrsFormat:$friendlyMinutesFormat:$friendlySecondsFormat:$friendlyMilliSecondsFormat"
        }
    }
}
