package com.androiddevs.runningappyt.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.db.RunDAO
import com.androiddevs.runningappyt.db.RunningDatabase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

internal class MainRepositoryWriteDeleteTest {

    private lateinit var dao: RunDAO
    private lateinit var db: RunningDatabase
    private val repo = MainRepository(dao)

    @BeforeEach
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            RunningDatabase::class.java
        ).build()
        dao = db.getRunDao()
    }

    @AfterEach
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertRun() {
        val run = Run(id = 1)
        repo.insertRun(run)
        Assertions.assertEquals(run, repo.getAllRunsSortedByDate().value?.first())
    }
}
