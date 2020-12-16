package com.nvmt.android.mtlibrary.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View


object CameraUtil {
    fun takeScreenShot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        val backgroundDrawable = view.background
        if (backgroundDrawable != null) {
            backgroundDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }
}