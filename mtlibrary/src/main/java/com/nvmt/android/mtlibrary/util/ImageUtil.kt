package com.nvmt.android.mtlibrary.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import com.esafirm.imagepicker.model.Image
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


object ImageUtil {
    fun getBitmapFromPath(path: String?): Bitmap? {
        if (path == null) return null
        val bitmap = BitmapFactory.decodeFile(path)
        return modifyOrientation(bitmap, path)
    }

    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        return try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, 70, stream)
            stream.toByteArray()
        } catch (ex: Exception) {
            null
        }
    }

    private fun modifyOrientation(bitmap: Bitmap, path: String?): Bitmap? {
        if (path == null) return bitmap
        val ei = ExifInterface(path)
        val orientation: Int =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, true, false)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)
            else -> bitmap
        }
    }

    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
        val matrix = Matrix()
        matrix.preScale(if (horizontal) -1f else 1f, if (vertical) -1f else 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun renderBarcodeImg(text: String, barcodeFormat: BarcodeFormat): Bitmap? {
        val multiFormatWriter = MultiFormatWriter();
        try {
            val bitMatrix = multiFormatWriter.encode(text, barcodeFormat, 200, 80);
            val barcodeEncoder = BarcodeEncoder();
            val bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap
        } catch (e: WriterException) {
            return null
        }
    }

    fun renderQrCodeImg(text: String, barcodeFormat: BarcodeFormat): Bitmap? {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(text, barcodeFormat, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            return bmp
        } catch (e: WriterException) {
            return null
        }
    }


    fun convertImageToPartType(name: String, imageFile: File): MultipartBody.Part {
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(imageFile.path))
        return MultipartBody.Part.createFormData(name, imageFile.name, fileReqBody)
    }

    fun convertImageToPartType(name: String, imageFile: Image): MultipartBody.Part {
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(imageFile.path))
        return MultipartBody.Part.createFormData(name, imageFile.name, fileReqBody)
    }

    fun convertByteArrayToPartType(name: String, byteArray: ByteArray): MultipartBody.Part {
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), byteArray)
        return MultipartBody.Part.createFormData(name, byteArray.hashCode().toString(), fileReqBody)
    }
}