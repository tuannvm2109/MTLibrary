package com.nvmt.android.mtlibrary.util

import android.location.Location
import com.nvmt.android.mtlibrary.extension.round

object MapUtil {
    fun calculateDistance(lat1: Double?, lon1: Double?, lat2: Double?, lon2: Double?): String {
        if (lat1 == null || lat2 == null || lon1 == null || lon2 == null) return ""
        try {
            val results = FloatArray(1)
            Location.distanceBetween(lat1, lon1, lat2, lon2, results);
            val distance = (results[0].toDouble() / 1000).round(1)
            return "$distance km"
        } catch (e: Exception) {
            return ""
        }
    }
}