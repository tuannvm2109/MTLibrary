package com.nvmt.android.mtlibrary

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.nvmt.android.mtlibrary.base.MTConstant
import com.nvmt.android.mtlibrary.extension.showDialogListAnchor
import com.nvmt.android.mtlibrary.extension.showPopupTakePicture
import java.io.File

class TestMTActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).setOnClickListener {
            showPopupTakePicture(123, findViewById<TextView>(R.id.tv))
        }
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