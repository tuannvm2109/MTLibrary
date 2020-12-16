package com.nvmt.android.mtlibrary.extension

import android.Manifest
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
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nvmt.android.mtlibrary.R


fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

//private val REQUEST_CODE = 321
//private val permissions =
//    listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
fun Activity.checkAndRequestPermission(listPermission: List<String>, requestCode: Int): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
    val listPermissionNotGranted = arrayListOf<String>()

    for (p in listPermission) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            listPermissionNotGranted.add(p)
        }
    }

    if (listPermissionNotGranted.isEmpty()) return true

    requestPermissions(
        listPermissionNotGranted.toTypedArray(),
        requestCode
    )
    return false
}

fun Activity.makePhoneCall(phone: String?) {
    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.CALL_PHONE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    if (response?.permissionName == Manifest.permission.CALL_PHONE) {
                        try {
                            val uris = Uri.parse("tel:$phone")
                            val intent = Intent(Intent.ACTION_CALL, uris)
                            startActivity(intent)
                        } catch (ex: Exception) {
                            ex.message?.let { toast(it) }
                        }
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response?.isPermanentlyDenied == true) {
                        showDialogConfirm(
                            content = "Ứng dụng cần quyền Gọi Điện để hoạt động chức năng này," +
                                    "Cấp lại quyền trong phần cài đặt ?",
                            positiveListener = {
                                startActivity(
                                    Intent(
                                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:$packageName")
                                    )
                                )
                            }
                        )
                    } else {
                        toast("Tính năng này sẽ không hoạt động cho tới khi bạn cấp quyền")
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .withErrorListener { toastError("Cấp quyền không thành công !") }
            .onSameThread()
            .check()
    } else {
        try {
            val uris = Uri.parse("tel:$phone")
            val intent = Intent(Intent.ACTION_CALL, uris)
            startActivity(intent)
        } catch (ex: Exception) {
            ex.message?.let { toast(it) }
        }
    }
}

fun Activity.makeDialCall(phone: String?) {
    try {
        val uris = Uri.parse("tel:$phone")
        val intent = Intent(Intent.ACTION_DIAL, uris)
        startActivity(intent)
    } catch (e: Exception) {
        toast("Không thể thực thực hiện cuộc gọi tới sđt này")
    }
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