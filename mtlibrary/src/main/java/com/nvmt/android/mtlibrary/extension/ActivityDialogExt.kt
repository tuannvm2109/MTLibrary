package com.nvmt.android.mtlibrary.extension

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.util.dialog.MultiChoiceDialog

fun Activity.showDialogList(
    title: String? = "",
    data: List<String>?,
    listener: ((Int) -> Unit)?
) {
    val dialog = AlertDialog.Builder(this).setTitle(title)
    if (data != null && data.isNotEmpty()) {
        dialog.setItems(data.toTypedArray()) { _, position ->
            listener?.invoke(position)
        }
    } else dialog.setMessage(getString(R.string.txt_no_data))
    dialog.show()
}


fun Activity.showDialogListAnchor(
    anchorView: View,
    data: List<String?>,
    listener: (Int) -> Unit
) {
    if (isFinishing) return

    hideKeyboard()
    val listPopupWindow =
        ListPopupWindow(this)
    if (data.size >= 5) {
        listPopupWindow.height = 500
    } else listPopupWindow.height = ListPopupWindow.WRAP_CONTENT
    listPopupWindow.width = anchorView.width
    listPopupWindow.anchorView = anchorView
    val arrayAdapterStatus = ArrayAdapter<String?>(
        this,
        R.layout.support_simple_spinner_dropdown_item,
        data
    )
    listPopupWindow.setAdapter(arrayAdapterStatus)
    listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
        if (position < 0 || position >= data.size) return@setOnItemClickListener
        listener(position)
        listPopupWindow.dismiss()
    }
    listPopupWindow.show()
}

fun <T> Activity.showDialogListMultiChoice(
    title: String = "",
    data: List<T>?,
    listener: ((List<T>) -> Unit)? = null,
    getItemLabel: ((T?) -> String),
    maxItemChecked: Int? = 99,
    minItemChecked: Int? = 1,
) {
    if (data.isNullOrEmpty()) {
        showDialogAlert(getString(R.string.txt_no_data))
        return
    }

    val builder = MultiChoiceDialog.Builder<T>()
        .positiveString(getString(R.string.txt_ok))
        .negativeString(getString(R.string.txt_cancel))
        .maxItemChecked(maxItemChecked ?: 1)
        .minItemChecked(minItemChecked ?: 1)
        .itemList(data)

    builder.title(title)
        .iOnItemChoose(object : MultiChoiceDialog.IOnItemChoose<T> {
            override fun onMultiItemChoosed(t: List<T>) {
                listener?.invoke(t)
            }

            override fun itemToLabel(t: T?): String {
                return getItemLabel(t)
            }

        })
    builder.build().show(this)
}
