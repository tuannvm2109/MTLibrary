package com.nvmt.android.mtlibrary.extension

import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt


fun Double.round(numberAfterDot: Int): Double {
    // Test đúng đến numberAfterDot < 8
    val x = Math.pow(10.0, numberAfterDot.toDouble())
    return (this * x).roundToInt() / x
}

fun Float.roundDownToHalf(): Float {
    return floor(this * 2) / 2
}

fun Float.roundUpToHalf(): Float {
    return ceil(this * 2) / 2
}

fun Double.roundDownToHalf(): Double {
    return floor(this * 2) / 2
}

fun Double.roundUpToHalf(): Double {
    return ceil(this * 2) / 2
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


