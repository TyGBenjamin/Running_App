package com.androiddevs.runningappyt.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val img: Bitmap? = null,
    val timestamp: Long = 0L,
    val avgSpeedInKMH: Float = 0f,
    val distanceInMeters: Int = 0,
    val timeInMillis: Long = 0L,
    val caloriesBurned: Int = 0
)
