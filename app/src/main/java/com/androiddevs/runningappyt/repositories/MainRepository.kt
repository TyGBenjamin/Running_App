package com.androiddevs.runningappyt.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.db.RunDAO
import javax.inject.Inject

/**
 * Repository class to handle run related queries.
 *
 * @property runDao used to query database for run objects.
 * @constructor Create instance of [MainRepository]
 */
@Suppress("TooManyFunctions")
class MainRepository @Inject constructor(
    private val runDao: RunDAO
) {
    /**
     * Inserts [Run] into the db for persistence.
     *
     * @param run to be persisted
     */
    suspend fun insertRun(run: Run) = runDao.insertRun(run)

    /**
     * Deletes [Run] from the db.
     *
     * @param run to be deleted
     */
    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    /**
     * Get run by id.
     *
     * @param id
     */
    suspend fun getRunById(id: Int) = runDao.getRunById(id)

    /**
     * Get all runs sorted by date.
     *
     * @return [LiveData] with [List] of [Run]
     */
    fun getAllRunsSortedByDate(): LiveData<List<Run>> {
        return runDao.getAllRunsSortedByDate()
    }

    /**
     * Get all runs sorted by distance.
     *
     * @return [LiveData] with [List] of [Run]
     */
    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()

    /**
     * Get all runs sorted by time in millis.
     *
     * @return [LiveData] with [List] of [Run]
     */
    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTimeInMillis()

    /**
     * Get all runs sorted by avg speed.
     *
     * @return [LiveData] with [List] of [Run]
     */
    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    /**
     * Get all runs sorted by calories burned.
     *
     * @return [LiveData] with [List] of [Run]
     */
    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    /**
     * Get total avg speed.
     *
     * @return [LiveData] with [Float]
     */
    fun getTotalAvgSpeed(): LiveData<Float> = runDao.getTotalAvgSpeed()

    /**
     * Get total distance.
     *
     * @return [LiveData] with [Int]
     */
    fun getTotalDistance(): LiveData<Int> = runDao.getTotalDistance()

    /**
     * Get total calories burned.
     *
     * @return [LiveData] with [Int]
     */
    fun getTotalCaloriesBurned(): LiveData<Int> = runDao.getTotalCaloriesBurned()

    /**
     * Get total time in millis.
     *
     * @return [LiveData] with [Long]
     */
    fun getTotalTimeInMillis(): LiveData<Long> = runDao.getTotalTimeInMillis()
}
