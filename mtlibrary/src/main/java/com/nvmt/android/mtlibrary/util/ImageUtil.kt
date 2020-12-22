package com.nvmt.android.mtlibrary.util

import android.R
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.widget.ImageView
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
    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        } catch (ex: Exception) {
            return null
        }
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


    fun convertImageToPartType(name: String, imageFile: File): MultipartBody.Part? {
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(imageFile.path))
        return MultipartBody.Part.createFormData(name, imageFile.name, fileReqBody)
    }

    fun convertImageToPartType(name: String, imageFile: Image): MultipartBody.Part? {
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("image/*"), File(imageFile.path))
        return MultipartBody.Part.createFormData(name, imageFile.name, fileReqBody)
    }

}