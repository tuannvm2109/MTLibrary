package com.nvmt.android.mtlibrary.base.captureimage

import android.net.Uri
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.extension.setGlidePath
import com.nvmt.android.mtlibrary.extension.setGlideSrc


class CaptureImagePreviewFragment : Fragment() {
    var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        path = arguments?.getString("path_image")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_capture_image_preview, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.imgView).setGlidePath(path)

        view.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            parentFragmentManager.popBackStack()
            (requireActivity() as CaptureImageActivity).discardResult()
        }
        view.findViewById<ImageView>(R.id.btnDone).setOnClickListener {
            (requireActivity() as CaptureImageActivity).doneResult()
            parentFragmentManager.popBackStack()
            (requireActivity() as CaptureImageActivity).doneResult()
        }
    }

}