package com.nvmt.android.mtlibrary.util

import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import okhttp3.ResponseBody
import java.io.*


object FileUtil {
    //Lưu vào bộ nhớ ngoài (Các app khác như gallery có thể thấy, khi app uninstall thì k bị xoá)
    fun saveImageExternalDirectory(bitmap: Bitmap?, context: Context?): Uri? {
        if (bitmap == null || context == null) return null
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        val appName =
            if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
                stringId
            )

        if (Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$appName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
            return uri
        } else {
            val directory = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + appName
            )
            // getExternalStorageDirectory is deprecated in API 29

            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)

            if (!directory.exists()) {
                directory.mkdirs()
            }
            if (!file.exists())
                file.createNewFile();

            saveImageToStream(bitmap, FileOutputStream(file))
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            return Uri.fromFile(file)
        }
    }

    //Lưu vào bộ nhớ ngoài những vẫn nội bộ của app
    // (Người dùng và app khách không thể xem dc. Khi app uninstall thì sẽ bị xoá luôn)
    // Return Uri để share cho app khác có thể xem được
    fun saveImageExternalDir(bitmap: Bitmap?, context: Context?): Uri? {
        if (bitmap == null || context == null) return null
        val cw = ContextWrapper(context)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val directory = cw.cacheDir

        // Create imageDir
        val fileName = System.currentTimeMillis().toString() + ".jpeg"
        val file = File(directory, fileName)

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return FileProvider.getUriForFile(
            context,
            "com.esmac.android.appnote.provider",
            file
        )
//        return file
    }

    private fun contentValues(): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveFile(
        input: InputStream?,
        path: String,
        filename: String
    ): String {
        if (input == null)
            return ""
        try {
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs();
            }
            val fos = FileOutputStream(path + File.separator + filename)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return path
        } catch (e: Exception) {
            Log.e("saveFile", e.toString())
        } finally {
            input?.close()
        }
        return ""
    }

    fun getFileName(context: Context, uri: Uri): String {
        try {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor =
                    context.contentResolver.query(uri, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor?.close()
                }
            }
            if (result == null) {
                result = File(uri.path).name ?: "Unknown File Name"
            }
            return result
        } catch (e: Exception) {
            return "Unknown File Name"
        }
    }

}