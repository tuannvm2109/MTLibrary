package com.nvmt.android.mtlibrary.extension

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.base.captureimage.CaptureImageActivity


const val OPEN_CAMERA = 0
const val OPEN_FOLDER = 1

private val REQUEST_PERMISSION = 456
private val permissions =
    listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

fun Fragment.showPopupTakePicture(requestCode: Int, anchorView: View) {
    val ctx = context ?: return
    val popup = PopupMenu(ctx, anchorView)
    popup.inflate(R.menu.popup_image_picker)
    popup.setOnMenuItemClickListener { item: MenuItem? ->
        when (item!!.itemId) {
            R.id.takePhoto -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    Dexter.withActivity(requireActivity())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                if (response?.permissionName == Manifest.permission.CAMERA) {
                                    openImagePicker(requestCode, OPEN_CAMERA)
                                }
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                                toastError("Bạn phải cấp quyền chụp ảnh để sử dụng tính năng này")
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                p: PermissionRequest?, t: PermissionToken?
                            ) {
                                t?.continuePermissionRequest()
                            }

                        })
                        .withErrorListener { toastError("Cấp quyền không thành công !") }
                        .onSameThread()
                        .check()
                } else openImagePicker(requestCode, OPEN_CAMERA)

            }
            R.id.uploadPhoto -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Dexter.withActivity(requireActivity())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                if (response?.permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                                    openImagePicker(requestCode, OPEN_FOLDER)
                                }
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                                toastError("Bạn phải cấp quyền chụp ảnh để sử dụng tính năng này")
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                p: PermissionRequest?, t: PermissionToken?
                            ) {
                                t?.continuePermissionRequest()
                            }

                        })
                        .withErrorListener { toastError("Cấp quyền không thành công !") }
                        .onSameThread()
                        .check()
                } else openImagePicker(requestCode, OPEN_FOLDER)
            }
        }
        true
    }

    popup.show()
}

fun Fragment.openImagePicker(requestCode: Int, type: Int) {
//    if (!checkAndRequestPermission(permissions, REQUEST_PERMISSION)) return
    if (context == null) return
    when (type) {
        OPEN_CAMERA -> {
            val intent = Intent(requireContext(), CaptureImageActivity::class.java)
            startActivityForResult(intent, requestCode)
//            ImagePicker.cameraOnly().start(this, requestCode)
        }
        OPEN_FOLDER -> {
            ImagePicker.create(this)
                .folderMode(true)
                .toolbarFolderTitle("Folder")
                .toolbarImageTitle("Tap to select")
                .toolbarArrowColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                .single()
                .imageDirectory("Camera")
                .returnMode(ReturnMode.ALL)
                .showCamera(true)
                .start(requestCode)
        }
    }

}

fun Activity.showPopupTakePicture(requestCode: Int, view: View) {
    if (isFinishing) return
    val popup = PopupMenu(this, view)
    popup.inflate(R.menu.popup_image_picker)
    popup.setOnMenuItemClickListener { item: MenuItem? ->
        when (item!!.itemId) {
            R.id.takePhoto -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    Dexter.withActivity(this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                if (response?.permissionName == Manifest.permission.CAMERA) {
                                    openImagePicker(requestCode, OPEN_CAMERA)
                                }
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                                toastError("Bạn phải cấp quyền chụp ảnh để sử dụng tính năng này")
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                p: PermissionRequest?, t: PermissionToken?
                            ) {
                                t?.continuePermissionRequest()
                            }

                        })
                        .withErrorListener { toastError("Cấp quyền không thành công !") }
                        .onSameThread()
                        .check()
                } else openImagePicker(requestCode, OPEN_CAMERA)
            }

            R.id.uploadPhoto -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Dexter.withActivity(this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                if (response?.permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                                    openImagePicker(requestCode, OPEN_FOLDER)
                                }
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                                toastError("Bạn phải cấp quyền chụp ảnh để sử dụng tính năng này")
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                p: PermissionRequest?, t: PermissionToken?
                            ) {
                                t?.continuePermissionRequest()
                            }

                        })
                        .withErrorListener { toastError("Cấp quyền không thành công !") }
                        .onSameThread()
                        .check()
                } else openImagePicker(requestCode, OPEN_FOLDER)
            }
        }
        true
    }
    popup.show()
}

fun Activity.openImagePicker(requestCode: Int, type: Int) {
    if (isFinishing) return
    when (type) {
        OPEN_CAMERA -> {
            val intent = Intent(this, CaptureImageActivity::class.java)
            startActivityForResult(intent, requestCode)

//            ImagePicker.create(this)
//                .returnMode(ReturnMode.CAMERA_ONLY)
//                .single()
//                .start(requestCode)
        }
        OPEN_FOLDER -> {
            ImagePicker.create(this)
                .folderMode(true)
                .toolbarFolderTitle("Folder")
                .toolbarImageTitle("Tap to select")
                .toolbarArrowColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .single()
                .imageDirectory("Camera")
                .returnMode(ReturnMode.ALL)
                .showCamera(true)
                .start(requestCode)
        }
    }

}