package com.nvmt.android.mtlibrary.extension

import java.text.NumberFormat
import kotlin.math.roundToInt


fun Double.round(numberAfterDot: Int): Double {
    // Test đúng đến numberAfterDot < 8
    val x = Math.pow(10.0, numberAfterDot.toDouble())
    return (this * x).roundToInt() / x
}

fun Int.toAtLeastTwoDigit(): String {
    return if (this <= 9) "0$this" else this.toString()
}

val Int.toHumanReadable: String
    get() = NumberFormat.getIntegerInstance().format(this)

val String.toHumanReadable: String
    get() = NumberFormat.getIntegerInstance().format(this)

val Long.toHumanReadable: String
    get() = NumberFormat.getIntegerInstance().format(this)


