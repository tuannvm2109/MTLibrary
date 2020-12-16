package com.nvmt.android.mtlibrary.util

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.NonNull

object ColorUtil {
    fun setColorFilter(@NonNull drawable: Drawable?, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}