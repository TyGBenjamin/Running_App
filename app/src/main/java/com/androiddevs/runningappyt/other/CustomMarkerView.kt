@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.other

import android.annotation.SuppressLint
import android.content.Context
import com.androiddevs.runningappyt.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.android.synthetic.main.marker_view.view.tvAvgSpeed
import kotlinx.android.synthetic.main.marker_view.view.tvCaloriesBurned
import kotlinx.android.synthetic.main.marker_view.view.tvDate
import kotlinx.android.synthetic.main.marker_view.view.tvDistance
import kotlinx.android.synthetic.main.marker_view.view.tvDuration

/**
 * Custom [MarkerView].
 *
 * @property runs to create custom markers for
 * @constructor Create instance of [CustomMarkerView]
 *
 * @param context used for view inflation
 * @param layoutId that references custom view xml
 */
@SuppressLint("ViewConstructor")
class CustomMarkerView(
    val runs: List<Run>,
    context: Context,
    layoutId: Int
) : MarkerView(context, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        tvDate.text = dateFormat.format(calendar.time)

        val avgSpeed = "${run.avgSpeedInKMH}km/h"
        tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${run.distanceInMeters / Constants.Float.THOUSAND}km"
        tvDistance.text = distanceInKm

        tvDuration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned
    }
}
