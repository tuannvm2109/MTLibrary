package com.nvmt.android.mtlibrary.extension

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.nvmt.android.mtlibrary.R
import java.util.*

fun EditText.setOnSearchListener(listener: () -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEARCH
    isSingleLine = true
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            context.hideKeyboard(this)
            listener.invoke()
            true
        }
        false
    }
}

fun EditText.setForceOnlyUpperCaseAlphaBet() {
    this.addTextChangedListener {
        val str = it.toString()
        val tag = this.getTag(R.string.unique_tag)
        if (str == tag) return@addTextChangedListener
        val newStr = removeAccent(str)
        this.setTag(R.string.unique_tag, newStr)
        this.setText(newStr)
        this.setSelection(newStr.length)
    }
}

private val SOURCE_CHARACTERS = charArrayOf(
    'À', 'Á', 'Â', 'Ã', 'È', 'É',
    'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
    'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
    'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
    'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
    'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
    'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
    'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
    'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
    'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
    'ữ', 'Ự', 'ự', 'Ỳ', 'ỳ', 'Ỵ', 'ỵ', 'Ỷ', 'ỷ', 'Ỹ', 'ỹ',
)

private val DESTINATION_CHARACTERS = charArrayOf(
    'A', 'A', 'A', 'A', 'E', 'E',
    'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a',
    'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y',
    'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A',
    'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a',
    'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E',
    'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e',
    'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
    'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
    'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U',
    'u', 'U', 'u', 'Y', 'y', 'Y', 'y', 'Y', 'y', 'Y', 'y',
)

private fun removeAccent(ch: Char): Char {
    var ch = ch
    val index: Int = Arrays.binarySearch(SOURCE_CHARACTERS, ch)
    if (index >= 0) {
        ch = DESTINATION_CHARACTERS[index]
    }
    return ch
}

private fun removeAccent(str: CharSequence): String {
    val re = Regex("[^[^\\W_]\\d\\s]")
    val newStr = re.replace(str, "").toUpperCase(Locale.getDefault())
    val sb = StringBuilder(newStr)
    for (i in sb.indices) {
        sb.setCharAt(i, removeAccent(sb[i]))
    }
    return sb.toString()
}
