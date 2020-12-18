package com.nvmt.android.mtlibrary.testkeeprecyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvmt.android.mtlibrary.R
import com.nvmt.android.mtlibrary.base.recyclerview.BaseLoadMoreRefreshFragment
import com.nvmt.android.mtlibrary.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNotificationsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)
        binding.rvNotification.recyclerView.hasFocus()
        return binding.root
    }
}