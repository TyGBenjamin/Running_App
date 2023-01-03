package com.androiddevs.runningappyt.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

/**
 * Converter for complex objects in [RunningDatabase].
 *
 * @constructor Create instance of [Converters]
 */
class Converters {

    /**
     * Converts [ByteArray] to [Bitmap].
     *
     * @param bytes to be converted
     * @return decoded [Bitmap]
     */
    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * Converts [Bitmap] to [ByteArray].
     *
     * @param bmp to be converted
     * @return [ByteArray]
     */
    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, outputStream)
        return outputStream.toByteArray()
    }

    companion object {
        private const val PNG_QUALITY = 100
    }
}
