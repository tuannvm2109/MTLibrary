package com.nvmt.android.mtlibrary

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nvmt.android.mtlibrary.base.iconbadge.IconBadgeView
import com.nvmt.android.mtlibrary.base.ratestar.RateStarView
import com.nvmt.android.mtlibrary.extension.setTextHtml
import com.nvmt.android.mtlibrary.extension.setTextHtmlWithImage

class TestMTActivity : AppCompatActivity() {
    val str = "<div style=\"text-align: center; color: #345; padding-top: 10px;\">\r\n<h1 style=\"color: #317399;\">Please Disable Your Ad Blocker 🙏</h1>\r\n<h2><strong>That would help a&nbsp;lot&nbsp;🥺🤑</strong></h2>\r\n<p><a href=\"https://www.youtube.com/watch?v=xB4-XAHiS-U&ab_channel=K%C3%AAnhThi%E1%BA%BFuNhi-BHMEDIA\" target=\"_blank\"><strong>How&nbsp;to disable&nbsp;adBlock?</strong></a> &larr; Ctrl+click</p>\r\n<p><img src=\"/editor/images/ab.png\" alt=\"adBlock icon\" /></p>\r\n</div>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv).setTextHtml(str)

//        findViewById<TextView>(R.id.tv).setOnClickListener {
////            Log.d("asdf", findViewById<EditText>(R.id.img2).text.toString())
//            findViewById<TextView>(R.id.tv).setTextHtmlWithImage(str)
////            val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=265+hoang+hoa+tham&daddr=phuong+linh+chieu+quan+thu+duc")
////            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
////            startActivity(mapIntent)
//        }

        findViewById<RateStarView>(R.id.img1).currentStar = 2

        findViewById<IconBadgeView>(R.id.tesst3).badgeNumb = 2
    }
}