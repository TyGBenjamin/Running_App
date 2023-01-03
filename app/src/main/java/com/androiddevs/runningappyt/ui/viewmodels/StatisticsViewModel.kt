package com.androiddevs.runningappyt.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.androiddevs.runningappyt.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] to manage all run statistics.
 *
 * @constructor Create instance of [StatisticsViewModel]
 *
 * @param mainRepository used to fetch and update runs
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(mainRepository: MainRepository) : ViewModel() {

    val totalTimeRun = mainRepository.getTotalTimeInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()
    val runsSortedByDate = mainRepository.getAllRunsSortedByDate()
}
