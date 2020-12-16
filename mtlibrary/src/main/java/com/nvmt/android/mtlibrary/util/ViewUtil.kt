package com.nvmt.android.mtlibrary.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Typeface
import android.util.Log

object ViewUtil {
    fun setCustomFont(ctx: Context, path: String?): Typeface? {
        var tf: Typeface? = null
        try {
            tf = Typeface.createFromAsset(ctx.assets, path)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Could not get typeface: " + e.message)
        }
        return tf
    }
}