package com.nvmt.android.mtlibrary.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.nvmt.android.mtlibrary.R


object TextUtil {
    fun createBoldText(text: String, startIndex: Int, endIndex: Int): SpannableString {
        val str = SpannableString(text)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return str
    }

    fun createBoldText(text: String?, taxtMakeBold: String?): SpannableString {
        if (text.isNullOrEmpty()) return SpannableString("")
        if (taxtMakeBold.isNullOrEmpty()) return SpannableString(text)

        val str = SpannableString(text)
        val startIndex = text.indexOf(taxtMakeBold)
        if (startIndex < 0) return str
        val endIndex = startIndex + taxtMakeBold.length
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return str
    }

    fun createColorText(text: String?, textMakeColor: String?, color: Int): SpannableString {
        if (text.isNullOrEmpty()) return SpannableString("")
        if (textMakeColor.isNullOrEmpty()) return SpannableString(text)

        val str = SpannableString(text)
        val startIndex = text.indexOf(textMakeColor)
        if (startIndex < 0) return str
        val endIndex = startIndex + textMakeColor.length
        str.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return str
    }

    fun switchHidePassword(context: Context, img: ImageView, edt: EditText) {
        if (edt.transformationMethod == null) {
            img.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.ic_hide_password)
            )
            edt.transformationMethod = PasswordTransformationMethod()
        } else {
            img.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.ic_show_password)
            )
            edt.transformationMethod = null
        }
    }
}