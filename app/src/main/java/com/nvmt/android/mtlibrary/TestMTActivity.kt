package com.nvmt.android.mtlibrary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nvmt.android.mtlibrary.base.MTConstant
import com.nvmt.android.mtlibrary.extension.checkPermissionAndHandle
import com.nvmt.android.mtlibrary.extension.toast
import com.nvmt.android.mtlibrary.util.ImageUtil
import java.io.File

class TestMTActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).setOnClickListener {
            val permissions =
                listOf(
                    Manifest.permission.CAMERA
                )
            checkPermissionAndHandle(permissions, 123) {
                toast("123123123")
            }
        }

        findViewById<ImageView>(R.id.img1).setImageBitmap(ImageUtil.renderQrCodeImg("9NjCs3rqOewMCw9tqRqQ", com.google.zxing.BarcodeFormat.QR_CODE))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            // Trường hợp lấy image từ gallery

            // Trường hợp mở camera X
            val file: File? = data?.extras?.get(MTConstant.KEY_DATA) as File?
            if (file != null) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}