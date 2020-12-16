package com.nvmt.android.mtlibrary.extension

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.nvmt.android.mtlibrary.R

fun Context?.showDialogAlert(msg: String?, positiveListener: (() -> Unit)? = null) {
    if (this == null) return
    if (this is Activity && isFinishing) return
    if (this is Fragment && context == null) return

    AlertDialog.Builder(this)
        .setMessage(msg)
        .setPositiveButton(getString(R.string.txt_ok)) { _, _ ->
            positiveListener?.invoke()
        }
        .setCancelable(false)
        .show()
}

fun Context?.showDialogConfirm(
    title: String? = null,
    content: String,
    positiveListener: (() -> Unit)? = null,
    negativeListener: (() -> Unit)? = null
) {
    if (this == null) return
    if (this is Activity && isFinishing) return
    if (this is Fragment && context == null) return

    lateinit var dialog: AlertDialog
    val builder = AlertDialog.Builder(this)
    if (title != null) builder.setTitle(title)
    builder.setMessage(content)
    builder.setPositiveButton(getString(R.string.txt_ok)) { _, _ ->
        positiveListener?.invoke()
    }
    builder.setNegativeButton(getString(R.string.txt_cancel)) { _, _ ->
        negativeListener?.invoke()
    }
//    builder.setNeutralButton("CANCEL",dialogClickListener)
    dialog = builder.create()
    dialog.show()
}
