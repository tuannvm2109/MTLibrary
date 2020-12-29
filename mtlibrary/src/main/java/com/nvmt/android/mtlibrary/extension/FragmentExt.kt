package com.nvmt.android.mtlibrary.extension

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nvmt.android.mtlibrary.R

fun Fragment.toast(msg: String) {
    try {
        if (context == null) return
        context!!.toast(msg)
    } catch (ex: Exception) {

    }
}

fun Fragment.toastError(msg: String) {
    try {
        if (context == null) return
        context!!.toastError(msg)
    } catch (ex: Exception) {

    }
}

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: String, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.checkPermissionIsGranted(listPermission: List<String>): Boolean {
    return requireActivity().checkPermissionIsGranted(listPermission)
}

fun Fragment.checkPermissionAndHandle(
    listPermission: List<String>,
    listener: (() -> Unit)?
) {
    requireActivity().checkPermissionAndHandle(listPermission, listener)
}

fun Fragment.makePhoneCall(phone: String?) {
    requireActivity().makePhoneCall(phone)
}

fun Fragment.makeDialCall(phone: String?) {
    try {
        val uris = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_DIAL, uris)
        startActivity(intent)
    } catch (e: Exception) {
        toast("Không thể thực thực hiện cuộc gọi tới sđt này")
    }
}

fun Fragment?.showDialogListAnchor(
    anchorView: View,
    data: List<String?>,
    listener: (Int) -> Unit
) {
    if (this?.context == null) return

    hideKeyboard()
    val listPopupWindow =
        ListPopupWindow(requireContext())
    listPopupWindow.width = anchorView.width
    listPopupWindow.anchorView = anchorView
    val arrayAdapterStatus = ArrayAdapter<String?>(
        requireContext(),
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