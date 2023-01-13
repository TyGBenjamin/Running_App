package com.androiddevs.runningappyt.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.repositories.MainRepository
import com.androiddevs.runningappyt.testutils.CoroutinesTestExtention
import com.androiddevs.runningappyt.testutils.InstantExecutorExtension
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtention::class, InstantExecutorExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsViewModelTest {
    private val mockRepo = mockk<MainRepository>()
    private val testTotalTime: MutableLiveData<Long> = MutableLiveData(4_200_000_000)
    private val testTotalDistance = MutableLiveData(2000)
    private val testTotalCalories = MutableLiveData(2000)
    private val testTotalAvgSpeed = MutableLiveData(1.7f)
    private val testRunsSortedByDate = MutableLiveData(listOf(Run(id = 1), Run(id = 2)))

    @Test
    fun testGetTotalDistance() = runTest {
        every { mockRepo.getTotalDistance() } answers { testTotalDistance }
        every { mockRepo.getTotalTimeInMillis() } answers { testTotalTime }
        every { mockRepo.getTotalCaloriesBurned() } answers { testTotalCalories }
        every { mockRepo.getTotalAvgSpeed() } answers { testTotalAvgSpeed }
        every { mockRepo.getAllRunsSortedByDate() } answers { testRunsSortedByDate }
        val statisticsViewModel = StatisticsViewModel(mockRepo)
        Assertions.assertEquals(testTotalDistance.value, statisticsViewModel.totalDistance.value)
    }

    @Test
    fun testGetTotalTimeInMillis() = runTest {
        every { mockRepo.getTotalTimeInMillis() } answers { testTotalTime }
        every { mockRepo.getTotalDistance() } answers { testTotalDistance }
        every { mockRepo.getTotalCaloriesBurned() } answers { testTotalCalories }
        every { mockRepo.getTotalAvgSpeed() } answers { testTotalAvgSpeed }
        every { mockRepo.getAllRunsSortedByDate() } answers { testRunsSortedByDate }
        val statisticsViewModel = StatisticsViewModel(mockRepo)
        Assertions.assertEquals(testTotalTime.value, statisticsViewModel.totalTimeRun.value)
    }

    @Test
    fun testGetTotalCalories() = runTest {
        every { mockRepo.getTotalTimeInMillis() } answers { testTotalTime }
        every { mockRepo.getTotalDistance() } answers { testTotalDistance }
        every { mockRepo.getTotalCaloriesBurned() } answers { testTotalCalories }
        every { mockRepo.getTotalAvgSpeed() } answers { testTotalAvgSpeed }
        every { mockRepo.getAllRunsSortedByDate() } answers { testRunsSortedByDate }
        val statisticsViewModel = StatisticsViewModel(mockRepo)
        Assertions.assertEquals(testTotalCalories.value, statisticsViewModel.totalCaloriesBurned.value)
    }

    @Test
    fun testGetTotalAvgSpeed() = runTest {
        every { mockRepo.getTotalTimeInMillis() } answers { testTotalTime }
        every { mockRepo.getTotalDistance() } answers { testTotalDistance }
        every { mockRepo.getTotalCaloriesBurned() } answers { testTotalCalories }
        every { mockRepo.getTotalAvgSpeed() } answers { testTotalAvgSpeed }
        every { mockRepo.getAllRunsSortedByDate() } answers { testRunsSortedByDate }
        val statisticsViewModel = StatisticsViewModel(mockRepo)
        Assertions.assertEquals(testTotalAvgSpeed.value, statisticsViewModel.totalAvgSpeed.value)
    }

    @Test
    fun testRunsSortedByDate() = runTest {
        every { mockRepo.getTotalTimeInMillis() } answers { testTotalTime }
        every { mockRepo.getTotalDistance() } answers { testTotalDistance }
        every { mockRepo.getTotalCaloriesBurned() } answers { testTotalCalories }
        every { mockRepo.getTotalAvgSpeed() } answers { testTotalAvgSpeed }
        every { mockRepo.getAllRunsSortedByDate() } answers { testRunsSortedByDate }
        val statisticsViewModel = StatisticsViewModel(mockRepo)
        Assertions.assertEquals(testRunsSortedByDate.value, statisticsViewModel.runsSortedByDate.value)
    }
}
