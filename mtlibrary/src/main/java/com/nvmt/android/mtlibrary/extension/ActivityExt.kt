package com.nvmt.android.mtlibrary.extension

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.nvmt.android.mtlibrary.R


fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

//private val REQUEST_CODE = 321
//private val permissions =
//    listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
fun Activity.checkPermissionIsGranted(listPermission: List<String>): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
    val listPermissionNotGranted = arrayListOf<String>()

    for (p in listPermission) {
        if (ActivityCompat.checkSelfPermission(
                this,
                p
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            listPermissionNotGranted.add(p)
        }
    }

    return listPermissionNotGranted.isEmpty()
}

fun Activity.checkPermissionAndHandle(
    listPermission: List<String>,
    listener: (() -> Unit)?
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        listener?.invoke()
        return
    }
    val listPermissionNotGranted = arrayListOf<String>()

    for (p in listPermission) {
        if (ActivityCompat.checkSelfPermission(
                this,
                p
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            listPermissionNotGranted.add(p)
        }
    }

    if (listPermissionNotGranted.isEmpty()) listener?.invoke()
    else {
        Dexter.withActivity(this)
            .withPermissions(listPermissionNotGranted)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report == null) {
                        toastError("Cấp quyền không thành công !")
                        return
                    }
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        listener?.invoke()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showDialogConfirm(
                            content = "Ứng dụng cần cấp quyền để hoạt động chức năng này," +
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
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .withErrorListener { toastError("Cấp quyền không thành công !") }
            .onSameThread()
            .check()
    }
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


fun Activity.setupStatusBarAndNavigationBar() {
    // translucent status bar but not navigation bar (after test, some device is not work :(( )
    // Plus more, this make keyboard can't ajustpan and hide edittext

    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.TRANSPARENT

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
    } else {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}