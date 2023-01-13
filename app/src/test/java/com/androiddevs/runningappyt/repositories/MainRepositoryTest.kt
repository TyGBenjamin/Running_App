package com.androiddevs.runningappyt.repositories

import androidx.lifecycle.MutableLiveData
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.db.RunDAO
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal open class MainRepositoryTest {

    private val mockDao = mockk<RunDAO>()
    private val repo = MainRepository(mockDao)

    @Test
    fun testGetAllRunsSortedByDate() = runTest {
        val testList = MutableLiveData(listOf(Run(id = 1, timeInMillis = 1000), Run(id = 2, timeInMillis = 2000)))
        every { mockDao.getAllRunsSortedByDate() } answers { testList }

        Assertions.assertEquals(testList, repo.getAllRunsSortedByDate())
    }

    @Test
    fun testGetAllRunsSortedByDistance() = runTest {
        val testList = MutableLiveData(listOf(Run(id = 1, timeInMillis = 1000), Run(id = 2, timeInMillis = 2000)))
        every { mockDao.getAllRunsSortedByDistance() } answers { testList }

        Assertions.assertEquals(testList, repo.getAllRunsSortedByDistance())
    }

    @Test
    fun testGetAllRunsSortedByTimeInMillis() = runTest {
        val testList = MutableLiveData(listOf(Run(id = 1, timeInMillis = 1000), Run(id = 2, timeInMillis = 2000)))
        every { mockDao.getAllRunsSortedByTimeInMillis() } answers { testList }

        Assertions.assertEquals(testList, repo.getAllRunsSortedByTimeInMillis())
    }

    @Test
    fun testGetAllRunsSortedByAvgSpeed() = runTest {
        val testList = MutableLiveData(listOf(Run(id = 1, timeInMillis = 1000), Run(id = 2, timeInMillis = 2000)))
        every { mockDao.getAllRunsSortedByAvgSpeed() } answers { testList }

        Assertions.assertEquals(testList, repo.getAllRunsSortedByAvgSpeed())
    }

    @Test
    fun testGetAllRunsSortedByCalsBurned() = runTest {
        val testList = MutableLiveData(listOf(Run(id = 1, timeInMillis = 1000), Run(id = 2, timeInMillis = 2000)))
        every { mockDao.getAllRunsSortedByCaloriesBurned() } answers { testList }

        Assertions.assertEquals(testList, repo.getAllRunsSortedByCaloriesBurned())
    }

    @Test
    fun testGetAvgSpeed() = runTest {
        val testTotalAvgSpeed = MutableLiveData(1.7f)
        every { mockDao.getTotalAvgSpeed() } answers { testTotalAvgSpeed }

        Assertions.assertEquals(testTotalAvgSpeed, repo.getTotalAvgSpeed())
    }

    @Test
    fun testGetTotalDistance() = runTest {
        val testTotalDistance = MutableLiveData(2000)
        every { mockDao.getTotalDistance() } answers { testTotalDistance }

        Assertions.assertEquals(testTotalDistance, repo.getTotalDistance())
    }

    @Test
    fun testGetTotalCalBurned() = runTest {
        val testTotalCalories = MutableLiveData(2000)
        every { mockDao.getTotalCaloriesBurned() } answers { testTotalCalories }

        Assertions.assertEquals(testTotalCalories, repo.getTotalCaloriesBurned())
    }

    @Test
    fun testGetTotalTime() = runTest {
        val testTotalTime = MutableLiveData(2000L)
        every { mockDao.getTotalTimeInMillis() } answers { testTotalTime }

        Assertions.assertEquals(testTotalTime, repo.getTotalTimeInMillis())
    }
}
