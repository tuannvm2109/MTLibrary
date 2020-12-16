package com.nvmt.android.mtlibrary.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.ByteArrayOutputStream


object ImageUtil {
    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        } catch (ex : Exception) {
            return null
        }
    }

}