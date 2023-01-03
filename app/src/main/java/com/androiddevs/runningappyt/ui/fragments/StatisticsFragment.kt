@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.other.Constants.Float.TEN
import com.androiddevs.runningappyt.other.Constants.Float.THOUSAND
import com.androiddevs.runningappyt.other.CustomMarkerView
import com.androiddevs.runningappyt.other.TrackingUtility
import com.androiddevs.runningappyt.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import kotlin.math.round
import kotlinx.android.synthetic.main.fragment_statistics.barChart
import kotlinx.android.synthetic.main.fragment_statistics.tvAverageSpeed
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalCalories
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalDistance
import kotlinx.android.synthetic.main.fragment_statistics.tvTotalTime

/**
 * [Fragment] to manage all run statistics.
 *
 * @constructor Create instance of [StatisticsFragment]
 */
@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun setupBarChart() {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    private fun subscribeToObservers() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner) { totalTimeRun: Long ->
            tvTotalTime.text = TrackingUtility.getFormattedStopWatchTime(totalTimeRun)
        }
        viewModel.totalDistance.observe(viewLifecycleOwner) { totalDistance: Int ->
            val km = totalDistance / THOUSAND
            val distance: Float = round(km * TEN) / TEN
            tvTotalDistance.text = String.format(Locale.getDefault(), "%,.1f km", distance)
        }
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner) { totalAvgSpeed: Float ->
            val avgSpeed: Float = round(totalAvgSpeed * TEN) / TEN
            tvAverageSpeed.text = String.format(Locale.getDefault(), "%,.1f km/h", avgSpeed)
        }
        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner) { totalCaloriesBurned: Int ->
            tvTotalCalories.text =
                String.format(Locale.getDefault(), "%d kcal", totalCaloriesBurned)
        }
        viewModel.runsSortedByDate.observe(viewLifecycleOwner) { sortedRunList ->
            val allAvgSpeeds = sortedRunList.indices
                .map { i -> BarEntry(i.toFloat(), sortedRunList[i].avgSpeedInKMH) }
            val barDataSet = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply {
                valueTextColor = Color.WHITE
                color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
            }
            barChart.data = BarData(barDataSet)
            barChart.marker = CustomMarkerView(
                runs = sortedRunList.reversed(),
                context = requireContext(),
                layoutId = R.layout.marker_view
            )
            barChart.invalidate()
        }
    }
}
