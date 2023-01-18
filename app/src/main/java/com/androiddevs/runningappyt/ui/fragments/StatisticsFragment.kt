@file:Suppress("ktlint:import-ordering")

package com.androiddevs.runningappyt.ui.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.databinding.FragmentStatisticsBinding
import com.androiddevs.runningappyt.other.Constants.BAR_COLOR
import com.androiddevs.runningappyt.other.Constants.CHART_COLOR
import com.androiddevs.runningappyt.other.Constants.CHART_LINE_COLOR
import com.androiddevs.runningappyt.other.Constants.Float.TEN
import com.androiddevs.runningappyt.other.Constants.Float.THOUSAND
import com.androiddevs.runningappyt.other.Constants.USE_CHART_CUSTOM_COLOR
import com.androiddevs.runningappyt.other.CustomMarkerView
import com.androiddevs.runningappyt.other.TrackingUtility
import com.androiddevs.runningappyt.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round
import java.util.Locale
import javax.inject.Inject

/**
 * [Fragment] to manage all run statistics.
 *
 * @constructor Create instance of [StatisticsFragment]
 */
@AndroidEntryPoint
class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    val binding: FragmentStatisticsBinding get() = _binding!!
    private val viewModel: StatisticsViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentStatisticsBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun setupBarChart() = with(binding) {
        var lineColor = Color.WHITE
        if (sharedPreferences.getBoolean(USE_CHART_CUSTOM_COLOR, false)) {
            val chartColor = sharedPreferences.getInt(CHART_COLOR, Color.BLACK)
            barChart.setBackgroundColor(chartColor)
            lineColor = sharedPreferences.getInt(CHART_LINE_COLOR, Color.WHITE)
        }
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = lineColor
            textColor = lineColor
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = lineColor
            textColor = lineColor
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = lineColor
            textColor = lineColor
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            description.textColor = if (lineColor == Color.BLACK) Color.WHITE else Color.BLACK
            legend.isEnabled = false
        }
    }

    private fun subscribeToObservers() = with(binding) {
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
                if (sharedPreferences.getBoolean(USE_CHART_CUSTOM_COLOR, false)) {
                    valueTextColor = sharedPreferences.getInt(CHART_LINE_COLOR, Color.WHITE)
                    color = sharedPreferences.getInt(BAR_COLOR, Color.WHITE)
                } else {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
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
