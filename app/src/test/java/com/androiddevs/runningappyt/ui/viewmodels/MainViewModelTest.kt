package com.androiddevs.runningappyt.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.SortType
import com.androiddevs.runningappyt.repositories.MainRepository
import com.androiddevs.runningappyt.testutils.CoroutinesTestExtention
import com.androiddevs.runningappyt.testutils.InstantExecutorExtension
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtention::class, InstantExecutorExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {
    private val mockRepo = mockk<MainRepository>()

    @Test
    fun testRunsSortedByDate() = runTest {
        val testList = listOf(Run(id = 1), Run(id = 2))
        coEvery { mockRepo.getAllRunsSortedByDate() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByDistance() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByCaloriesBurned() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByTimeInMillis() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByAvgSpeed() } coAnswers { MutableLiveData(testList) }

        val mainViewModel = MainViewModel(mockRepo)
        mainViewModel.runs.observeForever {}
        mainViewModel.sortRuns(SortType.DATE)

        Assertions.assertEquals(testList, mainViewModel.runs.value)
    }

    @Test
    fun testRunsSortedByDistance() = runTest {
        val testList =
            listOf(Run(id = 1, distanceInMeters = 200), Run(id = 2, distanceInMeters = 300))
        coEvery { mockRepo.getAllRunsSortedByDate() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByDistance() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByCaloriesBurned() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByTimeInMillis() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByAvgSpeed() } coAnswers { MutableLiveData(testList) }

        val mainViewModel = MainViewModel(mockRepo)
        mainViewModel.sortRuns(SortType.DISTANCE)
        mainViewModel.runs.observeForever {}

        Assertions.assertEquals(testList, mainViewModel.runs.value)
    }

    @Test
    fun testRunsSortedByCaloriesBurned() = runTest {
        val testList = listOf(Run(id = 1, caloriesBurned = 200), Run(id = 2, caloriesBurned = 300))
        coEvery { mockRepo.getAllRunsSortedByDate() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByDistance() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByCaloriesBurned() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByTimeInMillis() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByAvgSpeed() } coAnswers { MutableLiveData(testList) }

        val mainViewModel = MainViewModel(mockRepo)
        mainViewModel.sortRuns(SortType.CALORIES_BURNED)
        mainViewModel.runs.observeForever {}
        Assertions.assertEquals(testList, mainViewModel.runs.value)
    }

    @Test
    fun testRunsSortedByTimeInMillis() = runTest {
        val testList = listOf(Run(id = 1, timeInMillis = 1000), Run(id = 2, timeInMillis = 2000))
        coEvery { mockRepo.getAllRunsSortedByDate() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByDistance() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByCaloriesBurned() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByTimeInMillis() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByAvgSpeed() } coAnswers { MutableLiveData(testList) }

        val mainViewModel = MainViewModel(mockRepo)
        mainViewModel.sortRuns(SortType.RUNNING_TIME)
        mainViewModel.runs.observeForever {}
        Assertions.assertEquals(testList, mainViewModel.runs.value)
    }

    @Test
    fun testRunsSortedByAverageSpeed() = runTest {
        val testList = listOf(Run(id = 1, avgSpeedInKMH = 1.3f), Run(id = 2, avgSpeedInKMH = 1.5f))
        coEvery { mockRepo.getAllRunsSortedByDate() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByDistance() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByCaloriesBurned() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByTimeInMillis() } coAnswers { MutableLiveData(testList) }
        coEvery { mockRepo.getAllRunsSortedByAvgSpeed() } coAnswers { MutableLiveData(testList) }

        val mainViewModel = MainViewModel(mockRepo)
        mainViewModel.sortRuns(SortType.AVG_SPEED)
        mainViewModel.runs.observeForever {}
        Assertions.assertEquals(testList, mainViewModel.runs.value)
    }
}
