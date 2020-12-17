package com.nvmt.android.mtlibrary.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.provider.SyncStateContract
import com.esafirm.imagepicker.model.Image
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


object ImageUtil {
    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        } catch (ex: Exception) {
            return null
        }
    }

    fun convertImageToPartType(imageFile: File): MultipartBody.Part? {
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(imageFile.path))
        return MultipartBody.Part.createFormData("photo", imageFile.name, fileReqBody)
    }

    fun convertImageToPartType(imageFile: Image): MultipartBody.Part? {
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(imageFile.path))
        return MultipartBody.Part.createFormData("photo", imageFile.name, fileReqBody)
    }

}