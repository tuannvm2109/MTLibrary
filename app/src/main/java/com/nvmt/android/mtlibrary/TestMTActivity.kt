package com.nvmt.android.mtlibrary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.nvmt.android.mtlibrary.base.MTConstant
import com.nvmt.android.mtlibrary.base.ratestar.RateStarView
import com.nvmt.android.mtlibrary.extension.checkPermissionAndHandle
import com.nvmt.android.mtlibrary.extension.toast
import com.nvmt.android.mtlibrary.util.ImageUtil
import java.io.File

class TestMTActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).setOnClickListener {
        }

        findViewById<RateStarView>(R.id.img1).currentStar = 2
    }
}