package com.nvmt.android.mtlibrary.extension

import com.nvmt.android.mtlibrary.base.MTConstant.DESTINATION_CHARACTERS
import com.nvmt.android.mtlibrary.base.MTConstant.SOURCE_CHARACTERS
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

fun removeAccent(ch: Char): Char {
    var ch = ch
    val index: Int = Arrays.binarySearch(SOURCE_CHARACTERS, ch)
    if (index >= 0) {
        ch = DESTINATION_CHARACTERS[index]
    }
    return ch
}

fun removeAccent(str: CharSequence): String {
    val re = Regex("[^[^\\W_]\\d\\s]")
    val newStr = re.replace(str, "").toUpperCase(Locale.getDefault())
    val sb = StringBuilder(newStr)
    for (i in sb.indices) {
        sb.setCharAt(i, removeAccent(sb[i]))
    }
    return sb.toString()
}

fun String.equalVietnamese(anotherStr: String?, ignoreCase: Boolean = false): Boolean {
    if (anotherStr == null) return false
    val newStr1 = removeAccent(this)
    val newStr2 = removeAccent(anotherStr)
    return newStr1.equals(newStr2, ignoreCase = ignoreCase)
}
